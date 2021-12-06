(ns aoc.2021.day.06
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.string :as str]))

(def input "4,5,3,2,3,3,2,4,2,1,2,4,5,2,2,2,4,1,1,1,5,1,1,2,5,2,1,1,4,4,5,5,1,2,1,1,5,3,5,2,4,3,2,4,5,3,2,1,4,1,3,1,2,4,1,1,4,1,4,2,5,1,4,3,5,2,4,5,4,2,2,5,1,1,2,4,1,4,4,1,1,3,1,2,3,2,5,5,1,1,5,2,4,2,2,4,1,1,1,4,2,2,3,1,2,4,5,4,5,4,2,3,1,4,1,3,1,2,3,3,2,4,3,3,3,1,4,2,3,4,2,1,5,4,2,4,4,3,2,1,5,3,1,4,1,1,5,4,2,4,2,2,4,4,4,1,4,2,4,1,1,3,5,1,5,5,1,3,2,2,3,5,3,1,1,4,4,1,3,3,3,5,1,1,2,5,5,5,2,4,1,5,1,2,1,1,1,4,3,1,5,2,3,1,3,1,4,1,3,5,4,5,1,3,4,2,1,5,1,3,4,5,5,2,1,2,1,1,1,4,3,1,4,2,3,1,3,5,1,4,5,3,1,3,3,2,2,1,5,5,4,3,2,1,5,1,3,1,3,5,1,1,2,1,1,1,5,2,1,1,3,2,1,5,5,5,1,1,5,1,4,1,5,4,2,4,5,2,4,3,2,5,4,1,1,2,4,3,2,1")

(defn parse-fish [input]
  (->> (str/split input #",")
       (map parse-long)))

(def fish (parse-fish input))

(defn step-fish-counts [fish-counts]
  (reduce (fn [acc [time count]]
            (if (zero? time)
              (-> acc
                  (update 6 (partial + count))
                  (update 8 (partial + count)))
              (update acc (dec time) (partial + count))))
          (into {} (map vector (range 9) (repeat 0)))
          fish-counts))

(defn simulate [fish-counts]
  (iterate step-fish-counts fish-counts))

(defn num-fish-after [fish days]
  (let [fish (-> fish (simulate) (nth days))]
    (reduce + (vals fish))))

(defn part-1 [fish]
  (num-fish-after (frequencies fish)
                  80))

(defn part-2 [fish]
  (num-fish-after (frequencies fish)
                  256))

(rcf/tests
  (time (part-1 fish)) := 353079
  (time (part-2 fish)) := 1605400130036)







