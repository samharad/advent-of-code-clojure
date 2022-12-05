(ns aoc.2022.day.05
  (:require [clojure.string :as str]))

(defn- transpose [matrix]
  (apply mapv vector matrix))

(def input "    [C]             [L]         [T]\n    [V] [R] [M]     [T]         [B]\n    [F] [G] [H] [Q] [Q]         [H]\n    [W] [L] [P] [V] [M] [V]     [F]\n    [P] [C] [W] [S] [Z] [B] [S] [P]\n[G] [R] [M] [B] [F] [J] [S] [Z] [D]\n[J] [L] [P] [F] [C] [H] [F] [J] [C]\n[Z] [Q] [F] [L] [G] [W] [H] [F] [M]\n 1   2   3   4   5   6   7   8   9 \n\nmove 1 from 5 to 6\nmove 5 from 6 to 7\nmove 10 from 7 to 3\nmove 4 from 8 to 4\nmove 2 from 5 to 4\nmove 4 from 3 to 6\nmove 6 from 2 to 4\nmove 8 from 6 to 9\nmove 5 from 9 to 2\nmove 7 from 2 to 7\nmove 2 from 1 to 4\nmove 3 from 3 to 8\nmove 1 from 5 to 9\nmove 1 from 3 to 8\nmove 1 from 1 to 2\nmove 11 from 4 to 6\nmove 2 from 5 to 6\nmove 10 from 9 to 1\nmove 4 from 8 to 3\nmove 7 from 7 to 1\nmove 9 from 1 to 2\nmove 1 from 6 to 5\nmove 1 from 5 to 9\nmove 5 from 3 to 8\nmove 2 from 9 to 1\nmove 5 from 3 to 9\nmove 3 from 6 to 8\nmove 5 from 9 to 6\nmove 6 from 6 to 3\nmove 3 from 3 to 2\nmove 1 from 9 to 8\nmove 13 from 2 to 3\nmove 3 from 8 to 1\nmove 11 from 1 to 4\nmove 3 from 4 to 1\nmove 2 from 6 to 5\nmove 4 from 6 to 8\nmove 17 from 3 to 9\nmove 1 from 1 to 8\nmove 1 from 6 to 5\nmove 1 from 3 to 7\nmove 1 from 7 to 4\nmove 3 from 4 to 1\nmove 1 from 3 to 8\nmove 4 from 8 to 1\nmove 3 from 5 to 9\nmove 1 from 6 to 4\nmove 4 from 4 to 8\nmove 2 from 8 to 4\nmove 2 from 1 to 6\nmove 4 from 8 to 6\nmove 1 from 8 to 3\nmove 6 from 6 to 3\nmove 6 from 3 to 9\nmove 6 from 1 to 4\nmove 5 from 8 to 4\nmove 1 from 3 to 6\nmove 3 from 1 to 7\nmove 1 from 6 to 7\nmove 4 from 4 to 5\nmove 24 from 9 to 5\nmove 2 from 9 to 1\nmove 27 from 5 to 7\nmove 13 from 7 to 2\nmove 1 from 5 to 9\nmove 7 from 2 to 7\nmove 1 from 9 to 8\nmove 5 from 2 to 8\nmove 1 from 2 to 5\nmove 1 from 5 to 7\nmove 21 from 4 to 1\nmove 1 from 4 to 6\nmove 1 from 6 to 5\nmove 22 from 7 to 5\nmove 2 from 7 to 8\nmove 7 from 5 to 4\nmove 1 from 4 to 5\nmove 2 from 7 to 9\nmove 5 from 5 to 2\nmove 5 from 4 to 2\nmove 3 from 5 to 1\nmove 7 from 8 to 7\nmove 1 from 4 to 1\nmove 23 from 1 to 8\nmove 2 from 9 to 4\nmove 11 from 8 to 3\nmove 3 from 1 to 3\nmove 1 from 4 to 2\nmove 12 from 3 to 2\nmove 7 from 7 to 3\nmove 3 from 2 to 1\nmove 1 from 4 to 9\nmove 1 from 1 to 3\nmove 9 from 8 to 6\nmove 2 from 5 to 4\nmove 3 from 1 to 7\nmove 3 from 2 to 4\nmove 7 from 2 to 3\nmove 9 from 3 to 4\nmove 7 from 5 to 2\nmove 2 from 7 to 2\nmove 1 from 7 to 2\nmove 13 from 4 to 6\nmove 1 from 9 to 8\nmove 2 from 8 to 2\nmove 12 from 2 to 1\nmove 3 from 3 to 1\nmove 1 from 8 to 1\nmove 5 from 3 to 7\nmove 3 from 2 to 8\nmove 7 from 2 to 5\nmove 3 from 8 to 3\nmove 1 from 4 to 8\nmove 22 from 6 to 4\nmove 1 from 3 to 6\nmove 3 from 5 to 8\nmove 4 from 5 to 8\nmove 1 from 3 to 9\nmove 8 from 4 to 2\nmove 8 from 8 to 3\nmove 1 from 6 to 3\nmove 4 from 2 to 6\nmove 1 from 9 to 4\nmove 5 from 3 to 9\nmove 2 from 8 to 1\nmove 3 from 2 to 1\nmove 10 from 4 to 8\nmove 4 from 7 to 6\nmove 10 from 1 to 3\nmove 9 from 8 to 2\nmove 1 from 7 to 1\nmove 15 from 3 to 1\nmove 1 from 8 to 9\nmove 4 from 4 to 1\nmove 17 from 1 to 3\nmove 3 from 2 to 3\nmove 3 from 6 to 8\nmove 5 from 9 to 7\nmove 11 from 1 to 8\nmove 4 from 7 to 8\nmove 6 from 2 to 5\nmove 2 from 1 to 4\nmove 4 from 6 to 8\nmove 16 from 8 to 6\nmove 2 from 6 to 1\nmove 1 from 9 to 5\nmove 1 from 7 to 5\nmove 2 from 5 to 6\nmove 5 from 6 to 3\nmove 2 from 8 to 5\nmove 1 from 2 to 1\nmove 10 from 6 to 3\nmove 6 from 5 to 9\nmove 2 from 1 to 2\nmove 2 from 4 to 2\nmove 1 from 2 to 4\nmove 5 from 9 to 2\nmove 1 from 4 to 3\nmove 1 from 9 to 7\nmove 1 from 6 to 1\nmove 1 from 1 to 7\nmove 2 from 7 to 5\nmove 7 from 2 to 5\nmove 6 from 5 to 1\nmove 1 from 2 to 3\nmove 1 from 4 to 1\nmove 2 from 8 to 9\nmove 8 from 1 to 3\nmove 2 from 5 to 3\nmove 29 from 3 to 9\nmove 5 from 3 to 8\nmove 6 from 8 to 5\nmove 1 from 6 to 5\nmove 6 from 3 to 2\nmove 2 from 2 to 4\nmove 1 from 1 to 7\nmove 18 from 9 to 6\nmove 2 from 2 to 9\nmove 2 from 2 to 8\nmove 13 from 6 to 8\nmove 1 from 7 to 4\nmove 3 from 5 to 6\nmove 1 from 5 to 7\nmove 1 from 7 to 4\nmove 14 from 9 to 3\nmove 3 from 4 to 5\nmove 1 from 9 to 7\nmove 14 from 3 to 2\nmove 1 from 7 to 3\nmove 4 from 2 to 5\nmove 16 from 8 to 6\nmove 11 from 6 to 9\nmove 13 from 6 to 4\nmove 5 from 5 to 2\nmove 12 from 2 to 4\nmove 19 from 4 to 3\nmove 7 from 4 to 5\nmove 14 from 5 to 2\nmove 2 from 3 to 6\nmove 3 from 9 to 5\nmove 2 from 6 to 2\nmove 1 from 5 to 2\nmove 3 from 5 to 4\nmove 3 from 4 to 1\nmove 7 from 9 to 6\nmove 4 from 6 to 1\nmove 1 from 1 to 8\nmove 3 from 6 to 9\nmove 1 from 8 to 7\nmove 1 from 9 to 6\nmove 4 from 1 to 2\nmove 1 from 7 to 2\nmove 2 from 9 to 8\nmove 10 from 2 to 9\nmove 2 from 2 to 9\nmove 11 from 3 to 7\nmove 1 from 8 to 9\nmove 2 from 3 to 7\nmove 1 from 1 to 7\nmove 10 from 2 to 4\nmove 3 from 4 to 1\nmove 4 from 1 to 8\nmove 1 from 6 to 5\nmove 6 from 7 to 9\nmove 3 from 9 to 1\nmove 1 from 5 to 1\nmove 4 from 4 to 2\nmove 5 from 2 to 1\nmove 1 from 2 to 7\nmove 2 from 7 to 6\nmove 1 from 2 to 1\nmove 2 from 9 to 1\nmove 3 from 4 to 7\nmove 1 from 3 to 7\nmove 2 from 8 to 3\nmove 2 from 6 to 5\nmove 2 from 5 to 8\nmove 10 from 7 to 2\nmove 6 from 9 to 1\nmove 1 from 7 to 3\nmove 2 from 8 to 9\nmove 7 from 3 to 7\nmove 7 from 3 to 9\nmove 1 from 8 to 9\nmove 6 from 2 to 8\nmove 13 from 9 to 1\nmove 6 from 9 to 8\nmove 2 from 2 to 7\nmove 3 from 7 to 1\nmove 1 from 8 to 1\nmove 1 from 1 to 6\nmove 16 from 1 to 4\nmove 2 from 7 to 5\nmove 12 from 4 to 9\nmove 4 from 8 to 6\nmove 2 from 5 to 1\nmove 8 from 8 to 4\nmove 2 from 4 to 5\nmove 1 from 8 to 6\nmove 4 from 6 to 8\nmove 19 from 1 to 9\nmove 3 from 8 to 5\nmove 1 from 6 to 9\nmove 2 from 2 to 1\nmove 10 from 4 to 9\nmove 1 from 1 to 2\nmove 2 from 1 to 5\nmove 4 from 7 to 9\nmove 1 from 8 to 2\nmove 1 from 2 to 6\nmove 7 from 5 to 4\nmove 11 from 9 to 8\nmove 1 from 4 to 3\nmove 10 from 8 to 1\nmove 1 from 2 to 3\nmove 29 from 9 to 3\nmove 2 from 6 to 5\nmove 1 from 5 to 3\nmove 5 from 9 to 3\nmove 1 from 8 to 9\nmove 1 from 9 to 3\nmove 6 from 4 to 6\nmove 1 from 5 to 1\nmove 1 from 6 to 3\nmove 2 from 1 to 5\nmove 1 from 9 to 5\nmove 37 from 3 to 2\nmove 3 from 6 to 2\nmove 1 from 6 to 2\nmove 1 from 6 to 4\nmove 3 from 1 to 3\nmove 2 from 1 to 6\nmove 35 from 2 to 1\nmove 1 from 6 to 8\nmove 5 from 1 to 8\nmove 7 from 1 to 6\nmove 5 from 3 to 7\nmove 1 from 8 to 7\nmove 3 from 7 to 5\nmove 4 from 2 to 9\nmove 1 from 2 to 1\nmove 1 from 4 to 3\nmove 3 from 7 to 1\nmove 1 from 3 to 6\nmove 1 from 1 to 9\nmove 5 from 9 to 2\nmove 18 from 1 to 3\nmove 6 from 1 to 8\nmove 6 from 3 to 7\nmove 4 from 8 to 6\nmove 4 from 6 to 7\nmove 9 from 7 to 8\nmove 3 from 2 to 7\nmove 4 from 6 to 1\nmove 3 from 5 to 3\nmove 3 from 2 to 5\nmove 3 from 6 to 1\nmove 4 from 7 to 4\nmove 6 from 5 to 9\nmove 3 from 1 to 9\nmove 1 from 6 to 1\nmove 15 from 8 to 2\nmove 1 from 8 to 5\nmove 3 from 4 to 8\nmove 1 from 5 to 1\nmove 1 from 6 to 5\nmove 11 from 3 to 9\nmove 12 from 2 to 3\nmove 3 from 8 to 1\nmove 15 from 1 to 2\nmove 8 from 9 to 4\nmove 8 from 4 to 9\nmove 4 from 2 to 5\nmove 1 from 4 to 6\nmove 1 from 2 to 8\nmove 1 from 6 to 7\nmove 4 from 3 to 1\nmove 1 from 8 to 5\nmove 5 from 3 to 9\nmove 14 from 9 to 2\nmove 1 from 7 to 4\nmove 4 from 1 to 3\nmove 1 from 4 to 7\nmove 8 from 3 to 7\nmove 8 from 7 to 5\nmove 1 from 7 to 9\nmove 3 from 3 to 2\nmove 7 from 9 to 8\nmove 1 from 9 to 5\nmove 2 from 8 to 5\nmove 7 from 5 to 4\nmove 4 from 9 to 2\nmove 6 from 4 to 3\nmove 18 from 2 to 5\nmove 1 from 4 to 7\nmove 15 from 5 to 4\nmove 1 from 4 to 6\nmove 2 from 2 to 7\nmove 3 from 8 to 5\nmove 1 from 7 to 3\nmove 8 from 2 to 6\nmove 4 from 2 to 3\nmove 1 from 7 to 5\nmove 3 from 4 to 6\nmove 5 from 6 to 9\nmove 8 from 5 to 6\nmove 2 from 4 to 3\nmove 7 from 4 to 2\nmove 2 from 8 to 5\nmove 7 from 5 to 6\nmove 3 from 5 to 8\nmove 1 from 8 to 9\nmove 13 from 3 to 8\nmove 2 from 2 to 7\nmove 9 from 8 to 9\nmove 6 from 8 to 5\nmove 5 from 5 to 2\nmove 2 from 7 to 8\nmove 9 from 2 to 5\nmove 1 from 7 to 5\nmove 1 from 5 to 7\nmove 21 from 6 to 2\nmove 1 from 7 to 8\nmove 3 from 8 to 9\nmove 1 from 4 to 2\nmove 23 from 2 to 7\nmove 8 from 9 to 8\nmove 20 from 7 to 4\nmove 3 from 7 to 2\nmove 1 from 2 to 7\nmove 1 from 6 to 7\nmove 3 from 5 to 4\nmove 8 from 5 to 9\nmove 2 from 7 to 1\nmove 1 from 8 to 7\nmove 4 from 2 to 4\nmove 2 from 8 to 7\nmove 2 from 8 to 2\nmove 1 from 7 to 6\nmove 3 from 9 to 7\nmove 2 from 2 to 7\nmove 5 from 7 to 1\nmove 8 from 9 to 6\nmove 15 from 4 to 3\nmove 4 from 4 to 7\nmove 6 from 1 to 4\nmove 11 from 3 to 4\nmove 8 from 6 to 1\nmove 24 from 4 to 7\nmove 6 from 1 to 8\nmove 27 from 7 to 3\nmove 2 from 7 to 8\nmove 5 from 8 to 3\nmove 4 from 8 to 4\nmove 1 from 8 to 6\nmove 1 from 6 to 9\nmove 1 from 6 to 5\nmove 2 from 4 to 2\nmove 1 from 8 to 1\nmove 1 from 5 to 2\nmove 4 from 1 to 6\nmove 1 from 7 to 5\nmove 1 from 5 to 8\nmove 1 from 8 to 7\nmove 1 from 7 to 8\nmove 1 from 8 to 1\nmove 1 from 2 to 3\nmove 2 from 4 to 8\nmove 7 from 9 to 6\nmove 2 from 8 to 1\nmove 3 from 3 to 8\nmove 3 from 1 to 8\nmove 2 from 2 to 3\nmove 1 from 4 to 1\nmove 1 from 1 to 8\nmove 5 from 8 to 3\nmove 8 from 6 to 2\nmove 1 from 9 to 4\nmove 2 from 4 to 8\nmove 2 from 8 to 3\nmove 2 from 6 to 2\nmove 33 from 3 to 2\nmove 2 from 8 to 7\nmove 1 from 6 to 1\nmove 1 from 1 to 7\nmove 2 from 3 to 8\nmove 2 from 8 to 4\nmove 1 from 4 to 8\nmove 2 from 7 to 2\nmove 2 from 3 to 7\nmove 12 from 2 to 1\nmove 1 from 8 to 4\nmove 1 from 4 to 8\nmove 1 from 4 to 3\nmove 1 from 8 to 2\nmove 3 from 7 to 2\nmove 37 from 2 to 7\nmove 1 from 1 to 7\nmove 12 from 7 to 1\nmove 13 from 1 to 7\nmove 1 from 3 to 4\nmove 35 from 7 to 6\nmove 1 from 4 to 5\nmove 3 from 7 to 4\nmove 1 from 5 to 7\nmove 2 from 3 to 4\nmove 23 from 6 to 9\nmove 3 from 1 to 5\nmove 3 from 3 to 7\nmove 1 from 3 to 6\nmove 2 from 5 to 3\nmove 23 from 9 to 8\nmove 2 from 4 to 9\nmove 16 from 8 to 2\nmove 2 from 7 to 3\nmove 1 from 5 to 8\nmove 3 from 7 to 6\nmove 1 from 9 to 8\nmove 3 from 8 to 1\nmove 1 from 9 to 1\nmove 11 from 6 to 5\nmove 2 from 4 to 1\nmove 4 from 8 to 6\nmove 16 from 2 to 3\nmove 9 from 1 to 9\nmove 1 from 8 to 4\nmove 3 from 9 to 3\nmove 1 from 1 to 4\nmove 1 from 9 to 4\nmove 7 from 5 to 2\nmove 6 from 2 to 5\nmove 1 from 8 to 6\nmove 22 from 3 to 7\nmove 8 from 5 to 8\nmove 4 from 4 to 9\nmove 2 from 1 to 8\nmove 16 from 7 to 2\nmove 1 from 3 to 5\nmove 14 from 2 to 7\nmove 2 from 2 to 4\nmove 6 from 9 to 3\n")

(def num-stacks 9)

(defn parse-stacks [n-stacks lines]
  (let [w (count (first lines))
        cols-per-stack (inc (int (/ w n-stacks)))
        partitioned (->> lines
                         (map #(partition-all cols-per-stack %)))
        rows (mapv (partial mapv second) partitioned)
        cols (->> (transpose rows)
                  (mapv #(filterv (complement #{\space}) %))
                  (mapv (comp vec reverse)))]
    cols))

(defn parse-move [line]
  (let [[_ n _ f _ t] (str/split line #" ")
        [n f t] (map parse-long [n f t])]
    {:n n :from (dec f) :to (dec t)}))

(defn parse-procedure [lines]
  (map parse-move lines))

(defrecord CrateScenario [stacks procedure])

(defn parse-crate-scenario [input]
  (let [lines (str/split-lines input)
        [stack-lines [_ _ & proc-lines]] (split-at 8 lines)
        stacks (parse-stacks num-stacks stack-lines)
        procedure (parse-procedure proc-lines)]
    (->CrateScenario stacks procedure)))

(defn apply-move [{:keys [n from to]} stacks]
  (loop [i n
         stacks stacks]
    (if (zero? i)
      stacks
      (let [top (peek (stacks from))]
        (recur (dec i) (-> stacks
                           (update from pop)
                           (update to conj top)))))))

(defn step [apply-move {:keys [stacks procedure]}]
  (let [[move & procedure'] procedure
        stacks' (apply-move move stacks)]
    (->CrateScenario stacks' procedure')))

(defn simulate [apply-move crate-scenario]
  (some #(when (empty? (:procedure %)) %)
        (iterate (partial step apply-move) crate-scenario)))

(defn tops [{:keys [stacks]}]
  (apply str (mapv peek stacks)))

(tops (simulate apply-move (parse-crate-scenario input)))

;;;

(defn apply-move' [{:keys [n from to]} stacks]
  (let [from-stack (stacks from)
        moving (take-last n from-stack)
        from-stack' (vec (drop-last n from-stack))
        to-stack' (vec (concat (stacks to) moving))]
    (-> stacks
        (assoc from from-stack')
        (assoc to to-stack'))))

(tops (simulate apply-move' (parse-crate-scenario input)))
