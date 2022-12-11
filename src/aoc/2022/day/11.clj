(ns aoc.2022.day.11
  (:require [clojure.string :as str]))

(def input "Monkey 0:\n  Starting items: 77, 69, 76, 77, 50, 58\n  Operation: new = old * 11\n  Test: divisible by 5\n    If true: throw to monkey 1\n    If false: throw to monkey 5\n\nMonkey 1:\n  Starting items: 75, 70, 82, 83, 96, 64, 62\n  Operation: new = old + 8\n  Test: divisible by 17\n    If true: throw to monkey 5\n    If false: throw to monkey 6\n\nMonkey 2:\n  Starting items: 53\n  Operation: new = old * 3\n  Test: divisible by 2\n    If true: throw to monkey 0\n    If false: throw to monkey 7\n\nMonkey 3:\n  Starting items: 85, 64, 93, 64, 99\n  Operation: new = old + 4\n  Test: divisible by 7\n    If true: throw to monkey 7\n    If false: throw to monkey 2\n\nMonkey 4:\n  Starting items: 61, 92, 71\n  Operation: new = old * old\n  Test: divisible by 3\n    If true: throw to monkey 2\n    If false: throw to monkey 3\n\nMonkey 5:\n  Starting items: 79, 73, 50, 90\n  Operation: new = old + 2\n  Test: divisible by 11\n    If true: throw to monkey 4\n    If false: throw to monkey 6\n\nMonkey 6:\n  Starting items: 50, 89\n  Operation: new = old + 3\n  Test: divisible by 13\n    If true: throw to monkey 4\n    If false: throw to monkey 3\n\nMonkey 7:\n  Starting items: 83, 56, 64, 58, 93, 91, 56, 65\n  Operation: new = old + 5\n  Test: divisible by 19\n    If true: throw to monkey 1\n    If false: throw to monkey 0\n")

(defmacro op-fn [operand-a op operand-b]
  (let [s (gensym)]
    `(fn [~s]
       (let [[a# b#] (map #(or (parse-long %) ~s)
                          [~operand-a ~operand-b])
             f# (ns-resolve 'clojure.core (symbol ~op))]
         (f# a# b#)))))

(defrecord Monkey [items op-f divisible-by true-monkey false-monkey n-inspected])

(defn parse-monkey [lines]
  (let [[_ items-l ops-l test-l true-l false-l] lines
        items (->> (re-seq #"\d+" items-l)
                   (mapv parse-long))
        [operand-a op operand-b] (->> (str/split ops-l #"\s+")
                                      (take-last 3))
        operation (op-fn operand-a op operand-b)
        find-long #(parse-long (re-find #"\d+" %))
        divisible-by (find-long test-l)
        [true-monkey false-monkey] (map find-long [true-l false-l])]
    (->Monkey items operation divisible-by true-monkey false-monkey 0)))

(def monkeys (->> (str/split-lines input)
                  (partition-by #{""})
                  (filter (complement #{[""]}))
                  (mapv parse-monkey)))

(def modulo (apply * (map :divisible-by monkeys)))

(defn round [monkeys]
  (loop [monkeys monkeys
         i 0]
    (if (= i (count monkeys))
      monkeys
      (let [{:keys [items op-f divisible-by true-monkey false-monkey]}
            (monkeys i)
            monkeys'
            (reduce (fn [monkeys item]
                      (let [item' (int (mod (op-f item) modulo))
                            divis? (zero? (rem item' divisible-by))
                            throws-to (if divis? true-monkey false-monkey)]
                        (-> monkeys
                            (update-in [i :items] subvec 1)
                            (update-in [i :n-inspected] inc)
                            (update-in [throws-to :items] conj item'))))
                    monkeys
                    items)]
        (recur monkeys' (inc i))))))

(->> (iterate round monkeys)
     (drop 10000)
     first
     (map :n-inspected)
     sort
     (take-last 2)
     (apply *))