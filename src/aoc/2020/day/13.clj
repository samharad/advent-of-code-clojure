(ns aoc.2020.day.13
  (:require [clojure.string :as s]
            [debux.core :as d]))

(defn parse [input]
  (let [[target times] (s/split-lines input)
        target (Integer/parseInt target)
        times (->> (s/split times #",")
                   (map #(if (not= % "x")
                           (Integer/parseInt %)
                           %)))]
    [target times]))

(def input (parse "1000509\n17,x,x,x,x,x,x,x,x,x,x,37,x,x,x,x,x,739,x,29,x,x,x,x,x,x,x,x,x,x,13,x,x,x,x,x,x,x,x,x,23,x,x,x,x,x,x,x,971,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,19"))

(def t (parse "939\n7,13,x,x,59,x,31,19"))

(defn departures [interval]
  (iterate (partial + interval) 0))

(comment
  "So I have several infinite, lazy seqs, that are each sorted. I need
  a lazy-ish seq of their sorted union. So I grab an element from each;
  for the lower, I grab while lower than than the higher;")

(defn union-sorted [& sorted-infinite-seqs]
  (let [firsts (map first sorted-infinite-seqs)
        m (apply max firsts)
        splits (map (fn [sorted-seq]
                      (split-with #(<= % m) sorted-seq))
                    sorted-infinite-seqs)
        chunk (->> splits
                   (mapcat first)
                   (sort))
        more (map second splits)]
    (lazy-cat chunk (apply union-sorted more))))

;; A
(let [[target times] input
      times (->> times
                 (filter (partial not= "x"))
                 (sort))
      departure-time (->> times
                           (map departures)
                           (apply union-sorted)
                           (some #(when (>= % target) %)))
      bus-id (some #(when (zero? (rem departure-time %))
                      %)
                   times)]
  (* (- departure-time target)
     bus-id))

(defn egcd [a b]
  (if (zero? a)
    [b 0 1]
    (let [[g x' y'] (egcd (mod b a) a)]
      [g
       (- y'
          (* x' (quot b a)))
       x'])))

;; B (Chinese Remainder Theorem)
(let [[_ times] input
      ts (->> times
              (map-indexed #(vector %2 %1))
              (filter #(not= (first %) "x"))
              (map (fn [[modulus delay]]
                     (let [d (- modulus delay)]
                       (if (= d modulus)
                         [modulus 0]
                         [modulus d])))))
      ns (map first ts)
      as (map second ts)
      mod-product (apply * ns)
      ys (map #(/ mod-product %) ns)
      ss (map #(last (egcd %1 %2))
              ns
              ys)
      prods (map * as ss ys)]
  (mod (apply + prods)
       mod-product))
