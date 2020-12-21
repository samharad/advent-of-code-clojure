(ns aoc.2020.day.19
  (:require [clojure.string :as s]
            [clojure.math.combinatorics :as combo]
            [hashp.core]
            [debux.core :as d]
            [instaparse.core :as insta]))

(defn parse-rule [rule]
 (let [[i r] (s/split rule #":")
       i (Integer/parseInt i)
       parse-chunk (fn [s] (->> (s/split s #"\s")
                                (filter not-empty)
                                (map #(Integer/parseInt %))))
       r (if (re-find #"\".+\"" r)
           (list (read-string r))
           (->> (s/split r #"\|")
                (map parse-chunk)))]
   [i r]))

(defn parse [input]
  (let [[rules messages] (s/split input #"\n\n")
        rules (->> rules
                   (s/split-lines)
                   (map parse-rule)
                   (sort-by first))
        m (apply max (map first rules))
        rules (into {} rules)
        rules (vec (for [i (range (inc m))]
                     (or (get rules i) nil)))
        messages (s/split-lines messages)]
    {:rules rules :messages messages}))

(def input "17: 72 112 | 71 3\n31: 71 33 | 72 57\n49: 72 110 | 71 45\n37: 71 68 | 72 45\n124: 71 128 | 72 64\n1: 105 71 | 29 72\n44: 72 130 | 71 93\n93: 72 45 | 71 105\n126: 34 71 | 59 72\n89: 71 16 | 72 118\n92: 21 71 | 12 72\n55: 47 72 | 61 71\n7: 29 72 | 29 71\n45: 72 72 | 72 71\n28: 110 71 | 109 72\n10: 110 72 | 63 71\n29: 72 71\n27: 71 53 | 72 13\n107: 45 71\n84: 5 71 | 39 72\n119: 72 105 | 71 110\n53: 28 71 | 10 72\n18: 72 86 | 71 71\n25: 110 72 | 105 71\n82: 106 71 | 75 72\n46: 29 71 | 63 72\n71: \"a\"\n129: 49 71 | 80 72\n123: 12 71 | 79 72\n111: 110 72 | 96 71\n35: 72 107 | 71 104\n113: 10 72 | 1 71\n106: 26 72 | 6 71\n8: 42\n36: 71 10 | 72 79\n26: 72 122 | 71 51\n74: 48 72 | 2 71\n5: 72 104 | 71 22\n120: 71 48 | 72 2\n64: 72 96 | 71 56\n72: \"b\"\n131: 63 72 | 56 71\n79: 56 71\n23: 72 96 | 71 29\n6: 71 111 | 72 32\n43: 71 73 | 72 70\n78: 14 71 | 74 72\n16: 71 29\n54: 110 72 | 18 71\n80: 2 72 | 109 71\n75: 108 72 | 30 71\n13: 25 71 | 116 72\n132: 15 72 | 24 71\n70: 114 71 | 113 72\n112: 7 71 | 131 72\n88: 72 89 | 71 102\n130: 71 110 | 72 56\n0: 8 11\n117: 66 71 | 27 72\n127: 72 126 | 71 67\n52: 72 109 | 71 96\n122: 45 72 | 2 71\n103: 72 44 | 71 36\n58: 72 41 | 71 35\n114: 72 121 | 71 52\n85: 72 120 | 71 23\n19: 72 18 | 71 68\n2: 72 71 | 71 86\n65: 54 71 | 90 72\n96: 71 71 | 71 72\n69: 72 17 | 71 40\n116: 71 48 | 72 29\n66: 72 100 | 71 92\n115: 72 48 | 71 63\n39: 72 62 | 71 83\n22: 71 96\n50: 105 71 | 134 72\n108: 87 72 | 46 71\n34: 55 72 | 9 71\n11: 42 31\n14: 56 71 | 96 72\n24: 58 72 | 95 71\n83: 72 45 | 71 29\n47: 72 110 | 71 109\n94: 37 72 | 97 71\n12: 48 72\n51: 72 48 | 71 68\n98: 109 71 | 18 72\n32: 72 105 | 71 68\n95: 72 77 | 71 78\n118: 72 68 | 71 2\n21: 72 45 | 71 134\n59: 71 123 | 72 133\n105: 71 71\n76: 72 109 | 71 29\n56: 71 71 | 72 72\n68: 72 86 | 71 72\n3: 107 72 | 19 71\n86: 72 | 71\n33: 72 69 | 71 43\n128: 2 72 | 68 71\n67: 72 84 | 71 125\n60: 29 71 | 110 72\n100: 79 72 | 4 71\n97: 56 72 | 45 71\n133: 76 71 | 122 72\n104: 45 72 | 109 71\n109: 72 71 | 71 71\n30: 60 71 | 4 72\n99: 71 50 | 72 38\n9: 115 72 | 101 71\n87: 68 71 | 63 72\n77: 81 72 | 20 71\n101: 71 2 | 72 110\n20: 71 45 | 72 56\n40: 129 71 | 65 72\n42: 127 71 | 132 72\n91: 71 105 | 72 109\n81: 72 45\n62: 45 72 | 63 71\n61: 71 110 | 72 63\n4: 71 134 | 72 134\n57: 82 72 | 117 71\n41: 71 91 | 72 116\n134: 71 72\n73: 71 99 | 72 85\n90: 63 71 | 2 72\n15: 71 103 | 72 88\n38: 72 63\n121: 134 71 | 96 72\n110: 86 86\n48: 71 72 | 72 72\n63: 72 71 | 71 72\n125: 71 124 | 72 94\n102: 119 72 | 98 71\n\nabbaabbbaaaaaabaababbabbababaaba\nbabbbbbbbbababaaaaaababbbabababaabbaaaaabbabaaab\naaaaabaaabbbaabbaaababaa\nbabababbaaababbbbababaaaaabbaababbbabaaa\nbbaaaaaababbaaabaabaabbb\naabbbaaaabbaaabbbabbbaaa\naabbbaabaababbabbbbbbaba\nbbbaaabbaaaaabbabbabbaab\nabbbbbaababbababbbbbaabaabbababaabbaabaa\naabbabbabbabaabababbbabaabababbb\nbbaaabbabbaaabbabaabbaaaaaaaaaabaabaaabbbaabaabb\nbbaabbabbabbabaaabbbbabb\naaaabbbbaaaabaabbababaabbbabaaabaabbabab\nabbbaabbbbabbaaabaaabbab\nbbbabbbaaabbbababbaaabbb\nabbababbaaabababbbbbbbab\naabbaaaabbaaaaabaaababbbbbaabaaa\nabaaabaaaabbababbabbabbbabbaabbabbaabbbb\nabbbbbaaaaaabbaabbbbbbab\nbabaaaaabbbabbaaabbabaaa\naaabaaaaabaababaaaabbaab\nbabbababaaaaababaabbaaabbbbbaaababbbaaaaababaaabbabbbbabaabbabababbaabba\nbbbbbbbaababbabbaaaaabaabaaaaabbababbbbbbabaabbaabababaaaabbabba\nbbbaabbbaaaabababababbbb\nbabbbbbaabbbaababababbab\nbbabaaaaabbbabaaabbbbabb\nbbbaabaabaabbaaaaaababbbaababbbaaabababbbaaabbba\nabbabababbabaaaaabbbaaab\nbbbaaabaaaaaaabbabababbaaaabbbaabaaaaabaaabaaaaa\naabbabbabbaababbbbaababbbbabaaaaaabbabbabbaaabbbbaaabaabbabbbbbb\nbbababbaababbabababbaaababaabaabbaaabbabababaaabbabbbbba\nbaaababbbbabbbbababbaaababbaaabbbbbaaaaaabaabbabbababbba\naabbaaaabaaaabbabbababbb\naaaabbaaaabaabbaaabaabbb\nbbbbbbbbababbabaaaaaaaabaabbbabbbabbabbaaaaaabbaababbaabababbbab\naaaaabaaaabababaaabbbbbaabbbbbbbbabbaaaa\naabbbbbaaaabbbbbaabbaabb\nabbbbbbbaaabbbbbbaababab\naaababbababbaaababbabbaa\nbabbbabbbaaaaabbbabababa\nbbbaabbbbbbaaaaaaaaaaababbabaaaabbabbaababaaaaba\nbbaaaaaabbababaabbbaabaabaaaabaaabaaabbb\nbabbbabaaaabbbbbbabbaabb\naabaaabbaababababbabbaaaaabababb\nbabaaabbabaaaaaaabbaababbabababa\nbbababaabaaabaaababaababbaaaabbbaaaabaabaabbbaabbbaabbabbbbabbaaabbbbbbbaaaaabbabbbbabaaabbaaabb\naaabaaaaaabbbaababbbbaba\nbbbbaaaababaaabbbbabbabb\nbbbabbbaaabbbaaabbabbaaabbaabbba\nbaaaabbaabaaababbbbabaaa\naabbbababbbbbabbaaaaabbbaaabbababaaababbabbbabbb\nabaaabaaabaaabaaaaabaaabaababbaa\nbbaaababbbbbbbbbaaaaaaaabbbabaabbaaaaabbbbaaaaaabbaaaaaa\nbbbaaabbabbbbaabaabbbbbabaababba\nbabaaaaababbaaabababaabb\naaaaaaabaaaabaabbabaabababababbababababa\naabbaaabbaabaaaaaababbaa\nabbbaabaaabbbaababbaaabbbbaaabbbaabaaaaa\nbbbbbbbbbaababbbbbababaaababbbaa\nbabaaaabbbbaabbabbbbbaaaaaaaaaaaabbbbbabaabbbbbb\nabaababababaaabbaaabaaba\nbabaabbbaabbbabaaaaabbab\nababaabbaabbbaaaabbaabbaaaababbbbabaabaabbaabbbabbabbbbbaabbabaa\naabaabbaaaabaaaabaabbbaa\nbaabbbbbbaababbabbabbabaabbbabbb\nabaaaaaababbbbbaababababbbbbbbaaaabbbbabbaabbbbb\naabbbabaaaaabbaabaaaababbbbbaabbaaabbabbbabbbaaaaababbbb\nbabbabaaaaaaabbababbaabb\nababbabaaabbbbbabbaaaaba\nbaaabbbbbbbabbbababaabbbbbaabbbb\naaaabaaaaabbbbbbbbbbaababababbaabbbbbbabaabaabaaaaaabbbb\nabbababaaabbbbbabbabbbabbbbabbba\nabbababaabaababaabababaa\nbbaaaabaabbbbababbbbaabbbabaaaaabbabbbaabbaaabababaabbbbbbabbbab\nabbbbabbabbabbabbbabbbba\nbaaaaaaabbbbabbbbaabbaaaaabbbbabbabbbbabababababaababaaa\nbabbaaabababbbbbaaaaaaaa\nabaabbaaaaabaabbababaaaaaaabbabababbbabaaabbabaabbbbbbabbabbaaaabbbbbbbb\naaaabbbbbabbaaababbaaabbabababab\nbbbaabaaabbabbbbabbbbbbbbbaabbbbbbabbbbb\nabbababbabaabbaabbabbabaaaabbbbbbbabaaabbbbbbbaaabbbaabbababaaaabaabaababababbbbbbbbbabb\nabbbbaabababbabbbbbbabbbbbbabbab\nbaabbaaababaabbbabbbabaaaababbbaababaabababbbaabaababbbbbaabbabb\nabaabaabbabbaaaababbbbbbbbabbbaaaabbaaabbbbabbaaabbababbbbbbaaaa\naabaabbabbbbaabababbbaab\nababababbbaaaabaabbbabbbbbabaaab\naabbbabbbaaaaaaaaabaabaaaababaaabaabbbbaaaabaabaaaababaabbaaaaba\nbbaaaaaaababaabaabaaabab\nbbbbbabaabbbaabaabbbbbbbaabbbaaababaabbbbbbababaabbbbbbbaaaaabbabbbbaaba\nbbabbababbaaabaabbbbbbbbaaaababaabbbabbabaaabbbabbbaabab\naaaabaaabbbabbaaaaabbababaabaabb\nabbbaaaabbbbaaaabbbbbaab\naaababbabbbbbbbaabbbbbbabaabaaba\nabaaaaaaaabaabbaabaaaaaabbababaaaabbabab\naaababbaabbbaabbabbaabbbbbabbbaaabbabbabbbbabbab\naabbaaaaabbbbababababbab\nabbaabababbababbbbbabbaabbbbaaab\naaababbabbabaababbabbaab\nbaabbbbbbbabbbabaabbbbaaaaaababbaaabaaabbabbbaabbbaaaaba\nbabbaaababbababbbababaaababbabbabbbaabbbbabbbaab\nbbbaabbaaaaabaabababbaab\nbbbbbabaababbaabaababbbbaabbabba\nbaaaaaabbaabbbabaabaabbabaabbaabbbabaabbbabaaaaa\nababaabbbbbaabbbbbbbabbaabbbaaaaabbaabbaababaaabbbbbbabbabaaaabb\nbbabbbbbbbaaaabaabababaa\nabaabaabaabaabaaabaaaaba\naabbbbababaababaaabaaaaa\nbaabaabaaaabaabababbbbaababbaaba\nbbaaaaaaababbbbbbabababbbababbba\naababaabaabbbabaabbaabbbaababbababbaaaba\nababaaabaaabababbaabbbbaaabbbaab\nabbaaaabababbababbabaabb\nbabbbabbbaaababbaaabbaaaaaabaaabbbabbabb\nbbbbabbbbbbbbabbbbaababa\nbbbbabaabaabbabbaabbabaababbbbaa\nbabaaaaabaaabbbbabbaabbbaabbabbbbbabbabb\nbbbabbbaaabbbbaabbabaaabaaabbabbbaababbbbbabaababbbaaaabbaabbaab\nbaaaabbbabaabaaaabbbbaaaabbabaaaaabbabbbbbbbbbbbaabbabbbababababbaaabaabaabbabab\nabaabbabbababbbabbbababbbaababab\nabbbaabbbaaaababaaaaabbaabbabbbaaabaaaaa\nbaaaabbaaaaabbbbabbbaaab\nababbabbbbabaaaabaabbabaaabaaabbaabbbaaaaababbaabaaabbbabbbbabba\nabaababbbbaaaabaaaaaabbbaaaabababbaaababbabbbbaabbbbaaaaaabaababaabababbaabaababbaababab\nabbbaabaaaaabbbbabbbbaabbabbabababbbabaababbbbbaaababbbb\naaaabbaaaabaabbbbabbabbbbabbbabbbaaabbabbaabbaaaabbbabbbabaaaabbbaabbabababbbabbbaaabaaa\naaabbaaabaabbbbbaabbabab\nbabaabaaaabababaabbabbba\nbbbababbbababaaabbbaaaaaaaabaabbaabbbbbbaaabbbab\nabaabaaaabaaaababaabbaabbabaabba\nbaababbbbbbbaababbaababbbbbbbbbababbbaababaaabba\nbbbbaaaaaaaaabbaaaabbbaa\nabbaababaabbbaaabbbbaaaaaababaabbabababa\nbabbbabbababbabbbbbbaabb\naabaababaababbbaabbabbbbaaababababaabababbababaababbabbabbbaaaaabababbbabaaaaaaababaabbbabbbabbb\nbbabbaaabaaaabaaaaaaabaabbbbbbaabbbabbbaabaabbab\naabbbabbbbaababbabababaababbbbaabbaaabbbaaabaaba\naaaabaabbbbabbaabbbabaaa\nbaabbabbababbbaabababbbabaaabaab\nbbabbbaaaaabbbbbabababbb\naaabaaabbbaaaaabbbaabaaa\nabaaabbbabbbbbaaaabbbbabbababaabaabbabbababaabbbabbbbaababbbbaab\nbabbabbbbaaaabababbbabbababababaabaabbaaaabababbababbbabbbbaabab\nbbaabbaaabbbaabbbbabbabb\nabbaabbaaabaaaaabaaabbbaabbbaaabaaaaaabb\nbabaabbbabbbbbabbbbbabba\naabbbbbaabbababbaaabaaabbaababaabaabbbaabbbababa\nbbbbbbaaabbbaabbaaaabbaababbbaab\nbaaaaaaaababbabaabaaabab\nabbaababbaabbbababaabbab\nabbbabababbabbbbbabaabba\nbbbaabbbbbbbabbbaaabaaab\nbbbbababbbbaabbabbbbabbbbabbabbabbaabaaaaabbaabb\nabbaaabbaaaaabbabbbabbbb\nabbbbbbbbabbbababaaaaaaabaaabbba\nabbbbbbbbbaabbababaaaaab\nbbbbbbaabaaaabaabbbbbbabababaaaa\nbaabbbbaabbaabababbbaabaabbaaaabbbababaababaaabbbabaaababababaaa\nbabbbabbaaababbabbbbabbbaaababbbaaaaaaaabbaaabbb\naaabaaaaaabbbbbaaabaabbbbaaabaaa\naabbbabbaaaaabbaabbbaaab\nbaaabbaabbbaaabaabaaabaaabbbabbb\nbbbbbabbbbbaabaabbbbababbbaababa\nbaaabbaaaabbbaabbaababab\nababbbbaaabbaaababaaabaabbbaaabbaaabbaabaabababbaabbaabb\nbabaaaaabaababaabbaaabaa\nbbabaaaaaaaabbaabbaababa\nbbabababaabbababbbaaaabababbaabb\nbbbabbaaabbaababbaababba\nbbaaabaabbbaaaababaaaaba\naabbbbbbababaabaaabbbbbaaabbaaaaaabaabaa\naabaabaaaabaaaaaaaababaaababababbbaaabbababbaabbbbaaaababbbbabbbaabbbabaaaabbaba\nbababbbabaabbbbbabbabaababbaaabaaaaaabbabababaababbbabaaaaaabbbbababbbaa\nbaaaaabbabbbbbaabbbababbbababbab\naaabaaabbaaababbbbabbbba\nbabaaaaaababaaababaaabbbbaaaabaaaababbbaaaabbbaaaaaaaababbabbababbbbaabbbabaabab\nabbbbbbbaababbababbbbbbbabaabababaabaaab\nbaabbbbbbabaababaaaabaababaabaabbbaaabaa\nbbbabaababaaaabbabbaaaaaaaaabbaabababaaa\nbabbbbabbbabaabbabbabaabaaababbbaabbbabbaaaabbabbbaaaaaaababbaababaaaaaaaabbbababbbbabbb\naaabaaabbbbbbbaaaaabaabbbaabaabb\naaabbbbbababbabbabbbbbab\naabaabbabaabaabbbbbbbbabbbaabbbb\nbbbaabbabbbbabbbbbbaaabaababbababababaabaaaabbba\nabbababaabbbaaaabbbbbabbaabaabaababbaabbaaabbbbabbbababa\nabbbbaabaaabaabbaaabaaabaaaaaaabaaaaaabaaabaabaaababaaab\nababbababaaabbbbbbabbaaabababbba\naaabbbbbabbaabbbabbbaababaaabbab\nbbbbaaaabaababaabbaaabaa\nababbabbabbaaaaaabbaaaabaabbaabbbbabaabb\nabbabbbbbbaabaabababaaaaababbbab\nbaaaaabaaaabbabbaaaaababbaaaaaaaaabababababaaaaaaabaabaaababbbbb\nbbabaaabbaaaaabaaababaaaaaabbaababbabbab\nbaaaaaabbabbbbaabbababbbaaaabbbbaaaaaaaabbababaaaaabbbba\nbaabbbbbbababaaaabbbbbaaabababbbbbaaabbbaaabbababbaabbbabaaaaaabbabbbaaaaabbaaba\naaabbaaaabbaabbbbbabbabb\naaabaabbababbabbaababaaa\nbbbbbaaaaabbbaaabaabbbabaabbaaba\nbabaabbabbbbbbabababbbababaaabbabbbbbaba\nbbbaaaaaabbaaabbbabaaaabbbbabaaa\naaaababbbabaababaaababab\naaabbaaaaabbbaabbbabaaab\nabbbbaaabaabbaaaaabbbabaaaabaaaabbbababbababbabababbbbaabbbbaababbbaaaaa\nbbaaaaaababbabaaababbaaaabababba\nbbabbbaabbbabaabbbaaabaa\naabaaabbaabbbababbabaaaaaaabbbabaabbabaa\nbaaaaabbbbbbababbbabbbbbabaaabbbbbaaababbababbbaabababaaabbbabbbaaababba\nbabbbbbbababbaaaaabbabbaaabbababbbabaaabaabbbabaabaaababbabbabbabaaaaabbbaaabbbb\nabbbaabbaabbbaabbabbbbba\nabaaaabbaaabaaaaaabbabab\naaaaabbbbabbbabbbaababbababbaabababbabaababbaabbbbaaaababbabbabbaabaabaabbbbbaaaaabaaababbbbbaaa\nbbababbbbaaababababbbbab\nbbabbbabbbaaaaaaabbaabaa\nbbbaaababaaababbbbbabbaaaabbabbaaaaaaaababbbaaabbaabbabbaaaaaaabaaaaaabb\nbbbbbabbababbabbbbbaaaaaaaabbababababababbababab\nababbbbbaaababbaabaaaaaaabaaaaaabbbbbbba\nbabbabbabaaabbbbbbaaabab\nbababbaabbaaaaabaaaaaabb\nbbbbbbbbabbaabbaabaabaaababaabaabaaaabbbaabbaaba\nbaabbbbbbbbabababaabbaabababbbbbabbabaabbabbababaaabbbaa\nbbabbbaabbbbaaaaaabbabab\naaaabaababbbbbbaaabbbabbababaabb\naabbbbbaabbbbabaababaaab\nbbbaabaaaaabaaabbbabbbba\nbabbaaabaabababbbababaaaabbabbabbbbabbba\nabaaaababaabbabbababaaaa\nbababaaaababbabbaabbaabb\nbaaabbaaabbaababbbaaabab\nbbbbababbababaaabbbaabbaababaaba\nabaabaaaaaabbbbbbbbbabbabababbbaababbaabbbabbababbaaababbbbabbbbbbabaaabaabababbababbbab\naaaaaabaaababaabbbabbbbb\nabbbbbbbbaaaaabbbbaaaabb\nbaabbbbaabbaaabbbaaababa\nbaaabaaaabbaabaabbaaaaba\nbbbbbaababbaaaabbaaababbabbbbbaaaaabbbabaababababaaaabbaaabaaaabbbabbbaa\nbbbbababbbabbaaabbbbaaababaabbababbbaaab\naaabbaaabaaababbabaaabba\nbabbabaaaaaabbaaabbabaab\nbabbbbbaaaabbbabaabaababaabaaaabbaabbaaaaabbabbb\nabbbbababbbbbababaaabaaabbbabaabbaabababbabbabaabbaaabbbaaaabbaaabaaaabaabbbaabb\nbaabbbbbbbbababbbaababaaaaaabaababbabaab\nbabbabbaabbbbbaaabbbaabbbbaabbbb\nbaaabaabbaaabbbbaaababaabbbababbaaaaabaabbaaabbaaabbababababbbba\naabaabbabbaaabbabaabaabb\nbbbaabbababaaaaabbbbbbbaaabbaaba\naaabbaaabaababbbababaabb\nbbabbbaabbaaababababababbbbbbabaaabaabbbaabaaaabababaabb\nabbbaabaabbababaababbaba\nbbababaaaabbbaaabaabbabb\nababbabababababbbbaababa\naabaaabbaababbabbabbbaaa\naaaaababaabbbbaabbbaaaab\naaaabbbbbbaaabbababbbbaabaaabaaababbbbaaababbbaabaabbbab\nabaababaabbbaaaabbbaaaba\naaabbaabaaabbbababbbabbbbababbba\nbaaabbaaabbababbbbbbabab\nbbaabbbbaaababababbaaaabbaabbaabaababbaa\nbbbaabbaaaaaabbbbbabaaabaabbbaaabbaabaaaabbaabababbbabbb\nbababbaaabbaaabbbbbababbbaabaaba\nbaaaabbbbabaaabbbbaabbbb\naabbaaaabbbbaaababaaaaba\nababbbbbabbaabbbbbbbbaaaaaaaaaababbaaabbaabbaabbbbaabbba\nabaaaabbbbaabbaaaabbbaba\nbbaababbaaabaaaababbaaab\nabbbbabbaaababbaabbbbbbbbbbababbabaababbbbbbbabaababbabb\nbaaababbbaababbbaabbaaba\nbabbaaaaaabaabaaabaaaaaabaabbbbbababaabaaaababaaaabbbaabaaabaaaa\nbaabbaaaababaabbaabbbbaaaabbabbbaaaaaaaabbbabbbbabbbaabababababbaabaabaabaabbaaa\nababbbbbabbabbbbaababbbaaabababaababbbabbaaabaaa\nbabbabaababbbabbbbbbaababaabbabb\nabaabbaababaaaababaabbabbaaaabaaabbabbaa\nbaabbbbbaabbbabaaabbbbababbbaababbbbaabbabaaaaba\nbbbbbaababaaabaaaabaababbaaaaabbbabbbaabbaabbaabbbaababa\nbbaaaaabbbabaaaaabbababbbaabbababbabbbba\naaabaaabbaababbbbbbaaabaaababaabbbaabbbbabbabaaabaababbbaaaabaaabaaaaaba\nbaabbababbabbabbabababbababbaabbbbabaaabbbbabbbb\nabbabbbbbbabaabaaaaababbaaabbbba\nabaaaaaabbbbbbaaaaabbabaabbaaabbbbbbbabbaaababaa\nbbabaabababbaaababbabaab\naabababbaabaaabaaabaaaab\nabbaabbbababbbbabaabbaab\naabaaabaabaaabaaaaaabbbaabbabababaabbbbbaaaaabbabababbabbaaabbaabbbaaaaaabababab\nbbbbaababaaaabbabbaabaaa\nbabbbabababbabaaaabaabbb\nabbbbbaaaaababbbbababbbb\nbabbaabbbabababbaabbababbbabababaabaabaabaaabaab\nbabbabbaabbbbbbabbababaababaabba\nabbabbabbbaabaababbababbbbabbababaabbbbb\naabbababbaabbbabbbababab\naaaaabbaababbbbaaabbaaba\nbbbbbaaabaaaabbababbaaabbaaabbbbbbbbaaabaaabaabb\nbbaababbaabbbaaabbbaabbabbaabbaababaaabbababbbaa\nabaaabbbbbababbbbbaababa\nbbbbbabbbaababaaabbbaabbbababbab\nabbababaabbbbbbaaaaaababaabbbbaaabbaabbabbaaabaa\nbabbabaaaabaaabaabbabaabababaaab\naaababababbaabaabbababbabbbababbaabbaaabbabaaabb\nababbaabbabbbbaaababababbaaaabaaaabbabaa\nbbbaabbbabbabababaaaaaba\naaaabaaaabaaaaaaaababbbb\nbbbaaabaaaaabaaabbaaaaba\nbababaaabbaabbabbaaabaaa\nabbbabbababaaabbbbbaaabababaabbbababbaaa\naabbaaabbbbabbaabbababbb\nabbbababbbaaaaabaaaaabbbabaabbba\nbbbbabbbaaabbaaaaabaabababbabaabbabbbbbb\nbbbbbaaabaaaabbaaabbbaaabbbbababbaaaaabb\nbabbaabbaabaabbaabbbabaaaaabaaaa\nababbababbbaaababbbaabaabbbbbaabaaaaabbbbaababbabbabbbbabbbaabab\naabbbaabbbaababbbaaabbab\nbaabaaaababaaaababaabbbb\naaaababaabaabababbaaabab\nbaabbabaabbbababbbbbbaaaabbbababbbaaaabb\nbbbabaaabbbbbaaabbbbaabbbbbabbabbbaababababababa\naaaabbbbbaaaaabbbabbabbababbabbb\nbabaabbbbaabbabababaaaaaaaaaaaaa\nabbabababaabbababbabbbbb\naaabbbbbbabbabbaababbaaa\nbabaabaaaabbbaaabbbaabba\nbbbabbababbabaabbaaaaaabaababbaaabaabbbabbaaabbaaabaabaabbabbaababbbaabbbaaaababbbaaabbbbbabbbab\nababbbbbaaaabaabaaababab\nabbaaabbaaaaabaabaababab\nbbbbbaaaabaaabaaaaabbabaababaaaa\nbaabaaabbbbbbabbbabbabba\nbbbbbabbaaabaaabaaabbabb\naabbaaaaabbbbbbabbaaaaababbaaaba\nabaaaaaaaababaababbbabaa\nbbbaaabaabaabaababbabbba\naaaabbbbbaaabbbbaabbbbaaabaabaaa\nabbbabaaaabbbbabaaaaaaabbbbbaaaabaaaabbbaaaaabbabbaaabaa\naaabbbbababbbbaabaabaababbbbaabbbaabbaab\nbaaaaaaababaaabbbababbbb\naaabbabaabbbbbbaaabbaabb\naababbabbbbbbbbaaaababaa\naabbaaaaaaaaaabaabbbababaababbbb\nabbabbbbaababbbaaabbbabbaaabbabaaaaabbab\nbaababaaaababbbababbbaaa\nabbaaaaaaaabbbbbaababbab\nabaaaabbabbbaabbabaabbbb\naaababbabbbbabbbaabaabbb\nbaaaabbbbbbabaababababba\nabbbbbbaabbbabaaabaaaaaaaabbaabaaabbabbbbbbbabbabbbaaaabababbbabbbbabbbb\nbaababaababbbabaababaabb\nabaaabaabbaabbabbabaabbbaabaaaba\naaaabbabaabbbbabaaabaabbaabaabbaabbbabba\nbababbaabbbaaabbbbaabbaabaababaabaaababaaaabababbaabbaab\nbabbabbabaaaabaabababbba\naaabaaaababaababbaaabbba\nbaabaabbaaabbabaababababaabbaaaabababbab\nbaaabbbbbaabbabaaabbbaab\naabbabaabaaabbaabbaaaabaabaaaabbbbabaabaaaabbbbaaaabbaabbabbbbaa\naaaaaaaabbbababaabbbbaba\nabbbabbaaaabaaabbbaaaaaabbbbabbbabbbababbaabaabaabbabbaabaabaabaabaaabbb\nabbbabaaaabababaabbabbbbabbbabbb\nbbbbbbbbababbbbaaabaaaba\naabbaaaaaababababaaaabbabbbbaabb\nbabbbabbbbbaaababababbbb\nbbbbbaaababaabbbababbbaabababaab\nabbbbaabbbbaaaaabbaababbabaabaaa\nabbbababbbaabbabbbbbabba\nabbbabaababbbabbabbbbaaa\nbbabbabababaaaabbaaaabbbbaaaaaabbaabbbbaaabbbbbabaabbbba\nabbbababbababbaabbaababbaaaaabbbbaaaaaabbaabbbba\nbbabaabbbbbbbbabbaabbbabbaabbaabaababbbb\naaaaabaabaababbbabbaababbbbaaabaabbaaaab\nbbbbbbbbaaaaaabbaababbaaababaaab\nabbbabaaaaaabbaabbbaaaab\nbbababaaabbbaabaabababab\naaababbabbbbbaabbbbbaabb")

(def t "0: 4 1 5\n1: 2 3 | 3 2\n2: 4 4 | 5 5\n3: 4 5 | 5 4\n4: \"a\"\n5: \"b\"\n\nababbb\nbababa\nabbbab\naaabbb\naaaabbb")

(def t' "42: 9 14 | 10 1\n9: 14 27 | 1 26\n10: 23 14 | 28 1\n1: \"a\"\n11: 42 31\n5: 1 14 | 15 1\n19: 14 1 | 14 14\n12: 24 14 | 19 1\n16: 15 1 | 14 14\n31: 14 17 | 1 13\n6: 14 14 | 1 14\n2: 1 24 | 14 4\n0: 8 11\n13: 14 3 | 1 12\n15: 1 | 14\n17: 14 2 | 1 7\n23: 25 1 | 22 14\n28: 16 1\n4: 1 1\n20: 14 14 | 1 15\n3: 5 14 | 16 1\n27: 1 6 | 14 18\n14: \"b\"\n21: 14 1 | 1 14\n25: 1 1 | 1 14\n22: 14 14\n8: 42\n26: 14 22 | 1 20\n18: 15 15\n7: 14 5 | 1 21\n24: 14 1\n\nabbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa\nbbabbbbaabaabba\nbabbbbaabbbbbabbbbbbaabaaabaaa\naaabbbbbbaaaabaababaabababbabaaabbababababaaa\nbbbbbbbaaaabbbbaaabbabaaa\nbbbababbbbaaaaaaaabbababaaababaabab\nababaaaaaabaaab\nababaaaaabbbaba\nbaabbaaaabbaaaababbaababb\nabbbbabbbbaaaababbbbbbaaaababb\naaaaabbaabaaaaababaa\naaaabbaaaabbaaa\naaaabbaabbaaaaaaabbbabbbaaabbaabaaa\nbabaaabbbaaabaababbaabababaaab\naabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba")

(defn recursive-matches
  ([ss s]
   (if (= s "")
     true
     (some #(and (s/starts-with? s %)
                 (recursive-matches ss (subs s (count %))))
           ss)))
  ([start-ss end-ss s]
   (if (= s "")
     true
     (let [s-seq (seq s)
           c (count s-seq)]
       (some (fn [[start middle end]]
               (and (start-ss start)
                    (end-ss end)
                    (or (= middle "") (recursive-matches start-ss end-ss middle))))
             (for [a (range 1 c)
                   b (range 1 c)
                   :when (<= a b)]
               [(apply str (take a s-seq))
                (apply str (->> s-seq (drop a) (take (- b a))))
                (apply str (drop b s-seq))]))))))

(def expand-at-i
  (memoize
    (fn [rules i]
      (let [expand-is (fn [is]
                        (->> is
                             (map (partial expand-at-i rules))
                             (apply combo/cartesian-product)
                             (map #(apply str %))))
            r (get rules i)]
        (cond
          (some #{i} (flatten r)) [[:fail!]]
          (string? (first r)) (set r)
          :else (set (mapcat expand-is r)))))))

(defn expand-rules [rules]
  (let [expand-at-i (partial expand-at-i rules)]
    (for [i (range (count rules))]
      (expand-at-i i))))

(defn satisfies [rule message]
  (rule message))

(defn splits [s]
  (->> (for [i (range 1 (count s))]
         (split-at i (seq s)))
       (map #(map (fn [cs] (apply str cs))
                  %))))

(defn satisfies-cat-rules [a b s]
  (->> (splits s)
       (some (fn [[sa sb]] (and (a sa) (b sb))))))

;; A
(comment
  (time
    (let [{:keys [rules messages]} (parse input)
          rule (first (expand-rules rules))]
      (count (filter (partial satisfies rule) messages)))))

;; B :(
(comment
  (time
    (let [{:keys [rules messages]} (parse input)
          rules (assoc rules
                  8 [[42] [42 8]]
                  11 [[42 31] [42 11 31]]
                  0 [["not-referenced!"]])
          expanded (vec (expand-rules rules))
          r42 (get expanded 42)
          r8 (partial recursive-matches r42)
          r31 (get expanded 31)
          r11 (partial recursive-matches r42 r31)
          r0 (partial satisfies-cat-rules r8 r11)]
      (count (filter r0 messages)))))

;; Using Instaparse :) per Clojurians
(let [[rules messages] (s/split input #"\n\n")
      messages (s/split-lines messages)
      parser (insta/parser rules)]
  (prn "A:" (->> (map #(insta/parses parser % :start :0) messages)
                 (keep first)
                 (count)))
  (let [rules' (-> rules
                   (s/replace #"\n8:.*\n" "\n8: 42 | 42 8\n")
                   (s/replace #"\n11:.*\n" "\n11: 42 31 | 42 11 31\n"))
        parser' (insta/parser rules')]
    (prn "B:" (->> (map #(insta/parses parser' % :start :0) messages)
                   (keep first)
                   (count)))))
