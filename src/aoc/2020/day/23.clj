(ns aoc.2020.day.23
  (:require [clojure.string :as str]))

(def input "853192647")

(def t "389125467")

(defn move [[h a b c & more]]
  (let [more-sorted (sort > more)
        dest (or (some #(when (< % h) %) more-sorted)
                 (first more-sorted))
        [l [dest & r]] (split-with (complement #{dest}) more)]
    (vec (concat l [dest a b c] r [h]))))

(defn parse [input]
  (->> input
       (vec)
       (mapv #(Integer/parseInt (str %)))))

(defn after-1 [cups]
  (let [[l [one & r]] (split-with (complement #{1}) cups)]
    (concat r l)))

(defn add-million [cups]
  (let [m (apply max cups)]
    (into cups (range (inc m) 1000001))))

(comment (->> input
              (parse)
              (iterate move)
              (drop 100)
              (first)
              (after-1)
              (str/join)))

(prn (->> t
          (parse)
          (add-million)
          (iterate move)
          (drop 10000000)
          (first)
          (take 10)))

