(ns aoc.2021.cuboid
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.math.combinatorics :as combo]
            [taoensso.tufte :as tufte :refer (defnp p profiled profile)]))

(rcf/enable!)

(defn- segments-disjoint? [a b]
  (let [[ax1 ax2] a
        [bx1 bx2] b]
    (or (> bx1 ax2) (> ax1 bx2))))

(rcf/tests
  (segments-disjoint? [0 1] [2 3]) := true
  (segments-disjoint? [2 3] [0 1]) := true
  (segments-disjoint? [0 2] [2 3]) := false
  (segments-disjoint? [2 3] [0 2]) := false)

(defnp cuboids-disjoint? [a b]
  (boolean
    (some (partial apply segments-disjoint?)
          (map vector a b))))

(rcf/tests
  (cuboids-disjoint? [[0 1] [0 1] [0 1]]
                     [[2 3] [2 3] [2 3]]) := true
  (cuboids-disjoint? [[0 1] [0 1] [0 1]]
                     [[0 3] [0 3] [0 3]]) := false
  (cuboids-disjoint? [[0 1] [0 1] [0 1]]
                     [[1 1] [1 1] [1 1]]) := false
  ,)


(defn sub-segs [a b]
  ;{:post [(every? (fn [[a b]] (<= a b)) %)]}
  (let [[a b] (sort (map vec [a b]))
        [ax1 ax2] a
        [bx1 bx2] b]
    (cond
      (= a b) [a]
      (= ax1 bx1) [[ax1 ax2] [(inc ax2) bx2]]
      ;; Know that ax1 < bx1
      (= ax2 bx2) [[ax1 (dec bx1)] [bx1 bx2]]
      (= ax2 bx1) [[ax1 (dec ax2)] [ax2 ax2] [(inc bx1) bx2]]
      (> ax2 bx2) [[ax1 (dec bx1)] [bx1 bx2] [(inc bx2) ax2]]
      (> ax2 bx1) [[ax1 (dec bx1)] [bx1 ax2] [(inc ax2) bx2]]
      :else (throw (ex-info "No such!" {})))))

(rcf/tests
  (sub-segs [0 1] [0 1]) := [[0 1]]
  (sub-segs [0 1] [0 2]) := [[0 1] [2 2]]
  (sub-segs [0 2] [1 2]) := [[0 0] [1 2]]
  (sub-segs [0 3] [1 2]) := [[0 0] [1 2] [3 3]]
  (sub-segs [0 2] [1 3]) := [[0 0] [1 2] [3 3]]
  (sub-segs [0 2] [2 3]) := [[0 1] [2 2] [3 3]]
  (sub-segs [0 2] [2 2]) := [[0 1] [2 2]]
  ,)

(defn seg-contains? [[ax1 ax2] [bx1 bx2]]
  (and (<= ax1 bx1)
       (>= ax2 bx2)))

(defnp cuboid-contains? [a b]
  (every? (partial apply seg-contains?)
          (map vector a b)))

(rcf/tests
  (cuboid-contains? [[0 3] [0 3] [0 3]]
                    [[0 1] [0 1] [0 1]]) := true
  (cuboid-contains? [[0 1] [0 1] [0 1]]
                    [[0 1] [0 1] [0 1]]) := true
  (cuboid-contains? [[0 3] [0 3] [0 3]]
                    [[-1 1] [0 1] [0 1]]) := false)

(defn volume [[[x1 x2] [y1 y2] [z1 z2]]]
  (* (- (inc x2) x1)
     (- (inc y2) y1)
     (- (inc z2) z1)))

(defnp sub-cuboids [a b]
  ;{:post [(>= (+ (volume a) (volume b))
  ;            (reduce + (map volume %)))]}
  (if (cuboids-disjoint? a b)
    [a b]
    (let [[ax ay az] a
          [bx by bz] b
          xs (sub-segs ax bx)
          ys (sub-segs ay by)
          zs (sub-segs az bz)
          boxes (->> (combo/cartesian-product xs ys zs)
                     (filter #(or (cuboid-contains? a %)
                                  (cuboid-contains? b %))))]
      boxes)))

(rcf/tests
  (set (sub-cuboids [[0 2] [0 2] [0 2]]
                    [[0 2] [0 2] [0 1]])) := #{[[0 2] [0 2] [0 1]]
                                               [[0 2] [0 2] [2 2]]}
  (sub-cuboids [[0 1] [0 1] [0 1]]
               [[2 3] [2 3] [2 3]]) := [[[0 1] [0 1] [0 1]]
                                        [[2 3] [2 3] [2 3]]]
  (count (sub-cuboids [[0 1] [0 1] [0 1]]
                      [[0 3] [0 3] [0 3]])) := 8
  (count (sub-cuboids [[-1 1] [0 1] [0 1]]
                      [[0 3] [0 3] [0 3]])) := 9
  (count (sub-cuboids [[-1 1] [-1 1] [0 1]]
                      [[0 3] [0 3] [0 3]])) := 11
  (count (sub-cuboids [[-1 1] [-1 1] [-1 1]]
                      [[0 3] [0 3] [0 3]])) := 15
  "Done"
  ,)

(defnp slow-union [[c & cuboids :as all-cuboids]]
  ;{:post [(do
  ;          (prn
  ;               (->> (combo/combinations % 2)
  ;                    (not-any? (fn [[a b]]
  ;                                (when (or (cuboid-contains? a b)
  ;                                          (cuboid-contains? b a))
  ;                                  (prn "here" all-cuboids)
  ;                                  true)))))
  ;          (and
  ;            (<= (reduce + (map volume %))
  ;                (reduce + (map volume all-cuboids)))
  ;            (->> (combo/combinations % 2)
  ;                 (not-any? (fn [[a b]] (or (cuboid-contains? a b)
  ;                                           (cuboid-contains? b a)))))))]}
  (loop [acc #{c}
         [c & more-cuboids :as cuboids] cuboids]
    (if (nil? c)
      acc
      (let [[e novel-cuboids] (some (fn [e]
                                      (let [sub-cuboids (sub-cuboids e c)]
                                        (when (not= (set sub-cuboids) (set [e c]))
                                          [e sub-cuboids])))
                                               ;(filter #(and (cuboid-contains? c %)
                                               ;              (not (cuboid-contains? e %))))))))
                                    acc)]
        (cond
          ;(some #(cuboid-contains? % c) acc) (do (recur acc more-cuboids))
          ;contained-by-c (do (recur (disj acc contained-by-c) cuboids))
          ;; TODO: could leave the old-shape's cubes in the acc,
          ;;       and just recur with the newly-added sub-shapes?
          novel-cuboids (do (recur (disj acc e)
                                   (into more-cuboids novel-cuboids)))
          :fully-disjoint (do (recur (conj acc c) more-cuboids)))))))

(defnp fast-union [[c & cuboids :as all-cuboids]]
  ;{:post [(do
  ;          (prn
  ;               (->> (combo/combinations % 2)
  ;                    (not-any? (fn [[a b]]
  ;                                (when (or (cuboid-contains? a b)
  ;                                          (cuboid-contains? b a))
  ;                                  (prn "here" all-cuboids)
  ;                                  true)))))
  ;          (and
  ;            (<= (reduce + (map volume %))
  ;                (reduce + (map volume all-cuboids)))
  ;            (->> (combo/combinations % 2)
  ;                 (not-any? (fn [[a b]] (or (cuboid-contains? a b)
  ;                                           (cuboid-contains? b a)))))))]}
  (loop [acc #{c}
         [c & more-cuboids :as cuboids] cuboids]
    (if (nil? c)
      acc
      (let [contained-by-c (delay (some #(when (cuboid-contains? c %)
                                           %)
                                        acc))
            novel (delay (some (fn [e]
                                 (let [sub-cuboids (sub-cuboids e c)]
                                   (when (not= (set sub-cuboids) (set [e c]))
                                     [e sub-cuboids])))
                               acc))]
        (cond
          (p :contains (some #(cuboid-contains? % c) acc))
          (do (recur acc more-cuboids))

          (p :contained-by-c @contained-by-c)
          (do (recur (disj acc @contained-by-c) cuboids))

          (p :novel (first @novel))
          (do (recur (disj acc (first @novel))
                     (into more-cuboids (second @novel))))

          (p :disjoint :fully-disjoint)
          (do (recur (conj acc c) more-cuboids)))))))

(rcf/tests
  (count (slow-union [[[0 1] [0 1] [0 1]]
                      [[1 1] [1 1] [1 1]]])) := 8
  (count (slow-union [[[1 1] [1 1] [1 1]]
                      [[0 1] [0 1] [0 1]]])) := 8
  (->> (slow-union [[[0 1] [0 1] [0 1]]
                    [[1 2] [1 2] [1 2]]
                    [[1 1] [1 1] [1 1]]])
       (map volume)
       (reduce +)) := 15
  (count (slow-union [[[0 1] [0 1] [0 1]]
                      [[1 2] [1 2] [1 2]]
                      [[1 1] [1 1] [1 1]]])) := 15
  (slow-union [[[0 2] [0 2] [0 2]]
               [[0 2] [0 2] [0 1]]]) := #{[[0 2] [0 2] [0 1]]
                                          [[0 2] [0 2] [2 2]]}
  ;(count (union [[[0 3] [0 3] [0 3]]
  ;               [[1 1] [1 1] [1 4]]])) := 2
  ;(count (union [[[0 3] [0 3] [0 3]]
  ;               [[2 4] [0 3] [2 4]]])) := 4
  "Done"
  ,)

(defn segs-adjacent [a b]
  (or (not (segments-disjoint? a b))
      (let [[a b c d] (sort (concat a b))]
        (= 1 (- c b)))))

(defn merge-cuboids [a b]
  (let [[ax ay az] a
        [bx by bz] b]
    ;(if (cuboids-disjoint? a b)
    ;  [a b]
    (cond
      (and (= ax bx) (= ay by) (segs-adjacent az bz))
      [[ax ay [(reduce min (concat az bz))
               (reduce max (concat az bz))]]]
      (and (= ay by) (= az bz) (segs-adjacent ax bx))
      [[[(reduce min (concat ax bx))
         (reduce max (concat ax bx))] ay az]]
      (and (= ax bx) (= az bz) (segs-adjacent ay by))
      [[ax [(reduce min (concat ay by))
            (reduce max (concat ay by))]
        az]]
      :else [a b])))

(rcf/tests
  (merge-cuboids [[0 1] [0 1] [0 1]]
                 [[0 1] [0 1] [3 3]]) := [[[0 1] [0 1] [0 1]]
                                          [[0 1] [0 1] [3 3]]]
  (merge-cuboids [[0 1] [0 1] [0 1]]
                 [[0 1] [0 1] [0 3]]) := [[[0 1] [0 1] [0 3]]]
  (merge-cuboids [[0 1] [0 1] [0 1]]
                 [[0 1] [0 1] [1 3]]) := [[[0 1] [0 1] [0 3]]]
  (merge-cuboids [[-1 1] [0 1] [0 1]]
                 [[0 10] [0 1] [0 1]]) := [[[-1 10] [0 1] [0 1]]])
