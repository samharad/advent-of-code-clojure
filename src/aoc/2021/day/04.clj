;; For [Day 4](https://adventofcode.com/2021/day/4) I remained off
;; of the midnight-solvers' wagon, which was nice because it gave me
;; time today to develop my solution using [spec](https://clojure.org/guides/spec).
;; Now that I have my stars, I'll re-create my thought process in blog form.

;; First, the boilerplate (where I continue my foolhardy search for the upper
;; limit of dependencies I can use to solve a toy problem):

(ns aoc.2021.day.04
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            [net.danielcompton.defn-spec-alpha :as ds]
            [clojure.string :as str]
            [hyperfiddle.rcf :as rcf]))

(rcf/enable!)

;; Today's task is to simulate a game of bingo. The input consists of a
;; list of numbers drawn from a hat...

; 7,4,9,5,11,17,23,...

;; ... followed by several game boards:

; 22 13 17 11  0
;  8  2 23  4 24
; 21  9 14 16  7
;  6 10  3 18  5
;  1 12 20 15 19

; 3 15  0  2 22
;  9 18 13 17  5
; 19  8  7 25 23
; 20 11 10 24  4
; 14 21 16 12  6

;; The real input is much larger:
(def input "26,55,7,40,56,34,58,90,60,83,37,36,9,27,42,19,46,18,49,52,75,17,70,41,12,78,15,64,50,54,2,77,76,10,43,79,22,32,47,0,72,30,21,82,6,95,13,59,16,89,1,85,57,62,81,38,29,80,8,67,20,53,69,25,23,61,86,71,68,98,35,31,4,33,91,74,14,28,65,24,97,88,3,39,11,93,66,44,45,96,92,51,63,84,73,99,94,87,5,48\n\n62  5 77 94 75\n59 10 23 44 29\n93 91 63 51 74\n22 14 15  2 55\n78 18 95 58 57\n\n43 12 34 37 11\n84 69 52 38 68\n40 89 67 98 16\n47 59 96 63 95\n 3 21 58 75 20\n\n87 59 20 32 15\n16 50 12 24 86\n14 37 72 63 92\n25 30 85 55 39\n52 11 68 31 91\n\n43 76 57 14 21\n11 55 71 61 73\n72 51 86 97 13\n59  5 53 91 77\n99 35 63 95 47\n\n 6 33 98 87 81\n62 19 11 70 43\n67 71 74 13 82\n10  0 59  7 86\n38 40 21 27 66\n\n74 50 91 76 88\n59  4 73 39 47\n23 49 38 68 96\n 6 12 66 32 21\n24 44 53 43 55\n\n 4  0 44 12 56\n70 26 32 16  6\n59 19  1 97 40\n90 24 57 48 52\n21  3  8 93 34\n\n23 17 74 14 70\n 5 64 83  3 66\n79 28 27 85 77\n93 22 75 97 45\n99 33  2 11 95\n\n85 47 97 19 52\n59 28 24 27  1\n 9  5 55 34 82\n77 91 32 33 29\n81 48 96 80 18\n\n34 32 43 57 66\n35 47 40 14 63\n68 85 36 26  1\n78 89 97 10  9\n56 64 61 31 65\n\n23 82 72 13 92\n37 25 99 33 56\n98 47 74 78 83\n32  0 19 31 65\n59 20 30 49 96\n\n13 14 44 26  4\n65 54 98 19 82\n 8 74 72 91 99\n32 15 93 95 75\n34 52  7 78  2\n\n58  1 50 20 76\n57 67 47 66 69\n74 82 53 80  6\n56  8 21 36 26\n62 15 38 30 95\n\n88 21 24 81 17\n61 90 48 94  4\n95 75 84 59 86\n28 30 50 36 15\n55 92 19 53 58\n\n 3 72 23 55 22\n95  6 48  8 46\n13  5 35 57 45\n33 79 38 88 36\n94 64 12 14 63\n\n30 80 53 85 22\n 5 29 73  0  1\n79 68 40 76 13\n90 95 26 64 47\n32 21 97 50 52\n\n71 65  3 17 24\n69 38 28  0 73\n74 35 19 64 16\n13 90 72 56 20\n31 60 41 66 52\n\n48 60 49 92 20\n41 69 44 74 87\n 5 85 77 55 58\n 7 52 56 25 38\n22 24 13 46 30\n\n71 61 46 52 82\n64 53 62 81 98\n48  7 22 35 57\n93 30 66 47 55\n25 13 76 34 17\n\n71 73  4 86 88\n15 54 75 14 89\n 0 85 53 80 81\n17 21 97 57  5\n84 66 27 48 82\n\n23 79 93 76 74\n51 75 18 70 37\n83 38 13 17 20\n11 60 14 30 39\n 6 72 50 97 65\n\n62 72 36 95 37\n26 99 41 59  3\n88 91 74 33 19\n12 68  6 25 82\n49  1 32 55 43\n\n 0 70 89 72 26\n11 57 47 19 16\n 7  4 52 12 10\n80 96 49 53 55\n95 33 66 76 40\n\n34 10 57 94 17\n29 76 53  6 27\n92 26 18 15 14\n71 43 39 62  8\n88 32 93  0 25\n\n98 13  3 59 37\n65 25 73 38  6\n40 94 71 11 93\n85  8 21 17 26\n 0 77 43 12 15\n\n25 51 30 55 41\n47 45 77  1 91\n38 94 44 76  7\n34 35 65 50 11\n21 37 70 87 61\n\n14 11 48 58 30\n39 10  7 68 63\n31 69 88 90 29\n84 59 47 35 46\n94 26 66  6 78\n\n84 23 89 48 75\n16 56 93 26 86\n99  9  0  7 14\n87 61 50 76 32\n 6 22 72 53 73\n\n64  3 21 57 14\n82 19 63 65 67\n72 68 51  7 12\n88 34 95  6 71\n62 28 85  9 35\n\n82 77 50  9  5\n74  1 19 58 66\n95 54 41 27 47\n64 67  3 69 87\n68  0  4 81 92\n\n29 48 34 66 85\n83 86 65 30 13\n93 78 67 50 72\n54 12 59 70 90\n40 11 80 89 99\n\n49 44 33 55 20\n19 42 51 29 70\n57 87 86 82 36\n 2 92  3 25 95\n52 46  4 40 38\n\n13 51 71 91 45\n99 38 81 53  4\n88 30 94 76 92\n93 29 87 42 12\n72 52 24 64 62\n\n24 72  9 12  0\n37  5  1 61 30\n75 10 81 21 93\n22 68 38 64 41\n15 80 28 91 49\n\n49 19 38 60 64\n 7  5 65 32 69\n93 17 46 73 28\n 4 37 51 91  6\n 2 79 66 47 34\n\n 2 61 66 63 41\n26 44  7 77 80\n11 50 48 47 68\n76 53 86 55 81\n21 29 83 95 54\n\n53 72 37 75 66\n 0 63 55 23 70\n51 85 16 65 49\n31 74 42 40 12\n95 80 86 56 83\n\n78 62  8 22 45\n34 12  3 69 18\n 9 27  4 92 88\n95 36 87  7 46\n81 61 29 98 84\n\n50 43  4 69 23\n71 90 19 58 24\n35 64 45 70  8\n78 10 46 66 18\n75 98 49 81 61\n\n70 95 36  8 79\n19 89 58  7 17\n73 37 38 46 66\n12 69 61 59 83\n54 94 80 74 64\n\n 6 60 21 36 82\n30 72 86  9 59\n39 54 88 29 32\n91 81 92 16 48\n67 83 71 53 69\n\n95 41 99 27 28\n34 38 30 42 89\n 3 85 60 46 12\n45  4 82 26 16\n59 24 71 63 73\n\n22 25 53 67 87\n83 39 11 14 58\n70 88 73  8 33\n 0 79 95 27 12\n90 75 89 66 24\n\n79  7 72 76 97\n20 75 73  1 34\n58 81 53 98  6\n 2  5 90 88 77\n74 24 14 83 23\n\n80 56 54 78 84\n65 40 64 99 90\n19 30 60 72 61\n82 50 85 13 98\n33  8 86 81 39\n\n98 42 45 20  3\n 1 12 91 41 43\n86 36 51 77 47\n96 74  2 71 82\n83 93 39 17 92\n\n47 62 87 18 57\n86 97  1 53 16\n17 24 99 20 61\n59 12 29 38 52\n85  2 32 96 30\n\n 9  3 85 30 25\n17 28 18 68 76\n96 74  2 86 23\n 4 50 36 71 44\n14 81 90 97 92\n\n 4 69  5 16 23\n 0 57 82 19 75\n 6 71 30 59 34\n12 32 26 96 58\n13  3  9 64 78\n\n10 84 33  5  3\n 4 81 24 70 92\n52  2 13 41 37\n46  8 20 79 80\n21 83 76 69 62\n\n57 60 24 90 64\n84 21 45 13  0\n36 78 26 83  8\n85 20 19 53 76\n14 32 17 63 59\n\n 8 61 36 48 78\n85 17 70 47  6\n82 30 67 74 11\n88 34 15 68 62\n59 37 91  1 35\n\n72 89 85 70 24\n93 50 73 37 71\n13 44 95 69 28\n27 20 17  0 22\n30  7 84 77 83\n\n78 19 97 71 85\n15 76 40 90 66\n80 64  3 38 83\n10 30 77 84 14\n29 22  8 67 60\n\n46 42 64 82 60\n98 75 11 55 36\n94 80 53 25 14\n43 33 40 12 31\n91 19 34  3  7\n\n49 61 27 75 73\n32  0 30 47 57\n28 82 36 19 50\n33 14 22 53 52\n97 95 99 31 69\n\n63 32 58 95 87\n55  3 85 91 15\n69 66 45 35  1\n 2  8 37 67 40\n59 31 13 94 73\n\n41 29 37 47 88\n68 87 60 44 36\n19 97 16 15 32\n43 42 70 75 28\n72 92 58 96 48\n\n26 16 48 39  8\n44 24 85 47 80\n42 30 40 77 76\n 4 73 86 38 52\n17 79 66 54 96\n\n49 98 11 57 46\n26 32 14 87 56\n 8 33 53 96 34\n72 25 51 41 61\n67 99 50 35 47\n\n66 33 58 55 27\n57 31 68 95 65\n94 26 25 91 69\n99 24  0 70 78\n30 80 49 47 48\n\n20  3 16 11 39\n90 77 35 67 88\n58 15 21 43 78\n37 44 97 26 28\n25 76 41 18 64\n\n57 75 48 62 41\n15 56 67 32 52\n55  2 99 65 20\n63 79 94 78  3\n84  6 72 82 88\n\n99 74  3 29 59\n91 87 84 11 39\n18 89 21 36 98\n22 83 14  0 72\n57 32 76 26 34\n\n82  9 22 59 13\n62 81 19 99 35\n87 73 47 49 58\n75 88  6 21 28\n30 16 52 65 86\n\n63 79 78 41 67\n93 58 94 51 43\n88 98 34 23  8\n12 11 60 97 18\n 7 69 59 19 89\n\n56 49 15 53 47\n33  4 65 22 82\n59 11 98 23 78\n 6 74 20 89 45\n96 81 77 95 90\n\n86 85 22 80 64\n56 33 25  9 18\n52  3 39 63 34\n40 91  8 59 60\n82 93 35  4  7\n\n 0 55 97 38 71\n26 86 58 82  2\n85  8 51 70  7\n59 44 19 28 48\n73 52 83 36 87\n\n47 26 99 28 87\n54 20  3 38 17\n85  8 36  7 77\n62 29 68 22 16\n97 96 51 27 71\n\n43 78 20 34 55\n59 16 61 33 82\n48 42 40 29 58\n76  6 75 70  9\n69 50 87 47 27\n\n39 46 71 10 33\n79 99  0 18  3\n85  1 90 81 75\n31  8 88 62 73\n23 58 53 11 93\n\n40 49 92 55 51\n33  6 64 98 86\n77 24 48 89 13\n32  4 44 53 26\n30  0 34 65 95\n\n35 30  9 51 34\n98 80 56 62 85\n93 36 18 65 12\n54 32 26 79 49\n 5 83 41 60 89\n\n39 12 43 45 32\n58 81 94 62 90\n20 80 31 60 24\n42  4 87 44 88\n97 21 10 71 17\n\n96 23 55 11 57\n48 38 19 70 16\n12 32 54 21  1\n74 58 22 28 62\n53 66 95 78 41\n\n66 57 32 70 54\n33 88 91 99 72\n83 65 86 19  3\n64 73 37 12 35\n21 59  1 75 76\n\n65 40 90 99 22\n 7 59  0  9 89\n77 27 57 29 39\n16 52 38 82 88\n76 83 20 93 11\n\n57 63 59  4 12\n84 72 68 66 31\n76 50 98 91 85\n95 83 35 99 29\n78 40 90 21 86\n\n73 66 45 43 83\n 3 67 82 70 58\n65 64 44 72  8\n76  4 62 79 99\n75 97 36 31 89\n\n16 38 22 24 96\n49 92 99 13 95\n 0 57 29 78 56\n55  7 25 81 11\n27  3 91 93 33\n\n87 23 90 88 99\n66  0  3 85 54\n79 19 14  8 34\n33 22 50 36  9\n70 43 81 45 92\n\n81  8 55 76 58\n74 53 16 63 17\n30 31 41 72 37\n73  4 18 87 34\n88 95 21 50 77\n\n90 51 31 62 49\n85 89 75 73 72\n18  8 44 48 27\n71 17 15 95 34\n35 78  3 45  7\n\n39 10 11 51  6\n77 28 50 89 60\n31 18 14 72 85\n 1 87 21 66 40\n16 42 91 96 93\n\n53 41 38 71 37\n 4 54 70 58 60\n29 68 98 46 18\n 1 66 91 42 48\n44 81 62 63 15\n\n78 42 97 11  1\n49 33 35  0  2\n67 81 94 26 29\n98 54  9 93 25\n18 41 13 90 64\n\n73  8 40 72 45\n29 27 85 35  6\n78 53 51 71 16\n32 90 41 37 84\n59 88 56  0 26\n\n51 19 95 31 47\n59 58 82 12 68\n64 79 76 69 87\n36 17  1 48 16\n70 46 62 83 94\n\n10 70 39 85 28\n 9  3 48 95 56\n40 34 44 57 31\n79 27 97 50 15\n32 96  0 82 43\n\n42 61  6 22 41\n26 20  1 44 81\n10 39  4 18 32\n74 47  7 14 91\n55 86 75 99 76\n\n92 71  2 48 21\n11  4 90 15 49\n32 68 94 46 99\n56 26 78  8 64\n25 29 61 58 75\n\n19 68 20 16 61\n81 45 57 35 73\n39 44 56 72 79\n96 78 93 62  5\n33  2 26 18 77\n\n17 49 83 15 84\n38 64 35 57 61\n10 95 46 65 39\n62 92 66 18 60\n14  5 53  6 79\n\n27 84 67 87 53\n91  8 28 58 71\n60 72 39 75 31\n51 33 92 82 15\n68 40 88 45 86\n\n90 21 80 29  2\n19 63 36 48 83\n 0 82 40  1 49\n 4  9 97 54 46\n18 95 17 22 42\n\n68 51 57 20 37\n33 12 78 34 28\n31 93  5 85 95\n79 29 21 26 90\n60 69 99 39 24\n\n80 82 55  3 20\n74 69 72 41 97\n56  1 78 45 53\n60 93  8 36 19\n85 86 15 35 14\n\n38 40 36 64 82\n87 31 25 74 75\n48 43 72 79 51\n62 86 22 83 29\n77 93 46 81 13\n\n 1 93 61 43 39\n20 67  4 58 32\n99 31 72 40  6\n88 19 42 52 49\n35 45 65 50 91\n")

;; I began by considering how to model the bingo game as Clojure data. This went something
;; like the following:
;; * A game is a sequence of hat-draws and a collection of players. Each player...
;;     * ... has a board, a 2D vector of spaces, each of which...
;;         * ... has a bingo-number and an indicator for whether the spot has been "filled"
;;     * ... has also, for efficiency's sake, a map of bingo-number to grid-index
;;     * ... may have a winning-number -- the first number drawn for which the player's board became a winner

;; In fact, before thinking this through completely, I decided to use spec -- Clojure's powerful, a-la-carte
;; runtime type-checking-ish system -- as a tool for thought for solidifying my more intricate, nested model.

;; First, the board and its contributing specs:

(s/def ::number integer?)
(s/def ::is-picked boolean?)
(s/def ::space (s/keys :req [::number ::is-picked]))
(defn spec-grid-of [pred]
  (s/coll-of (s/coll-of pred :kind vector?) :kind vector?))
(s/def ::board (spec-grid-of ::space))

;; Then the map of bingo number to coord:

(s/def ::coord (s/coll-of integer? :kind vector? :count 2))
(s/def ::coords-by-number (s/map-of ::number ::coord))

;; The player:

(s/def ::winning-number (s/or :nil nil? :number ::number))
(s/def ::player (s/keys :req [::coords-by-number ::board ::winning-number]))

;; The hat-pulls:

(s/def ::selections (s/coll-of ::number))

;; And finally the game, i.e., the entire program state:

(s/def ::players (s/coll-of ::player))
(s/def ::game (s/keys :req [::players ::selections]))


;; With the data out of the way, now on to the algorithms.

;; I figured that since I had these nice specs lying at hand, I may as well
;; spec out my functions. I find spec's own `fdef` somewhat tedious to use --
;; it turns each function definition into two top-level forms -- so I first
;; pulled in [orchestra](https://github.com/jeaye/orchestra) for it's `defn-spec`
;; macro, and then switched to Daniel Compton's [defn-spec](https://github.com/danielcompton/defn-spec),
;; since its `defn` macro is resolvable by [Cursive](https://cursive-ide.com/). I
;; kept Orchestra around for the runtime checking of function return values.

;; Now, to parse the input:

(ds/defn parse-board :- ::board
  [row-strs :- (s/coll-of string?)]
  (let [parse-row (fn [row-str]
                    (->> (str/split (str/trim row-str)
                                    #"\s+")
                         (map parse-long)
                         (mapv (fn [number] {::number    number
                                             ::is-picked false}))))]
    (mapv parse-row row-strs)))

(defn rows [board]
  (range (count board)))

(defn cols [board]
  (range (count (first board))))

(defn coords [board]
  (for [r (rows board)
        c (cols board)]
    [r c]))

(ds/defn board->player :- ::player
  [board :- ::board]
  (let [coords-by-number (->> (for [[r c] (coords board)
                                    :let [number (get-in board [r c ::number])]]
                                [number [r c]])
                              (into {}))]
    {::coords-by-number coords-by-number
     ::board            board
     ::winning-number   nil}))


(ds/defn parse-game :- ::game
  [input :- string?]
  (let [[[selections-s] & boards-s] (->> (str/split-lines input)
                                         (partition-by empty?)
                                         (filter (complement #{'("")})))
        selections (map parse-long (str/split selections-s #","))
        boards (map parse-board boards-s)
        players (map board->player boards)]
    {::players players
     ::selections selections}))

(def init-game (parse-game input))

;; Now for some more building-block functions that will contribute
;; to the ultimate simulation algorithm:

(ds/defn unmarked-numbers :- (s/coll-of ::number)
  [board :- ::board]
  (for [coord (coords board)
        :let [{::keys [number is-picked]} (get-in board coord)]
        :when (not is-picked)]
    number))

(ds/defn score :- (s/or :nil nil? :int integer?)
  [player :- ::player]
  (let [unmarked (unmarked-numbers (::board player))
        winner (::winning-number player)]
    (when winner
      (* (reduce + unmarked)
         winner))))

(ds/defn won-at? :- boolean?
  [board :- ::board
   coord :- ::coord]
  (let [[r c] coord
        row (for [c (cols board)] [r c])
        col (for [r (rows board)] [r c])
        picked? (fn [[r c]] (get-in board [r c ::is-picked]))]
    (or (every? picked? row)
        (every? picked? col))))

(ds/defn mark-number :- ::player
  [player :- ::player
   number :- ::number]
  (let [{::keys [board coords-by-number winning-number]} player
        [r c :as coord] (coords-by-number number)]
    (if (not coord)
      player
      (let [board (assoc-in board [r c ::is-picked] true)
            did-win (won-at? board coord)
            ; Leave the old winning number if the player has already won
            winning-number (or winning-number
                               (and did-win number)
                               nil)]
        (-> player
            (assoc ::board board)
            (assoc ::winning-number winning-number))))))

;; And finally, the simulation algorithms.

;; Part 1 asks us to play the game until a single player wins:

(ds/defn first-winning-score :- integer?
  [game :- ::game]
  (loop [{::keys [selections players]} game]
    (let [[selection & selections] selections
          players (map #(mark-number % selection) players)
          winner (some score players)]
      (or winner (recur {::selections selections ::players players})))))

;; Part 2 asks us to play until all players have won.

;; (In my solution, I continue to update already-winning players' boards
;; and scores as more numbers are called; that's probably "wrong," even though
;; it works, but I've already spent all my Advent points today 😆.)

(ds/defn last-winning-score :- integer?
  [game :- ::game]
  (loop [{::keys [selections players]} game]
    (let [[selection & selections] selections
          players (map #(mark-number % selection) players)
          final-winner (and (every? score players)
                            (first (filter #(= selection (::winning-number %))
                                           players)))]
      (if final-winner
        (score final-winner)
        (recur {::selections selections ::players players})))))

;; And here it is:

(defn part-1 []
  (first-winning-score init-game))

(defn part-2 []
  (last-winning-score init-game))

;; Lastly, before I do my testing, I'll ask Orchestra to runtime-check my
;; function specs:
(st/instrument)

(part-1)

(rcf/tests
  (part-1) := 35670
  (part-2) := 22704)

;; Was it worthwhile to spec out every high level function? It was barely any additional code,
;; and it definitely turned up some bugs, in the same way hand-written unit tests might.

;; On the other hand, grokking the spec exceptions was more difficult for me than it would have been to
;; understand failing test cases. When sending my namespace to the REPL and so executing my RCF tests,
;; spec exception details weren't printed -- all I'd get was something like `Call to aoc.2021.day.04/mark-number
;; did not conform to spec`, so I'd then have to explicitly evaluate a function-call form to see the exception details
;; printed (or scroll through a heap of `*e`). It also took me a long time to realize that the aforementioned
;; error message may mean a failure to conform the **return** value, and not necessarily the arguments --
;; possibly a reason to use spec's native instrumentation, which only checks function arguments.

;; Overall takeaways:
;; * Spec was very helpful at the beginning for thinking through my model concretely
;; * Using spec for runtime checking has a bit of a learning curve, but can help find bugs without writing tests
;; * When using spec in the future, I want to make an effort early on to ensure that its runtime checks integrate
;;   smoothly into my dev workflow

;; Thanks for reading! I'm looking forward to seeing other Clojurians' solutions, which will surely
;; be much pithier than mine. And as always, comments and feedback are welcome.

;; ⭐️⭐️