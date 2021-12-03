;; There was no midnight attempt for me last night since I was asleep
;; early after getting my Covid booster; thankfully, I feel great today
;; and can take my time with the puzzle.

(ns aoc.2021.day.03
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.string :as str]
            [criterium.core :as bench]))

(rcf/enable!)

;; [Day 3's puzzle](https://adventofcode.com/2021/day/3) gives a list of
;; binary strings as input. For part 1, we essentially need to determine
;; for each bit-place (most-significant to least-significant), whether 0
;; or 1 is the most-common bit across the list of binary numbers.

(def input "010100010111\n100100100110\n100110111001\n011001011011\n010000110111\n000011101001\n011000011101\n101111011111\n011001011010\n111100001001\n111111000110\n100010100110\n011100100100\n011111010000\n111010001100\n010111001110\n100010100100\n101000010000\n011101110100\n100010011000\n001111110011\n111001100001\n010000011001\n000011101010\n100010101100\n111011100010\n110000100001\n101010110001\n111101110101\n001010010100\n001001111001\n100001110010\n100100000111\n000101010101\n001101111011\n111100011000\n100111110101\n010101111000\n100110011001\n010001111010\n010111111001\n111000100010\n011000011011\n100010111111\n010110110010\n010100000001\n100011000100\n100000010001\n101010001000\n100111011001\n101011001010\n010110110111\n111011000100\n110010111110\n010101000111\n000101111101\n101101101101\n010100010001\n000111101111\n101101001101\n000011110010\n000101101011\n001111100000\n001000100110\n111000000100\n001000010110\n000111010001\n111011001010\n100011100010\n011010101011\n001100010001\n000001011001\n110001101101\n010101100001\n111000011100\n000101000000\n001001010101\n100000100111\n110000001000\n111001110100\n001111000110\n111110000100\n100111111000\n100101111111\n100101011111\n111000000011\n110000011001\n000101000101\n110110010011\n110111010100\n001001101100\n101101100001\n110011001000\n010010100111\n000110101011\n101010000011\n100110001011\n000111111011\n000100111011\n101011111001\n011010110110\n100101011000\n100000010110\n000010101000\n100100111000\n011100011000\n000010011001\n000001010000\n011001110110\n111101101010\n011110001000\n011100110001\n011011010101\n101011100001\n000000111000\n110000110011\n101111001100\n111000101111\n100110110110\n110000111101\n001101110000\n100010001110\n001001010010\n001010000110\n000110011001\n111111000101\n111011011010\n101101111010\n101001000110\n101100100101\n001110100000\n110001000001\n010010011101\n011110100010\n011000000010\n101110100111\n000001001010\n101101110001\n001110101100\n011101011001\n011010000001\n100010011110\n000111100010\n011000100110\n101001010101\n000110011010\n111100101001\n011010000011\n010101011101\n010011011000\n011001000100\n010011101111\n100110100111\n110111010110\n011001101101\n110001100111\n010100111110\n111101011010\n011010000100\n010101101110\n101000101100\n110110110001\n110011110011\n111100001100\n000011110101\n011100000110\n011100000111\n100101001010\n001110010001\n011101110111\n001101100001\n111111010011\n101001111110\n000000100000\n000000100010\n101100101000\n010011011111\n101100100000\n000110100100\n000010001001\n110011000111\n101000101101\n100101010111\n010111001010\n000000011011\n110110100011\n111001100000\n000011101101\n001011100010\n001100110100\n010010100010\n001001010100\n100111010110\n001001111000\n011011110101\n110111101011\n000000001000\n111110011011\n010101011001\n001011111111\n011011111100\n011000001011\n101000100000\n010011011011\n011100111100\n011001011110\n111001101010\n111011111111\n100010101010\n101100001101\n110001000111\n100111011010\n010000111010\n011001101000\n111111001001\n000111100000\n011000010111\n110110000111\n110100111100\n011111000001\n000001110100\n100110101110\n111000111001\n010001000000\n111001001010\n111100111101\n101000110100\n000101000011\n010100101111\n010100011010\n111110000110\n110000010111\n010100001100\n010000011010\n111010000100\n000011010010\n011001101001\n010100111001\n011001010111\n001001100011\n110001100101\n101001001001\n011101010010\n011101011110\n000111111110\n010111010001\n101011011110\n001011101101\n100110010000\n001111100101\n011101001110\n010111100000\n000010111111\n101001010000\n001101001010\n110010111101\n001010110110\n001001001001\n110110110101\n000001011011\n001110010101\n011110000001\n101111011011\n100000011100\n101000010001\n000101101100\n010111110111\n010001101110\n011101001000\n110100001110\n110010110101\n101001101000\n000101010010\n011101001100\n100111101100\n001011111001\n011111101100\n100100110001\n101001101101\n011100001010\n000111101101\n110010110111\n100111010011\n000101110110\n100111000010\n101010000111\n101101110101\n100000100101\n111100010101\n111011100111\n110011011111\n100110100011\n111011000101\n001010101110\n010011000110\n110001001111\n110100110110\n011011011100\n111111100100\n101010110010\n110011101001\n000110100001\n010011110111\n110111001101\n100100100100\n000111000110\n001001010000\n001111111110\n110000011101\n011101011000\n110000111001\n000100000000\n100110111100\n111000010101\n100010011100\n110100111011\n010111110110\n101111101010\n100110111101\n110100110101\n011111001010\n000010010001\n011001010010\n010111101101\n000000110000\n111001101110\n001011000101\n100011011101\n101101010100\n010000010111\n000010101111\n001110101010\n100000001011\n111101011110\n110110001000\n010100010000\n010100110011\n000001001110\n101011000111\n110111101000\n001100110111\n100110101000\n000100111110\n101111110000\n101001001000\n001101001011\n111011111101\n111011110011\n110111111100\n110001010100\n011110001110\n010011100100\n101100010000\n111010000111\n100100011010\n100100011011\n100001101111\n000111100111\n001101011010\n101110000010\n000000000101\n110111000110\n000010100111\n001100101110\n000010011101\n101100100001\n011011000101\n100110101100\n011000110000\n111000100101\n011110110101\n111011011111\n000110010110\n001010011110\n001110111111\n110110011100\n111110001001\n000001111000\n101100101110\n011110001001\n000101010011\n011100100000\n100011111010\n111011101011\n000111100110\n101011111010\n010100111101\n110100110000\n000000000011\n011000010101\n101110110101\n011100010101\n000111001101\n100011010000\n010111001100\n110000110010\n010001001100\n000001011100\n110111000000\n011110011001\n101000010110\n000100100111\n101110000011\n001111011001\n101010011101\n111100110110\n111101100000\n000000101111\n011111011110\n000000000111\n001000101110\n011101101111\n011010100011\n010101000010\n001000101100\n010000110100\n100010101111\n000011010111\n011011110000\n010101100110\n011110001011\n101011000001\n011100101101\n111001100011\n100001100101\n101011010110\n010101000101\n001001011111\n111010001101\n010011001111\n001010100110\n011101000001\n000000111101\n111010000101\n011000100011\n100100011110\n010001111011\n111100111001\n011101111111\n001111110000\n100100010001\n100111100001\n001111000011\n100010111011\n110110000110\n110000001010\n000000100101\n000110111010\n100001010100\n011011000010\n111001001110\n100010000000\n100101000011\n111101010000\n001001110110\n111010010011\n110101101000\n101100111001\n101010100111\n000001101001\n000110010001\n010001100100\n101011100100\n111000110000\n001100111101\n011000111101\n110010110110\n101101010111\n110110100100\n101001001011\n000101100111\n000110010011\n011000110011\n100011110110\n110000101111\n100110111000\n100000000010\n001001011100\n100100000110\n100001001001\n010000011011\n110111110100\n000110100110\n011010000010\n110001100110\n011000101010\n111011011110\n100011010110\n100001001000\n101010101000\n011100100110\n010000010010\n100010010011\n100000101011\n010110000101\n000000101010\n000000010111\n110110110010\n101111010101\n000010001000\n100000110101\n000100111010\n111100000100\n101000101110\n100100001111\n000111011101\n110100101101\n011010010100\n001010110011\n111110011101\n101110111010\n001110110110\n111100011010\n100110000010\n111000001110\n110010010010\n101010100110\n011101010011\n101100100111\n100011101110\n111110101010\n110011111101\n101110001001\n001101100101\n011010100100\n110010001101\n010011001010\n100100010010\n100110011111\n110010111001\n001111111100\n110011111110\n111001101000\n010101100000\n101101000010\n100011011110\n010000111110\n110000111010\n011111110110\n101010111001\n000110111001\n100111010000\n111001010001\n111101001000\n111000001101\n001100001001\n001101111111\n011010101001\n001100111000\n000100101101\n010101110000\n000100100010\n010111010101\n110011001111\n101001010110\n100000001010\n110010001110\n100011100101\n110101011111\n101110110011\n010010110110\n100100001011\n111111110110\n011100101011\n101111110011\n110001110001\n011011101000\n110110000101\n101001110000\n110111111111\n001100000110\n001100111111\n010101010111\n100001001110\n110011001010\n011010100000\n001010101011\n000010000101\n100101010010\n111011110101\n111110010001\n011000011110\n001111111101\n011010111100\n001011100110\n100110101010\n101100001000\n101011000000\n111000010011\n001101000111\n010001010111\n000100000001\n000110001101\n001111100010\n111000100011\n111111001010\n010011110100\n111111000010\n001100001100\n100010110110\n010111101110\n100010000111\n100101001000\n000011011100\n110010001111\n111000111110\n010110001101\n100110001100\n101001000011\n010110010000\n010010100101\n010110110001\n001111001001\n101011100111\n101010010100\n000001101100\n100010000110\n010111101010\n110010100100\n110011010000\n100110100110\n011010110101\n000011100111\n001001100111\n101011100000\n010010011000\n000010000011\n010111111100\n001010111101\n001111010010\n101011110000\n110111011100\n011100001110\n010000000001\n101110001100\n001011111110\n110100100001\n011010111101\n100110010101\n111111100010\n100111101010\n110010010001\n100010111001\n001010000000\n010011110001\n001100010110\n001111100100\n001011010111\n001010110101\n010111100101\n010100001110\n000110010100\n000001111101\n110101100101\n010101100011\n011110111000\n100001101001\n001001001011\n111010101101\n001111011110\n110111110001\n001110100100\n100011100001\n100001111111\n100000111001\n000110110100\n111001110011\n010111000110\n101100101111\n000110001011\n110111101101\n110101001101\n101111101011\n111010110101\n101101111110\n011001001100\n010100110101\n110100101100\n110011101111\n010000111101\n111101011111\n111010111100\n011000101001\n010000111000\n001010001001\n100111110111\n010111011010\n001011010100\n011001001101\n010011100000\n000111010010\n000001101110\n001101011110\n100000010000\n010100000010\n010010000110\n111000101100\n101111101100\n000101111010\n111011011101\n011101000011\n000111000100\n010101111001\n100110010001\n000001011101\n010010101101\n100101100111\n100101110010\n101001000001\n110000110001\n101110111111\n100100000010\n011010001110\n011011000110\n000110001001\n010110100101\n111001110101\n000010011000\n010110011011\n100110010111\n111111010010\n101011001101\n010011100101\n010010110011\n101100011100\n010111010110\n110001101100\n000101101000\n010011101000\n101011100011\n000001001100\n101111110010\n111001111100\n000111111010\n110100101010\n101010110011\n000011111010\n011100111001\n101010001110\n010101010001\n011110101011\n001111001011\n010100101001\n110001100000\n110101000010\n011100110011\n111111011111\n100010000011\n101001100111\n000000101000\n101110110100\n110100111010\n111101110010\n010111111011\n111110100100\n010001100001\n000011010011\n000011100011\n110010010011\n111010011000\n101111001111\n011000100111\n010101101011\n001000001101\n110110110111\n111100010100\n100001100001\n100010100001\n010001011001\n101010110000\n011110101010\n011110111100\n100110100010\n011111111101\n100111001110\n101011011111\n000011100000\n111110101001\n000010110010\n110000001110\n110011111010\n001001101111\n100010010001\n101001010010\n111111111010\n000000111100\n000001000001\n100000110100\n010000101111\n001111111001\n001110001010\n100111110100\n000110111111\n010100010100\n001000000110\n011011110001\n100011110011\n011101111000\n011011000011\n110101100011\n111101001100\n010001001010\n010010001101\n111110010111\n111010011101\n100111101011\n000110000101\n010111011000\n111110010011\n101001100001\n011010100001\n100111100011\n101010001101\n000011101100\n010001010001\n010000000000\n111001100111\n110010000101\n010011101101\n100101000100\n000011100101\n111101101110\n001110101000\n010110101111\n010011110000\n011101011100\n011001100100\n001101011011\n101110011111\n100001101010\n110111111000\n000001111011\n101111000100\n100100001010\n101001110011\n010111100010\n110111001011\n100001000100\n001001101000\n000001100010\n001011001110\n001100000000\n101110010111\n110011101011\n001110001101\n001000110101\n100111101110\n000001010101\n011001100101\n010001000111\n011010100101\n011110111111\n110001000000\n000001110000\n011101000000\n100011000010\n110101001011\n101100000110\n111001001111\n110100010110\n001001001000\n110100011110\n101101001000\n000001100000\n101000000100\n000000100110\n111101111000\n100001101011\n111100011101\n010001001101\n101011010001\n001100010010\n100101010000\n111110010000\n101110101001\n101001101011\n111000011010\n001111101101\n001001011101\n111000010010\n111110111100\n110000101101\n111000000000\n001010001010\n100001101100\n010101110111\n001001011001\n001110000001\n100100101100\n011111011010\n001001000100\n100000111111\n111001100110\n100101110000\n000001001111\n111110010100\n100101000000\n001000000111\n010100000101\n010101000001\n100111000110\n110101000100\n110110001111\n001001000011\n111110001101\n011000001110\n100001111011\n101110011001\n011011001110\n000100100110\n100101100010\n111110101011\n010000101010\n000011011110\n101110010001\n101000011101\n101001001010\n110101011101\n001111110111\n110001110000\n001011101011\n111110000011\n100111011011\n101011001000\n010001000110\n100100111010\n010111001111\n111101000111\n011001111110\n000110010000\n111100111111\n000111011111\n110000101000\n011001101100\n111000011011\n011110010110\n010101100111\n011111000101\n100111100000\n000001001001\n010111010100\n101010100000\n010111001011\n110001010111\n000110100111\n010110010001\n010111000001\n011010001010\n110111010000\n000000100001\n000110110110\n110011111111\n011011101100\n010011011100\n111000011101\n000001111111\n010100000011\n110001111100\n100000011001\n010100100101\n011010010011\n000011110110\n110100011001\n110111100101\n000001000100\n001110111100\n010011010110\n100100010111\n110111011110\n000011111100\n110011010011\n100110001110\n000101001011\n110110101001\n111010011111\n110001010000\n100111111100\n010000110001\n011110000111\n110011110001\n010110110011\n100010001011\n110010101011\n110111100010\n110010001001\n100000001100\n010110010011\n000111111111\n011010010111\n110101100100\n110100000101\n100111010001\n110110111101\n100100001101\n100111101000\n000111000011\n100001010001\n101011001100\n100100111001\n100010001111\n")

(defn ->bit-strs [input]
  (str/split-lines input))

(def bit-strs (->bit-strs input))

;; High-level, what I need to do is to iterate over each bit-str and
;; track, for each bit, the difference between the number of observed
;; 1s and 0s.

;; For example: for 4 bit words, I'll start with a "bit-differential-seq"
;; (for lack of a better name) of `(0 0 0 0)`, indicating that, across
;; all the binary numbers I've seen so far (none), there is 0 difference in the
;; number of 1s and 0s I've observed at each bit. This seq is ordered from
;; least-significant to most-significant bit; so, if I fold the bit-str `"0001"`
;; into my differential seq, my result is `(1 -1 -1 -1)`.

;; First, a helper function to determine the "word size", i.e. number
;; of bits per entry:

(defn word-size [bit-strs]
  (count (first bit-strs)))

;; Next, a helper function for converting my bit-differential-seq into
;; a number:

(defn differentials->int [differentials]
  (loop [[head & more] differentials
         i 0
         acc 0]
    (if (not head)
      acc
      (recur more
             (inc i)
             (if (neg? head)
               acc
               (+ acc (bit-shift-left 1 i)))))))
(rcf/tests
  (differentials->int '(10 -9 10 -9)) := 2r0101)

;; Now, to derive my differential-seqs from my bit-strings.
;; Frankly, I'm not sure whether it will be faster to treat the strings as strings
;; and read each character in sequence, branching on the contents, or to convert to
;; integers and use arithmetic to determine these differentials.

;; Surely the CPU is good at both arithmetic *and* iterating over the characters in an
;; array, but I'm guessing that Clojure and the JVM introduce more overhead to the
;; latter. I'll give both a try and see what happens.

;; So now I have another helper function that performs the iteration (reduction),
;; taking a (terribly named) `bit-differential-seq-f` as a parameter.
;; Parameterizing on this function will allow me to try both the
;; string-iteration and conversion-plus-arithmetic approaches.

(defn differentials [bit-strs bit-differential-seq-f]
  "Calculates the gamma rate given a collection of bit-strs
  and a function of bit-str => bit-differential-seq, where
  bit-differential-seq is a sequence [x0, x1, ..., xn] where xi
  is 1 if bit-str's i-th least-significant bit is 1, else -1, and
  n is the power of the most-significant set bit."
  (let [reducer (fn [differentials bit-str]
                  (map +
                       differentials
                       (concat (bit-differential-seq-f bit-str)
                               ; Pad missing most-significant bits with -1,
                               ; since they're implicitly 0-bits
                               (repeat -1))))
        init (repeat (word-size bit-strs) 0)
        differentials (reduce reducer init bit-strs)]
    differentials))

(defn gamma-rate [bit-strs bit-differential-seq-f]
  (differentials->int (differentials bit-strs bit-differential-seq-f)))

;; (Of course -- a more legible and possibly more DRY approach would be to
;; parameterize on a more general `bit-seq` function which just returns the bits
;; from least- to most-significant; then, within `gamma-rate`, I can map `0` to `-1`;
;; but in the foolish interest of shaving off millis, I want to avoid repeated mappings.)

;; Now, the epsilon rate -- the least-common bit at each place -- is the inverse
;; of the gamma rate:

(defn epsilon-rate [gamma-rate word-size]
  (let [
        ; We only deal with binary numbers of `word-size` bits; so
        ; a word-size of 2 means a mask of `100b - 1b == 011b`
        mask (dec (bit-shift-left 1 word-size))]
    (-> gamma-rate
        (bit-not)
        (bit-and mask))))

;; And now for the solution algo (parameterized on the function
;; that takes us from bit-str to bit-differential-seq):

(defn part-1 [bit-differential-seq-f]
  (let [gamma (gamma-rate bit-strs bit-differential-seq-f)
        word-size (word-size bit-strs)
        epsilon (epsilon-rate gamma word-size)]
    (* gamma epsilon)))

;; Now, the fun part: to write and benchmark some means of transforming
;; `"0101"` to `(1 -1 1 -1)`:

(defn part-1-iter-map []
  (part-1
    (fn [bit-str]
      (reverse (map {\0 -1 \1 1} bit-str)))))

(defn part-1-iter-branch []
  (part-1
    (fn [bit-str]
      (reverse (map #(if (= \0 %) -1 1) bit-str)))))

(defn bit-str->differentials [bit-str]
  (loop [x (Integer/parseInt bit-str 2)
         acc []]
    (if (zero? x)
      acc
      (recur (bit-shift-right x 1)
             (conj acc (if (bit-test x 0)
                         1
                         -1))))))

(defn part-1-bit-test []
  (part-1 bit-str->differentials))

;; First, make sure we have the solution, and get anecdotal timings:

(rcf/tests
  (print "String iter map: ") (time (part-1-iter-map)) := 4139586
  (print "String iter branch: ") (time (part-1-iter-branch)) := 4139586
  (print "Number bit test: ") (time (part-1-bit-test)) := 4139586)

;; Now, the moment of truth (note that outlier stats are omitted for brevity):

(comment
  (do
    (bench/bench (part-1-map))
    ;=>
    ;                Evaluation count : 21360 in 60 samples of 356 calls.
    ;             Execution time mean : 2.833861 ms
    ;    Execution time std-deviation : 43.560325 µs
    ;   Execution time lower quantile : 2.793355 ms ( 2.5%)
    ;   Execution time upper quantile : 2.963503 ms (97.5%)
    ;                   Overhead used : 2.088084 ns

    (bench/bench (part-1-str-iter))
    ; =>
    ;                Evaluation count : 22200 in 60 samples of 370 calls.
    ;             Execution time mean : 2.749760 ms
    ;    Execution time std-deviation : 39.356220 µs
    ;   Execution time lower quantile : 2.713795 ms ( 2.5%)
    ;   Execution time upper quantile : 2.824935 ms (97.5%)
    ;                   Overhead used : 2.088084 ns

    (bench/bench (part-1-bit-test))
    ; =>
    ;                Evaluation count : 47760 in 60 samples of 796 calls.
    ;             Execution time mean : 1.265880 ms
    ;    Execution time std-deviation : 13.627753 µs
    ;   Execution time lower quantile : 1.245638 ms ( 2.5%)
    ;   Execution time upper quantile : 1.291653 ms (97.5%)
    ;                   Overhead used : 2.088084 ns
    ,))

;; No big surprise that the string iteration approaches, using either a 2-arity map
;; or a branch, are about equivalent -- the former might even be compiled to the latter.

;; The arithmetical approach is more than twice as fast as the string iteration approach, though,
;; which at first glance is confusing because it's the same O(bits) time complexity, and it does
;; the same branch per bit.

;; That could be because, for the string iteration, I always iterate over each of the 12
;; bit-places, whereas with the arithmetic approach I do no work for any leading 0s.

;; But more importantly: a single 8-byte long, in the case of the arithmetic approach, can
;; be held in a CPU register and thus quickly operated on, whereas the array of 12 2-byte characters
;; occupies more memory and is thus more expensive to move around between memory/cache/register.

;; Obvious in hindsight, but glad I investigated! I may delve further when I have more
;; time. If you have any insights, please drop a comment!


;; ### On to part 2!

;; I spent too much time playing with the above, so I'll be one-and-done on
;; this part.

(defn find-life-support-rating [bit-strs bit-criteria]
  (let [bit-str (loop [candidates bit-strs
                       i 0]
                  (let [differentials (-> (differentials candidates bit-str->differentials)
                                          ; Sort most- to least-significant
                                          (reverse)
                                          (vec))]
                    (if (= 1 (count candidates))
                     (first candidates)
                     (recur (filter #(bit-criteria (nth % i)
                                                   (nth differentials i))
                                    candidates)
                            (inc i)))))]
    (Integer/parseInt bit-str 2)))

(defn o2-rating [bit-strs]
  (find-life-support-rating bit-strs
                            (fn [bit bit-differential]
                              (if (neg? bit-differential)
                                (= bit \0)
                                (= bit \1)))))

(defn co2-rating [bit-strs]
  (find-life-support-rating bit-strs
                            (fn [bit differential]
                              (if (neg? differential)
                                (= bit \1)
                                (= bit \0)))))


(defn part-2 []
  (let [o2 (o2-rating bit-strs)
        co2 (co2-rating bit-strs)]
    (* o2 co2)))

(rcf/tests
  (part-2) := 1800151)

;; Phew! That took longer than anticipated :)

;; ⭐️⭐️