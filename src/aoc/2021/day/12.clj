;; Finding myself up at midnight last night I couldn't resist
;; trying Day 12's puzzle, and thankfully I got my answers without
;; staying up until sunrise.

(ns aoc.2021.day.12
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.string :as str]))

(rcf/enable!)

;; [The puzzle](https://adventofcode.com/2021/day/12) is another graph
;; problem. We're given a graph with a start, an end, and other nodes
;; that are either "big caves" (capital letters) or "small caves" (lower-case
;; letters).

;     start
;     /   \
; c--A-----b--d
;     \   /
;      end

;; We're asked to find all paths from start to end with the constraint that
;; no small cave is visited more than once in a given path.

;; The input is given as a list of adjacencies; we'll parse it into a hash-map
;; for efficiency's sake.

(def input "LP-cb\nPK-yk\nbf-end\nPK-my\nend-cb\nBN-yk\ncd-yk\ncb-lj\nyk-bf\nbf-lj\nBN-bf\nPK-cb\nend-BN\nmy-start\nLP-yk\nPK-bf\nmy-BN\nstart-PK\nyk-EP\nlj-BN\nlj-start\nmy-lj\nbf-LP\n")
(def t-in-sm "start-A\nstart-b\nA-c\nA-b\nb-d\nA-end\nb-end")
(def t-in-med "dc-end\nHN-start\nstart-kj\ndc-start\ndc-HN\nLN-dc\nHN-end\nkj-sa\nkj-HN\nkj-dc")

(defn parse-adjacencies [input]
  (->> (str/split-lines input)
       (map #(str/split % #"-"))
       (mapcat (juxt identity reverse))
       (group-by first)
       (reduce-kv (fn [m k v] (assoc m k (map second v))) {})))

;; (It was tempting to use the new `update-vals` as the last step here,
;; but since it takes a map as its *first* argument I couldn't use it
;; with the thread-last macro.)

(def adjacencies (parse-adjacencies input))
(def t-adj-sm (parse-adjacencies t-in-sm))
(def t-adj-med (parse-adjacencies t-in-med))

;; Now for our graph search. BFS or DFS will do, and at midnight one
;; uses whichever comes naturally, so it was DFS for me. The only
;; non-standard thing we need to do is to ensure that "big caves"
;; are never marked as visited, since they may be returned to any number
;; of times. (It's implied that the puzzle input will not cause infinite
;; loops.)

(defn all-caps? [s] (= s (str/upper-case s)))

(defn paths-1 [adjacencies start end]
  (letfn [(paths [curr visited]
            (if (= curr end)
              '((curr))  ; Singleton path [curr] is the only path
              (let [to-visit (remove visited (adjacencies curr))
                    visited (if (all-caps? curr)
                              visited
                              (conj visited curr))
                    visit-neighbor (fn [neighbor]
                                     (map (fn [path] (cons curr path))
                                          (paths neighbor visited)))]
                (mapcat visit-neighbor to-visit))))]
    (paths start #{})))


(defn part-1 [adjacencies]
  (count (paths-1 adjacencies "start" "end")))

(rcf/tests
  (part-1 t-adj-sm) := 10
  (part-1 t-adj-med) := 19
  (part-1 adjacencies) := 3298)

;; Part 2 changes the rules -- it says that in a given path, a single
;; small cave may be visited twice.

;; To me there's no obvious axis on which to generalize this rule -- might
;; we want to permit a single small cave to be visited 3 or 4 times, or might
;; we want to permit up to 2 or 3 small caves to be visited twice...? And of
;; course there's no part 3 to the puzzle anyway; so we'll solve only the
;; problem we have, using a `did-double` flag to indicate whether we've used
;; our twofer-small-cave already, and call it a day.

(defn paths-2 [adjacencies start end]
  (letfn [(paths [curr visited did-double]
            (if (= curr end)
              '((curr))
              (let [did-double (or did-double (boolean (visited curr)))
                    to-visit (remove #{start}  ; May never return to start
                                     (if did-double
                                       (remove visited (adjacencies curr))
                                       (adjacencies curr)))
                    visited (if (all-caps? curr)
                              visited
                              (conj visited curr))
                    visit-neighbor (fn [neighbor]
                                     (map (fn [path] (cons curr path))
                                          (paths neighbor
                                                 visited
                                                 did-double)))]
                (mapcat visit-neighbor to-visit))))]
    (paths start #{} false)))

(defn part-2 [adjacencies]
  (count (paths-2 adjacencies "start" "end")))

(rcf/tests
  (time (part-2 adjacencies)) := 93572)  ; => 340.475625 ms

;; I'm curious to see how fast the optimized Clojure solutions are.

;; ⭐️⭐️
