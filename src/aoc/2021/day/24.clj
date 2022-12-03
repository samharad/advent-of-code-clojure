(ns aoc.2021.day.24
  (:require [clojure.string :as str]
            [hyperfiddle.rcf :as rcf]
            [clojure.math.combinatorics :as combo]))

(rcf/enable!)

(def unaltered-input "inp w\nmul x 0\nadd x z\nmod x 26\ndiv z 1\nadd x 14\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 12\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 1\nadd x 11\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 8\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 1\nadd x 11\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 7\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 1\nadd x 14\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 4\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 26\nadd x -11\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 4\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 1\nadd x 12\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 1\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 26\nadd x -1\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 10\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 1\nadd x 10\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 8\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 26\nadd x -3\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 12\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 26\nadd x -4\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 10\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 26\nadd x -13\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 15\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 26\nadd x -8\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 4\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 1\nadd x 13\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 10\nmul y x\nadd z y\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 26\nadd x -11\neql x w\neql x 0\nmul y 0\nadd y 25\nmul y x\nadd y 1\nmul z y\nmul y 0\nadd y w\nadd y 9\nmul y x\nadd z y\n")
(def input "

------------------- 1st DIGIT -------------------
inp w
// zero out x
mul x 0
// set x to 0
add x z
// keep x at 0
mod x 26
div z 1
// set x to 14
add x 14
// set x to 0
eql x w

// set x to 1
eql x 0
assert= x 1
// set y 0
mul y 0

// x is 1; rest are 0

// set y to 25
add y 25
// w is input, x is 1, y is 25

mul y x
assert= y 25
// y to 26
add y 1
assert= y 26

mul z y
assert= z 0

mul y 0

// w is input, x is 1

// set y to w
add y w
// w, y are input; x is 1
assert= w y
assert= x 1
assert= z 0

add y 12

// w is input; y is input + 12; x is 1
assert= x 1
mul y x

add z y

// w is input; y, z are dig0+12; x is 1
------------------- 2nd DIGIT -------------------
inp w

// w is input1; y, z are input0 + 12; x is 1
// zero out x
mul x 0
// set x to input0+12
add x z
assert= x y z

mod x 26
div z 1
// set x to input0+23
add x 11

// w is input1; y, z are input0 + 12; x is input0+23

// set x to 0
eql x w
assert= x 0
// set x to 1
eql x 0
assert= x 1
// set y to 0
mul y 0
// set y to 25
add y 25
mul y x
assert= y 25

// w is input1; y is 25; z is input0 + 12; x is 1

// set y to 26
add y 1

// set z to 26*(input0+12)
;prn y
assert= y 26
mul z y

// set y to 0
mul y 0

// w input1; x 1; y 0; z 26*(input0+12)

// set y to input1
add y w
assert= y w

// set y to input1+8
add y 8
// set y to y
mul y x

// set z to 26*(input0+12) + input1+8
add z y

// w input1; x 1; y input1+8; z 26*(input0+12) + input1+8
// at this point, z CANNOT be 0
------------------- 3rd DIGIT -------------------
inp w

// w input2; x 1; y input1+8; z 26*(input0+12) + input1+8

// set x to 0
mul x 0

// set x to z
add x z

// set x to (26*(input0+12) + input1+8) mod 26
mod x 26
div z 1

// set x to ((26*(input0+12) + input1+8) mod 26) + 11
add x 11

// set x to 0
eql x w
assert= x 0


// set x to 1
eql x 0
assert= x 1

// set y to 0
mul y 0
// set y to 25
add y 25
// keep y at 25
mul y x
// set y to 26
add y 1
assert= y 26

// w input2
// x 1
// y 26
// z 26*(input0+12) + input1+8

// set z 26 * (26*(input0+12) + input1+8)
// set z to (* 26 (+ dig1 8 (* 26 (+ dig0 12))))
mul z y
// set y to 0
mul y 0
// set y to dig2
add y w
// set y to input2+7
add y 7
// keep y at digit2+7
mul y x
// set z to (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))
add z y

------------------- 4th DIGIT -------------------
inp w
// set x 0
mul x 0
// set x z
add x z
// set x to (mod (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))
//               26)
mod x 26
// set z to z
div z 1
// set x to (+ 14 (mod (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))
//                     26))
add x 14
// set x to 0
eql x w
assert= x 0
// set x to 1
eql x 0
assert= x 1
// set y to 0
mul y 0
// set y to 25
add y 25
// keep y at 25
mul y x
assert= y 25
// set y to 26
add y 1
assert= y 26
// set z to (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12))))))
mul z y
// set y 0
mul y 0
// set y to dig3
add y w
assert= y w
// set y to dig3+4
add y 4
// keep y at dig3+4
mul y x
// set z to (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
add z y

------------------- 5th DIGIT -------------------
prn z
inp w
// set x to 0
mul x 0
// set x to z
add x z
// x is between 0 and 25
mod x 26
// set z to (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//             26)
div z 26
prn z
// x is between -11 and 14
add x -11
// set x to 0 or 1
eql x w
// set x to 0 or 1
eql x 0

// set y to 0
mul y 0
// set y to 25
add y 25
// set y to 25 or 0
mul y x
// set y to 26 or 1
add y 1
// MAYBE multiply z by 26
// i.e. set z to (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                  26?1)
mul z y
prn z
// set y to 0
mul y 0
// set y to w
add y w
assert= y w
// set y to dig4+4
add y 4
// set y to dig4+4 OR 0
mul y x
// set z to (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                           26?1))
prn y z
add z y
prn y z

------------------- 6th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// x is between 0 and 25
mod x 26
// keep z at z
prn z
div z 1
prn z
// x is between 12 and 37
add x 12
// set x to 0
eql x w
// set x to 1
eql x 0
// set y to 0
mul y 0
// set y to 25
add y 25
// keep y at 25
mul y x
// set y to 26
add y 1
assert= y 26
// set z to (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                 26?1)))
mul z y
// set y to 0
mul y 0
// set y to dig5
add y w
// set y to dig5+1
add y 1
// keep y at dig5+1
mul y x
// set z to (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                            26?1))))
add z y
prn z

------------------- 7th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// x is between 0 and 25
mod x 26

// set z to (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                              26?1))))
//             26)
div z 26
prn z
// x is between -1 and 24
add x -1
// x is 0 or 1
eql x w
// x is 0 or 1
eql x 0
// set y to 0
mul y 0
// set y to 25
add y 25
// set y to 25 or 0
mul y x
// set y to 1 or 26
add y 1

// set z to (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                              26?1))))
//             26?1)
mul z y
// set y to 0
mul y 0
// set y to dig6
add y w
// set y to dig6+10
add y 10
// set y to 0 or dig6+10
mul y x

// set z to (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                             26?1))))
//                            26?1))
add z y
prn z

------------------- 8th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// set x to between 0 through 25
mod x 26
// keep z at z
div z 1
// set x to between 10 through 35
add x 10
// set x to 0
eql x w
// set x to 1
eql x 0
assert= x 1
// set y to 0
mul y 0
// set y to 25
add y 25
// keep y at 25
mul y x
// set y to 26
add y 1
assert= y 26
// set z to (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                   26?1))))
//                                  26?1)))
mul z y
// set y to 0
mul y 0
// set y to dig7
add y w
// set y to dig7+8
add y 8
// keep y at dig7+8
mul y x
// set z to (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                             26?1))))
//                                             26?1)))
add z y
prn z

------------------- 9th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// x is between 0 through 25
mod x 26
// set z to (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                26?1))))
//                                               26?1)))
//             26)
div z 26
// x is between -3 through 22
add x -3
// x is 0 or 1
eql x w
// x is 0 or 1
eql x 0
// set y to 0
mul y 0
// set y to 25
add y 25
// set y to 25 or 0
mul y x
// set y to 1 or 26
add y 1
// set z to (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                26?1))))
//                                               26?1)))
//             26?1)
mul z y
// set y to 0
mul y 0
// set y to dig8
add y w
// set y to dig8+12
add y 12
// set y to 0 or dig8+12
mul y x
// set z to (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                               26?1))))
//                                                              26?1)))
//                            26?1)
add z y
prn z

------------------- 10th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// x is between 0 and 25
mod x 26
// set z to (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                  26?1))))
//                                                                 26?1)))
//                               26?1)
//             26)
div z 26
// x is between -4 and 21
add x -4
// set x to 0 or 1
eql x w
// set x to 0 or 1
eql x 0
// set y to 0
mul y 0
// set y to 25
add y 25
// set y to 0 or 25
mul y x
// set y to 1 or 26
add y 1
// set z to (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                  26?1))))
//                                                                 26?1)))
//                               26?1)
//             26?1)
mul z y
// set y to 0
mul y 0
// set y to dig9
add y w
// set y to dig9+10
add y 10
// set y to 0 or dig9+10
mul y x
// set z to (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                 26?1))))
//                                                                                26?1)))
//                                              26?1)
//                            26?1)
add z y
prn z

------------------- 11th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// set x to between 0 and 25
mod x 26
// set z to (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                    26?1))))
//                                                                                   26?1)))
//                                                 26?1)
//                               26?1)
//             26)
div z 26
// set x to between -13 through 12
add x -13
// set x to 0 or 1
eql x w
// set x to 0 or 1
eql x 0
// set y to 0
mul y 0
// set y to 25
add y 25
// set y to 0 or 25
mul y x
// set y to 1 or 26
add y 1
// set z to (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                    26?1))))
//                                                                                   26?1)))
//                                                 26?1)
//                               26?1)
//             26?1)
mul z y
// set y to 0
mul y 0
// set y to dig10
add y w
// set y to dig10+15
add y 15
// set y to 0 or dig10+15
mul y x
// set z to (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                    26?1))))
//                                                                                                   26?1)))
//                                                                 26?1)
//                                               26?1)
//                             26?1))
add z y

------------------- 12th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// set x to between 0 and 25
mod x 26
// set z to (/ (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                       26?1))))
//                                                                                                      26?1)))
//                                                                    26?1)
//                                                  26?1)
//                                 26?1))
//             26)
div z 26
// set x to between -8 and 17
add x -8
// set x to 0 or 1
eql x w
// set x to 0 or 1
eql x 0
// set y to 0
mul y 0
// set y to 25
add y 25
// set y to 0 or 25
mul y x
// set y to 1 or 26
add y 1
// set z to (/ (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                       26?1))))
//                                                                                                      26?1)))
//                                                                    26?1)
//                                                  26?1)
//                                 26?1))
//             26?1)
mul z y
// set y to 0
mul y 0
// set y to dig11
add y w
// set y to dig11+4
add y 4
// set y to 0 or dig11+4
mul y x
// set z to (+ (dig11+4)?0 (/ (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                                      26?1))))
//                                                                                                                     26?1)))
//                                                                                   26?1)
//                                                                 26?1)
//                                               26?1))
//                            26?1))
add z y

------------------- 13th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// set x to between 0 and 25
mod x 26
// leave z at z
div z 1
// set x to between 13 and 38
add x 13
// set x to 0
eql x w
// set x to 1
eql x 0
assert= x 1
// set y to 0
mul y 0
// set y to 25
add y 25
// set y to 25
mul y x
// set y to 26
add y 1
assert= y 26
// set z to (* 26 (+ (dig11+4)?0 (/ (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                                            26?1))))
//                                                                                                                           26?1)))
//                                                                                         26?1)
//                                                                       26?1)
//                                                     26?1))
//                                  26?1)))
mul z y
// set y to 0
mul y 0
// set y to dig12
add y w
// set y to dig12+10
add y 10
// set y to dig12+10
mul y x
// set z to (+ dig12 10 (* 26 (+ (dig11+4)?0 (/ (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                                                        26?1))))
//                                                                                                                                       26?1)))
//                                                                                                     26?1)
//                                                                                   26?1)
//                                                                 26?1))
//                                              26?1))))
add z y

------------------- 14th DIGIT -------------------
inp w
// set x to 0
mul x 0
// set x to z
add x z
// set x to between 0 and 25
mod x 26
// set z to (/ (+ dig12 10 (* 26 (+ (dig11+4)?0 (/ (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                                                           26?1))))
//                                                                                                                                          26?1)))
//                                                                                                        26?1)
//                                                                                      26?1)
//                                                                    26?1))
//                                                 26?1))))
//             26)
div z 26
// set x to between -11 through 14
add x -11
// set x to 0 or 1
eql x w
// set x to 0 or 1
eql x 0
// set y to 0
mul y 0
// set y to 25
add y 25
// set y to 0 or 25
mul y x
// set y to 1 or 26
add y 1
// set z to (/ (+ dig12 10 (* 26 (+ (dig11+4)?0 (/ (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                                                           26?1))))
//                                                                                                                                          26?1)))
//                                                                                                        26?1)
//                                                                                      26?1)
//                                                                    26?1))
//                                                 26?1))))
//             26?1)
mul z y
// set y to 0
mul y 0
// set y to dig13
add y w
// set y to dig13+9
add y 9
// set y to 0 or dig13+9
mul y x
// set z to (+ (dig13+9)?0 (/ (+ dig12 10 (* 26 (+ (dig11+4)?0 (/ (+ (dig10+15)?0 (/ (+ (dig9+10)?0 (/ (+ (dig8+12)?0 (/ (+ dig7 8 (* 26 (+ (dig6+10)?0 (/ (+ dig5 1 (* 26 (+ (dig4+4)?0 (/ (+ dig3 4 (* 26 (+ dig2 7 (* 26 (+ dig1 8 (* 26 (+ dig0 12)))))))
//                                                                                                                                                                                          26?1))))
//                                                                                                                                                         26?1)))
//                                                                                                                       26?1)
//                                                                                                     26?1)
//                                                                                   26?1))
//                                                                26?1))))
//                            26?1))
add z y
")

(defn monad-as-understood-by-me
  [dig0 dig1 dig2 dig3 dig4
   dig5 dig6 dig7 dig8 dig9
   dig10 dig11 dig12 dig13])

(defn parse-instructions [input]
  (->> (str/split-lines input)
       (map #(str/split % #" "))
       (filter (comp #{"inp"
                       "add"
                       "mul"
                       "div"
                       "mod"
                       "eql"

                       "prn"
                       "assert="
                       "HALT"}
                     first))
       (map #(mapv (fn [word]
                     (or (parse-long word)
                         (keyword word)))
                   %))
       (take-while (complement #{[:HALT]}))
       (filter (complement #{[:HALT]}))))


(def instructions (parse-instructions input))
(def unaltered-instructions (parse-instructions unaltered-input))

(defn ->state [{:keys [input]}]
  {:input input
   :w 0
   :x 0
   :y 0
   :z 0})

(defn- exec-op [[f a b] state]
  (let [a-val (a state)
        b-val (if (number? b) b (b state))
        res-val (f a-val b-val)]
    (assoc state a res-val)))

(defmulti execute (fn [[op] _] op))
(defmethod execute :inp [[_ reg] state]
  (let [[head & input] (:input state)]
    (-> state
        (assoc reg head)
        (assoc :input input))))
(defmethod execute :add [[_ a b] state]
  (exec-op [+ a b] state))
(defmethod execute :mul [[_ a b] state]
  (exec-op [* a b] state))
(defmethod execute :div [[_ a b] state]
  (exec-op [quot a b] state))
(defmethod execute :mod [[_ a b] state]
  (exec-op [rem a b] state))
(defmethod execute :eql [[_ a b] state]
  (exec-op [(comp {false 0 true 1} =) a b] state))
(defmethod execute :prn [[_ & regs] state]
  (println (select-keys state regs))
  state)
(defmethod execute :assert= [[_ & xs] state]
  (assert (->> xs
               (map #(get state % %))
               (apply =)))
  state)

(defn execute-program [instructions input]
  (let [state (->state {:input input})]
    (reduce (fn [state instr]
              (execute instr state))
            state
            instructions)))

(def negate
  (comp :x
        (partial execute-program (parse-instructions "comment a b c d e \ninp x\nmul x -1"))
        list))
(defn =3x? [a b]
  (-> (execute-program (parse-instructions "comment foo bar\ninp z\ninp x\nmul z 3\neql z x")
                       (list a b))
      :z
      {0 false 1 true}))

(defn set-w-x-14 [x]
  (execute-program (parse-instructions "top\ninp w\nmul x 0\nadd x z\nmod x 26\ndiv z 1\nadd x 14")
                   (list x)))

(rcf/tests
  (negate 100) := -100
  (=3x? 1 3) := true
  (=3x? 1 2) := false
  (=3x? 10 30) := true
  ((juxt :w :x) (set-w-x-14 4)) := [4 14])

(defn take-prog [small big]
  (loop [[s & small :as all-small] small
         [b & big :as all-big] big
         acc []]
    (cond
      (nil? s) acc
      (#{:assert= :HALT :prn} (first s)) (recur small all-big acc)
      (nil? b) (throw (ex-info "Fail!" {}))
      (= s b) (recur small big (conj acc b))
      :else (throw (ex-info "FAIL!" {})))))
      ;:else (recur all-small big acc))))


(defn execute-monad [input]
  (let [unaltered-instructions (take-prog instructions
                                          unaltered-instructions)
        unaltered-res (execute-program unaltered-instructions
                                       input)
        res (execute-program instructions input)]
    (assert (= unaltered-res res))
    res))

(defn candidates [s]
  (->> s
       (map (fn [x]
              (map (comp parse-long str) (str x))))
       (filter (partial every? (complement zero?)))))

(defn satisfies-monad? [input]
  (-> (execute-monad input)
      :z
      zero?))

(defn random-digs []
  (for [_ (range 14)]
    (inc (rand-int 9))))

(println "")

(doall (->> [(repeat 14 1)]
            (map execute-monad)))
#_(defonce tail-candidates
    (doall
      (->> (apply combo/cartesian-product
                  (repeat 7 (range 1 10)))
           ;(take 100000)
           (map #(vector % (execute-program instructions
                                            (concat [1 1 1 1 8 9 8] %))))
           (filter (comp zero? :z second))
           (map first))))
          ;(apply min-key (comp :z second))))
          ;(map second)
          ;(map :z)
          ;(reduce min)))
#_(def head-candidates
    (doall
      (->> (apply combo/cartesian-product
                  (repeat 7 (range 1 10)))
           (map #(vector % (execute-program instructions
                                            %)))
           (take 100)
           (map (comp :y second))
           (prn)
           (filter (comp zero? :z second))
           (map first))))

;(count head-candidates)
;(count tail-candidates)

#_(prn
    (let [candidates (map (fn [[a b]] (concat a b))
                          (combo/cartesian-product
                            head-candidates
                            tail-candidates))]
      (->> candidates
           (map #(vector % (execute-program instructions
                                            (concat [1 1 1 1 8 9 8] %))))
           (filter (comp zero? :z second))
           (first))))
       ;(map first)))
;(apply min-key (comp :z second))))
;(map second)
;(map :z)
;(reduce min)))


#_(doall
    (->> (repeatedly random-digs)
         (take 300000)
         (map #(execute-program instructions %))
         (map #(dissoc % :instructions :input))))

#_(prn (-> (execute-program instructions (take 14 (cycle [8 9])))
           (dissoc :instructions :input)))
;
;(prn (-> (execute-program instructions (random-digs))
;         (dissoc :instructions :input)))
;
;(prn (-> (execute-program instructions (random-digs))
;         (dissoc :instructions :input)))
;
;(prn (-> (some satisfies-monad? (candidates (range 99999999999999 0 -1)))))
;(some satisfies-monad? (repeatedly random-digs))
