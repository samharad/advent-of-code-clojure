(ns aoc.2022.day.10
  (:require [clojure.string :as str]))

(def input "addx 1\naddx 4\nnoop\nnoop\naddx 30\naddx -24\naddx -1\nnoop\naddx 4\naddx 1\naddx 5\naddx -4\naddx 5\naddx 4\naddx 1\nnoop\naddx 5\naddx -1\naddx 5\naddx 3\nnoop\naddx -38\naddx 9\naddx -4\nnoop\naddx 3\nnoop\naddx 2\naddx 3\nnoop\naddx 2\naddx 3\nnoop\naddx 2\naddx 3\nnoop\naddx 2\naddx -17\naddx 22\naddx -2\naddx 5\naddx 2\naddx 3\naddx -2\naddx -36\nnoop\naddx 5\nnoop\naddx 3\nnoop\naddx 2\naddx -5\nnoop\naddx 10\naddx 3\naddx -2\naddx 3\naddx 2\naddx 4\nnoop\nnoop\nnoop\nnoop\naddx 3\nnoop\nnoop\naddx 7\naddx 1\nnoop\nnoop\naddx -38\naddx 39\naddx -32\nnoop\nnoop\nnoop\naddx 5\naddx 2\naddx -1\naddx 4\nnoop\naddx 5\naddx -2\naddx 5\naddx 2\naddx -26\naddx 31\naddx -2\naddx 4\naddx 3\naddx -18\naddx 19\naddx -38\naddx 7\nnoop\nnoop\naddx 34\naddx -39\naddx 8\naddx 5\naddx 2\naddx 10\naddx -5\naddx -2\naddx 5\naddx 2\naddx 11\naddx -6\nnoop\naddx 3\nnoop\naddx 2\naddx 3\naddx -2\naddx -38\nnoop\nnoop\nnoop\naddx 5\naddx 11\nnoop\naddx -3\nnoop\nnoop\nnoop\naddx 2\nnoop\naddx -11\naddx 16\nnoop\naddx 3\naddx 2\naddx 8\nnoop\nnoop\nnoop\nnoop\nnoop\naddx 4\naddx 3\nnoop\naddx -20\nnoop\n")

(defn line->instr [line]
  (let [[op & args] (str/split line #" ")]
    (cons op (map parse-long args))))

(defn parse-instrs [input]
  (->> (str/split-lines input)
       (map line->instr)))

(def instrs (parse-instrs input))

(defn exec [X [op maybe-V :as instr]]
  (case op
    "noop" [X]
    "addx" [X (+ X maybe-V)]
    (throw (ex-info "invalid instr!" {:instr instr
                                      :X X}))))

(defn exec-all [X instrs]
  (loop [acc [X]
         [instr & instrs'] instrs]
    (if-not instr
      acc
      (let [acc' (into acc (exec (last acc) instr))]
        (recur acc' instrs')))))

(defn strength [[cycle X]]
  (* cycle X))

(->> (exec-all 1 instrs)
     (cons :dummy)
     (map-indexed vector)
     (drop 20)
     (take-nth 40)
     (map strength)
     (apply +))
; 10760

(def width 40)
(def height 6)
(def init-CRT
  (vec (repeat height (vec (repeat width ".")))))

(->> (exec-all 1 instrs)
     (map-indexed vector)
     (reduce (fn [crt [i X]]
               (let [r (quot i width)
                     c (mod i width)]
                 (if (<= (dec c) X (inc c))
                   (assoc-in crt [r c] "#")
                   crt)))
             init-CRT)
     (mapv #(apply str %)))