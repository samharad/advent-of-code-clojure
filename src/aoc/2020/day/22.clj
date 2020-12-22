(ns aoc.2020.day.22
  (:require [clojure.string :as s]))

(defn ->decks [input]
  (->> (s/split input #"\n\n")
       (map s/split-lines)
       (map rest)
       (map (fn [ss] (mapv #(Integer/parseInt %) ss)))))

(def input "Player 1:\n6\n25\n8\n24\n30\n46\n42\n32\n27\n48\n5\n2\n14\n28\n37\n17\n9\n22\n40\n33\n3\n50\n47\n19\n41\n\nPlayer 2:\n1\n18\n31\n39\n16\n10\n35\n29\n26\n44\n21\n7\n45\n4\n20\n38\n15\n11\n34\n36\n49\n13\n23\n43\n12")

(def t "Player 1:\n9\n2\n6\n3\n1\n\nPlayer 2:\n5\n8\n4\n7\n10")

(defn round [[p1 p2]]
  (let [[c1 & c1more] p1
        [c2 & c2more] p2
        add-cards (fn [hand & cs]
                    (vec (concat hand cs)))]
    (if (> c1 c2)
      [(add-cards c1more c1 c2) c2more]
      [c1more (add-cards c2more c2 c1)])))

(defn game-result [hands]
  (->> hands
       (iterate round)
       (drop-while #(not (some empty? %)))
       (first)))

(->> (->decks input)
     (game-result)
     (some not-empty)
     (reverse)
     (map * (rest (range)))
     (apply +))
