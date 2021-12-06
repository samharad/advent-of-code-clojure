;; On the eve of [Day 6](https://adventofcode.com/2021/day/6) I was resolved
;; to get an early night's sleep, and so of course found myself at my desk
;; at midnight. Thankfully today's puzzle was a fairly quick solve.

(ns aoc.2021.day.06
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.string :as str]
            [criterium.core :as bench]))

(rcf/enable!)

;; The task is to simulate the lifecycle of a certain fish population. Each fish
;; has a time-until-reproduction in days, given as an integer, which ticks down as
;; the days pass. When the fish's counter hits 0, the fish's own counter resets to
;; 6, **and** a new fish is spawned with a time-until-reproduction count set to 8.

;; From the puzzle example:
; Initial state: 3,4,3,1,2
; After  1 day:  2,3,2,0,1
; After  2 days: 1,2,1,6,0,8
; After  3 days: 0,1,0,5,6,7,8
; After  4 days: 6,0,6,4,5,6,7,8,8

(def input "4,5,3,2,3,3,2,4,2,1,2,4,5,2,2,2,4,1,1,1,5,1,1,2,5,2,1,1,4,4,5,5,1,2,1,1,5,3,5,2,4,3,2,4,5,3,2,1,4,1,3,1,2,4,1,1,4,1,4,2,5,1,4,3,5,2,4,5,4,2,2,5,1,1,2,4,1,4,4,1,1,3,1,2,3,2,5,5,1,1,5,2,4,2,2,4,1,1,1,4,2,2,3,1,2,4,5,4,5,4,2,3,1,4,1,3,1,2,3,3,2,4,3,3,3,1,4,2,3,4,2,1,5,4,2,4,4,3,2,1,5,3,1,4,1,1,5,4,2,4,2,2,4,4,4,1,4,2,4,1,1,3,5,1,5,5,1,3,2,2,3,5,3,1,1,4,4,1,3,3,3,5,1,1,2,5,5,5,2,4,1,5,1,2,1,1,1,4,3,1,5,2,3,1,3,1,4,1,3,5,4,5,1,3,4,2,1,5,1,3,4,5,5,2,1,2,1,1,1,4,3,1,4,2,3,1,3,5,1,4,5,3,1,3,3,2,2,1,5,5,4,3,2,1,5,1,3,1,3,5,1,1,2,1,1,1,5,2,1,1,3,2,1,5,5,5,1,1,5,1,4,1,5,4,2,4,5,2,4,3,2,5,4,1,1,2,4,3,2,1")

;; Since the input and the example are in the form of integer-lists, it's
;; easy to stick with that model, and I did so successfully to solve part 1,
;; ending up with a ~300k-length list of individual fish after the prescribed 80 days
;; of simulation.

;; But part 2 asks us to simulate for 256 days, and the list of fish becomes
;; too long to process quickly on a single machine -- and although I hope someone
;; used [Hadoop](https://hadoop.apache.org/) to solve the puzzle, I went the easy
;; route.

;; The key insight is that modeling as a list of integers in the range [0, 8]
;; is grossly wasteful; there is no significance to the ordering, so we can model
;; as either:
;; * (Fast) A map of `{0 a, 1 b, ... 8 i}`, where the keys are tickers and the vals are counts
;; * (Faster, probably) A vector of `[a, b, ..., i]`, where the indices are tickers and the vals are counts
;; * (Fastest?) A circular vector of `[a, b, ..., i]`, paired with a `zero-index`, which gives the
;;   array index that is currently acting as the head

;; Last night I came up with the "fast" solution, and today while refactoring I came up with the
;; fastest (for me) solution. Rather than shift all of our array values down an index at every
;; simulation step, we can just increment a `zero-index` to "rotate" the array (and then account for
;; the reproducing fish).

;; So here's what I ultimately came up with:

(def new-fish-init-ticker 8)
(def old-fish-init-ticker 6)

(defn parse-fish-counts [input]
  (->> (str/split input #",")
       (map parse-long)
       (frequencies)
       ; Default absent fish-counts to 0
       (merge (into {} (map vector (range (inc new-fish-init-ticker))
                                   (repeat 0))))
       (sort)
       (mapv (fn [[_ v]] v))))

(def fish-counts (parse-fish-counts input))

(defn step-fish-counts [[fish-counts zero-index]]
  (let [reset-fish (get fish-counts zero-index)
        zero-index (mod (inc zero-index) (count fish-counts))
        reset-fish-idx (mod (+ zero-index old-fish-init-ticker)
                            (count fish-counts))
        fish-counts (update fish-counts reset-fish-idx + reset-fish)]
    [fish-counts zero-index]))

(defn simulate [fish-counts]
  (iterate step-fish-counts [fish-counts 0]))

(defn num-fish-after [fish days]
  (let [fish (-> fish (simulate) (nth days) (first))]
    (reduce + fish)))

(defn part-1 [fish-counts]
  (num-fish-after fish-counts 80))

(defn part-2 [fish-counts]
  (num-fish-after fish-counts 256))

(rcf/tests
  (time (part-1 fish-counts)) := 353079          ; => 0.262708 ms
  (time (part-2 fish-counts)) := 1605400130036)  ; => 0.247541 ms

;; Interestingly, 256 iterations seems to run faster than 80
;; iterations on maybe a quarter of runs, anecdotally, according to
;; Clojure's `time` macro. I should benchmark these as a sanity check:

(comment
  (bench/quick-bench (part-1 fish-counts))
  ; =>
  ;                Evaluation count : 67986 in 6 samples of 11331 calls.
  ;             Execution time mean : 9.123164 µs
  ;    Execution time std-deviation : 184.425417 ns
  ;   Execution time lower quantile : 8.904276 µs ( 2.5%)
  ;   Execution time upper quantile : 9.332687 µs (97.5%)
  ;                   Overhead used : 2.160607 ns
  (bench/quick-bench (part-2 fish-counts))
  ; =>
  ;                Evaluation count : 23364 in 6 samples of 3894 calls.
  ;             Execution time mean : 26.943226 µs
  ;    Execution time std-deviation : 1.069218 µs
  ;   Execution time lower quantile : 25.639478 µs ( 2.5%)
  ;   Execution time upper quantile : 28.172301 µs (97.5%)
  ;                   Overhead used : 2.160607 ns
  ,)

;; Sure enough, more iterations means a longer runtime. I'm honestly not sure
;; how Clojure's time macro differs from what criterium is doing, and why the
;; numbers differ by orders of magnitude. Please let me know if you know; otherwise
;; it's a question for another day!

;; ⭐️⭐️







