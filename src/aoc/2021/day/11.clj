;; I wasn't able to start today's puzzle until later in the day, so I was
;; thankful that it was a familiar one.

(ns aoc.2021.day.11
  (:require [clojure.string :as str]
            [hyperfiddle.rcf :as rcf]))

(rcf/enable!)

;; The [task](https://adventofcode.com/2021/day/11) revolves around another
;; grid-based simulation. We have a grid of energies (integers) that are incremented
;; at each tick of the clock; when an energy level exceeds 9, a "flash" occurs and the
;; cell's energy is reset to 0 while bumping each of its neighboring energies up by 1;
;; because of these chain reactions, a seemingly low-energy step can result in a large
;; number of flashes.

(def input "2478668324\n4283474125\n1663463374\n1738271323\n4285744861\n3551311515\n8574335438\n7843525826\n1366237577\n3554687226\n")

(def t-input "5483143223\n2745854711\n5264556173\n6141336146\n6357385478\n4167524645\n2176841721\n6882881134\n4846848554\n5283751526")

;; We start with some very familiar grid functions. (Next year, if not this year, I'll
;; invest in writing some AoC library code.)

(defn parse-grid [input]
  (->> (str/split-lines input)
       (mapv #(mapv (comp parse-long str) %))))

(def grid (parse-grid input))
(def t-grid (parse-grid t-input))

(defn rows [grid] (count grid))
(defn cols [grid] (count (first grid)))

(defn coords [grid]
  (for [r (range (rows grid))
        c (range (cols grid))]
    [r c]))

(defn neighbor-coords
  ([coord]
   (let [ds (range -1 2)]
     (for [rd ds
           cd ds
           :when (not= 0 rd cd)]
       (mapv + coord [rd cd]))))
  ([grid coord]
   (filter (fn [[r c]]
             (and (<= 0 r (dec (rows grid)))
                  (<= 0 c (dec (cols grid)))))
           (neighbor-coords coord))))

;; On to the more problem-specific code. First some helpers:

(def charge-threshold 9)
(defn charged? [energy] (> energy charge-threshold))
(defn newly-charged? [energy] (= energy (inc charge-threshold)))

;; Now we encode the first step of the simulation, which is to increment all
;; cells in our grid. As an optimization, we use this pass over the grid to
;; also track those cells that get bumped past the charge threshold.

(defn inc-energy-levels [grid]
  (reduce (fn [[grid charged] coord]
            (let [e (inc (get-in grid coord))
                  grid (assoc-in grid coord e)
                  charged (if (charged? e)
                            (conj charged coord)
                            charged)]
              [grid charged]))
          [grid #{}]
          (coords grid)))

;; With the initial step out of the way, we write a function that "flashes"
;; a single cell, returning both the updated grid and those neighboring cells
;; that are pushed past the charge threshold. (Today's sinister bug was that
;; I initially returned the set of *all* neighbors that were charged, either
;; newly or since before the flash, and so I ended up double-flashing them.

(defn flash-coord [grid coord]
  (let [neighbors (neighbor-coords grid coord)
        grid (assoc-in grid coord 0)
        grid (reduce (fn [grid coord]
                       (update-in grid coord #(if (zero? %) % (inc %))))
                     grid
                     neighbors)
        newly-charged-neighbors (filter #(newly-charged? (get-in grid %))
                                        neighbors)]
    [grid newly-charged-neighbors]))

;; Finally, we have a recursive function that flashes cells in "rounds" -- first
;; we flash those cells that were triggered by the initial universal incrementing,
;; and then we flash those cells that were triggered by the first flash, etc., until
;; all cells are at calm, stable energy levels. As we progress, we track the set of
;; cells that were flashed.

(defn flash [grid charged]
  (loop [grid grid
         charged charged
         flashed #{}]
    (let [[grid charged flashed]
          (reduce (fn [[grid charged flashed] coord]
                   (let [[grid newly-charged] (flash-coord grid coord)
                         flashed (conj flashed coord)
                         charged (into charged newly-charged)]
                     [grid charged flashed]))
                 [grid #{} flashed]
                 charged)]

      (if (empty? charged)
        [grid flashed]
        (recur grid charged flashed)))))

;; All that's left is to simulate.

(defn step [grid]
  (let [[grid charged] (inc-energy-levels grid)
        [grid flashed] (flash grid charged)]
    [grid flashed]))

(defn simulate [grid]
  (iterate (fn [[grid num-flashed]]
             (let [[grid flashed] (step grid)
                   num-flashed (+ num-flashed (count flashed))]
               [grid num-flashed]))
           [grid 0]))

(defn part-1 [grid]
  (let [[_ num-flashed] (nth (simulate grid) 100)]
    num-flashed))

(rcf/tests
  (first
    (step
      [[1 1 1 1 1]
       [1 9 9 9 1]
       [1 9 1 9 1]
       [1 9 9 9 1]
       [1 1 1 1 1]]))
  := [[3 4 5 4 3]
      [4 0 0 0 4]
      [5 0 0 0 5]
      [4 0 0 0 4]
      [3 4 5 4 3]]
  (part-1 t-grid) := 1656
  (part-1 grid) := 1700)

;; Part 2 asks us to simulate until all cells flash in unison, i.e., until
;; a round begins with all cells at 0 energy. Luckily, this doesn't take a
;; preventatively long time -- only a few hundred steps.

(defn part-2 [grid]
  (->> (simulate grid)
       (map vector (range))
       (some (fn [[i [grid _]]]
               (when (every? zero? (flatten grid))
                 i)))))

(rcf/tests
  (part-2 t-grid) := 195
  (time (part-2 grid)) := 273)  ; => 33.669917 ms

;; ⭐️⭐️
