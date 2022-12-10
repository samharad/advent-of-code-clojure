(ns aoc.2022.day.09
  (:require [clojure.string :as str]))

(def input "U 1\nL 1\nR 2\nL 1\nD 2\nR 1\nU 2\nR 1\nU 1\nR 2\nD 2\nL 1\nR 1\nD 2\nR 1\nL 1\nR 1\nD 1\nU 1\nR 1\nL 1\nR 2\nU 1\nR 1\nD 2\nL 2\nU 1\nR 1\nD 1\nL 1\nR 1\nL 2\nD 1\nU 1\nD 2\nR 2\nD 2\nR 2\nD 2\nR 2\nU 1\nL 2\nR 2\nD 2\nL 2\nU 2\nR 1\nL 1\nU 1\nR 1\nD 1\nR 1\nL 1\nR 1\nD 2\nL 2\nU 1\nR 1\nL 1\nR 1\nU 2\nD 2\nU 2\nL 1\nU 2\nL 1\nR 2\nU 2\nD 1\nL 1\nR 1\nL 2\nU 1\nD 2\nR 2\nU 2\nR 1\nL 1\nU 2\nR 2\nL 1\nR 2\nD 1\nU 2\nD 2\nR 2\nL 1\nR 1\nU 2\nD 2\nU 2\nD 1\nU 1\nR 2\nD 1\nR 1\nL 1\nD 2\nL 2\nU 2\nR 2\nU 1\nR 2\nU 1\nD 1\nR 2\nU 1\nR 1\nU 2\nR 2\nD 2\nL 2\nD 3\nR 2\nU 2\nR 2\nU 2\nL 3\nU 1\nL 1\nR 3\nD 2\nR 2\nU 1\nL 2\nD 2\nL 3\nU 3\nD 2\nR 3\nU 3\nR 2\nD 2\nU 3\nL 1\nU 2\nR 2\nL 1\nD 3\nU 1\nR 3\nL 2\nD 1\nU 2\nL 3\nR 3\nU 3\nL 1\nD 1\nR 3\nD 3\nR 1\nU 2\nR 3\nL 2\nR 1\nU 2\nL 3\nD 1\nR 3\nU 2\nD 2\nR 2\nD 3\nL 3\nD 1\nL 2\nD 1\nR 2\nL 1\nD 2\nU 1\nD 3\nU 1\nR 1\nU 1\nR 3\nL 3\nU 2\nD 3\nR 3\nU 1\nL 1\nR 1\nU 3\nL 2\nU 2\nD 3\nL 2\nD 1\nR 2\nD 2\nL 1\nR 2\nU 1\nD 3\nR 3\nD 2\nR 3\nL 3\nR 3\nD 1\nU 1\nR 1\nU 2\nD 2\nL 2\nD 3\nR 2\nD 3\nU 2\nL 2\nR 1\nU 3\nL 1\nD 1\nU 1\nD 2\nU 1\nL 3\nR 2\nD 1\nL 3\nU 1\nR 3\nD 2\nU 4\nD 1\nL 4\nR 2\nL 2\nR 2\nU 4\nR 1\nU 3\nL 2\nU 2\nR 3\nL 3\nD 3\nR 4\nD 4\nR 3\nD 1\nL 4\nD 4\nU 3\nD 3\nL 4\nR 4\nL 2\nR 3\nL 4\nD 3\nL 3\nU 1\nR 2\nU 1\nL 3\nU 4\nD 1\nL 2\nU 2\nL 1\nD 1\nR 2\nL 4\nR 3\nU 4\nR 1\nD 1\nR 1\nU 3\nD 4\nR 1\nU 4\nR 2\nD 2\nU 2\nR 2\nU 4\nL 1\nD 3\nL 4\nD 2\nL 2\nD 2\nU 4\nL 4\nR 1\nD 1\nR 3\nD 3\nL 4\nR 3\nD 2\nR 1\nD 1\nR 1\nU 1\nD 1\nL 1\nD 1\nR 2\nL 3\nD 1\nR 1\nL 4\nD 4\nR 4\nL 3\nD 4\nU 2\nL 1\nD 3\nU 2\nD 1\nR 2\nD 2\nL 2\nU 1\nD 4\nR 3\nU 4\nD 1\nR 3\nU 1\nD 1\nU 1\nR 1\nD 1\nR 3\nU 4\nR 4\nU 2\nL 1\nU 1\nL 3\nR 5\nD 2\nU 1\nR 3\nD 3\nR 1\nD 1\nL 5\nR 1\nD 1\nR 2\nU 3\nR 2\nL 4\nU 3\nD 4\nU 1\nR 3\nD 1\nR 2\nL 3\nR 3\nU 2\nL 5\nU 3\nR 4\nD 4\nR 2\nD 3\nR 2\nU 4\nL 5\nR 2\nL 3\nU 4\nL 5\nR 3\nD 3\nU 4\nR 2\nU 5\nR 2\nU 5\nD 1\nL 2\nD 2\nU 3\nR 3\nL 3\nU 3\nD 5\nR 4\nD 4\nL 4\nD 5\nR 1\nU 4\nL 2\nR 1\nD 3\nL 2\nR 2\nL 3\nR 2\nL 2\nD 3\nU 5\nD 5\nL 3\nR 5\nU 3\nL 3\nD 5\nL 4\nD 1\nL 2\nD 4\nU 4\nD 1\nR 1\nL 4\nR 3\nU 2\nL 3\nU 4\nD 4\nR 2\nD 4\nU 2\nD 4\nU 2\nL 4\nD 5\nL 4\nR 1\nL 2\nR 3\nD 2\nR 5\nL 1\nU 5\nR 1\nD 5\nL 1\nD 2\nU 5\nD 2\nU 1\nD 6\nU 2\nL 5\nD 2\nR 1\nD 1\nR 1\nD 5\nU 1\nD 5\nR 4\nL 3\nR 4\nU 1\nD 5\nU 5\nL 4\nU 5\nL 4\nU 5\nR 2\nL 6\nD 4\nL 1\nU 1\nR 6\nD 4\nL 1\nD 2\nR 2\nU 5\nR 5\nU 4\nL 5\nR 4\nD 4\nU 4\nD 1\nR 2\nL 5\nU 3\nR 1\nL 3\nU 6\nR 5\nL 2\nD 4\nR 4\nU 5\nR 2\nU 6\nL 4\nU 6\nR 2\nL 6\nD 5\nR 6\nU 1\nL 3\nU 4\nR 2\nL 5\nD 4\nL 6\nU 1\nD 1\nR 2\nD 5\nL 1\nR 5\nL 3\nU 5\nD 4\nU 5\nD 4\nU 2\nR 4\nL 3\nD 4\nL 5\nR 6\nU 6\nD 5\nU 2\nR 4\nD 5\nL 5\nR 3\nL 3\nD 3\nU 6\nL 1\nD 3\nR 2\nL 3\nR 2\nL 6\nD 1\nL 6\nD 4\nL 6\nR 4\nD 5\nL 3\nU 6\nR 5\nU 4\nD 1\nL 5\nU 1\nD 2\nU 2\nD 5\nR 7\nL 3\nU 5\nR 4\nL 5\nU 1\nR 2\nD 6\nL 6\nD 2\nL 1\nD 6\nU 2\nD 2\nU 2\nD 6\nL 1\nR 6\nU 7\nD 2\nL 3\nR 3\nU 6\nD 3\nR 4\nU 3\nL 7\nD 3\nU 6\nD 2\nU 1\nR 4\nL 1\nR 3\nU 5\nD 6\nR 2\nL 4\nD 5\nR 6\nL 5\nD 1\nR 3\nD 2\nL 7\nU 1\nR 5\nD 6\nU 6\nD 4\nU 6\nD 2\nL 2\nU 7\nD 1\nR 3\nU 3\nR 1\nL 6\nD 7\nL 4\nR 1\nD 7\nL 2\nU 6\nD 2\nR 6\nD 7\nU 7\nR 5\nL 1\nU 3\nR 5\nD 4\nU 2\nD 2\nL 6\nD 2\nU 7\nR 4\nL 7\nU 4\nD 6\nU 2\nD 6\nL 3\nU 5\nR 5\nD 4\nR 1\nD 6\nU 6\nR 3\nL 2\nU 1\nR 7\nD 4\nL 2\nD 4\nL 5\nU 3\nR 6\nL 3\nD 7\nR 7\nL 1\nU 4\nL 6\nR 7\nU 3\nR 1\nD 8\nL 3\nD 3\nU 3\nR 2\nL 6\nU 4\nL 2\nD 7\nL 5\nD 1\nR 3\nU 2\nL 6\nR 2\nU 1\nD 6\nU 4\nD 1\nU 8\nD 7\nR 3\nL 3\nR 1\nD 1\nR 5\nL 5\nR 6\nU 4\nR 4\nU 3\nR 4\nL 6\nR 4\nU 6\nL 5\nU 8\nL 1\nD 1\nL 4\nU 8\nR 2\nU 7\nL 4\nU 7\nL 7\nD 5\nU 1\nR 7\nU 7\nD 6\nR 5\nU 5\nD 2\nR 8\nL 1\nD 3\nL 5\nD 5\nR 1\nU 1\nR 4\nD 3\nR 1\nU 4\nR 8\nD 3\nL 4\nR 7\nL 4\nR 7\nL 7\nR 1\nL 1\nD 2\nL 6\nR 7\nD 5\nU 7\nL 8\nU 1\nL 8\nU 2\nR 6\nL 4\nU 1\nR 1\nU 3\nL 6\nD 7\nL 3\nD 2\nR 4\nU 5\nD 1\nU 2\nD 6\nL 6\nU 4\nD 1\nL 2\nR 4\nU 2\nL 1\nR 5\nL 3\nR 5\nU 7\nR 4\nL 4\nR 2\nD 4\nL 1\nR 2\nD 1\nL 2\nR 2\nU 2\nR 5\nL 7\nU 3\nR 1\nD 5\nU 7\nD 8\nR 7\nD 5\nU 2\nL 5\nD 4\nL 2\nD 8\nU 7\nD 3\nU 4\nL 9\nD 2\nU 7\nR 7\nD 9\nU 3\nR 2\nL 7\nU 5\nL 9\nD 5\nR 6\nD 5\nR 1\nD 1\nR 5\nD 3\nU 2\nR 5\nD 8\nU 4\nR 9\nU 5\nL 2\nU 2\nR 5\nD 9\nU 2\nD 3\nR 4\nD 5\nU 7\nL 2\nU 8\nR 7\nL 1\nU 4\nD 6\nR 8\nU 3\nD 4\nU 1\nR 5\nU 8\nD 9\nU 5\nD 9\nR 7\nD 3\nL 1\nD 4\nR 8\nD 8\nU 5\nD 7\nL 5\nU 1\nD 6\nL 7\nD 1\nR 1\nU 2\nL 3\nR 4\nL 1\nD 7\nL 3\nU 9\nR 1\nU 9\nL 7\nR 1\nU 5\nL 4\nU 8\nD 9\nU 3\nD 9\nR 5\nU 8\nL 9\nU 9\nD 7\nR 7\nL 2\nU 9\nR 7\nU 10\nD 7\nL 1\nD 2\nR 4\nD 9\nL 4\nD 7\nR 9\nU 1\nD 4\nU 8\nL 9\nR 9\nD 3\nR 2\nD 7\nU 2\nD 7\nU 1\nR 3\nD 1\nU 10\nD 5\nU 6\nD 1\nR 5\nD 6\nU 1\nR 6\nU 2\nD 5\nU 8\nL 9\nD 1\nU 9\nD 6\nL 10\nR 4\nD 1\nL 6\nR 1\nL 3\nD 4\nR 9\nD 7\nR 4\nD 8\nR 8\nL 5\nU 7\nR 3\nU 5\nD 4\nL 5\nD 7\nL 10\nU 3\nD 9\nR 7\nU 4\nD 3\nR 7\nU 5\nD 10\nR 1\nL 7\nU 7\nR 5\nU 4\nL 7\nD 6\nR 1\nD 1\nR 4\nL 3\nR 4\nD 3\nL 7\nD 7\nL 6\nR 4\nL 1\nD 2\nU 4\nD 8\nL 8\nR 1\nU 4\nD 1\nU 2\nL 1\nR 4\nU 7\nD 9\nU 4\nD 10\nU 5\nR 3\nD 6\nU 7\nD 6\nL 7\nR 7\nL 7\nR 6\nD 4\nU 4\nL 7\nU 9\nL 6\nD 2\nL 2\nU 10\nL 4\nU 3\nD 3\nL 5\nU 10\nD 10\nR 8\nD 2\nL 6\nD 9\nR 7\nL 8\nR 3\nU 1\nD 4\nU 2\nR 4\nU 5\nD 8\nU 6\nL 4\nU 9\nR 5\nU 8\nL 11\nD 11\nU 6\nD 4\nU 3\nR 7\nL 8\nR 5\nD 5\nU 5\nR 1\nU 10\nL 4\nD 6\nU 11\nR 3\nD 10\nU 10\nL 7\nR 11\nU 9\nL 10\nU 10\nL 11\nD 2\nL 9\nD 3\nL 11\nD 2\nU 1\nD 5\nL 6\nU 3\nD 10\nR 8\nU 10\nR 9\nL 1\nD 6\nU 6\nD 6\nL 8\nR 3\nU 1\nD 2\nL 6\nR 10\nU 7\nD 2\nL 5\nD 10\nU 5\nD 9\nR 4\nL 9\nD 6\nU 9\nR 5\nL 9\nU 1\nD 2\nR 2\nL 5\nR 4\nD 8\nU 8\nD 11\nL 4\nD 3\nU 1\nL 9\nR 8\nL 10\nR 8\nL 10\nU 10\nD 5\nR 11\nU 2\nR 8\nU 1\nL 6\nU 5\nD 2\nU 2\nR 3\nL 4\nR 1\nD 6\nL 6\nU 5\nR 1\nL 6\nD 1\nR 5\nL 2\nR 11\nD 5\nL 6\nR 1\nD 11\nU 9\nD 12\nU 8\nR 7\nL 6\nR 9\nU 7\nD 2\nR 5\nD 12\nL 10\nD 7\nL 5\nU 7\nD 9\nR 7\nU 8\nD 3\nU 8\nD 5\nU 1\nL 10\nU 7\nL 2\nR 5\nL 2\nU 1\nL 8\nR 2\nD 8\nR 9\nD 9\nR 11\nD 4\nR 3\nU 2\nD 9\nR 12\nU 11\nD 8\nR 6\nD 9\nU 3\nL 5\nR 1\nU 4\nR 4\nU 10\nR 11\nU 5\nR 9\nU 1\nR 5\nD 3\nR 8\nU 3\nR 6\nU 4\nR 3\nD 2\nL 2\nU 7\nL 2\nU 12\nD 10\nL 7\nU 6\nR 7\nU 1\nL 11\nU 12\nR 12\nL 12\nD 10\nR 9\nU 12\nL 6\nU 2\nL 7\nU 11\nR 7\nU 6\nL 3\nU 3\nD 1\nU 11\nR 1\nD 4\nR 10\nL 10\nR 6\nD 11\nR 1\nD 5\nL 1\nR 1\nL 4\nR 7\nU 12\nR 6\nL 10\nD 12\nL 4\nR 8\nU 5\nL 7\nR 10\nD 13\nU 7\nD 1\nR 1\nD 9\nR 3\nD 1\nL 4\nD 3\nR 5\nU 9\nR 11\nL 4\nR 1\nD 11\nL 2\nR 4\nD 7\nR 3\nU 2\nD 6\nR 2\nL 13\nR 5\nL 4\nR 4\nU 13\nR 3\nU 4\nR 3\nD 1\nL 3\nU 6\nL 8\nU 5\nR 4\nD 10\nL 3\nU 10\nR 11\nD 11\nU 2\nD 2\nR 4\nU 2\nD 1\nR 10\nU 5\nL 9\nU 11\nR 10\nD 4\nL 1\nU 5\nR 4\nU 6\nR 9\nD 12\nL 10\nR 5\nL 5\nD 9\nU 2\nR 7\nU 2\nR 5\nL 8\nD 5\nR 1\nD 5\nR 8\nU 11\nR 7\nL 13\nU 10\nD 3\nU 1\nD 2\nL 1\nD 12\nL 9\nD 12\nR 5\nU 8\nR 6\nL 6\nR 5\nU 7\nL 10\nD 5\nU 11\nD 4\nU 8\nR 7\nU 8\nR 2\nL 10\nD 5\nL 10\nD 3\nU 13\nD 11\nU 3\nR 10\nU 9\nD 7\nU 6\nR 1\nU 9\nR 11\nD 7\nR 6\nU 9\nR 6\nU 12\nD 6\nR 13\nL 14\nU 10\nR 9\nL 13\nR 7\nD 9\nU 12\nL 8\nD 5\nR 3\nU 5\nR 10\nL 4\nD 5\nR 4\nD 2\nL 12\nR 8\nD 9\nR 5\nD 7\nU 5\nD 11\nL 13\nD 12\nR 2\nU 5\nL 1\nU 2\nL 6\nU 9\nD 10\nL 3\nD 8\nR 12\nU 9\nR 9\nU 9\nR 8\nU 6\nL 10\nU 5\nR 9\nD 2\nU 12\nD 3\nR 12\nL 10\nD 5\nL 7\nD 9\nL 5\nR 7\nD 8\nU 10\nL 11\nR 9\nL 13\nR 8\nU 9\nD 8\nU 9\nR 10\nL 4\nU 1\nD 10\nL 1\nD 4\nL 1\nU 13\nR 8\nL 3\nD 5\nR 5\nU 4\nR 5\nU 6\nL 14\nR 5\nU 8\nL 7\nD 7\nR 4\nU 4\nL 14\nU 7\nR 14\nD 3\nU 10\nL 7\nR 5\nL 5\nR 15\nL 3\nD 15\nL 6\nR 3\nL 10\nU 7\nL 2\nR 7\nL 1\nR 8\nU 12\nD 6\nU 11\nD 12\nU 9\nR 2\nD 14\nL 4\nU 9\nD 15\nR 8\nD 9\nL 9\nD 14\nU 5\nR 14\nD 15\nR 6\nL 3\nU 13\nR 13\nU 5\nD 14\nL 2\nD 12\nU 6\nR 5\nU 5\nL 6\nR 5\nD 12\nU 9\nL 7\nD 15\nR 5\nU 7\nD 1\nR 2\nL 11\nU 8\nR 3\nD 3\nU 6\nD 15\nU 2\nD 11\nR 6\nU 9\nD 7\nU 3\nL 13\nU 3\nL 15\nR 10\nL 6\nD 15\nL 3\nU 5\nR 13\nD 13\nL 5\nR 8\nD 2\nR 8\nL 9\nR 7\nL 5\nU 3\nD 11\nR 13\nU 15\nD 5\nR 4\nU 11\nR 14\nL 1\nU 6\nL 13\nR 14\nL 12\nD 5\nR 3\nD 4\nR 5\nU 8\nR 11\nD 8\nU 5\nR 9\nD 8\nR 4\nU 5\nL 7\nD 6\nU 4\nL 2\nD 15\nU 5\nR 16\nL 4\nR 1\nD 12\nL 2\nR 6\nD 6\nR 14\nU 6\nL 14\nR 12\nD 8\nR 1\nU 2\nR 3\nD 7\nR 4\nL 2\nU 6\nL 1\nU 7\nL 12\nR 11\nD 13\nU 16\nR 15\nD 14\nU 13\nD 15\nR 11\nU 12\nR 5\nD 8\nR 2\nL 14\nR 10\nL 16\nU 10\nD 13\nL 11\nU 10\nR 5\nD 7\nL 15\nD 12\nL 4\nR 16\nU 8\nR 8\nL 7\nU 9\nR 7\nU 16\nR 2\nU 5\nR 5\nD 4\nL 7\nD 5\nL 4\nU 13\nD 9\nR 2\nD 2\nU 5\nL 3\nD 16\nR 13\nL 10\nR 7\nL 9\nR 3\nL 5\nD 10\nL 10\nR 2\nL 13\nR 5\nL 11\nR 16\nU 12\nR 11\nD 12\nR 7\nL 2\nU 12\nD 11\nU 12\nL 1\nR 6\nU 3\nL 3\nD 1\nR 5\nU 6\nL 15\nR 8\nD 5\nL 16\nD 13\nR 15\nD 14\nR 4\nU 15\nR 5\nL 8\nD 12\nU 11\nD 13\nL 9\nD 13\nL 7\nU 9\nR 15\nU 13\nD 16\nR 6\nL 8\nR 7\nU 4\nR 10\nU 7\nD 10\nU 8\nD 7\nL 17\nR 7\nL 8\nD 5\nL 5\nU 15\nD 6\nL 1\nD 2\nR 17\nL 6\nD 15\nR 4\nL 2\nR 10\nD 4\nR 6\nD 11\nU 1\nL 17\nD 11\nU 6\nD 17\nR 15\nL 13\nD 9\nR 12\nU 13\nR 2\nL 12\nD 1\nR 17\nU 13\nD 15\nU 7\nR 8\nD 6\nR 8\nD 16\nU 16\nD 2\nU 1\nR 7\nU 15\nL 7\nR 12\nU 10\nD 4\nR 17\nD 11\nR 12\nD 8\nL 3\nR 3\nD 5\nR 17\nL 15\nU 11\nR 1\nL 4\nR 6\nD 6\nU 2\nL 1\nD 2\nU 1\nD 11\nL 1\nR 11\nD 7\nU 16\nL 14\nU 5\nR 13\nD 9\nU 17\nD 3\nR 12\nU 16\nR 10\nL 17\nD 5\nU 16\nD 12\nL 2\nR 16\nL 17\nU 4\nR 15\nD 4\nL 11\nU 3\nR 17\nD 8\nR 7\nD 15\nR 14\nD 6\nR 5\nL 1\nD 5\nL 11\nD 5\nU 13\nL 13\nU 8\nD 8\nL 13\nD 1\nL 10\nU 14\nR 7\nL 11\nD 4\nR 16\nD 3\nL 9\nR 3\nD 4\nL 4\nU 2\nR 13\nD 18\nU 15\nD 7\nL 18\nU 3\nD 17\nU 13\nR 9\nL 8\nR 9\nU 16\nR 14\nD 17\nL 16\nR 3\nU 4\nD 1\nR 8\nD 14\nL 14\nU 17\nL 16\nR 15\nD 9\nU 13\nR 14\nU 9\nD 13\nR 2\nL 3\nD 8\nU 5\nR 1\nL 5\nR 4\nU 8\nL 7\nU 5\nR 11\nD 17\nL 4\nU 3\nR 1\nU 18\nD 1\nU 6\nR 2\nD 16\nU 15\nD 8\nL 13\nR 2\nU 16\nL 9\nD 1\nU 9\nR 5\nL 16\nD 2\nL 2\nR 8\nD 17\nR 13\nU 5\nR 6\nU 2\nL 12\nR 2\nD 14\nR 14\nL 11\nR 1\nL 9\nD 18\nL 4\nU 4\nD 15\nL 14\nR 15\nU 6\nD 3\nR 15\nL 12\nU 3\nD 12\nU 13\nL 6\nU 17\nD 6\nL 10\nR 16\nL 7\nU 2\nD 1\nR 10\nU 11\nR 1\nU 19\nD 5\nL 11\nD 14\nL 17\nU 16\nL 13\nR 4\nL 16\nD 10\nL 4\nD 9\nR 19\nL 19\nR 12\nL 12\nD 8\nL 6\nD 14\nU 7\nD 13\nU 18\nD 13\nU 6\nD 6\nR 18\nU 18\nD 15\nL 14\nR 7\nD 11\nR 4\nL 14\nR 5\nD 4\nL 12\nD 16\nR 17\nU 15\nL 9\nU 10\nR 7\nD 10\nL 3\nU 15\nL 1\nD 15\nU 1\nD 12\nL 4\nD 10\nU 6\nR 6\nL 11\nD 5\nR 1\nD 11\nU 16\nL 16\nR 19\nU 8\nR 11\nD 17\nL 19\nR 6\nU 7\nR 2\nD 9\nR 11\nD 17\nU 17\nD 4\nR 8\nD 6\nU 10\nR 3\nL 17\nD 5\nR 19\nU 1\nL 5\nU 5\nD 6\nU 6\nL 18\nR 18\nL 4\nR 12\nD 14\nL 17\nR 8\nL 13\nR 6\nL 14\nD 3\nR 13\nL 16\nD 14\nU 6\nD 6\n")

(defn parse-instrs [input]
  (->> (str/split-lines input)
       (mapcat #(let [[dir n] (str/split % #" ")]
                  (repeat (parse-long n) dir)))))

(def instrs (parse-instrs input))

(defrecord State [head tail instrs])

(def init-state (->State [0 0] [0 0] instrs))

(def dir->dir-vec {"U" [-1 0]
                   "D" [1 0]
                   "L" [0 -1]
                   "R" [0 1]})

(defn in-range? [[r c] [r' c']]
  (<= (max (abs (- c' c))
           (abs (- r' r)))
      1))

(defn chase-dir [[hr hc] [tr tc]]
  (let [dr (- hr tr)
        dc (- hc tc)
        bound #(max -1 (min 1 %))]
    (mapv bound [dr dc])))

(defn move [coord ds]
  (mapv + coord ds))

(defn chase [chaser chased]
  (if (in-range? chaser chased)
    chaser
    (move chaser (chase-dir chased chaser))))

(defn step [{:as state :keys [head tail instrs]}]
  (let [[instr & instrs'] instrs]
    (if instr
      (let [dir-vec (dir->dir-vec instr)
            head' (move head dir-vec)
            tail' (chase tail head')]
        (->State head' tail' instrs'))
      state)))

(->> (iterate step init-state)
     (take-while (comp seq :instrs))
     (map :tail)
     set
     count)
; 5907

(defrecord State' [knots instrs])

(defn step' [{:as state :keys [knots instrs]}]
  (let [[instr & instrs'] instrs]
    (if instr
      (let [[head & tail] knots
            head' (move head (dir->dir-vec instr))
            knots' (loop [acc [head']
                          [knot & more] tail]
                     (if-not knot
                       acc
                       (let [knot' (chase knot (last acc))]
                         (recur (conj acc knot') more))))]
        (->State' knots' instrs'))
      state)))

(def init-state'
  (->State' (repeat 10 [0 0]) instrs))

(->> (iterate step' init-state')
     (take-while (comp seq :instrs))
     (map (comp last :knots))
     set
     count)
; 2303