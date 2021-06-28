(ns aoc.2015.day.11
  (:require [clojure.string :as str]))

(defn parse-long [x]
  (try
    (Long/parseLong (str x))
    (catch NumberFormatException _ nil)))

(defn ->pass [l]
  (let [base-26 (Long/toString l 26)
        to-alpha (fn [c] (char (+ (int c)
                                  (if (parse-long c) 49 10))))
        pad (fn [n s] (-> (format (str "%1$" n "s") s)
                          (str/replace #" " "a")))]
    (->> base-26
         (map to-alpha)
         (apply str)
         (pad 8))))

(defn ->long [pass]
  (let [to-base26-numeral (fn [c] (char (- (int c)
                                           (if (>= (int c) (int \j))
                                             10
                                             49))))
        translated (apply str (map to-base26-numeral pass))]
    (Long/valueOf translated 26)))

(defn has-increasing-straight? [sz pass]
  (let [ns (map int pass)
        chunks (partition sz 1 ns)
        is-straight (fn [ns]
                      (= ns (range (first ns) (inc (last ns)))))]
    (boolean
      (some is-straight chunks))))

(defn omits? [disallowed pass]
  (not (some disallowed pass)))

(defn has-at-least-n-distinct-pairs? [n pass]
  (->> (partition-by identity pass)
       (filter #(>= (count %) 2))
       (map first)
       (distinct)
       (count)
       (<= n)))

(defn valid-pass? [pass]
  (let [preds [(partial has-increasing-straight? 3)
               (partial omits? #{\i \o \l})
               (partial has-at-least-n-distinct-pairs? 2)]
        preds (apply juxt preds)]
    (every? identity
            (preds pass))))

(comment
  (time
    (do
      (println "Computing...")
      (doall
        (->> (map ->pass (range (->long "hepxcrrq") Long/MAX_VALUE))
             (filter valid-pass?)
             (take 2))))))