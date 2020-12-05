(ns aoc.2020.advent
  (:require [clojure.test :refer [is]]))

(defn day [n input parse & more]
  (let [[a & more] more
        [a-cases more] (split-with vector? more)
        [b & b-cases] more
        [a' b'] (map #(comp % parse) [a b])
        run-case (fn [f [expected input]] (and f (is (= expected (f input)))))
        run-cases (fn [[f cases]] (every? identity (map (partial run-case f) cases)))]
    (when (every? identity (map run-cases [[a' a-cases] [b' b-cases]]))
      (and a (println "Solution A:" (a' input)))
      (and b (println "Solution B:" (b' input))))))
