;; I was up when the day turned to the 16th, but with the wordy spec
;; and what I suspected would be a pile of code to write, I saved the puzzle
;; for the daylight hours.

(ns aoc.2021.day.16
  (:require [hyperfiddle.rcf :as rcf]))

(rcf/enable!)

;; [Day 16](https://adventofcode.com/2021/day/16) is this year's first
;; tedious-spec-following puzzle; it might be a multi-day project like a
;; previous year's 'intcode' interpreter. These puzzles are nice, if a
;; little monotonous, because there usually aren't walls of confusion to
;; be hit, and they feel sort of real-worldy.

;; This one took some time from a busy day to implement, so minimal cleanup
;; was done.

;; The input is a hexadecimal string which we're told to convert immediately
;; to binary.

(def input "E20D79005573F71DA0054E48527EF97D3004653BB1FC006867A8B1371AC49C801039171941340066E6B99A6A58B8110088BA008CE6F7893D4E6F7893DCDCFDB9D6CBC4026FE8026200DC7D84B1C00010A89507E3CCEE37B592014D3C01491B6697A83CB4F59E5E7FFA5CC66D4BC6F05D3004E6BB742B004E7E6B3375A46CF91D8C027911797589E17920F4009BE72DA8D2E4523DCEE86A8018C4AD3C7F2D2D02C5B9FF53366E3004658DB0012A963891D168801D08480485B005C0010A883116308002171AA24C679E0394EB898023331E60AB401294D98CA6CD8C01D9B349E0A99363003E655D40289CBDBB2F55D25E53ECAF14D9ABBB4CC726F038C011B0044401987D0BE0C00021B04E2546499DE824C015B004A7755B570013F2DD8627C65C02186F2996E9CCD04E5718C5CBCC016B004A4F61B27B0D9B8633F9344D57B0C1D3805537ADFA21F231C6EC9F3D3089FF7CD25E5941200C96801F191C77091238EE13A704A7CCC802B3B00567F192296259ABD9C400282915B9F6E98879823046C0010C626C966A19351EE27DE86C8E6968F2BE3D2008EE540FC01196989CD9410055725480D60025737BA1547D700727B9A89B444971830070401F8D70BA3B8803F16A3FC2D00043621C3B8A733C8BD880212BCDEE9D34929164D5CB08032594E5E1D25C0055E5B771E966783240220CD19E802E200F4588450BC401A8FB14E0A1805B36F3243B2833247536B70BDC00A60348880C7730039400B402A91009F650028C00E2020918077610021C00C1002D80512601188803B4000C148025010036727EE5AD6B445CC011E00B825E14F4BBF5F97853D2EFD6256F8FFE9F3B001420C01A88915E259002191EE2F4392004323E44A8B4C0069CEF34D304C001AB94379D149BD904507004A6D466B618402477802E200D47383719C0010F8A507A294CC9C90024A967C9995EE2933BA840")

(defn to-bit-seq [hex-str]
  ;; Courtesy https://stackoverflow.com/questions/4421400/how-to-get-0-padded-binary-representation-of-an-integer-in-java
  (mapcat (fn [char]
            (as-> (Character/digit ^char char 16) $
                  (Long/toBinaryString $)
                  (format "%4s" $)
                  (.replace $ \space \0)
                  (seq $)))
          hex-str))

;; The string is a packet, like one sent over a wire; a packet
;; is a recursive tree-like structure, i.e., a packet has sub-packets.

;; A packet has a version (not totally sure what this represents) and
;; a type; a packet may be of type 'literal number,' 'plus,' 'minimum,'
;; or one of several other mathematical operators.

;; Our task is to decode the packets (part 1) and then evaluate the
;; mathematical expression it represents (part 2).

;; First, an annoying var declaration for a function which will feed
;; the following multimethod.
(declare parse-packets)

;; This multi-method switches on the packet type and does the required
;; parsing work for that packet. Parsing is recursive for those packets
;; that contain sub-packets; `decode-packets` is mutually recursive with
;; `parse-packets`.

;; We dispatch on the packet type, but only switch on literal- or non-literal-,
;; i.e. operator-, packet, since the work to be done ends up being about the same.
;; (Kind of inconsistent, but gets the job done.)
(defmulti decode-packets (fn [version type bits] (#{4} type)))

;; Literal packet decoder:
(defmethod decode-packets 4 [version type bits]
  (let [[literal bits] (loop [literal-acc []
                              bits bits]
                         (let [[[indicator & chunk] bits] (split-at 5 bits)
                               literal-acc (concat literal-acc chunk)]
                           (if (= \0 indicator)
                             [(Long/parseLong (apply str literal-acc) 2) bits]
                             (recur literal-acc bits))))]
    [bits [{:version version :type type :literal literal}]]))

(def bool->int {true 1 false 0})
(defn bool-int [f] (comp bool->int f))

;; Operator packet decoder and helpers:

(defn decode-length-type-packet [version type bits op]
  (let [[length-str init-bits] (split-at 15 bits)
        length (Long/parseLong (apply str length-str) 2)
        [bits sub-packets] (loop [sub-packets []
                                  bits init-bits]
                             (if (= length (- (count init-bits) (count bits)))
                               [bits sub-packets]
                               (let [[bits packets] (parse-packets bits)]
                                 (recur (into sub-packets packets) bits))))]
    [bits [{:version version :op op :type type :sub-packets sub-packets}]]))

(defn decode-count-type-packet [version type bits op]
  (let [[count-str bits] (split-at 11 bits)
        sub-packet-count (Long/parseLong (apply str count-str) 2)
        [bits sub-packets] (loop [bits bits
                                  sub-packets []]
                             (if (= (count sub-packets) sub-packet-count)
                               [bits sub-packets]
                               (let [[bits packets] (parse-packets bits)]
                                 (recur bits (into sub-packets packets)))))]
    [bits [{:version version :op op :type type :sub-packets sub-packets}]]))

(defmethod decode-packets nil [version type bits]
  (let [[length-type-id & bits] bits
        op ({0 + 1 * 2 min 3 max 5 (bool-int >) 6 (bool-int <) 7 (bool-int =)}
            type)]
    (if (= length-type-id \0)
      (decode-length-type-packet version type bits op)
      (decode-count-type-packet version type bits op))))

;; Instead of stuffing logic into the multimethod dispatch function,
;; I left it here in this helper:

(defn parse-packets [bits]
  (let [[version bits] (split-at 3 bits)
        version (Long/parseLong (apply str version) 2)
        [type bits] (split-at 3 bits)
        type (Long/parseLong (apply str type) 2)]
    (decode-packets version type bits)))

;; Part 1 asks us to sum the versions parsed from the packets:

(defn version-sum [packets]
  (->> packets
       (mapcat #(tree-seq :sub-packets :sub-packets %))
       (map :version)
       (reduce +)))

(defn part-1 [hex]
  (let [[leftover-bits packets] (parse-packets (to-bit-seq hex))]
    (version-sum packets)))

(rcf/tests
  (part-1 input) := 951)

;; Part 2 asks us to evaluate the tree. Initially I had a separate
;; multimethod here but there was a lot of duplication in its methods;
;; it seemed at least more succinct to give each packet an operator
;; earlier on.

(defn eval-packet [{:keys [type literal op sub-packets]}]
  (or literal (apply op (map eval-packet sub-packets))))

(defn part-2 [hex]
  (let [[leftover-bits packets] (parse-packets (to-bit-seq hex))]
    (eval-packet (first packets))))

(rcf/tests
  (time (part-2 input)) := 902198718880)  ; => 14.68375 ms

;; Satisfying, if not super clean. I hope this is a multi-day-er so that
;; I can revisit it.

;; ⭐️⭐️