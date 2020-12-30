(ns aoc.2020.day.25)

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(def card-public-key 13316116)
(def door-public-key 13651422)

(defn transformations [^long subject-num]
  (iterate (fn [^long n] (rem (* n subject-num) 20201227))
           1))

(defn find-loop-size [pub-key subject-number]
  (first (keep-indexed #(when (= pub-key %2) %1)
                       (transformations subject-number))))

(comment (time (let [card-loop-size (find-loop-size card-public-key 7)]
                 (transform door-public-key card-loop-size))))

