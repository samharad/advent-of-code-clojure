(ns aoc.2015.day.14
  (:require [clojure.string :as str]))

(def input "Vixen can fly 8 km/s for 8 seconds, but then must rest for 53 seconds.\nBlitzen can fly 13 km/s for 4 seconds, but then must rest for 49 seconds.\nRudolph can fly 20 km/s for 7 seconds, but then must rest for 132 seconds.\nCupid can fly 12 km/s for 4 seconds, but then must rest for 43 seconds.\nDonner can fly 9 km/s for 5 seconds, but then must rest for 38 seconds.\nDasher can fly 10 km/s for 4 seconds, but then must rest for 37 seconds.\nComet can fly 3 km/s for 37 seconds, but then must rest for 76 seconds.\nPrancer can fly 9 km/s for 12 seconds, but then must rest for 97 seconds.\nDancer can fly 37 km/s for 1 seconds, but then must rest for 36 seconds.")

(defn parse-line [line]
  (let [words (str/split line #" ")
        deer (first words)
        speed (Integer/parseInt (get words 3))
        fly-time (Integer/parseInt (get words 6))
        rest-time (Integer/parseInt (get words 13))]
    [deer speed fly-time rest-time]))

(def all-deer (map parse-line (str/split-lines input)))

(defn distance [time deer]
  (let [[_ speed fly-time rest-time] deer
        round-time (+ fly-time rest-time)
        full-rounds (int (/ time round-time))
        additional-seconds (min fly-time (mod time round-time))
        total-seconds-flown (+ (* full-rounds fly-time)
                               additional-seconds)]
    (* total-seconds-flown speed)))

(apply max (map (partial distance 2503) all-deer))

(defn round-score [time deer]
  (let [ds (map (partial distance time) deer)
        winning-dist (apply max ds)]
    (map #(if (= winning-dist %) 1 0)
         ds)))

(->> (range 1 2504)
     (map #(round-score % all-deer))
     (apply (partial map +))
     (apply max))