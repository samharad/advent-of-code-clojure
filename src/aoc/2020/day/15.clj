(ns aoc.2020.day.15
  (:require [hashp.core]
            [clojure.string :as s]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn parse [input]
  (->> (s/split input #",")
       (map #(Integer/parseInt %))))

(def input (parse "2,15,0,9,1,20"))

(def t (parse "0,3,6"))

;; LEAKING TRANSIENT
(defn init-game-state [ns]
  (let [butlast-ns (butlast ns)
        last-n (last ns)
        last-mentioned (->> butlast-ns
                            (map-indexed vector)
                            (map reverse)
                            (map vec)
                            (into {}))
        turn (count ns)]
    {:last-mentioned (transient last-mentioned)
     :last last-n
     :turn turn}))

(defn turn [game-state]
  (let [{:keys [last-mentioned last turn]} game-state
        to-say (if-let [last-turn (last-mentioned last)]
                 (dec (- ^long turn ^long last-turn))
                 0)
        last-mentioned' (assoc! last-mentioned last (dec ^long turn))]
    {:last-mentioned last-mentioned'
     :last to-say
     :turn (inc ^long turn)}))

(defn solution [i]
  (->> input
       (init-game-state)
       (iterate turn)
       (drop-while #(not= i (:turn %)))
       (first)
       :last))

(prn (time (solution 2020)))
(prn (time (solution 30000000)))
;; "Elapsed time: 99515.620188 msecs"
