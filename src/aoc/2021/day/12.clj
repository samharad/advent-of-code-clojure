(ns aoc.2021.day.12
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.string :as str])
  (:import (clojure.lang PersistentQueue)))

(rcf/enable!)

(def input "LP-cb\nPK-yk\nbf-end\nPK-my\nend-cb\nBN-yk\ncd-yk\ncb-lj\nyk-bf\nbf-lj\nBN-bf\nPK-cb\nend-BN\nmy-start\nLP-yk\nPK-bf\nmy-BN\nstart-PK\nyk-EP\nlj-BN\nlj-start\nmy-lj\nbf-LP\n")
(def t-input "start-A\nstart-b\nA-c\nA-b\nb-d\nA-end\nb-end")
(def t-input-med "dc-end\nHN-start\nstart-kj\ndc-start\ndc-HN\nLN-dc\nHN-end\nkj-sa\nkj-HN\nkj-dc")

(defn parse-adjacencies [input]
  (->> (str/split-lines input)
       (map #(str/split % #"-"))
       (mapcat (juxt identity reverse))
       (group-by first)
       (reduce-kv (fn [m k v] (assoc m k (map second v))) {})))

(def adjacencies (parse-adjacencies input))
(def t-adjacencies (parse-adjacencies t-input))
(def t-adj-med (parse-adjacencies t-input-med))

(defn paths [adjacencies start end]
  (let [paths (fn paths [curr visited]
                (if (= curr end)
                  [[curr]]
                  (mapcat (fn [neighbor]
                            (map (fn [path] (cons curr path))
                                 (paths neighbor
                                        (if (= curr (str/upper-case curr))
                                          visited
                                          (conj visited curr)))))
                          (remove visited (adjacencies curr)))))]
    (paths start #{})))


(defn part-1 [adjacencies]
  (count (paths adjacencies "start" "end")))

(rcf/tests
  (part-1 adjacencies) := 3298)

(defn paths [adjacencies start end]
  (let [paths
        (fn paths [curr visited did-double]
          (if (= curr end)
            [[curr]]
            (let [did-double (or did-double (visited curr))
                  to-visit (remove #{start}
                                   (if did-double
                                     (remove visited (adjacencies curr))
                                     (adjacencies curr)))]
              (mapcat (fn [neighbor]
                        (map (fn [path] (cons curr path))
                             (paths neighbor
                                    (if (= curr (str/upper-case curr))
                                      visited
                                      (conj visited curr))
                                    did-double)))
                      to-visit))))]

    (paths start #{} false)))

(defn part-2 [adjacencies]
  (count (paths adjacencies "start" "end")))

(rcf/tests
  (part-2 adjacencies) := 93572)
