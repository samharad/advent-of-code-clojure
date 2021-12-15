;; This was a slow midnight solve for me, and a groggy writeup
;; this morning. Not the cleanest or most succinct code, but it
;; got the job done.

(ns aoc.2021.day.14
  (:require [clojure.string :as str]
            [hyperfiddle.rcf :as rcf]))

(rcf/enable!)

;; [The problem](https://adventofcode.com/2021/day/14) deals with a "polymer", i.e.
;; a string; as the polymer... polymerizes?... a new character is inserted between each
;; pair of existing characters. This occurs recursively, resulting in geometric growth.

;; The input gives the initial polymer, as well as the mapping of adjacent-characters =>
;; character-inserted-between-them.

(def t-input "NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C")

(def input "FSHBKOOPCFSFKONFNFBB\n\nFO -> K\nFF -> H\nSN -> C\nCC -> S\nBB -> V\nFK -> H\nPC -> P\nPH -> N\nOB -> O\nPV -> C\nBH -> B\nHO -> C\nVF -> H\nHB -> O\nVO -> N\nHK -> N\nOF -> V\nPF -> C\nKS -> H\nKV -> F\nPO -> B\nBF -> P\nOO -> B\nPS -> S\nKC -> P\nBV -> K\nOC -> B\nSH -> C\nSF -> P\nNH -> C\nBS -> C\nVH -> F\nCH -> S\nBC -> B\nON -> K\nFH -> O\nHN -> O\nHS -> C\nKK -> V\nOK -> K\nVC -> H\nHV -> F\nFS -> H\nOV -> P\nHF -> F\nFB -> O\nCK -> O\nHP -> C\nNN -> V\nPP -> F\nFC -> O\nSK -> N\nFN -> K\nHH -> F\nBP -> O\nCP -> K\nVV -> S\nBO -> N\nKN -> S\nSB -> B\nSC -> H\nOS -> S\nCF -> K\nOP -> P\nCO -> C\nVK -> C\nNB -> K\nPB -> S\nFV -> B\nCS -> C\nHC -> P\nPK -> V\nBK -> P\nKF -> V\nNS -> P\nSO -> C\nCV -> P\nNP -> V\nVB -> F\nKO -> C\nKP -> F\nKH -> N\nVN -> S\nNO -> P\nNF -> K\nCB -> H\nVS -> V\nNK -> N\nKB -> C\nSV -> F\nNC -> H\nVP -> K\nPN -> H\nOH -> K\nCN -> N\nBN -> F\nNV -> K\nSP -> S\nSS -> K\nFP -> S\n")

;; Daily parsing code; we'll parse a mapping of e.g. `{[\A \B] \C}` for
;; our pair insertions.

(defn parse-instructions [input]
  (let [lines (str/split-lines input)
        [[template] _ mappings] (partition-by #{""} lines)
        pair-insertions (->> mappings
                             (map #(vec (str/split % #" -> ")))
                             (map (fn [[pair target]]
                                    [(vec pair)(first target)]))
                             (into {}))]
    [template pair-insertions]))

(def instructions (parse-instructions input))
(def t-instructions (parse-instructions t-input))

;; For our simulation, instead of using an ever-growing string (or char-vec),
;; we'll use a mapping of e.g. `{[\A \B] 10}`, which counts the number of times
;; that the substring "AB" appears in our polymer. (Of course my first implementation
;; just grew a vector; and, of course, that wasn't feasible for part 2.)

;; At each step of the simulation, we'll build a new count-map using our current
;; count-map.

(defn step-polymerization [[pair-counts pair-insertions]]
  (let [pair-counts (->> pair-counts
                         (reduce (fn [acc [pair count]]
                                   (let [[a b] pair
                                         insertion (pair-insertions pair)
                                         add-count (fnil (partial + count) 0)]
                                     (-> acc
                                         (update [a insertion] add-count)
                                         (update [insertion b] add-count))))
                                 {}))]
    [pair-counts pair-insertions]))

(defn simulate-polymerization [instructions]
  (iterate step-polymerization instructions))

;; Now, the question asks us for the difference in the count between
;; the most-common and least-common characters in the polymer after
;; some number of steps.

;; The thing to remember here -- which I, of course, did not initially
;; remember -- is that if, e.g., out resulting polymer is "ABCD", then
;; our resulting pair counts will be:

; {[A B] 1,
;  [B C] 1,
;  [C D] 1}

;; Which, when "flattened" naively, gives us individual character counts:

; {A 1,
;  B 2
;  C 2
;  D 1}))

;; Which double-counts *only the middle characters* of the string;
;; the endcap character counts are accurate.

;; So -- and there is probably a better way to do this, but --
;; we just need to account for this by first incrementing the
;; character counts of our endcap characters before halving
;; all counts.

(defn count-differential [pair-counts template]
  (let [freqs (->> pair-counts
                   (mapcat (fn [[[a b] v]] [[a v] [b v]]))
                   (group-by first)
                   (reduce-kv (fn [m char char-counts]
                                (assoc m char (->> char-counts
                                                   (map second)
                                                   (reduce +))))
                              {}))
        under-counted ((juxt first last) template)
        freqs (reduce (fn [freqs c] (update freqs c inc))
                      freqs
                      under-counted)
        freqs (update-vals freqs #(/ % 2))
        [min max] (apply (juxt min max) (map second freqs))]
    (long (- max min))))

(defn count-differential-after [[template pair-insertions] n]
  (let [pair-counts (->> template (partition 2 1) (frequencies))
        polymerizations (simulate-polymerization [pair-counts pair-insertions])
        [pair-counts _] (nth polymerizations n)]
    (count-differential pair-counts template)))

(defn part-1 [instructions]
  (count-differential-after instructions 10))

(defn part-2 [instructions]
  (count-differential-after instructions 40))

(rcf/tests
  (part-1 t-instructions) := 1588
  (part-1 instructions) := 3143

  (part-2 t-instructions) := 2188189693529
  (time (part-2 instructions)) := 4110215602456)

;; ⭐️⭐️