;; Today's puzzle was very satisfying to solve -- one big
;; hack notwithstanding.

(ns aoc.2021.day.21
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.math.combinatorics :as combo]))

(rcf/enable!)

;; [Day 21](https://adventofcode.com/2021/day/21) involves
;; simulating a dice game. Basically we have a circular
;; board with spaces marked from 1 to 10, and two players
;; that start out on different spaces. They take turns
;; rolling 3 dice in sequence; they move the corresponding sum
;; number of spaces; and then they add their landing spot to
;; their score.

(def min-board-space 1)
(def max-board-space 10)
(def board-spaces (range min-board-space
                         (inc max-board-space)))

(def init-board (cycle board-spaces))

(defn init-player [player-start]
  {:score 0 :board (drop (dec player-start) init-board)})

(defn init-game [die & players]
  {:die die :players (vec players) :winner-i nil :num-rolls 0})

;; Part 1 asks us to play until the first player scores
;; at least 1000, using a deterministic die with 100 sides.

(def rolls-per-turn 3)
(def min-winning-score 1000)

(def deterministic-100-die (cycle (map inc (range 100))))

;; We'll use a too-long function to simulate the game
;; until there's a winning player.

(defn play-game [game]
  (loop [game game]
    (if (:winner-i game)
      game
      (recur
        (reduce (fn [game player-i]
                  (let [{:keys [die players]} game
                        player (get players player-i)
                        [rolls die] (split-at rolls-per-turn die)
                        roll (reduce + rolls)
                        board (drop roll (:board player))
                        spot (first board)
                        score (+ (:score player) spot)
                        player (-> player
                                   (assoc :board board)
                                   (assoc :score score))
                        game (-> game
                                 (assoc :die die)
                                 (assoc-in [:players player-i] player)
                                 (update :num-rolls (partial + rolls-per-turn)))]
                    (if (>= score min-winning-score)
                      (reduced (assoc game :winner-i player-i))
                      game)))
                game
                (range (count (:players game))))))))

(def t-game (init-game deterministic-100-die
                       (init-player 4)
                       (init-player 8)))

(def game (init-game deterministic-100-die
                     (init-player 10)
                     (init-player 4)))

(defn part-1 [game]
  (let [game (play-game game)
        {:keys [winner-i players num-rolls]} game
        loser (nth (cycle players) (inc winner-i))
        loser-score (:score loser)]
    (* loser-score num-rolls)))

(rcf/tests
  (part-1 t-game) := 739785
  (part-1 game) := 908091)

;; Part 2 went in a [direction I didn't expect](https://rickandmorty.fandom.com/wiki/A_Rickle_in_Time?file=A_Rickle_in_Time.png).
;; It asks us to play until a player hits just 21 points, using
;; a die with just 3 sides: 1, 2, and 3. But it wants us to play
;; every possible game -- every sequence of dice rolls until a
;; player wins -- and count up the number of wins each player
;; will get. This means **hundreds of trillions** of games!

;; We have a tree that branches **27** ways each time a player
;; rolls his dice, i.e., 3 times for the first die rolled, times
;; 3 times for the second die rolled, times 3 times for the third
;; die rolled. (This eluded me for a long time -- I forgot that
;; three dice, not one, are rolled per turn.)

;; Naively navigating to each leaf would take terribly long.
;; Initially I thought there must be a slick math trick to speed
;; this up (and maybe there is), but it turns out we can make
;; an efficient solution just by caching some results so that
;; we don't have to descend to every leaf of the reality tree.
;; (And full disclosure -- I accidentally saw a [meme on my Reddit feed](https://www.reddit.com/r/adventofcode/comments/rl80mi/2021_day_21/)
;; today which was reassuring if not outright helpful.)

;; First, we define a mapping of current-space to the list
;; of spaces we can reach by rolling the three dice. A space
;; may appear multiple times -- e.g. from space 1, we can reach
;; space 7 by rolling (2, 2, 2), or by rolling (1, 2, 3), or
;; (3, 2, 1), etc.

(def next-spaces
  (let [spaces (cycle (map inc (range 10)))
        roll-seqs (combo/permuted-combinations [1 1 1
                                                2 2 2
                                                3 3 3] 3)
        roll-sums (map #(apply + %) roll-seqs)
        nexts (fn [board]
                (map (fn [roll]
                       (first (drop roll board)))
                     roll-sums))
        nexts (->> (range 10)
                   (map #(nexts (drop % spaces))))]
    (into {} (map vector (take 10 spaces) nexts))))

;; From each space we can go 27 ways, corresponding to the
;; 3x3x3 dice rolls:

next-spaces
; =>
; {7 (10 1 1 1 2 2 2 2 2 2 3 3 3 3 3 3 4 4 4 3 4 4 4 5 5 5 6),
;  1 (4 5 5 5 6 6 6 6 6 6 7 7 7 7 7 7 8 8 8 7 8 8 8 9 9 9 10),
;  4 (7 8 8 8 9 9 9 9 9 9 10 10 10 10 10 10 1 1 1 10 1 1 1 2 2 2 3),
;  6 (9 10 10 10 1 1 1 1 1 1 2 2 2 2 2 2 3 3 3 2 3 3 3 4 4 4 5),
;  3 (6 7 7 7 8 8 8 8 8 8 9 9 9 9 9 9 10 10 10 9 10 10 10 1 1 1 2),
;  2 (5 6 6 6 7 7 7 7 7 7 8 8 8 8 8 8 9 9 9 8 9 9 9 10 10 10 1),
;  9 (2 3 3 3 4 4 4 4 4 4 5 5 5 5 5 5 6 6 6 5 6 6 6 7 7 7 8),
;  5 (8 9 9 9 10 10 10 10 10 10 1 1 1 1 1 1 2 2 2 1 2 2 2 3 3 3 4),
;  10 (3 4 4 4 5 5 5 5 5 5 6 6 6 6 6 6 7 7 7 6 7 7 7 8 8 8 9),
;  8 (1 2 2 2 3 3 3 3 3 3 4 4 4 4 4 4 5 5 5 4 5 5 5 6 6 6 7)}

;; Now we write a new function to simulate our many games;
;; the only thing we need to track is the winner, i.e. whether
;; player A or B wins a given game. By memoizing this function,
;; we ensure that far fewer than the hundreds-of-trillions of
;; nodes in our decision tree need to be visited.

;; There's a fun hack here: to avoid explicitly dealing with
;; the fact that player A goes first and terminates a round
;; early if he wins, we instead count each of his wins as one
;; 27th of a win, since in my simulation the round will always be
;; finished by player B making his 27 different dice rolls.

;; For fun, we'll count some stats re: our cache efficiency.
(def cache-misses (atom 0))
(def total-invocations (atom 0))

(declare ways-to-win)

(def ways-to-win-slow
  (fn [[a-space a-needs] [b-space b-needs]]
    (swap! cache-misses inc)
    (cond
      (zero? a-needs) [1/27 0]  ; fun hack!
      (zero? b-needs) [0 1]
      :else (let [a-nexts (next-spaces a-space)
                  b-nexts (next-spaces b-space)
                  nexts (combo/cartesian-product a-nexts b-nexts)]
              (->> nexts
                   (map (fn [[a-next b-next]]
                          (ways-to-win  ; recur to the memoized fn
                            [a-next (max 0 (- a-needs a-next))]
                            [b-next (max 0 (- b-needs b-next))])))
                   (reduce (partial map +)))))))

(def ways-to-win-memoized (memoize ways-to-win-slow))

(defn ways-to-win [a b]
  (swap! total-invocations inc)
  (ways-to-win-memoized a b))

(defn part-2 [a-space b-space]
  (let [target-points 21
        [a-wins b-wins] (ways-to-win [a-space target-points]
                                     [b-space target-points])]
    (max a-wins b-wins)))

(rcf/tests
  ;(time (part-2 4 8)) := 444356092776315
  (time (part-2 10 4)) := 190897246590017)  ; => 10264.366666 ms

(def pct #(str (double (* 100 (/ %1 %2)))
               "%"))
(println "Total invocations: " @total-invocations)
(println "Cache misses:      " @cache-misses)
(println "Miss percentage:   " (pct @cache-misses @total-invocations))
(println "Hit percentage:    " (pct (- @total-invocations @cache-misses)
                                    @total-invocations))

; =>
; Total invocations:  10017919
; Cache misses:       15996
; Miss percentage:    0.1596738803737583%
; Hit percentage:     99.84032611962624%

;; To me, it's almost non-intuitive that of the hundreds of
;; trillions of scenarios, virtually all of them are repetitive.

;; ⭐️⭐️



