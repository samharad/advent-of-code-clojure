(ns aoc.2022.day.02
  (:require
    [clojure.set :as set]
    [clojure.string :as str]))

(def input "B Z\nC Z\nB X\nA Y\nB X\nB X\nA X\nB Z\nC Z\nB Y\nA Z\nC X\nB X\nC X\nB Z\nB Z\nC Y\nB Z\nB Z\nC Z\nB Z\nB Y\nB X\nB Y\nC Z\nC Y\nC Z\nA X\nC Z\nB X\nC Z\nB Y\nB X\nA Y\nA X\nA Y\nB Y\nB X\nB X\nA Z\nB Z\nB Y\nC Z\nB X\nC Y\nB Z\nB Y\nC Y\nA X\nA Y\nC Y\nC Z\nB Z\nB X\nC Z\nA X\nB X\nA Y\nB Z\nC Y\nA Y\nC Z\nC Z\nA X\nB X\nC Z\nA Z\nA Z\nB X\nB X\nB X\nA Y\nB X\nB X\nC Y\nB X\nC Z\nC Y\nB Z\nA X\nB X\nB X\nA X\nC Y\nC Y\nA X\nA X\nB Z\nB X\nC Z\nB X\nB Z\nA Z\nB Z\nA X\nA X\nB Z\nA X\nB X\nB X\nB X\nA Y\nA Y\nA Y\nB X\nC Y\nB Z\nA Y\nB X\nA Z\nC X\nA Z\nB Y\nB Z\nC Z\nB Z\nA Y\nB X\nB Z\nB Z\nB Z\nC Y\nB X\nA Y\nB Z\nB Y\nB Z\nB X\nA X\nA X\nB Y\nB X\nC Y\nA Y\nA Z\nB Z\nB Z\nB Y\nB X\nB Z\nB X\nB Z\nB Z\nB X\nB Z\nB Z\nB Z\nB X\nA Y\nB X\nB Z\nA X\nB Z\nB Z\nB X\nC Y\nA Z\nB Z\nC Z\nB X\nA Z\nB X\nA Z\nC Y\nC Y\nA Y\nA Y\nB Z\nA Y\nA Y\nC Z\nA X\nB X\nB X\nC Y\nA Z\nB Y\nC Y\nB Z\nB Y\nB Z\nA X\nB Z\nC Z\nB X\nB Y\nA X\nC Z\nB Y\nB Z\nB Z\nA Z\nB X\nA Y\nC Y\nC Y\nB Z\nB Z\nB X\nB Z\nB Z\nB Y\nB Z\nB Z\nB Z\nB X\nB X\nB Z\nB Y\nB Z\nC Y\nA Z\nA Y\nB X\nA X\nB Z\nB Z\nA Z\nB Z\nB Z\nB X\nA Y\nC Z\nC Y\nB Z\nB Z\nC Z\nA X\nB Z\nB Z\nB X\nA Y\nA Z\nB Z\nC Y\nC Z\nA Y\nB Z\nB X\nC Z\nA X\nC Z\nB Z\nC Z\nB X\nC Y\nB X\nB Z\nB X\nA Y\nA Z\nB Z\nB X\nB Z\nC Z\nC Z\nC Y\nB X\nB Y\nB Z\nC Z\nC Z\nB Z\nB X\nB Z\nC Y\nA Y\nC Y\nB X\nC Y\nB Y\nC X\nB X\nA Y\nC Z\nB X\nB Z\nB Y\nB X\nB X\nA Z\nB Z\nC Z\nB X\nB X\nA X\nB X\nB X\nC X\nC Y\nA X\nB X\nB Z\nB X\nA Y\nB X\nB Y\nB X\nB X\nB Y\nB X\nA Z\nB X\nB Z\nB X\nC Z\nA Z\nC X\nB Z\nA Y\nB X\nB X\nA Z\nA Y\nB Z\nB Z\nA Y\nC Z\nB X\nB X\nC Z\nB Z\nB Z\nB Z\nA Y\nA Y\nA X\nB X\nC X\nB X\nB Z\nB X\nA X\nB X\nB Z\nB X\nA Y\nB X\nC Y\nB X\nB Z\nB Z\nC Z\nC Z\nC Y\nB Z\nB X\nB Z\nA Y\nC Y\nB X\nC Y\nC Z\nA X\nB Z\nA X\nB X\nC Y\nA X\nA Y\nB X\nA Y\nA Z\nC X\nB X\nB Y\nB Z\nB X\nA Y\nB Z\nB X\nA X\nB X\nB X\nB Y\nB Z\nB Z\nB X\nB X\nA X\nB Z\nB Z\nB Z\nA Y\nC Z\nA X\nA Y\nB Z\nB X\nC Y\nA X\nB X\nB Z\nB Y\nA Y\nB Z\nB Z\nB X\nA Y\nA Y\nA Y\nC Y\nB X\nB X\nB Z\nB Z\nA Z\nA Y\nB Z\nB X\nB X\nB X\nA X\nB X\nC Y\nB Z\nA Y\nB X\nA Y\nA X\nC Y\nB Z\nC Z\nB Z\nB X\nB Z\nA Y\nB Z\nC Z\nB Z\nB Z\nA X\nB Z\nB Z\nB X\nB Z\nB Z\nB Z\nC X\nB X\nB X\nA Y\nA Y\nB X\nB X\nC Z\nB X\nB X\nC Y\nC Y\nC Z\nB X\nB Z\nB Z\nB Z\nA Y\nA Y\nA X\nC Z\nA Z\nA X\nB Z\nC Z\nA X\nB X\nB Y\nC Z\nB Y\nB Z\nC Y\nC Y\nC Y\nB Z\nB Z\nB X\nB Z\nB Y\nB X\nB Z\nA X\nB X\nA Y\nA X\nB Z\nA Y\nC Z\nC X\nB X\nB X\nC Y\nB Z\nA Y\nB Z\nB X\nC X\nB Z\nA X\nA Y\nA Y\nC Y\nC Z\nC Z\nB X\nA X\nB X\nB X\nB Z\nC Y\nB X\nC Z\nC Y\nB X\nB Z\nB X\nB X\nC Z\nA Y\nB Z\nB X\nA X\nB Z\nB Z\nB X\nB X\nC Z\nC Z\nB Y\nC Z\nB Z\nC Y\nB X\nB X\nC Y\nB X\nB X\nA Z\nB X\nB X\nA Y\nB X\nB X\nB X\nB Z\nB Z\nC Z\nA Y\nB X\nB Z\nB X\nB Z\nB Z\nB X\nB Y\nC Z\nA Z\nA Y\nC Y\nB X\nA X\nB Y\nA X\nB Y\nA Y\nC X\nB X\nA Y\nB Z\nB Z\nB X\nB Z\nB Z\nB X\nB X\nA Y\nB Z\nA Y\nB Y\nB Y\nB Y\nB X\nB Z\nB Z\nA Y\nA X\nC Y\nB X\nB X\nA Y\nB X\nA Y\nB Z\nB Z\nB Y\nB Z\nC Z\nC Z\nC Y\nB Z\nC Y\nA X\nB X\nC Y\nC Z\nB X\nC Y\nA Y\nB Z\nB Z\nA X\nC Y\nB X\nA Y\nC Z\nB Z\nB X\nA Y\nC Z\nA X\nA Y\nC X\nC Y\nA Y\nB Z\nB X\nA Z\nB Z\nB Z\nB X\nC Z\nB X\nB Z\nB X\nB X\nB Z\nB X\nA X\nA Y\nB Y\nA Y\nA Y\nB Z\nC Z\nB X\nB Z\nA Y\nA Y\nA Z\nB X\nA Y\nA Y\nB X\nA X\nB Y\nB Z\nB Y\nA Z\nC Y\nB Z\nA X\nA Z\nC Z\nB X\nB Z\nB X\nC Y\nA X\nB Z\nA Y\nB X\nB Z\nB X\nC Y\nB Z\nB X\nA Y\nB X\nC Y\nC X\nA X\nB Z\nB X\nA X\nA Z\nB X\nB Y\nA Z\nB Z\nC Y\nA Y\nC X\nB Z\nA Y\nC Y\nC Z\nB Y\nC Y\nA Z\nB Z\nB Z\nA Y\nB X\nC X\nA Y\nA Y\nA Z\nB X\nB X\nB Y\nA X\nB X\nB Z\nB Z\nB X\nB Y\nC Z\nC Y\nA Y\nA Y\nC X\nC Z\nC Y\nC Y\nA Y\nA Y\nB Z\nC Y\nC Y\nA Y\nA Y\nC Z\nB Z\nA X\nB Y\nB Z\nB Z\nB Z\nB Z\nB X\nA Y\nA Y\nB Z\nA Y\nC X\nA X\nC Z\nB Z\nB Z\nA X\nC Y\nB Z\nB X\nB X\nB X\nB Z\nC Y\nB Z\nB X\nB X\nB Z\nC Y\nB X\nA Z\nB X\nB Z\nA X\nC X\nA X\nB Z\nB Z\nB Z\nB Z\nA Y\nB Z\nB Y\nB Z\nB Z\nB X\nB Z\nB Z\nA Y\nA X\nB X\nA Z\nB Z\nA Z\nB X\nB X\nB Z\nA Y\nB Y\nA X\nB X\nB X\nA Y\nB X\nB X\nB Z\nB Y\nB Z\nC Z\nB X\nC Y\nB Z\nB Z\nC Y\nB Z\nB Z\nB Z\nB Z\nB Z\nC Y\nA Y\nA X\nB Z\nC Y\nB Y\nC Z\nB Z\nB Z\nC Z\nB Z\nB Z\nB X\nA Y\nB Z\nB Z\nC Y\nA Y\nB Z\nA Y\nB Z\nC Z\nC Y\nA Y\nA Z\nB X\nA X\nB Z\nB Y\nA X\nA X\nA Y\nB X\nC Y\nB X\nB Z\nA Y\nB X\nB X\nA Y\nA Z\nC Z\nC Y\nA Y\nB X\nB Z\nB X\nC Z\nA Y\nB X\nA Y\nB Z\nB X\nC Y\nA Y\nB Z\nC Y\nB Y\nB X\nC Y\nA Y\nB Z\nC Y\nB X\nA X\nB Z\nB Z\nC Y\nA Y\nB Z\nC Y\nB X\nA X\nA Z\nC X\nB Z\nB Z\nC Y\nB Y\nC Z\nC Y\nA X\nA Y\nA X\nA Y\nC Z\nC Y\nC Z\nC Z\nC Y\nA X\nC Z\nB X\nC X\nB X\nA Z\nB X\nC Z\nA Y\nA X\nA Z\nC Z\nB X\nC Y\nA Y\nC Y\nC Z\nC Y\nC Y\nC X\nB Z\nB X\nB Y\nA X\nB Z\nB Y\nC Y\nC Y\nC Z\nA Z\nA X\nA Y\nC Z\nB Z\nB X\nB Z\nB Z\nB X\nB Z\nC Y\nA X\nB X\nA Z\nB X\nC Y\nB Z\nB X\nB Z\nC Z\nC Z\nA X\nB Z\nB X\nA Y\nB Z\nA Y\nB Y\nC Z\nC Y\nA X\nA Y\nC Y\nC Z\nB X\nC Y\nB Z\nB Z\nB Z\nC Z\nB X\nC Y\nB Z\nC Z\nB X\nA Y\nA X\nB Z\nB Z\nC Y\nB X\nB Z\nC Y\nA Y\nC Y\nA Y\nB X\nC Z\nA X\nA Y\nC Y\nC Z\nB Z\nB Z\nB Z\nA Y\nA Y\nC Z\nA Z\nB X\nA X\nB Z\nC Z\nB X\nC Y\nB Z\nB X\nB Z\nB Z\nB X\nC Z\nB X\nB Z\nB X\nA X\nB X\nA X\nB Z\nB Z\nA Y\nB X\nB Z\nB Z\nC Z\nC Y\nB X\nB X\nB Y\nC Z\nC Y\nA X\nB Z\nC Y\nA Y\nB X\nB X\nA X\nA Y\nC X\nB Z\nB Z\nA Y\nA X\nC Y\nB Z\nB Z\nC X\nC Y\nA Y\nB Z\nC Y\nB Z\nB X\nB Z\nC Y\nB X\nB Z\nB X\nB X\nB X\nB Z\nB Z\nC Y\nB X\nB Z\nA Z\nA Y\nA Z\nA Y\nB X\nC Z\nA Y\nB X\nB X\nC Z\nB Z\nA X\nB X\nC Y\nA Y\nA X\nB Z\nA X\nA Y\nB Z\nB X\nB Z\nC Y\nA X\nA X\nB X\nB Y\nC X\nA X\nB X\nB X\nA Y\nC Y\nB Z\nB Z\nC Z\nB X\nB Z\nB X\nB Y\nB Z\nB Z\nB X\nB Z\nA X\nB X\nA Y\nA Z\nB Z\nB X\nA Y\nA X\nB Z\nB X\nC Z\nA Y\nA Y\nC Z\nB X\nA X\nC Y\nB X\nB Z\nB X\nB Z\nB X\nC Y\nB Z\nC Y\nC Z\nA Z\nC Z\nA X\nC Z\nB Y\nB X\nB Z\nC Z\nA Y\nA Z\nA X\nB Z\nA X\nB Z\nB X\nA Z\nC Z\nC Y\nB Z\nC X\nA X\nA X\nB X\nA Y\nA X\nB Z\nB Z\nB Z\nB Z\nA X\nA X\nA Y\nB Z\nB Y\nB Z\nA Y\nB Y\nA X\nA Z\nB Z\nA Y\nB X\nA X\nA X\nB Z\nA Z\nB Y\nB Z\nC Z\nC Y\nB X\nB Y\nA X\nA Z\nB Z\nA Z\nB Z\nA X\nB Z\nA Z\nB Y\nA Z\nC Y\nB Z\nC Z\nB Z\nB X\nB Z\nC X\nA X\nB X\nC Z\nB X\nB Y\nA X\nB X\nB X\nA Z\nB X\nB Z\nC Z\nB X\nB X\nB X\nA Y\nA Z\nC Y\nA Y\nB X\nA Z\nA Z\nB Y\nB Y\nC Z\nC Z\nB Z\nC Z\nB Z\nA Y\nA X\nC Y\nB X\nB Z\nB Z\nB Z\nB X\nB Z\nB Z\nA X\nA Y\nB X\nB X\nB X\nC X\nC Y\nC X\nB X\nB Z\nB Y\nC X\nA Y\nA Y\nB Z\nC Y\nC Z\nC Z\nC Z\nA Y\nB Y\nB Z\nB X\nB Z\nB Y\nA X\nC Y\nC Z\nA Y\nB Z\nA X\nA X\nA X\nB Z\nB X\nC Y\nB Y\nC Z\nB Z\nB Z\nC Y\nB Z\nC Z\nB X\nB Y\nA Y\nC Z\nA Y\nB Z\nB Z\nB X\nB X\nB Z\nB Z\nB X\nB X\nC Z\nB Y\nB Z\nB X\nC Y\nC Z\nA Y\nC Z\nB Z\nC Y\nB X\nC Z\nA X\nB Z\nB X\nC X\nC Z\nB Z\nC Z\nA X\nB Z\nC X\nB Z\nC Z\nA Y\nB Z\nB Z\nC Z\nB Y\nB Z\nB X\nB X\nA X\nA Y\nA Y\nC Y\nC Y\nC Y\nB Z\nB Z\nA Y\nB X\nA Z\nC Y\nC Z\nB X\nA Y\nA Y\nC Z\nC Z\nC Y\nA Z\nB Z\nB Z\nB Z\nA Y\nA Y\nC Z\nB Z\nB X\nC Z\nB Z\nC Y\nA Z\nB X\nB Z\nA Z\nB X\nA X\nB X\nA X\nB X\nB Z\nB Z\nB Z\nC Y\nC Z\nA Y\nB X\nA X\nC Z\nC Y\nC Z\nB Z\nB X\nA Y\nA X\nC Z\nB X\nC Z\nC Y\nA X\nB X\nC Z\nB X\nB Z\nC Y\nB X\nA X\nA Y\nA X\nB Z\nB Z\nC Z\nB X\nA Y\nB X\nB X\nA Z\nB Y\nB Z\nB X\nB Z\nB X\nB Y\nB X\nB X\nA Y\nA Y\nA X\nC Y\nA Y\nB X\nC Y\nB Z\nB Z\nA Y\nB X\nC Y\nC Z\nC Y\nB Z\nC Z\nC Y\nA Y\nA Y\nB Z\nB X\nA X\nA Y\nB X\nB Z\nB X\nC Z\nC Z\nA Y\nB X\nB Z\nB Y\nC X\nC Y\nB Z\nA X\nB Z\nA Y\nA X\nA Y\nB X\nB Z\nB Z\nB X\nB Z\nC Z\nB Z\nA Y\nB Z\nC Z\nB X\nB X\nB X\nB X\nB X\nB X\nB Y\nB Z\nB X\nB Z\nA Z\nB Z\nC Y\nA X\nB Z\nB Z\nC Z\nB X\nA Z\nC Y\nB Z\nB X\nA X\nA Y\nC Y\nB Y\nA X\nB Y\nB X\nB Z\nC Y\nB Z\nC Y\nA Z\nB Z\nC Y\nC Z\nA Y\nC X\nC Y\nB Z\nB X\nB Z\nB X\nB X\nA Y\nB Y\nB X\nB X\nC Y\nB X\nC X\nB Y\nA Y\nC Y\nB X\nB X\nA X\nB X\nA X\nA X\nB X\nB X\nA Z\nC Z\nC Y\nB X\nB X\nC Z\nB X\nC Y\nC Z\nA Y\nB Z\nC Y\nB X\nB Y\nB X\nB X\nC X\nA X\nB X\nB Z\nB Z\nC Y\nC Y\nB Y\nA Y\nB Z\nB X\nB X\nA Z\nB Z\nB X\nB X\nA Y\nB X\nB X\nB X\nA X\nB X\nB X\nB X\nB Z\nB X\nA Z\nB Y\nB X\nB Z\nB Z\nB Z\nA X\nB Z\nB Z\nB X\nB Z\nC Z\nC Y\nA Z\nC X\nC Y\nA Y\nB X\nB Z\nC Z\nB X\nC Z\nB Z\nA Z\nA Y\nB Y\nB Z\nB X\nA X\nB Z\nC Z\nC Y\nB Z\nA X\nA Y\nA Z\nB Z\nC Y\nA Y\nB X\nC Z\nA Y\nB X\nB Z\nB X\nC Y\nB X\nB X\nB X\nA Z\nB Z\nC Z\nB Z\nC Y\nB Z\nC Z\nB Z\nB X\nC Y\nC Z\nA X\nC Z\nC Y\nC Y\nB X\nA Y\nA Z\nB X\nB Z\nB Z\nB Z\nA X\nA Z\nB Z\nA Z\nA Y\nC Z\nB Y\nB Z\nB X\nB X\nC Z\nB Z\nB Z\nB Z\nB Z\nB X\nB X\nA X\nA X\nA Z\nB Z\nB X\nB Z\nB Z\nC X\nA Y\nB Y\nB X\nB X\nB Z\nB X\nB X\nB X\nC Y\nB Z\nB X\nC Y\nB Z\nA Y\nB Y\nB Z\nA Y\nA X\nB X\nB X\nB Z\nA X\nB Z\nA Y\nB Z\nB X\nA X\nA X\nA X\nA Y\nB Z\nA Y\nA X\nB X\nB Z\nA Y\nB Z\nB X\nB X\nA Z\nB Z\nB Z\nB Z\nB Z\nA X\nB Z\nB Z\nB X\nB Z\nC Y\nB Z\nB X\nB Z\nB X\nC Y\nB X\nB Y\nB Z\nB X\nA X\nC Y\nB Z\nB Z\nB X\nA Y\nB X\nB Z\nC X\nC Y\nA Y\nB X\nB X\nA Y\nB Z\nC Y\nB Z\nA Y\nC Y\nB Z\nA X\nA X\nA Y\nC Y\nC Z\nB Z\nC Z\nB X\nA X\nB X\nA Y\nA Y\nC Z\nC Y\nA Z\nB Z\nA Y\nB X\nB X\nB Z\nC Z\nB X\nB X\nB Y\nC Y\nC Z\nA Y\nA Z\nA X\nA Y\nA Y\nA Z\nB Z\nB Z\nC Z\nB Z\nB X\nC Y\nA Y\nB X\nC Z\nA X\nB Z\nB Y\nA Y\nB X\nB X\nA X\nC Z\nC Z\nC Y\nC Y\nA X\nB X\nB X\nB Y\nA Z\nC Z\nA Y\nC Z\nC Y\nB X\nC Y\nB Z\nA Z\nB Y\nB X\nC Y\nB Y\nB Z\nA Z\nA X\nB X\nC Z\nC Z\nB Z\nB Z\nC Z\nB X\nB X\nC Y\nA Y\nC Y\nC Z\nB Z\nB X\nA Y\nB Y\nB Z\nC X\nB X\nB Z\nB Z\nB X\nB Z\nB Z\nC Y\nA Y\nB Z\nB X\nA Y\nA Y\nB Z\nB Y\nC Y\nB Z\nB Y\nB Z\nA Y\nB X\nC Z\nA X\nB X\nC Y\nB Z\nB Y\nB Y\nB Z\nA X\nA X\nB Z\nB X\nA X\nB Z\nB Z\nA Y\nB X\nA X\nB X\nB X\nA Y\nC Z\nC Y\nB Z\nB X\nB X\nB X\nA X\nB X\nB Z\nB Z\nB Z\nB X\nB Z\nB Z\nA Z\nC Y\nB X\nB X\nB X\nA X\nC Z\nA Y\nA Y\nB Z\nB X\nC Z\nB Y\nC Z\nB X\nA Y\nC X\nB Z\nB X\nB X\nC Y\nB X\nB X\nB Z\nA X\nB X\nA Z\nB X\nB X\nB Z\nB X\nB X\nB Z\nA Y\nB X\nB Z\nB X\nB X\nC Y\nB X\nB Z\nB Z\nB X\nB Z\nC Z\nB Y\nA Y\nB Z\nB X\nB X\nA X\nB Z\nB X\nB X\nB Z\nC Z\nB Z\nB X\nC Y\nB Z\nB Y\nB Z\nB Z\nA Z\nB X\nB X\nB X\nB X\nC Y\nA Y\nB X\nB Z\nB X\nB X\nC Z\nC X\nB Z\nB Z\nB X\nB Z\nA Y\nA Z\nC Z\nA X\nA X\nA Y\nA Y\nA Y\nC Z\nB Z\nA Y\nC Z\nB X\nA Z\nC Y\nA X\nA X\nA Y\nB Z\nA X\nB X\nB X\nC Z\nB Z\nC Z\nB Z\nA X\nC Y\nC Z\nA X\nA Z\nB X\nC Y\nA X\nB X\nB Z\nB X\nA Y\nA Z\nC Z\nB Z\nA X\nB X\nC Y\nB X\nB X\nC Y\nB X\nA Z\nA X\nC X\nB Z\nB Y\nC Z\nC Z\nA Y\nA Y\nA Y\nB X\nA Z\nB X\nA X\nC Y\nB X\nA X\nB X\nB X\nB Z\nB Z\nB Y\nA Y\nC Z\nC Z\nB X\nB Z\nC Z\nB X\nC Z\nB Z\nA Y\nA X\nB X\nC Z\nC Y\nA Y\nB Z\nC Y\nB X\nC X\nA X\nB Z\nC Z\nB Z\nB X\nA Y\nB Z\nA Y\nA Y\nA Y\nC Y\nC Y\nC Z\nA Y\nC Y\nC Y\nB Z\nB X\nC Y\nC Y\nB X\nB Z\nB X\nC Z\nC Y\nC Y\nB X\nB X\nA Y\nB Z\nB Z\nB X\nB X\nC Z\nA X\nB Y\nB X\nA Z\nB Z\nB X\nB Z\nC Y\nB Z\nC Y\nB Z\nA Z\nC Y\nA Z\nC Z\nB X\nB X\nC Y\nB X\nC Y\nA Z\nB X\nB Z\nB Z\nB X\nB Z\nB Y\nA Y\nB Z\nB X\nB Z\nA Y\nA Y\nC Z\nA Y\nC X\nB Z\nA Y\nA Y\nB X\nB Z\nA Y\nC Z\nB Z\nB Z\nC Y\nA X\nB Z\nC Z\nB X\nB X\nB Z\nA Y\nB Y\nC Z\nA Y\nB X\nC Z\nB X\nB Z\nB Z\nB Z\nB X\nB Z\nB X\nA Z\nB X\nB X\nB X\nB Z\nA Z\nA Y\nB Z\nB X\nB X\nC X\nA Y\nA X\nC Y\nA Z\nA X\nC Y\nA Y\nC Y\nA Z\nB X\nB Z\nB Z\nC Y\nA Y\nB X\nB X\nA Y\nC Z\nB Z\nB Z\nA X\nA Y\nB Z\nA Z\nA Y\nC Z\nB X\nB Z\nB Z\nC Y\nA Y\nC Z\nB Z\nA Y\nC Y\nC Y\nB Z\nB Z\nB Z\nA Y\nB Y\nA Y\nB Z\nC Z\nA Y\nB X\nC Y\nA Z\nA X\nB X\nA Z\nB Z\nA Z\nC Z\nB Z\nA Y\nA X\nA X\nC Y\nA Y\nB Z\nA Y\nB Z\nB X\nA Y\nA X\nA Y\nA X\nC Z\nA Y\nB X\nC Z\nB X\nB Z\nB Z\nC Y\nB Z\nB X\nA X\nB Y\nA Y\nB X\nC Z\nA Y\nB Z\nB X\nA Y\nC Z\nC Y\nB Z\nB Z\nB Z\nA X\nB X\nA Y\nB Z\nC Y\nA X\nA Z\nB Y\nB X\nC Y\nB Z\nC Y\nA Z\nB X\nA Y\nA Y\nC Y\nA Z\nB X\nA Z\nB X\nB X\nA X\nB X\nB Z\nB X\nB Y\nB Z\nB Y\nB X\nA Y\nA X\nC Z\nB Y\nC Y\nB X\nC Y\nB X\nB Z\nB X\nB X\nB Z\nB Z\nB Z\nC Z\nB Z\nB Z\nA X\nA Y\nB X\nB X\nB X\nC Z\nB X\nB Z\nC Y\nA X\nA Z\nB X\nC Z\nA X\nA X\nB Z\nA Y\nB Z\nA X\nC X\nC Z\nB X\nB X\nC Y\nA X\nB X\nA X\nC Y\nC Y\nA X\nA X\nB X\nB Y\nB Z\nA X\nB X\nB X\nB X\nB Y\nA Z\nB Z\nC Z\nB X\nB X\nB Z\nA Y\nC Y\nB X\nB X\nA X\nC Y\nC X\nC Z\nB X\nB Y\nA Z\nC Z\nA X\nC Y\nB Z\nB X\nA X\nB X\nB X\nB Z\nC Y\nA Y\nA X\nC Z\nB Z\nA Y\nB X\nB X\nB X\nA Y\nB X\nC Y\nB Y\nA X\nA X\nB Y\nB X\nB X\nB Z\nB Z\nA X\nC Z\nC Z\nA X\nC Y\nB X\nC Z\nB X\nB Z\nB X\nB Z\nA Y\nC Y\nB X\nB X\nB X\nB X\nB Y\nC Y\nB Y\nB Y\nA Y\nB Z\nC Y\nA X\nC Z\nB X\nB Z\nC Y\nA Y\nB X\nC Z\nB Z\nA Z\nA Z\nA Z\nA Y\nC Z\nA Z\nB X\nC Z\nB Z\nB X\nC X\nA Z\nB Y\nA Y\nB Y\nC Y\nB X\nA X\nA X\nA Y\nA Y\nA X\nB Y\nB Z\nB X\nA X\nC Y\nB X\nB Y\nA Y\nC Y\nA Y\nB Z\nB X\nB Z\nB Z\nB X\nB X\nB Z\nB Z\nC Z\nC Y\nA Z\nB X\nB X\nA Y\nC Y\nB X\nB Z\nB X\nC Z\nA Z\nB X\nC Z\nB X\nB X\nB X\nB Z\nC Z\nC Z\nB X\nC Y\nB Z\nC Z\nB Z\nC Z\nB Y\nB X\nC Z\nA X\nB X\nB X\nC Y\nB Z\n")

;;;

(defrecord GuideEntry [left-char right-char])

(defrecord RoundChoice [their-shape my-shape])

(defrecord Guide [guide-entries guide-entry->round-choice])

;;;

(defn line->guide-entry [line]
  (apply ->GuideEntry
         ((juxt first last) line)))

(defn parse-guide-entries [input]
  (->> input
       str/split-lines
       (map line->guide-entry)))

(def guide-entries (parse-guide-entries input))

(def shapes [::rock ::paper ::scissors])
(def shape-indices (set/map-invert shapes))

(def left->their-shape (zipmap [\A \B \C] shapes))
(def right->my-shape (zipmap [\X \Y \Z] shapes))

(def shape-scores (zipmap shapes (rest (range))))

(def outcomes [::loss ::draw ::win])
(def outcome-scores (zipmap outcomes [0 3 6]))

(defn that-beaten-by [shape]
  (shapes (mod (dec (shape-indices shape))
               (count shapes))))

(defn that-which-beats [shape]
  (-> shape that-beaten-by that-beaten-by))

(defn my-outcome [{:keys [their-shape my-shape]}]
  (cond
    (= my-shape their-shape) ::draw
    (= their-shape (that-beaten-by my-shape)) ::win
    :else ::loss))

(defn round-score [round-choice]
  (let [outcome-score (->> round-choice
                           my-outcome
                           outcome-scores)
        shape-score (->> round-choice :my-shape shape-scores)]
    (+ outcome-score shape-score)))

(defn game-score [{:keys [guide-entries guide-entry->round-choice]}]
  (let [round-choices (map guide-entry->round-choice guide-entries)
        round-scores (map round-score round-choices)]
    (apply + round-scores)))

;;;

(defn guide-entry->round-choice [{:keys [left-char right-char]}]
  (->RoundChoice (left->their-shape left-char)
                 (right->my-shape right-char)))

(game-score (->Guide guide-entries guide-entry->round-choice))

;;;

(def right->my-outcome
  {\X ::loss
   \Y ::draw
   \Z ::win})

(def my-outcome->my-shape-fn
  {::loss that-beaten-by
   ::win that-which-beats
   ::draw identity})

(def my-shape
  (memoize
    (fn [their-shape my-outcome]
      ((my-outcome->my-shape-fn my-outcome) their-shape))))

(defn guide-entry->round-choice' [{:keys [left-char right-char]}]
  (let [their-shape (left->their-shape left-char)]
    (->RoundChoice their-shape
                   (my-shape their-shape (right->my-outcome right-char)))))

(game-score (->Guide guide-entries guide-entry->round-choice'))
