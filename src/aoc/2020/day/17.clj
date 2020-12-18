(ns aoc.2020.day.17
  (:require [clojure.string :as s]
            [hashp.core]))

(defn pad [p x] (vec (concat p x p)))

(defn parse-world
  ([input]
   (parse-world 7 input))
  ([n input]
   (let [ls (s/split-lines input)
         rows (mapv vec ls)
         padding (vec (repeat n \.))
         rows (mapv (partial pad padding) rows)
         w (count (first rows))
         empty-row (vec (repeat w \.))
         board (pad (repeat n empty-row) rows)
         empty-board (mapv #(mapv (constantly \.) %)
                           board)]
     (pad (repeat n empty-board) (vector board)))))

(defn parse-4d-world
  ([n input]
   (let [w (parse-world n input)
         empty-world (mapv #(mapv (fn [r] (mapv (constantly \.) r))
                                  %)
                           w)]
     (pad (repeat n empty-world) (vector w)))))


(def input "######.#\n#.###.#.\n###.....\n#.####..\n##.#.###\n.######.\n###.####\n######.#")

(def t ".#.\n..#\n###")

(defn world-coords [world]
  (let [z (count world)
        y (count (first world))
        x (count (first (first world)))]
    (if (= java.lang.Character (type (-> world first first first)))
      (for [x (range x) y (range y) z (range z)] [z y x])
      (let [w (count (-> world first first first))]
        (for [w (range w)  x (range x) y (range y) z (range z)] [z y x w])))))

(defn neighbor-coords [coord]
  (let [d (range -1 2)]
    (if (= 3 (count coord))
      (for [x d y d z d
            :when (not= 0 x y z)]
        (map + coord [z y x]))
      (for [w d x d y d z d
            :when (not= 0 w x y z)]
        (map + coord [z y x w])))))

(defn neighbors [world coord]
  (let [neighbor-coords (neighbor-coords coord)]
    (map #(get-in world %) neighbor-coords)))

(defn update-world [world]
  (reduce (fn [acc coord]
            (let [cell (get-in world coord)
                  neighbors (neighbors world coord)
                  active-ns (count (filter #{\#} neighbors))
                  cell' (if (= cell \#)
                          (if (#{2 3} active-ns)
                            \#
                            \.)
                          (if (= 3 active-ns)
                            \#
                            \.))]
              (assoc-in acc coord cell')))
          world
          (world-coords world)))

(defn iterate-world [world]
  (iterate update-world world))

(defn count-active [world]
  (->> world
       (flatten)
       (filter #(= % \#))
       (count)))

(time (->> input
           (parse-world 8)
           (iterate-world)
           (drop 6)
           (first)
           (count-active)))

(time (->> input
           (parse-4d-world 7)
           (iterate-world)
           (drop 6)
           (first)
           (count-active)))