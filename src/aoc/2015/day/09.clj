(ns aoc.2015.day.09
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(def i "AlphaCentauri to Snowdin = 66\nAlphaCentauri to Tambi = 28\nAlphaCentauri to Faerun = 60\nAlphaCentauri to Norrath = 34\nAlphaCentauri to Straylight = 34\nAlphaCentauri to Tristram = 3\nAlphaCentauri to Arbre = 108\nSnowdin to Tambi = 22\nSnowdin to Faerun = 12\nSnowdin to Norrath = 91\nSnowdin to Straylight = 121\nSnowdin to Tristram = 111\nSnowdin to Arbre = 71\nTambi to Faerun = 39\nTambi to Norrath = 113\nTambi to Straylight = 130\nTambi to Tristram = 35\nTambi to Arbre = 40\nFaerun to Norrath = 63\nFaerun to Straylight = 21\nFaerun to Tristram = 57\nFaerun to Arbre = 83\nNorrath to Straylight = 9\nNorrath to Tristram = 50\nNorrath to Arbre = 60\nStraylight to Tristram = 27\nStraylight to Arbre = 81\nTristram to Arbre = 90")

(defn parse-line [line]
  (let [matcher #"(\S*) to (\S*) = (\S*)"
        [a b c] (rest (re-groups (doto (re-matcher matcher line)
                                   re-find)))]
    [a b (Integer/parseInt c)]))

(def distances (->> (str/split-lines i) (map parse-line)))

(def cities (->> distances
                 (mapcat (partial take 2))
                 (set)))

(def distances-by-pair (->> distances
                            (map (fn [[a b dist]]
                                   [(set [a b])
                                    dist]))
                            (into {})))

(def routes (->> cities (combo/permutations)))

(defn route-length [route]
  (->> (partition 2 1 route)
       (map set)
       (map distances-by-pair)
       (reduce +)))

(def route-lengths (map route-length routes))

(prn [(apply min route-lengths) (apply max route-lengths)])
