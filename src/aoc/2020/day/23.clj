(ns aoc.2020.day.23
  (:require [clojure.string :as str]))

(comment
  "NOTE that I've completely based my solution on Aleksandr Zhuravlёv's,
  https://github.com/zelark/AoC-2020/blob/master/src/zelark/aoc_2020/day_23.clj")

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(def input "853192647")

(def t "389125467")

(defn ->longs [input]
  (->> input
       (vec)
       (mapv #(Long/parseLong (str %)))))

(defprotocol ICircleNode
  "Taken from zelark@: https://github.com/zelark/AoC-2020/blob/master/src/zelark/aoc_2020/day_23.clj"
  (value    [this])
  (get-next [this])
  (set-next [this node])
  (insert-after   [this node])
  (insert-n-after [this start-node end-node])
  (remove-n-after [this n]))

;; Adapted from zelark@:
;; https://github.com/zelark/AoC-2020/blob/master/src/zelark/aoc_2020/day_23.clj
(deftype CircleNode [value ^:volatile-mutable next]
  ICircleNode
  (value [this] value)
  (get-next [this] next)
  (set-next [this node] (do (set! next node) this))
  (insert-after [this node]
    (set-next node next)
    (set-next this node))
  (insert-n-after [this start-node end-node]
    (set-next end-node next)
    (set-next this start-node))
  (remove-n-after [this n]
    (->> (iterate get-next this)
         (drop (inc ^long n))
         (first)
         (set-next this))))

(defn circle-node [value]
  (let [node (->CircleNode value nil)]
    (set-next node node)))

(defn ->nodes [[h & more :as ints]]
  (reduce #(insert-after %1 (circle-node %2))
          (circle-node h)
          (reverse more)))

(defn values [node]
  (let [h node
        more (get-next node)]
    (->> more
         (iterate get-next)
         (take-while #(not (identical? % h)))
         (cons h)
         (map value))))

(defn ->by-value [node]
  (zipmap (values node) (iterate get-next node)))

(defn move' [^long max-label by-label hand]
  (let [num-movers 3
        current-value (value hand)
        movers (doall (->> hand
                           (iterate get-next)
                           (rest)
                           (take num-movers)))
        mover-vals (map value movers)
        hand (remove-n-after hand num-movers)
        target-label (loop [n (dec ^long current-value)]
                       (cond
                         (< n 1) (recur max-label)
                         (some #{n} mover-vals) (recur (dec n))
                         :else n))
        target (by-label target-label)]
    (insert-n-after target (first movers) (last movers))
    (get-next hand)))

(defn after-1' [hand]
  (let [one (some #(when (= 1 (value %)) %)
                  (iterate get-next hand))]
    (->> one
         (get-next)
         (values)
         (butlast))))

;; A
(comment (let [nodes (-> input ->longs ->nodes)
               by-value (->by-value nodes)]
           (->> nodes
                (iterate (partial move' 9 by-value))
                (drop 100)
                (first)
                (after-1')
                (str/join))))

(defn append-to-million [ints]
  (let [m (apply max ints)
        more (range (inc ^long m) 1000001)]
    (concat ints more)))

;; B
(comment
  (let [nodes (-> input ->longs append-to-million ->nodes)
        by-value (->by-value nodes)]
    (time (->> nodes
               (iterate (partial move' 1000000 by-value))
               (drop 10000000)
               (first)
               (after-1')
               (take 2)
               (apply *)))))



