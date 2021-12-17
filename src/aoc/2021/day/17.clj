;; Today's puzzle was fun, but all I came up with
;; was a **hacky and messy** solution. Gotta copy-paste to
;; success and move on for now, though!

(ns aoc.2021.day.17
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]
            [hyperfiddle.rcf :as rcf]))

(rcf/enable!)

;; [Day 17](https://adventofcode.com/2021/day/17) finds us
;; shooting a projectile in a 2D plane that is affected by
;; wind resistance and gravity -- that is, our projectile
;; accelerates downward constantly, and it loses horizontal
;; velocity until it's not moving forward at all.

;; We're asked to find horizontal and vertical velocities
;; at which to shoot the projectile such that it lands in
;; a "target zone"; it must land there on a whole-number tick
;; of the clock.

;; The input is a description of the target area.

(def t-in "target area: x=20..30, y=-10..-5")
(def input "target area: x=85..145, y=-163..-108")

(defn parse-target [input]
  (let [[x1 x2 y1 y2] (->> (str/split input #"[=\.*,]")
                           (rest)
                           (take-nth 2)
                           (map parse-long))
        [x1 x2] (sort [x1 x2])
        [y1 y2] (sort [y1 y2])]
    [[x1 y1] [x2 y2]]))

(def t-target (parse-target t-in))
(def target (parse-target input))

;; My solution is brute-force. We treat the x and y axes
;; separately. First identify all x-velocities that land
;; us at the correct horizontal range by trying a couple
;; thousand velocities -- we're assuming the given target
;; area is not too far from the origin.

;; We'll do the same for the y-axis, and then we'll identify
;; pairs of x- and y-velocities that get us in the correct
;; horizontal and vertical zones *at the same tick of the
;; clock*.

(defn simulate-vx [vx]
  (iterate (fn [vx]
             (if (zero? vx)
               vx
               (- vx (/ vx (Math/abs vx)))))
           vx))

(defn simulate-x-pos [vx]
  (->> (simulate-vx vx)
       (reductions + 0)))


(defn simulate-x
  "Simulates projectile launch in the x-axis at
  velocity vx. Returns any times at which the target
  zone is hit."
  [vx [[x1 _] [x2 _]]]
  (let [within-target? (fn [[t x vx]]
                         (<= x1 x x2))
        approaching-target? (fn [[t x vx]]
                              (or (within-target? [t x vx])
                                  (and (pos? vx) (< x x1))
                                  (and (neg? vx) (> x x1))))]
    (->> (map vector
              (range)
              (simulate-x-pos vx)
              (simulate-vx vx))
         (take-while approaching-target?)
         (drop-while (complement within-target?))
         (take-while within-target?)
         (map first)
         ;; Since velocity can zero-out, we could end up
         ;; sitting in the target zone for eternity; but
         ;; sadly we assume later that our seqs are not
         ;; infinite.
         (take 1000))))

;; Now we lift and shift for the y axis 😬

(defn simulate-vy [vy]
  (iterate dec vy))

(defn simulate-y-pos [vy]
  (reductions + 0 (simulate-vy vy)))

(defn simulate-y
  "Simulates projectile launch in the y-axis at
  velocity vy. Returns any times at which the target
  zone is hit."
  [vy [[_ y1] [_ y2]]]
  (let [within-target? (fn [[t y vy]]
                         (<= y1 y y2))
        approaching-target? (fn [[t y vy]]
                              (or (within-target? [t y vy])
                                  (pos? vy)
                                  (>= y y2)))]
    (->> (map vector
              (range)
              (simulate-y-pos vy)
              (simulate-vy vy))
         (take-while approaching-target?)
         (drop-while (complement within-target?))
         (take-while within-target?)
         (map first))))

;; Here's where we attempt all velocities between -1000
;; and 1000 for the given dimension (x or y).

(defn v-dim-candidates [target simulate-f]
  (let [grouped (->> (range -1000 1000)
                     (map (fn [v] [v (simulate-f v target)]))
                     (filter (fn [[_ ts]] (some? ts)))
                     (mapcat (fn [[v ts]]
                               (map (fn [t] (vector t v))
                                    ts)))
                     (group-by first))]
    (update-vals grouped #(map second %))))

;; Now we identify velocities that get us where we
;; need to be.

(defn v-candidates [target]
  (let [vy-cs (v-dim-candidates target simulate-y)
        vx-cs (-> (v-dim-candidates target simulate-x)
                  (select-keys (keys vy-cs)))]
    (mapcat (fn [[t vxs]]
              (combo/cartesian-product vxs (get vy-cs t)))
            vx-cs)))

;; Part 1 asks us to select the velocity that peaks
;; at the highest point.

(defn peak [[_ vy]]
  (or (->> (simulate-y-pos vy)
           (partition 2 1)
           (take-while #(apply < %))
           (flatten)
           (last))
      0))

(defn part-1 [target]
  (->> (map (juxt identity peak) (v-candidates target))
       (apply max-key second)
       (second)))

;; Part 2 asks us for the number of velocities that get
;; us where we need to go.

(defn part-2 [target]
  (count (distinct (v-candidates target))))

(rcf/tests
  (part-1 target) := 13203
  (time (part-2 target)) := 5644)  ; => 230.899625 ms

;; Hoping to come back and optimize and clean this one up.

;; ⭐️⭐️