;; This was a fun one! It seemed strange initially, particularly
;; arcane, but turned out not to be too bad.

(ns aoc.2021.day.23
  (:require [clojure.math.combinatorics :as combo]
            [hyperfiddle.rcf :as rcf]))

(rcf/enable!)

;; The problem is like a cross between Pac-Man and the Towers
;; of Hanoi. We have amphipods -- think shrimp -- of types A
;; through D. They're assigned to alphabetical burrows according
;; to their type; but they're mixed up and need to find their
;; ways home.
;;
;; E.g., they might begin like this:

;; ```
;; #############
;; #...........#
;; ###B#C#B#D###
;;   #A#D#C#A#
;;   #########
;; ```

;; But they should always end up like this:

;; ```
;; #############
;; #...........#
;; ###A#B#C#D###
;;   #A#B#C#D#
;;   #########
;; ```

;; I played with a few ways of representing this state:

#_(def state {:alley [nil nil :x nil :x nil :x nil :x nil nil]
              :holes [nil nil '(:B :A)]})

#_(def test-state {:spent 0
                   :board [{:slot nil}
                           {:slot nil}
                           {:slot nil :hole [:A :B] :home-to :A}
                           {:slot nil}
                           {:slot nil :hole [:D :C] :home-to :B}
                           {:slot nil}
                           {:slot nil :hole [:C :B] :home-to :C}
                           {:slot nil}
                           {:slot nil :hole [:A :D] :home-to :D}
                           {:slot nil}
                           {:slot nil}]})

#_(def test-board [[:. :. :. :. :. :. :. :. :. :. :.]
                   [:x :x :B :x :C :x :b :x :d :x :x]
                   [:x :x :A :x :D :x :c :x :a :x :x]
                   [:x :x :x :x :x :x :x :x :x :x :x]])

;; This last, a good-ol' grid, could probably have worked, but it seemed
;; like I'd be better off with a more tailored representation.

;; I landed on basically an array of columns for the board:

(def test-state {:spent 0
                 :board [[nil]
                         [nil]
                         [nil :B :A]
                         [nil]
                         [nil :C :D]
                         [nil]
                         [nil :B :C]
                         [nil]
                         [nil :D :A]
                         [nil]
                         [nil]]})

(def state {:spent 0
            :board [[nil]
                    [nil]
                    [nil :B :C]
                    [nil]
                    [nil :C :D]
                    [nil]
                    [nil :A :D]
                    [nil]
                    [nil :B :A]
                    [nil]
                    [nil]]})

;; Some constants -- a mapping of column index to the amphipod it houses,
;; each amphipod's per-space movement energy cost, and the set of amphipod
;; types:

(def home [nil nil :A nil :B nil :C nil :D nil nil])
(def amphipod-cost {:A 1 :B 10 :C 100 :D 1000})
(def amphipods (set (keys amphipod-cost)))

;; This function takes a board, an amphipod type and an index,
;; and returns all destinations to which an amphipod of type `a`
;; could move from the given index:

(defn candidate-dests [board a i-from]
  (let [walkway (mapv first board)
        must-go-home (nil? (home i-from))]
    (for [i-to (range (count walkway))
          :let [slot-to (board i-to)
                walkway-path (apply subvec walkway (if (< i-from i-to)
                                                     [(inc i-from) (inc i-to)]
                                                     [i-to i-from]))
                is-accessible (every? nil? walkway-path)
                is-non-hole (nil? (home i-to))
                is-home (= a (home i-to))
                is-like-kinded (every? #{a} (keep amphipods slot-to))
                depth (last (keep-indexed (fn [i a] (when-not a i))
                                          slot-to))
                dest (when is-accessible
                       (cond
                         (and is-non-hole (not must-go-home)) [i-to 0]
                         (and is-home is-like-kinded) [i-to depth]
                         :else nil))]
          :when (and dest (not= i-from i-to))]
      dest)))

;; A helper for calculating the cost of an amphipod's move:

(defn move-cost [cost from to]
  (let [[i-from depth-from] from
        [i-to depth-to] to
        exit-cost (* cost depth-from)
        entrance-cost (* cost depth-to)
        traversal-cost (* cost (abs (- i-from i-to)))]
    (+ exit-cost entrance-cost traversal-cost)))

;; Now the important part: a function which, given a board,
;; returns a sequence of `[next-board cost]`, where `next-board`
;; is the board after a single legal move and cost is the cost
;; of getting there, for all legal moves. Memoizing this function
;; turns out to give a solid boost.

(def moves
  (memoize
    (fn moves
      ([board]
       (let [is-to-move (for [i (range (count board))
                              :let [home (home i)
                                    slot (board i)
                                    slot-pods (keep identity slot)]
                              :when (if home
                                      (some (complement #{home}) slot-pods)
                                      (seq slot-pods))]
                          i)]
         (mapcat #(moves board %) is-to-move)))
      ([board from-i]
       (let [slot (board from-i)
             [from-depth a] (->> slot
                                 (keep-indexed (fn [i a] (when a [i a])))
                                 (first))]
         (when a
           (let [dests (candidate-dests board a from-i)
                 cost (amphipod-cost a)]
             (map (fn [dest]
                    (let [board (-> board
                                    (assoc-in [from-i from-depth] nil)
                                    (assoc-in dest a))]
                      [board (move-cost cost [from-i from-depth] dest)]))
                  dests))))))))

;; Translate the above into the world of out `{:board board :spent spent}` datatype:

(defn next-states [state]
  (let [{:keys [board spent]} state
        moves (moves board)]
    (map (fn [[board cost]] {:board board
                             :spent (+ cost spent)})
         moves)))

;; Determine whether a state is terminal, and if so, its total cost:

(defn final-cost [state]
  (when (every? identity (map (fn [home-to slot]
                                (or (not home-to)
                                    (every? #{home-to} (rest slot))))
                              home
                              (:board state)))
    (:spent state)))

;; Now the outer loop: starting with `[init-state]`, we take the set
;; of accessible states and determine from it the set of next-states,
;; repeatedly, until we've attempted all sequences of moves. We calculate
;; the min cost.

;; The key here is to `distinct`-ify our set of states; otherwise, we end
;; up with a massive list of duplicate states, each arrived at by a different
;; ordering of moves.

(defn min-cost [state]
  (loop [min-cost Long/MAX_VALUE
         states [state]]
    (println "States:" (count states) "; Min cost:" min-cost)
    (let [next-states (distinct (mapcat next-states states))
          costs (keep final-cost next-states)
          min-cost (reduce min (conj costs min-cost))
          candidates (->> next-states
                          (filter #(< (:spent %) min-cost)))]
      (if (empty? candidates)
        min-cost
        (recur min-cost candidates)))))

(def test-state-2 {:spent 0
                   :board      [[nil]
                                [nil]
                                [nil :B :D :D :A]
                                [nil]
                                [nil :C :C :B :D]
                                [nil]
                                [nil :B :B :A :C]
                                [nil]
                                [nil :D :A :C :A]
                                [nil]
                                [nil]]})
(def state-2 {:spent 0
              :board [[nil]
                      [nil]
                      [nil :B :D :D :C]
                      [nil]
                      [nil :C :C :B :D]
                      [nil]
                      [nil :A :B :A :D]
                      [nil]
                      [nil :B :A :C :A]
                      [nil]
                      [nil]]})

(rcf/tests
  (min-cost test-state) := 12521
  (time (min-cost state)) := 14350  ; => 5522.568208 ms
  (min-cost test-state-2) := 44169
  (time (min-cost state-2)) := 49742  ; => 6484.733917 ms
  ,)

;; There are probably some tricks to speed this up -- e.g., skip trying
;; to move an amphipod way out into the hallway when it could instead
;; go directly to its home -- but we're already well past December.

;; ⭐️⭐️


