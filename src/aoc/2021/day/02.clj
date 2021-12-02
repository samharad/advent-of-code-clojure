;; [Day 2](https://adventofcode.com/2021/day/2) was another midnight attempt for me.
;; I went for speed but didn't crack the top thousand solves. Now, after a night's sleep,
;; I'll do some cleanup and add some commentary.

(ns aoc.2021.day.02
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.string :as str]))

(rcf/enable!)

;; It's another familiar AoC puzzle -- a list of commands to interpret -- but whereas
;; we're usually thinking in cartesian `(x, y)`, our submarine has a horizontal axis and a depth.

(def input "forward 6\nforward 6\ndown 6\ndown 5\nup 2\nforward 4\nforward 8\nup 9\nforward 3\ndown 1\nforward 2\nforward 3\ndown 1\ndown 3\ndown 3\nforward 9\ndown 6\nforward 1\nup 5\ndown 1\nforward 8\nforward 7\nup 8\nforward 7\ndown 9\nforward 5\nforward 4\nforward 2\ndown 3\ndown 6\ndown 6\ndown 5\nforward 5\nforward 4\nforward 8\nup 3\nforward 6\ndown 4\nup 2\nforward 8\nforward 6\nforward 1\nup 2\ndown 9\ndown 9\nup 5\ndown 1\nup 1\nup 3\nforward 2\nforward 4\ndown 8\ndown 1\nup 1\nup 4\nup 1\nup 2\nforward 4\ndown 1\nforward 1\ndown 3\nforward 4\ndown 1\ndown 6\nforward 3\ndown 9\ndown 4\nup 4\ndown 3\nforward 4\ndown 3\nup 4\nforward 5\ndown 9\nforward 4\nforward 1\nforward 1\nforward 4\nup 6\nup 9\ndown 1\ndown 1\nforward 6\ndown 1\ndown 5\ndown 4\nforward 8\ndown 8\ndown 2\ndown 5\ndown 6\ndown 4\ndown 9\nup 8\ndown 4\nforward 5\nup 6\nforward 2\nforward 9\ndown 5\nforward 3\nforward 6\ndown 9\nup 3\nforward 7\nforward 1\nforward 1\nup 6\nforward 3\ndown 3\ndown 1\nup 7\nforward 2\nforward 9\nforward 4\ndown 9\nforward 4\nforward 5\nup 7\ndown 1\nup 9\ndown 6\nup 5\nforward 9\nforward 9\ndown 4\nforward 1\nforward 2\nforward 1\ndown 2\nforward 7\nup 6\nup 5\nup 6\ndown 4\ndown 6\ndown 9\nforward 9\ndown 9\ndown 1\ndown 2\nup 7\nforward 3\ndown 2\nup 8\nforward 5\nforward 2\nup 2\ndown 9\ndown 2\ndown 8\nforward 8\ndown 2\ndown 8\nforward 3\nup 1\nforward 7\nforward 1\nup 9\nforward 1\nforward 1\nforward 1\ndown 1\ndown 6\nforward 2\ndown 8\ndown 9\nforward 3\nup 9\ndown 5\ndown 2\nforward 7\nforward 1\nforward 6\ndown 5\ndown 4\ndown 2\ndown 7\ndown 1\nforward 8\ndown 3\nup 8\nforward 2\ndown 6\nforward 9\nup 6\nforward 3\nforward 7\ndown 3\ndown 8\ndown 8\ndown 7\ndown 8\nforward 3\ndown 1\nforward 4\ndown 8\nforward 1\nforward 1\nforward 4\nforward 6\nup 9\nforward 8\nup 6\nforward 4\nforward 4\ndown 1\ndown 7\nup 9\nforward 5\ndown 9\ndown 1\nup 2\ndown 7\nforward 8\nforward 9\nforward 6\nforward 8\nup 1\nforward 2\ndown 7\nup 9\nup 5\nforward 6\nforward 7\ndown 4\nforward 1\ndown 2\ndown 7\ndown 4\ndown 8\ndown 4\nforward 7\ndown 2\ndown 7\nforward 5\ndown 3\nforward 6\nup 5\nup 9\ndown 5\nup 2\nup 6\nforward 6\nforward 9\ndown 8\nforward 8\nforward 8\nforward 3\nup 2\nforward 4\ndown 9\ndown 3\nup 2\ndown 9\nforward 9\nforward 8\nforward 6\nforward 4\nup 8\ndown 3\nup 7\nup 7\nup 5\nup 3\nforward 3\nup 7\nup 8\ndown 6\ndown 3\ndown 4\ndown 1\nforward 1\nforward 3\ndown 6\ndown 6\nforward 2\nup 1\nforward 9\nup 1\nforward 9\ndown 1\nforward 2\nforward 3\nup 3\ndown 7\nforward 6\nup 4\nforward 5\nup 4\nforward 4\ndown 6\ndown 9\ndown 8\ndown 1\nforward 8\nup 4\nforward 6\ndown 8\ndown 7\ndown 9\nforward 7\nforward 4\ndown 4\nforward 8\nup 4\ndown 7\ndown 1\ndown 7\nup 6\nforward 3\ndown 8\ndown 6\ndown 5\ndown 7\ndown 5\nforward 3\nforward 5\ndown 2\ndown 8\nup 4\nforward 9\ndown 5\ndown 1\nforward 6\ndown 2\ndown 6\ndown 3\nup 3\nup 5\nforward 8\nup 2\ndown 4\ndown 5\nup 4\nforward 1\nforward 2\nup 4\nforward 7\nforward 2\nforward 4\nforward 6\ndown 1\ndown 9\nup 2\ndown 7\ndown 6\nup 1\nup 2\nforward 7\nforward 9\nforward 4\nforward 6\ndown 4\nup 7\nup 2\nforward 3\ndown 8\nup 5\nup 7\ndown 4\ndown 4\nforward 1\nforward 8\nforward 4\nforward 1\nup 8\ndown 3\ndown 5\ndown 7\nup 2\nforward 6\ndown 6\ndown 8\nforward 2\nup 7\ndown 6\ndown 6\nup 4\nup 6\nup 4\ndown 3\nforward 9\nup 4\nforward 8\nforward 7\ndown 5\ndown 4\ndown 3\nforward 7\nforward 3\nup 7\nforward 5\ndown 2\nforward 4\nforward 3\nforward 1\ndown 9\nup 2\nup 3\nup 7\nup 6\nforward 1\nup 3\ndown 3\nup 9\nforward 2\nforward 7\nforward 6\nforward 2\nforward 9\nforward 9\nforward 5\nup 2\ndown 6\ndown 3\ndown 2\nforward 7\ndown 4\nforward 1\nup 7\nforward 8\ndown 5\ndown 6\ndown 7\nup 5\nforward 6\nforward 5\nup 5\nup 6\ndown 4\nup 8\nup 3\nforward 9\ndown 4\ndown 4\ndown 7\nup 7\ndown 8\ndown 7\nforward 2\nforward 9\ndown 2\ndown 1\nforward 5\ndown 2\nforward 7\ndown 5\ndown 4\ndown 7\nforward 9\nforward 2\ndown 6\nforward 8\ndown 6\ndown 6\nup 8\nforward 9\nup 4\ndown 9\nforward 7\nup 1\nup 2\nforward 9\ndown 9\ndown 6\ndown 5\nforward 2\ndown 9\ndown 1\nforward 1\ndown 7\ndown 6\nup 6\ndown 4\nforward 9\nup 5\ndown 3\ndown 9\nforward 5\ndown 2\nforward 1\nforward 4\nforward 1\nforward 1\nforward 4\ndown 2\nup 3\nforward 9\ndown 5\ndown 2\nforward 5\ndown 6\ndown 4\nforward 9\nforward 3\nforward 4\nforward 9\nforward 5\nforward 3\ndown 5\nup 9\ndown 5\nforward 8\ndown 9\nforward 7\ndown 3\nup 3\ndown 7\nup 2\nforward 5\nforward 3\nup 7\ndown 1\nforward 2\ndown 9\ndown 5\ndown 2\nforward 6\nforward 6\nforward 5\ndown 5\ndown 1\ndown 4\ndown 7\nforward 4\nforward 3\nforward 1\nforward 4\ndown 1\nup 7\nup 5\nforward 2\nup 3\ndown 2\nforward 2\nforward 8\ndown 7\nforward 9\nforward 8\ndown 4\ndown 5\nforward 4\nforward 7\nup 9\ndown 5\nforward 4\ndown 7\nforward 5\ndown 8\nforward 5\nforward 2\nforward 7\nforward 3\nforward 1\nforward 2\nup 1\nup 5\nup 1\nup 3\ndown 9\nup 9\ndown 8\nforward 4\ndown 3\nforward 7\ndown 6\nforward 1\ndown 7\nup 3\nforward 1\nforward 6\nup 9\ndown 6\nforward 3\ndown 1\nforward 7\ndown 9\nup 3\nup 9\nforward 6\nup 1\nforward 5\nforward 7\nforward 7\nup 7\ndown 2\nup 7\ndown 8\nforward 7\nup 5\ndown 9\nup 1\nforward 4\nforward 4\nforward 9\ndown 6\nup 3\ndown 8\ndown 8\nup 2\ndown 8\ndown 8\nup 7\ndown 8\nup 2\nup 4\nup 1\nforward 7\nforward 9\nforward 9\ndown 4\nup 8\nforward 9\ndown 9\nup 1\nforward 1\nforward 1\ndown 5\nup 7\ndown 8\nforward 4\nforward 3\ndown 7\nforward 8\nup 2\ndown 2\ndown 6\ndown 4\nforward 9\nforward 7\ndown 9\ndown 4\nforward 8\ndown 5\nforward 7\ndown 2\nforward 6\nup 8\nforward 3\ndown 5\nforward 2\nforward 6\ndown 9\nup 6\nup 9\nup 2\nforward 2\ndown 2\nforward 5\ndown 7\ndown 8\ndown 4\ndown 5\nforward 6\nforward 2\nup 9\ndown 3\nforward 3\nup 8\nforward 2\ndown 9\nforward 4\nforward 1\nforward 1\nup 3\nup 9\nforward 6\ndown 5\ndown 2\nup 2\nup 9\nforward 9\nforward 6\nforward 3\nforward 9\nup 3\nforward 9\nup 4\nup 5\nforward 6\nforward 6\ndown 8\nforward 5\ndown 9\nup 5\nforward 5\ndown 8\ndown 3\nup 8\ndown 2\nforward 4\nforward 6\nup 4\ndown 3\ndown 3\ndown 5\nup 8\ndown 7\ndown 4\nforward 9\nforward 2\ndown 1\ndown 8\nforward 8\nup 9\nforward 3\ndown 2\nup 8\ndown 9\nup 2\ndown 3\nforward 8\nforward 8\nforward 2\ndown 3\nforward 7\ndown 7\ndown 4\nforward 9\nforward 9\ndown 5\nup 7\nforward 2\nup 7\nup 1\nforward 4\nup 2\ndown 4\ndown 7\ndown 4\ndown 1\nup 3\ndown 5\ndown 5\nforward 6\nforward 2\ndown 2\nforward 9\ndown 4\nup 8\nforward 9\nforward 9\nup 7\nforward 4\nforward 9\nforward 2\nforward 2\nforward 1\nforward 6\ndown 3\ndown 5\nforward 7\nup 4\nforward 9\nforward 9\nup 1\nforward 9\ndown 5\nup 2\nup 2\ndown 5\ndown 5\nforward 7\ndown 1\nforward 5\nup 8\nup 9\ndown 9\nforward 3\nup 1\nforward 6\ndown 8\ndown 2\nforward 6\nup 9\ndown 3\ndown 1\ndown 1\nup 2\nup 1\nup 9\nforward 5\nforward 6\ndown 7\nforward 1\ndown 3\nforward 6\nforward 2\nforward 9\nforward 7\nup 5\ndown 4\ndown 6\ndown 2\ndown 5\nforward 7\nup 1\ndown 7\ndown 4\ndown 7\ndown 4\nforward 2\nforward 8\nup 7\nup 9\ndown 6\nup 8\nup 3\nup 3\nup 5\ndown 1\nforward 4\nforward 9\nforward 1\ndown 9\nup 5\ndown 3\ndown 1\ndown 1\nup 3\ndown 1\nup 2\nup 2\ndown 5\nforward 8\ndown 8\nup 6\nforward 9\nup 1\nup 3\ndown 4\nforward 7\nup 4\nforward 3\ndown 8\nforward 6\ndown 6\nforward 1\ndown 4\ndown 7\nup 3\ndown 4\nforward 5\nforward 4\ndown 6\nup 4\ndown 6\nup 8\nforward 1\nup 8\nforward 3\ndown 8\nforward 3\ndown 9\nforward 5\ndown 6\nforward 5\nforward 7\nforward 8\ndown 8\ndown 1\ndown 1\ndown 7\nforward 9\ndown 4\nforward 3\ndown 8\ndown 4\ndown 6\ndown 8\nforward 5\ndown 5\ndown 1\ndown 9\ndown 6\ndown 7\ndown 1\nforward 7\ndown 2\ndown 9\ndown 9\ndown 8\ndown 9\nforward 9\ndown 8\nforward 1\nup 2\nforward 4\nup 2\nup 7\nforward 1\ndown 9\nup 7\nforward 4\nforward 2\ndown 2\ndown 5\ndown 7\ndown 4\nforward 8\nup 2\nforward 1\nforward 5\ndown 7\nforward 3\nforward 6\ndown 3\nup 5\nup 8\ndown 5\ndown 1\ndown 7\ndown 6\nforward 2\nforward 3\nforward 7\nforward 6\ndown 2\ndown 4\ndown 1\ndown 5\ndown 4\ndown 7\nup 5\ndown 4\nup 9\nforward 7\ndown 9\ndown 9\nforward 3\nforward 9\ndown 5\nforward 1\nup 1\ndown 4\nforward 7\nup 4\ndown 5\nforward 8\nforward 3\nforward 6\nforward 7\ndown 8\ndown 3\nforward 8\ndown 8\nforward 7\ndown 4\ndown 2\ndown 8\ndown 3\nforward 4\ndown 5\nup 5\ndown 9\nup 5\nup 4\nup 3\nforward 7\nforward 8\nforward 9\nforward 5\ndown 7\ndown 2\nforward 2\ndown 2\nforward 5\nforward 2\nforward 6\ndown 4\ndown 5\ndown 7\nforward 3\nforward 3\nforward 9\nforward 6\ndown 2\nforward 3\ndown 5\nforward 5\nforward 9\nforward 6\nup 4\nforward 6\nforward 9\ndown 3\ndown 2\nforward 9\ndown 1\nup 1\nforward 1\nup 3\nforward 3\nforward 6\nup 4\nup 4\nforward 8\nforward 1\nforward 2\n")

;; As usually, there's no need to handle deviant input when parsing:

(defn parse-command [line]
  (let [[dir x-str] (str/split line #"\s")
        x (parse-long x-str)]
    [dir x]))

(defn parse-commands [input]
  (->> (str/split-lines input)
       (map parse-command (str/split-lines input))))

(def commands (parse-commands input))

;; Now for the cleaned-up solution algos. All we need to do is take an initial
;; state, `horizontal: 0, depth: 0`, and, for part 2, `aim: 0`, and fold our commands
;; into it one by one.

;; At midnight, I used a vector of `[horz depth aim]` for my state,
;; but I'll go with a map now for the sake of over-engineering.

(def init-sub {:horz  0
               :depth 0
               :aim   0})

(defn pilot-sub
  "Pilot the submarine according to the given commands and interpreter-f,
  which is a function with signature (f sub-state command)."
  ([commands interpreter-f] (pilot-sub commands interpreter-f init-sub))
  ([commands interpreter-f init] (reduce interpreter-f init commands)))

;; Wrapping `reduce` like this is probably overly-DRY, but why not?

;; We also need a function for encoding the solution as an AoC-legible number:

(defn encode-solution [{:keys [horz depth]}]
  (* horz depth))

;; Part 1:

(defn part-1-command-handler [state [dir x]]
  (case dir
    "forward" (update state :horz + x)
    "down"    (update state :depth + x)
    "up"      (update state :depth - x)))

(defn part-1 []
  (encode-solution
    (pilot-sub commands part-1-command-handler)))

;; Part 2:

(defn part-2-command-handler [{:keys [aim] :as state} [dir x]]
  (case dir
    "forward" (-> state
                  (update :horz + x)
                  (update :depth + (* aim x)))
    "down"    (update state :aim + x)
    "up"      (update state :aim - x)))

(defn part-2 []
  (encode-solution
    (pilot-sub commands part-2-command-handler)))

(rcf/tests
  (part-1) := 1815044
  (part-2) := 1739283308)

;; ⭐️⭐️