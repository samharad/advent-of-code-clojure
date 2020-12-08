(ns aoc.2020.day.08
  (:require [clojure.string :as s]))

(def input "acc +8\nnop +139\nnop +383\njmp +628\nacc -6\nacc +29\nacc +9\njmp +457\nacc +29\nacc +38\nnop +451\njmp +44\nacc +24\nnop +260\nacc +20\njmp +24\nacc +36\nacc +41\nacc +31\nacc +42\njmp +35\nacc +21\nnop +216\nacc +7\njmp +42\nnop +408\nacc +18\nacc -4\njmp +532\nacc -8\nacc +13\njmp +529\nacc +36\njmp +257\nacc +26\nacc +24\nacc -11\nnop +508\njmp +249\nacc +44\nacc +25\njmp +464\nacc +47\njmp +318\nacc -6\nacc -1\njmp +175\nacc +19\nacc +25\nacc +24\njmp +523\nacc -7\nacc +45\njmp +353\njmp +67\nacc -1\njmp -17\nacc -9\njmp +180\nacc +19\nacc -15\nacc +5\njmp +52\nnop +312\nacc +38\njmp -6\nacc +6\nacc -11\nacc -6\njmp +357\njmp +455\nacc +27\nacc +29\nacc +9\nnop +414\njmp +380\njmp +1\nacc +33\njmp +47\nacc +12\nacc +34\nacc -9\nacc +17\njmp +214\njmp +125\nacc +38\nacc +38\nacc +16\njmp +310\nacc +9\nacc +10\nacc -2\nnop -84\njmp +329\nnop +120\nnop -70\njmp -88\njmp +1\njmp +1\nacc +23\njmp -34\nnop +372\nacc +18\nacc +3\nacc +24\njmp -50\nacc -11\nacc +27\nnop +441\njmp +443\nacc +44\nacc -3\nacc +7\njmp +389\nacc -5\nacc +36\nacc -17\nacc +15\njmp +13\nacc +39\njmp +407\nacc +8\nacc +21\njmp +360\njmp +425\nacc +7\nacc +29\nacc +6\njmp +10\njmp +1\njmp +426\nacc +23\njmp +3\nnop +2\njmp +1\njmp +240\nacc +16\njmp -2\njmp +47\njmp +488\njmp +1\nacc +45\njmp +1\nacc +20\njmp +344\njmp +407\nacc +2\nacc +3\njmp +117\nacc +50\njmp -134\nacc +1\njmp +238\nacc +27\nacc -11\nacc +16\nnop +134\njmp +116\nacc -16\nnop +393\njmp +2\njmp +24\nnop +98\nacc +36\nacc +0\nnop +268\njmp +131\nnop +294\nacc +23\nacc +26\nacc +43\njmp +358\nnop +316\njmp +300\nacc +22\nacc +47\nacc +19\nacc +2\njmp +228\njmp +176\nacc -4\nacc +20\nnop +83\nacc -19\njmp -99\nacc +39\nnop +374\nacc +3\njmp +57\njmp +1\nacc +42\njmp +404\nacc +4\nacc -4\nacc -10\nnop +293\njmp +349\nacc -9\nacc +33\njmp +284\nnop +1\nacc +18\nacc -1\nacc -5\njmp +411\nacc +4\nacc -6\nacc +16\njmp +313\nacc +0\nacc +39\njmp +3\nacc -13\njmp +148\nnop +51\nnop -207\nnop +357\nacc -19\njmp +409\nacc +32\njmp +103\nacc +5\njmp +116\nnop +399\nacc +19\nacc +39\nnop +78\njmp +298\nacc -16\nacc +31\njmp +1\nacc +45\njmp -207\nacc +6\nacc +36\nacc +34\nacc +47\njmp -112\nacc +23\nacc +4\njmp +289\nacc -17\nnop +307\nnop -92\njmp -146\njmp +293\nnop +44\nacc +5\nacc +2\nacc -19\njmp +172\nacc +10\nacc +49\nacc -2\njmp +6\nacc +3\njmp +1\nacc -19\nacc +35\njmp +328\nacc +38\nacc -5\nnop -83\njmp -138\njmp -55\nacc +35\njmp +1\njmp -216\nnop +220\njmp +116\njmp -78\nnop +331\njmp +118\njmp +287\njmp -258\njmp -181\nnop +85\nacc +31\nacc +10\nacc -13\njmp +175\njmp +66\nacc +34\nacc +6\nnop -126\nacc +38\njmp +4\nacc +18\njmp -13\njmp -8\njmp +1\nacc -9\nacc +31\nnop +92\njmp +118\nnop +21\njmp +110\nacc +20\nacc +20\njmp +280\njmp -142\njmp +8\nnop -286\njmp +170\nacc +22\nacc +29\nnop -127\njmp +36\njmp +328\nacc +27\njmp -110\nacc +28\nacc +44\nacc +34\nacc +43\njmp -42\nacc -19\njmp +185\nacc -8\nnop -3\nacc +31\nacc -11\njmp +142\nacc +49\nacc -2\nacc +28\nacc +17\njmp +167\nacc +16\njmp +165\njmp -190\njmp -117\nacc +0\nacc +16\njmp +1\nacc -10\njmp -39\njmp +69\njmp -187\nnop -101\njmp +1\nacc +12\njmp +218\nacc -6\njmp -300\nacc +10\njmp +104\njmp +1\nacc +36\njmp -243\nnop +138\nacc -15\nacc +13\nnop +278\njmp -192\nacc +13\njmp +275\nacc +0\nnop +92\nnop +214\njmp -90\nacc +44\nnop +225\nacc +9\njmp -348\nacc +16\njmp -334\nnop -335\nacc -16\nacc -9\njmp -89\nacc +35\nacc +2\nacc +26\njmp +68\nacc -19\nacc +14\nacc -19\nacc -19\njmp +262\nacc -16\nnop -246\nnop -251\nacc +26\njmp +143\nnop +48\nacc +48\njmp +249\nacc -8\njmp +9\nacc +42\nacc +12\nacc +19\nacc -4\njmp +213\nacc +1\nacc +29\njmp -259\nacc +3\nacc +17\nacc -13\njmp -131\nacc +16\nacc -9\nacc +28\njmp +222\nacc +42\nacc +20\nacc -3\nacc +50\njmp -339\njmp +1\nacc +1\nnop -315\nacc +44\nacc +50\njmp -143\nnop -87\nnop +155\njmp +1\nnop +44\njmp +158\nnop -10\nacc +9\njmp +90\njmp +89\nacc +27\nacc -5\nacc -8\nacc +40\njmp +3\nnop -360\njmp +117\nacc -14\nacc -16\nnop -184\nacc +20\njmp +185\nacc -2\nacc +36\nnop +24\njmp +195\nacc -8\nacc -3\nacc +7\njmp -84\nacc +0\nacc -12\nacc +43\njmp -270\nnop -434\nacc -15\njmp +163\nacc +14\njmp +29\nacc +32\nacc +26\nacc +13\njmp -91\nacc +30\njmp -163\nnop +175\njmp -175\njmp +20\nacc +0\nnop -426\nacc +22\nacc -6\njmp -83\nacc +12\nacc +43\nacc +31\njmp -46\nacc +27\nacc +26\nacc +24\nacc -12\njmp +118\nnop -88\nnop -251\nacc -15\nacc +3\njmp -450\nacc +11\nacc +19\njmp -406\nacc +39\njmp -45\nacc +40\nacc -7\nacc +22\nnop -329\njmp -160\njmp +80\nacc +0\nacc -4\njmp +31\nacc +11\nacc -2\nacc +3\nacc +48\njmp -366\nacc -16\njmp -345\njmp -259\nacc -8\nacc +29\nacc +4\nacc +2\njmp -208\nacc -6\nacc +48\nacc -3\njmp +82\nacc +1\nacc +47\nacc +40\nacc +31\njmp -214\nacc +10\njmp -328\nacc -18\nacc +39\njmp +2\njmp -403\njmp +48\njmp +19\njmp -468\njmp -397\nacc +40\nacc +39\nacc +22\njmp +1\njmp -305\nacc +22\nacc -5\nacc +23\njmp -399\nacc +21\njmp +17\nnop -239\nacc +0\nacc +29\nacc +2\njmp -56\nacc -14\nacc +23\njmp -375\njmp -450\nnop +1\njmp -29\njmp -208\nnop +50\njmp -468\nacc +3\njmp +5\njmp -347\nacc +21\nacc +35\njmp -527\nacc -4\nacc -14\nacc -5\nnop +24\njmp -418\njmp +66\nacc +8\nacc -16\njmp -447\nnop -324\nacc +27\nacc +4\njmp -393\njmp -276\njmp +1\nacc +37\nacc +15\nnop -304\njmp -534\nacc -16\nnop -514\nacc +11\njmp -331\nnop -69\nacc +14\nacc +44\njmp -125\nacc +3\nacc +5\nacc +0\nnop -381\njmp -440\nacc +48\nnop -561\njmp -543\nacc -10\nacc +44\nacc -16\nacc +45\njmp -177\nacc +36\njmp -576\nacc +36\nacc +14\nnop -323\njmp -163\njmp -118\nacc -1\nacc -14\nacc +7\nacc +7\njmp +11\nacc +31\nnop -309\njmp -109\nacc +41\nacc +11\nacc +4\njmp -52\nacc +9\nacc +16\njmp -481\njmp -161\nacc -8\nacc +13\nacc +16\njmp -488\nacc +30\njmp -390\nnop -313\njmp -333\njmp -44\nacc +38\nacc +33\nacc +38\njmp -619\njmp -186\nacc +49\nacc -5\nacc -16\njmp -214\nacc +16\nacc +35\nnop -584\nacc -12\njmp +1")
(def t "nop +0\nacc +1\njmp +4\nacc +3\njmp -3\nacc -99\nacc +1\njmp -4\nacc +6")

(defn ->program [input]
  (->> (s/split-lines input)
       (map #(s/split % #" "))
       (mapv (fn [[instr num-s]] [instr (Integer/valueOf num-s)]))))

(defn init-state [program]
  {:program program
   :accumulator 0
   :pc 0
   :status :rest})

(defn run-cycle [{:keys [program accumulator pc] :as s}]
  (let [[instr arg] (get program pc)
        incd (assoc s :pc (inc pc))]
    (if (= (count program) pc)
      (assoc incd :status :terminated)
      (case instr
        "acc" (assoc incd
                :accumulator (+ accumulator arg))
        "jmp" (assoc s :pc (+ pc arg))
        "nop" incd
        incd))))

(defn run [s]
  (letfn [(run' [s]
            (if (#{:terminated} (:status s))
              (list s)
              (lazy-seq (cons s (run' (run-cycle s))))))]
    (run' (assoc s :status :running))))

(defn run-safely
  "Returns if a PC is encountered for a second time"
  [s]
  (let [ss (run s)
        pcs (map :pc ss)
        with-pcs (map vector ss (reductions conj #{} pcs))]
    (->> with-pcs
         (reduce (fn [acc next]
                   (let [[_ pcs] (peek acc)
                         [s pcs'] next]
                     (if (= pcs pcs')
                       (reduced (conj acc
                                      [(assoc s :status :infinite-loop-encountered)
                                       pcs']))
                       (conj acc next))))
                 [])
         (map first))))

(defn alt-progs [prog]
  (->> (map vector
            (repeat (count prog) prog)
            (range))
       (map (fn [[p i]]
              (when-let [instr' ({"nop" "jmp" "jmp" "nop"} (get-in p [i 0]))]
                (assoc-in p [i 0] instr'))))
       (filter identity)
       (cons prog)))

;; A
(prn (->> input
          (->program)
          (init-state)
          (run-safely)
          (take-while #(= :running (:status %)))
          (last)
          (:accumulator)))

(prn (->> input
          (->program)
          (alt-progs)
          (map init-state)
          (map run-safely)
          (map last)
          (some #(when (#{:terminated} (:status %))
                   %))
          (:accumulator)))