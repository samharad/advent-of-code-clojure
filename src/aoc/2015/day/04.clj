(ns aoc.2015.day.04
  (:require [digest]))

(def i "yzbqklnj")

(defn valid-coin? [leading-0s hash]
  (->> (seq hash)
       (take leading-0s)
       (every? #{\0})))

(defn valid-postfix? [leading-0s num]
  (let [hash (digest/md5 (str i num))]
    (valid-coin? leading-0s hash)))

(defn mine [leading-0s]
  (->> (range)
       (filter (partial valid-postfix? leading-0s))
       (first)))

(def s1 (mine 5))
(def s2 (mine 6))

[s1 s2]