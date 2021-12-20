;; Today's a late solve for me, so I'll try to write my
;; thoughts as I solve the problem.

(ns aoc.2021.day.18
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.walk :as walk]
            [clojure.edn :as edn]
            [com.rpl.specter :as sp]))

(rcf/enable!)

;; Okay, the [puzzle] (https://adventofcode.com/2021/day/18) is
;; a long one to read, but seems straightforward enough.

;; We have a number system for snailfish where a snailfish number is
;; defined recursively as a 2-tuple in which each member is either a
;; decimal number or a snailfish number, e.g. `[9,[8,7]]`.

;; Snailfish numbers can be added, e.g. `[1,2] + [[3,4],5]`
;; becomes `[[1,2],[[3,4],5]]`

(defn- sf+ [& sf-nums]
  (vec sf-nums))

(rcf/tests (sf+ [1 2] [[3 4] 5]) := [[1 2] [[3 4] 5]])

;; Snailfish numbers must be "reduced":
;; > To reduce a snailfish number, you must repeatedly do the first action in
;;    this list that applies to the snailfish number:
;;
;;    If any pair is nested inside four pairs, the leftmost such pair explodes.
;;    If any regular number is 10 or greater, the leftmost such regular number splits.
;;
;;    Once no action in the above list applies, the snailfish number is reduced.

(defn- sf-flatten [sf-num]
  (let [sf-flatten (fn sf-flatten [sf-num nesting]
                     (walk/walk #(if (number? %)
                                   [[% nesting]]
                                   (sf-flatten % (inc nesting)))
                                (partial mapcat identity)
                                sf-num))]
    (sf-flatten sf-num 1)))

(defn- bubble-in [acc x depth]
  (let [wrap (fn [x xdepth]
               (nth (iterate vector x) xdepth))]
    (loop [acc acc
           x (wrap x depth)])))

(defn- bubble-in [acc x depth]
  (let [wrap (fn [x xdepth]
               (nth (iterate vector x) xdepth))]
    (loop [acc acc
           x (wrap x depth)
           depth depth]
      (if (= depth 1)
        (assoc acc (count acc) x)
        (update acc (dec (count acc)) #(bubble-in % (first x) (dec depth)))))))

(bubble-in [[6]] 5 3)

(defn- explode-one [sf-num]
  (sp/transform [#(vector?)]))


(defn- sf-unflatten [flat-sf-num]
  (let [wrap (fn [x xdepth]
               (nth (iterate vector x) xdepth))
        depth (fn [x] (loop [x x
                             depth 0]
                        (if (coll? x)
                          (recur (first x) (inc depth))
                          depth)))
        merge (fn [va vb]
                (vec (concat va vb)))]
    (->> flat-sf-num
         (map (partial apply wrap))
         (reduce (fn [acc x]
                   (let [min-depth (min (depth acc) (depth x))]
                     (prn acc (depth acc))
                     (nth (iterate (partial apply merge) [acc x])
                          min-depth)))))))

(sf-flatten [[3 4] [[5 6] 7]])
(sf-unflatten (sf-flatten [[3 4] [[5 6] 7]]))

(defn explode-one [[a b]]
  (cond
    (vector? a)))

(defn- sf-explode-leftmost [sf-num]
  (let [did-explode (atom false)
        sf-reduce (fn sf-reduce [sf-num nesting]
                    (walk/walk (fn [sf-num]
                                 (cond
                                   (number? sf-num) sf-num
                                   (= nesting 4 ()))
                                 (if)
                                 (sf-reduce % (inc nesting)))
                               identity
                               sf-num))]
    (sf-reduce sf-num 1)))

(defn sf-reduce [sf-num])

(sf-reduce [[3 4] [[5 6] 7]])




(def input "[[[[8,6],[0,8]],[5,2]],4]\n[[[9,[7,7]],[6,2]],[[[5,9],5],[[7,1],6]]]\n[[[7,[6,6]],[[8,5],8]],[[[2,9],[1,6]],[0,6]]]\n[[[5,[7,5]],[9,2]],[[[2,4],[8,1]],[7,1]]]\n[[[[9,1],[4,1]],[[5,9],[2,0]]],[5,[8,[0,3]]]]\n[[[9,5],[9,[1,7]]],[[[9,9],[8,9]],4]]\n[2,[[2,4],7]]\n[[[[8,7],[9,5]],0],[[[6,7],[9,2]],7]]\n[[[[5,5],3],[4,[2,3]]],[7,[7,7]]]\n[8,[5,5]]\n[[[0,[3,4]],[[3,4],[0,3]]],[[[9,6],[1,1]],[1,[8,7]]]]\n[[[4,8],3],[3,[9,[8,9]]]]\n[[5,[2,[5,2]]],[2,9]]\n[[[[5,6],[1,8]],[3,6]],[3,[[4,4],[3,4]]]]\n[[[1,0],3],[[1,[2,1]],[[2,5],[2,0]]]]\n[[[6,7],[[0,5],9]],[[0,7],[[4,7],5]]]\n[[[4,[3,1]],7],[[5,2],[3,0]]]\n[[[6,6],[9,[2,6]]],[9,[[3,8],[2,6]]]]\n[[[9,2],0],[[3,[6,6]],[7,[1,7]]]]\n[[8,0],[9,[[5,5],5]]]\n[[2,7],[[[8,7],0],[[1,1],[6,9]]]]\n[[4,[7,[9,3]]],7]\n[[[5,3],[[3,2],0]],[7,3]]\n[[0,[8,[8,6]]],1]\n[[4,5],[[[4,6],9],[3,0]]]\n[[[[1,5],9],[[0,4],9]],[[[8,0],[4,2]],[0,[8,6]]]]\n[[[6,4],[5,3]],3]\n[[[[4,0],[6,6]],9],[[[7,0],7],[[4,5],3]]]\n[[[[3,5],9],[[7,4],[6,8]]],[1,7]]\n[[[[2,7],2],[9,3]],[[[1,2],3],7]]\n[[[[9,4],[7,3]],0],[[7,[5,9]],[[7,0],[0,7]]]]\n[[[5,6],[[6,5],[5,3]]],[[4,[8,5]],1]]\n[[[2,6],0],[[1,0],[[7,2],[1,0]]]]\n[[9,3],8]\n[[[5,9],[2,[8,5]]],[[5,[4,7]],[[1,8],9]]]\n[[[6,[6,4]],[0,1]],[0,[[3,9],9]]]\n[3,9]\n[[[7,5],[[9,3],[1,5]]],[[6,3],[8,[6,5]]]]\n[[0,[0,5]],[7,0]]\n[[[[0,9],7],3],[[8,6],[8,7]]]\n[6,[7,[4,[9,0]]]]\n[[[[2,4],[7,7]],4],[[1,5],5]]\n[[3,6],[8,4]]\n[[4,1],[[[3,9],[4,6]],[7,[3,0]]]]\n[[[6,[9,1]],[7,4]],[[[5,7],[3,5]],[[2,2],5]]]\n[[[[9,7],[8,2]],[0,[3,7]]],[[[8,8],4],[[5,2],5]]]\n[[7,[0,[2,2]]],[4,[2,4]]]\n[[[0,8],[[7,7],[8,0]]],[6,1]]\n[6,[[[4,4],[3,9]],[[3,0],[4,3]]]]\n[[[8,8],[2,[4,2]]],[[8,[0,1]],9]]\n[4,[[6,[4,6]],[1,[6,9]]]]\n[[[[8,1],[3,6]],[[5,3],7]],[[9,6],1]]\n[[8,[[1,5],[1,7]]],[[[8,6],5],7]]\n[6,3]\n[[[[7,2],[9,9]],0],[[[9,0],8],[5,7]]]\n[[[[1,0],3],[[7,0],[1,2]]],0]\n[2,[[0,4],[6,[6,8]]]]\n[[[[3,2],[4,1]],3],6]\n[[[5,6],5],[[[3,4],[5,7]],[[5,5],6]]]\n[1,[[8,[2,2]],[4,2]]]\n[5,[[[4,7],1],[[6,6],7]]]\n[[[[0,3],9],9],[0,[2,2]]]\n[[[[8,9],3],5],[[1,6],[[6,5],[1,6]]]]\n[[1,[[0,1],0]],[[[5,8],1],[1,[0,0]]]]\n[[[[3,5],[9,4]],[7,[0,9]]],[8,[9,[1,9]]]]\n[4,4]\n[[7,[7,[0,2]]],[[8,8],[5,5]]]\n[[[5,[2,8]],[3,3]],[4,[8,7]]]\n[3,[8,[6,4]]]\n[[8,[[7,7],2]],[[9,1],[[5,8],[8,1]]]]\n[[[4,[5,2]],[[6,0],[7,7]]],[[[5,4],[8,3]],[[5,2],8]]]\n[9,[2,5]]\n[3,[5,5]]\n[[[6,6],[3,[0,0]]],[[5,[3,3]],[8,8]]]\n[[5,[2,[5,2]]],[[[8,7],9],[[4,1],6]]]\n[[[[7,2],[8,5]],8],[[1,[5,5]],[0,7]]]\n[[4,[6,8]],[[[2,4],[6,2]],[7,[3,6]]]]\n[[8,5],[[[5,0],3],9]]\n[[[[5,2],6],[7,2]],[[[7,4],9],0]]\n[[[4,[6,5]],8],[[9,[1,5]],[8,9]]]\n[[[5,[2,4]],[8,[9,4]]],[[1,5],2]]\n[[[[1,4],2],[3,[8,9]]],7]\n[[6,[1,7]],[9,2]]\n[[[2,[9,0]],[[4,8],[3,4]]],[[[6,5],0],[[3,3],[4,3]]]]\n[[[[4,4],[9,7]],[[4,8],7]],[[5,[6,6]],0]]\n[[[[5,2],[2,2]],[[8,3],0]],[2,[3,5]]]\n[[0,4],[[[9,0],[9,3]],[[1,1],6]]]\n[5,[[[2,0],2],1]]\n[[[[1,8],3],[[3,9],4]],[0,[[8,2],[7,4]]]]\n[[[8,6],[[3,9],1]],0]\n[[[[5,0],2],8],[4,[[3,5],[7,8]]]]\n[[[7,[3,3]],[[2,5],[4,6]]],[[[0,9],[1,1]],[1,[4,9]]]]\n[[[7,5],[[7,4],9]],[[[6,3],6],[2,6]]]\n[[6,2],9]\n[0,[[6,[2,0]],[[4,5],8]]]\n[[9,[[6,2],[7,2]]],[[[6,5],6],[8,8]]]\n[[[[2,0],[8,4]],[5,4]],[[3,2],[7,4]]]\n[[[[0,1],[8,2]],3],[[6,[4,9]],[[0,2],1]]]\n[2,[[8,[4,9]],[7,1]]]\n[[[[6,4],4],[0,5]],[[6,0],3]]\n")
