(ns aoc.2015.day.10)

(def i "1321131112")

(defn say [s]
  (->> s
       (partition-by identity)
       (mapcat (juxt count first))
       (apply str)))

(defn play [init rounds]
  (->> (iterate say init)
       (drop rounds)
       (first)
       (count)))

(prn [(play i 40) (play i 50)])
