(ns aoc.2020.day.12
  (:require [clojure.string :as s])
  (:import (java.math RoundingMode)))

(def s->int #(Integer/parseInt %))

(defn parse [input]
  (->> (s/split-lines input)
       (map vec)
       (map #(let [[c & is] %]
               [c (Integer/parseInt (apply str is))]))))

(def input (parse "N4\nF85\nL90\nN4\nE3\nL90\nE1\nL90\nW4\nR270\nS5\nF54\nN5\nW4\nL90\nN1\nF55\nL90\nS2\nN2\nL180\nF97\nW1\nF55\nN1\nL180\nF45\nF49\nL90\nF76\nR90\nS3\nF35\nN3\nW1\nS4\nR90\nS4\nF83\nL90\nS1\nF41\nS2\nR90\nF98\nN5\nF51\nL90\nN4\nW1\nF73\nW5\nF34\nS3\nR180\nF85\nN1\nF74\nW4\nF97\nL270\nF65\nW5\nR90\nS1\nW5\nL90\nW2\nN4\nN1\nF81\nS3\nL270\nF96\nW2\nF39\nF27\nW4\nL180\nF75\nN4\nW2\nF38\nR90\nW4\nF100\nR180\nE5\nR180\nE5\nR90\nS5\nL90\nF17\nW3\nN5\nF9\nN1\nW4\nF80\nS1\nW4\nF50\nR180\nW4\nL90\nE4\nF78\nN3\nF86\nW2\nR90\nF46\nS3\nW5\nL180\nS1\nL90\nL90\nF54\nR90\nN5\nE5\nF83\nS4\nW3\nR180\nE4\nN5\nE5\nF90\nW1\nS3\nW5\nF91\nL90\nN1\nF22\nE4\nS2\nF65\nR90\nF16\nR90\nE4\nR180\nW3\nR90\nF94\nL180\nF2\nW1\nR180\nN1\nF59\nR270\nE2\nR90\nS3\nS3\nR90\nF52\nN2\nL90\nR90\nE5\nL90\nR180\nS3\nF26\nE4\nS1\nW5\nR90\nE5\nF69\nE2\nL90\nS2\nE3\nS5\nW2\nS3\nW3\nF78\nN5\nE5\nF87\nL90\nW5\nS1\nL90\nF21\nL90\nF80\nW2\nN4\nW2\nL270\nF52\nL90\nR90\nN3\nF29\nL90\nF20\nR90\nW1\nR90\nE1\nF100\nW1\nS4\nE3\nS3\nF73\nS4\nL90\nS5\nW4\nR180\nE4\nF32\nS1\nR270\nE4\nS5\nL90\nF64\nF12\nE5\nF59\nE4\nF85\nN2\nF27\nR90\nW4\nN2\nF7\nN2\nE3\nF70\nW1\nR180\nF50\nR270\nF43\nL90\nE3\nF23\nW4\nN2\nF15\nL180\nN4\nF31\nR90\nF35\nN1\nE2\nS4\nE4\nN1\nF98\nS2\nW5\nN5\nR90\nF59\nW2\nF22\nS1\nS4\nW5\nF10\nL90\nE3\nR90\nW4\nF11\nN3\nL90\nF12\nS1\nR90\nF25\nE3\nE1\nR90\nF66\nS5\nE1\nF33\nW1\nF57\nL90\nN4\nF44\nW4\nL90\nN5\nL270\nF25\nS3\nF20\nL180\nE4\nR90\nF49\nE4\nS1\nF96\nW2\nR90\nF26\nL180\nF78\nL180\nF88\nW2\nN4\nE1\nF12\nE4\nF81\nE1\nF97\nS3\nE2\nF21\nR180\nS4\nE1\nS4\nE5\nR90\nE4\nS2\nS1\nL90\nL90\nW1\nS5\nL180\nS2\nW2\nS2\nE5\nN5\nW5\nF57\nR270\nN1\nW3\nN5\nF47\nR180\nF28\nS1\nE1\nL180\nF34\nN1\nF96\nS5\nW2\nL90\nS5\nF48\nL90\nE4\nS5\nF24\nL90\nF95\nW1\nR180\nE2\nF76\nR90\nN1\nF51\nS3\nE4\nF24\nS2\nE5\nR90\nE1\nR90\nS4\nE3\nF70\nS1\nE4\nF43\nR180\nF84\nE2\nR90\nF70\nE1\nR90\nF85\nE2\nL90\nF29\nS4\nF73\nF25\nS5\nR180\nS2\nF66\nS3\nF5\nF52\nN3\nL180\nE5\nF14\nW5\nS5\nF81\nN4\nF67\nE3\nR180\nS1\nW4\nF96\nF100\nL270\nF65\nR90\nW2\nS5\nE3\nF32\nF61\nL180\nF57\nN2\nL90\nF10\nF57\nN2\nR90\nF34\nE5\nF13\nE3\nN2\nL90\nN2\nF68\nN3\nW1\nF49\nL90\nF91\nE3\nF84\nF73\nL90\nS1\nE3\nS4\nF66\nL90\nN3\nE2\nS1\nL180\nS5\nL90\nS4\nE4\nS1\nL180\nR90\nS2\nW4\nF59\nL90\nF6\nR90\nW2\nS5\nR180\nS1\nS2\nF96\nW1\nR180\nF87\nR90\nF67\nW5\nL270\nE4\nF51\nR180\nS4\nL90\nF59\nE5\nF66\nE2\nS1\nE4\nF79\nS1\nL180\nF41\nR270\nF66\nL90\nF90\nE4\nS2\nR180\nW2\nF4\nL90\nS5\nL90\nF18\nL90\nF90\nR90\nN4\nE5\nR90\nE4\nS2\nW3\nF97\nW4\nF53\nL90\nS5\nF61\nS2\nF72\nE4\nR90\nS2\nR90\nS2\nF56\nW2\nR180\nW3\nL90\nF31\nF65\nS2\nF11\nN5\nW4\nL270\nE2\nN4\nL90\nF90\nL90\nN3\nL90\nE3\nL90\nF8\nS2\nF63\nL270\nE4\nF51\nF40\nL90\nF34\nW1\nL180\nF79\nN5\nR90\nL90\nW3\nL180\nW1\nL180\nE1\nR90\nN2\nE5\nS1\nR90\nE3\nS2\nE2\nF56\nS2\nW3\nF95\nR90\nN4\nR270\nW4\nS5\nF33\nL180\nN3\nF95\nN1\nE2\nS3\nF4\nL90\nF66\nS1\nR90\nE2\nS3\nF11\nR90\nS5\nN1\nE2\nF64\nN2\nW5\nS2\nF2\nN1\nR90\nN4\nL180\nN1\nF95\nW5\nF99\nF6\nN4\nF69\nS2\nE4\nF49\nR90\nF91\nF76\nR90\nS1\nE2\nS3\nF79\nW4\nL90\nF18\nS5\nR90\nE4\nS1\nF91\nN3\nF40\nE2\nL90\nW2\nS4\nR90\nS1\nR90\nF59\nW3\nN2\nW1\nF86\nR90\nF32\nS1\nF22\nS2\nF4\nS3\nE5\nS4\nS4\nE5\nS4\nF36\nE4\nL90\nF35\nL180\nN1\nW1\nL90\nN4\nR180\nR90\nF22\nR180\nE2\nS2\nW5\nF99\nE2\nS3\nF22\nE1\nL180\nS2\nL180\nW4\nR270\nF26\nN5\nE2\nF89\nE5\nL90\nN4\nR90\nW4\nL270\nW5\nN5\nW4\nS2\nE3\nN4\nW1\nF95\nW1\nF10\nN3\nR90\nW1\nF73\nW1\nN3\nF33\nL180\nE3\nR90\nR90\nN2\nE4\nE5\nW4\nN1\nF91\nN1\nW1\nF49\nS2\nE5\nS3\nF43\nW5\nF34\nE3\nE1\nN1\nE3\nL180\nW2\nF27\nL180\nE5\nF28\nR90\nW1\nL90\nF99\nS2\nF48\nW1\nW1\nR180\nW1\nL180\nF35"))

(def t (parse "F10\nN3\nF7\nR90\nF11"))

(def init-ship {:dir [1 0]
                :loc [0 0]
                :way [10 1]})

(defn with-scale [scale n]
  (.setScale n scale RoundingMode/HALF_UP))

(defn degs-about [point degrees [cx cy :as center]]
  (let [rads (Math/toRadians degrees)
        [x y] (map bigdec
                   (map - point center))
        c (bigdec (Math/cos rads))
        s (bigdec (Math/sin rads))
        x' (with-scale 5 (+ cx (- (* c x)
                                  (* s y))))
        y' (with-scale 5 (+ cy (+ (* s x)
                                  (* c y))))]
    [x' y']))

(defn degs-about-origin [degrees point]
  (degs-about point degrees [0 0]))

(defn rotate-ship [ship degs]
  (update ship :dir #(degs-about-origin degs %)))

(defn rotate-way [ship degs]
  (update ship :way #(degs-about-origin degs %)))

(defn move-point [point dir units]
  (let [v (map (partial * units) dir)]
    (map + point v)))

(defn move-ship [ship dir units]
  (update ship :loc #(move-point % dir units)))

(defn move-way [ship dir units]
  (update ship :way #(move-point % dir units)))

(defn step [{:keys [dir] :as ship} [command value]]
  (case command
    \N (move-ship ship [0 1] value)
    \E (move-ship ship [1 0] value)
    \S (move-ship ship [0 -1] value)
    \W (move-ship ship [-1 0] value)
    \L (rotate-ship ship value)
    \R (rotate-ship ship (- value))
    \F (move-ship ship dir value)))

(defn step' [{:keys [dir way] :as ship} [command value]]
  (case command
    \N (move-way ship [0 1] value)
    \E (move-way ship [1 0] value)
    \S (move-way ship [0 -1] value)
    \W (move-way ship [-1 0] value)
    \L (rotate-way ship value)
    \R (rotate-way ship (- value))
    \F (move-ship ship way value)))

(defn manhattan-dist [coord]
  (->> coord
       (map #(.abs (biginteger %)))
       (apply +)))

(defn simulate [commands interpreter]
  (->> commands
       (reduce interpreter init-ship)
       (:loc)
       (manhattan-dist)))

;; A
(simulate input step)
;; B
(simulate input step')
