(ns aoc.2021.day.25
  (:require [hyperfiddle.rcf :as rcf]
            [clojure.string :as str]))

(rcf/enable!)

(def input "vv.v.v>.>.>.>vvvvv..v>v..>vv........>>>..v.v>>..vv>>>>vv>>>..>>..>>..v.v>....>.>..>..>>>..>>v.vv..>.vv>.v>vv>.>.v>v>vv>>v.>v..v..vvv.v>...v\n..v..>.>>v.>..>v>>.......>v..>..v>>.vv.vv>>v.....>.>...>.v.>v.>>..v.>v.v.>>..v>>.>>v>>...v..>.v.v.v.>vv>>v.>.v.....v>.>.v.>....>....>..v...\n>.>v...v>....>.>v...v.>....>vvv.vvv...>v....>v...>..>...>...>v....v>v.v>v>>..v.>...>>..v.v.vvv.>....>.>>.....>v>.>>..v.v..v....>vv>>.>v..>>\n.vv>.vv..>..>vvv.vv..>........vvv>>vv.>>>.v.v.v.>v>..>..v......vv>...>..v.>v.v.>.v.v>>.>.>..vv...v..vvv.>vv>..v>v>v.>.>v>v>>..>.v...v.v>>.>\n.>>.>v...v...>..>.v.vv.>v>.v...vv>.v..>>..v>.>v>vv.>..v>.v.>.v....>.vv.>..>.>.>.....v...>.>>.v>v>...vv.>>...>.v>v>v..>..>.v.>>......vv..vv.\nv...v.>v>...v...>vv>v..>v...vvv....v>.v...>.>>.>.v.v..>.>>...vv..>...vv.v>v>.v..v...v....v..>.v>vv.>vvv..vv>v.>.v..v>..v..v.>>v>..>.....v.>\n>....v>.v..vv....vv......v>>.v.v.>..>vv..vv.>.>v.>v>.>vvv.>..>>v.vvvv.>..>>....v>>.>v...>>>v.v..v...>>...>v>>.....>.vvvv>>.>v..v..>.vvv...>\n..>..>>>.>.>.v....v.....v>>.>v>v...v.v..>v..v>...v.v..vv.vvvv...>>.>v>.>v...>v>.>..vvv...v>...>v>.>.>v.>...>.v.v.v.v.vvv>.v..>vv.v>..>>.>.v\n>>.vv..>v.>.>...v.v..>v......vvv.vv>.vv....>..vv>.>..v>>.>vvv..>.>...>>v....v.>.v.vv>>.v..>>vv>......>....>v..>v>..>v.>>..v>>.v>.>....v..v.\n.v.>..>.>v>.v>>.v.v..>..v..>...>v..>>vv.>.vv.>>>>v>.>>.v.v.>.....vv....v..>v.>vv>v>>..v.vvv.v.....>>......>.v....>v>..>>>>v>vv.vvv..v>.>>.v\n.v.v>.v.....>>v..>.....>.......v...vvv.vv.>>v.vvv.....v....v.vv....v>......>v.v..>>v.>>.v.>>..>..v>.>.>.v>.v>.vv>.v...>.vv>v.....>..>>.....\n>..>..>>.>.vvv.>.>.v.vv.>..v...>.>v.>.v...>>.....>v.>v..v>.v.v.......>v....v>vvv.v.v>v..v>>...v>>.v>v>vv..vvvv>..>..v>v..>.>...>..>v......v\n.v..>v>v..vv.v..v..v..v.....>.>....>v>.....vv..v....vv.>vv..>.>..v.>..vv.>>...v...v.>>>.v..v..>...>.........vv>...>>>....>>.v>..v..>.>v.v.v\n>>>..>.>>>v..>.>.vv.>.>>>>.>>.......v.v.>..v>v>..vvv..>.....>.>.>vv..>v..v..v...v>v.>>....v>..>.v..v...>v>>v>.vv.>>...>>v...vv.>.v>>>..>>.>\n>....>.v>v>.>.v.>vv.>>....>...>.v.v>v>v.vv>v.>.>..>vv.>...v..>.vv..>...>vvv>vv..>....>....>.....v.>..>..>>v>.>...>...>>>>v.v>>v...>v>.v>.>.\n>.v.>.vvv.vv.>.>..v......>>v>vv>......v..>......v.v>>...>..v>...v>..v...vvv.>.>....>.>>v..v>..>.vv.vv....>v>..v>...>..>>>.v>>>.v...>..>v...\n.v.vvv>.v.v.>v>..v...v..>v>>v.....v......vv.>vvv...v>v.>>>v...>>.>v>........v>...vv>v...>..v..v>.>....v>.v.v.vv>.v>>v..>>>..v.v.>v>v...vv..\nv>..>vv.>..v>v.vv......vv.vv>.>.....v.vvv.>>>>>>>v.>>>.>>.>.v...>v.v.>>v.>.v.......>.>..v>..>>>>.v.v..v....v.>>..>.v>.>v.>..v.v.>.>..v.>..v\n..>v..>.>vvv>>v..v.v.......>.>.>>....>>.>...>..v..>>....>>v...>>...v..>.>.v>>.>>.>vv>v.v>.v.>..>vvv>........v.v>...vv.>v>..v>>.vvv>vv.>....\n...>.vvv..>.v..>>.>.v>v.v..>v..vv....>>>>.>>>.v>..vv.>.v..vv>>>..>.>>.v.....v.vv>>.v.........>v.v.>.>>.>>..>>.>.v.>>.....vv...vv>.v..>.v...\n>>...>..>..vv>..vv.....v.....>v.....>>..v>.v...>.>.........>>.>.>..>...v.vv>v...>.v.v..>..v.>.v>.v.>.v..vv.....v...>.>>v>...v.>..v...>v>v.>\n.>v.v.........>.v>..v.v.vv.>>.>v>v.v>>v...v>.>vvv>>.v.v.v.>.>.v>v..>.......v>..v..>>v.v>...v>..v>...v>v>.vv>....v>.>.>>.v.>.>v..>v>.>vv....\n>........v..>...v.>>v.v.>..v.>v>>...>>..>>>>>>v.>.v...v..>.vvv>>>v.>>vv.>.vvv..v>...>.v>v.>.>v..v>.>>...v>.>.>....v.vv....>v.v>>vv>.v>vv.>v\n..v>..v.>v..vv.v>>.>.>...v>v.vv>>v.v..v>.>...v>>>.vv....v>.vv.>>>.>.>.v.v...v...>...v....>.v>>v>....v>>v..v.>v>>..v...v....v.>>..v....v....\n>.v..v..v.v>..>>..v.v>v..v.v..>.v>...>>>v>.>>..>>...v..>..>.>>.......>....vv.v>>...v.>>>vvvv>>..vv..>>v>..>.vvv>..>>..vv>.v.>v.>..>vvv..>v>\n.v>.>...>..>>>.>v>vvvv...vv>v>v...>>.>.....v.>vv>.v.>.vv.v.>...vv.>v>>>v>...v.v...>>>>..v.>..>v>....v>.>.>vv..v..>....>.v.vv.>..v.>..v.v.>>\nv..v.>..vv..v..v.>..v>v..>.v>....vvv..>>>.>vv..vv..v.>...>>.....>v>.v>..>vvvv>.vv.>.......v..v.v.>.....>...v.>......>.....vv..>v>vv.v.....>\n.v>>vvv>>.v.v.v.........>>..v>v.v..vv.>..>>.>..vvv>.>..v>.vvvv>.v>>.>...>vv..v......>.v>>v...>>.vv...v>...v......>.v.v.>...>.v.v.>..v>>>>vv\n.v>.>v>....v.vvv>.v>>>.>v.>>>..>.>..v..>.v.v>.>..v..v>...v.v.v>.>v>.v>.v>v.vv...>..>..>...v.>v>...vv>>.....v.v>..v>.v>..>>.>vv..vv..>.v>>.>\nv>v>>vv....>>.>.v....>..>v....vv..>vv..>>>v.v>.....>>...>v>v>vv>.>v......v.>.>vv>.>.>.>.>v>.v..>..>...>.v.v>..v>>v>..>v>..v.v.vvv>.>>.v..vv\n.>>>>v...>..v..v>....v>..v....v.>>...>>..v..v..>vvv...>..vv>.>..>.v>vv>vv..>>.>>vv>.....>>v..>vvv.>..........v..>>.>...>>...v...>.v>..>v>v>\n...v...v..>..v>.>..>.>....v.>>>.>v>..>.v..>v>>...vvv..>vvvv.>>..v..>>>>...vvv>>.>v>...v...>.>vv.v.>.>..>.v.>.>.v..>.>...>.>>.v>..vv.v..v>..\nv...v>..>>>.>.>v>>.>.......v>.>.vv>>>v..v...v>.v..v...v.vv......>>.>v.>..v.v...>>.>>vvv.v.v...>.>..v>.......v>.>v.>.......>.>v>.v.v.>>>.v..\nv..>.>..v..>vv>v..vv.>v..>v.>vv>...v........>v.>...>.>.v>.v.....>>>>....vv.>v>v.v..v....vv>.....>>..v>.>.v>v......v........v..v..>v..>v.v..\nv.vvv...>.>>.v.>.>.>>.>.v..v>>>>...>>v..v......>.v...>....v.>.......>.v>..v.v.v..vv>>>v.v....>.....vv>..>.>....>.v....v>>>v.>v.>.v....>..v.\n>..v.>v.>.vv>.v>>...>..>>..>v.>vvvvv.>.>.v>.>.>vv.>>v.v..>v..v>.v.>.......vvv>...v.>......v...vv.>..>>>v.>v>.v.>v>>>v>>v>.vv.vv.>>>..>.>v>v\n.v.v..>vv>.>v.v.>.v...v.v..v..vvv.>....v.>v.v>>>.v.>>>>.>.v.v>>v>>>...>.>>.v>.>.v.v.>.v.vv>.>v.v>.v.v>..v.v..>v.>...v..>>..>..v...v.>v.>...\n..>.>v>..v>.>>v>>>..>vv.v>>.>.vvv>.>..v..>.v..>>..>>v.>>v.v..v...>.>v>v.vv.v>v.vvv..>.......vv.>.vvv.....>>v..>>.>..>>.vv....>v...>..>...>v\n>.>.>v...>..>.v..vv...>...v.v....vvvv.>.v...>..>..v>..>>>....>..>.....>v>........vv.>v>.v..v.......vv...>vvv..>v.vv>>v>...v..v>.>>>.>>...>.\n>.>v..v..v>v...>..vv.>.>..>.>vv.vv..>>.v.>>>.>.>vvv>.v..v.....v..>...vv..v.v>...>v>>>..vvv.>vv.vv.>vv...vv.>v.....>>..>.v.vv.......>vv>v.>>\n>.vv>..v>>>>...>v>.v>vv>>>....vvv.>.>..v.v...>>.>.>...vvvv...v.>.vv>>.>>.v>.>>>........vvv...>>.v.v.>v.>.>.v.v>>>>..>.>.vv>.v>..>..vvv>>v.v\n..v.v..>>...v.vv...>...vv>vv..v>.>......vv.v..>..>>>.>...vvv..v>.vv....>v>.v>>..v.>>v>.>..v>vvv.>v>>.>>>.>.v>......vv.>>v.>v..>..>vvv>.....\nvvv.vv.>....v.>.....>.>.v..v...v>...>>v>.v.vv....vvv.>v...>>>.>.>>v>.v...>>.>>..>v.>...>.>.vv>>v.....>.v..>>v>v..v>..>....>v..vvv>...v.v>>.\nvv.>>.v.>...>..>v.v.v....>>v...>.>>v..v...v....v.>...v..vvvv...v>>.>.v>v..>.>v..>.v.v>.v....>....>>.v>..v..>.v.v...>.vvv.v.vv>>.....>>vv>v.\nv..v.v.v.v>>..>..v......vv.>>.v.>.v.vvv.>..v..>>..>>>v.vv..>.v>v..v>...v>.....>.v..v>.>.v.v.>>>>..v.>>v....>>>>.v.v>.v>v>.v>......>.vvv>.>.\n.v..>....v>.....v>.v>.v.vv.>v>>v>.>vvvv..v>...>.>.>....>.v>.vvv>....v....>>...>v...v..v>>.>.v>>v.vvv.>>.>vv..v.v>..>..>...vv.>>v>vv.vvv..>.\n..v...>..>.vv.v.vv....>>v>.>>v.....>>..>vv.>..vv>>....>..>v.vvv>.v.v.>.>v.v..>..v..>vv..v..>.....>...v>...v>.v.v..v..>>>>>>.v>>v.>>.v.>....\nvv.>..v..>...v..>vv...v.>..v...v>>.v...>..vv..v>......v>......>.v>.>vv...>>..>v.>vv....v>vv..v.v>...>.v...vvv>.vv.>.vvv.v.>vv.....v.>.>...>\n.v>>..v.v.>v.>..vv>>v..>.>v.v.v.>>.>>>>>>.>v>.vv.>.v..>>..v>...>.>>v>>.....>.vv..vvv...>v...vv.>.>.>v..vv.v.>..>>...>.>.v>.......v.v..v.>v.\n>.>.v>v.v>...v...>>.v>>v.>v.>..v.>..>.>vvv.v..v.v.v.v...>...v..>v.>..v>v....vv.v.>.v>>>>.>>vvv..v>.>.v.>v.>.>vv>.v.......>.vvv.v>.v.v..>v>.\n>v>.>.v.v.vv.>>.v...>.>.v>....>vv.v>..v>..>..v..>vv>.>.>vv..v....>vvv>.....>.>..v.vv.>.>....v>.>.>...>.vv.v..>....v...>>..v.v.vv>>...v>.>.v\n>.v....v.v..v...v>v.>>.v.v>.vv>..>.>>v>.v.>v>v>...>...>>.v>>..v.v....>..v>>.v.>>..v.v.v>.>>v.v...v>>v......>v>..v.v.>>vv>.>vv.>....vv>.v>..\n>.vv>>v.>.>.v.....v>v>......v.vv.>>v.vv....vv>....>.>.>.>.....>v.>vvv>..vv.........>>>>.>.>>v>.>.v>>v..>vv.vv.>v>.v...v>vv>..>..vvv..>>v.v>\n.>>.v.vvv.vvvv...v.>>vv.v..>vv.....>.v...v.v>>.>......>>....>v..>..>>>vv.>>>.v..>.>>..>.>>.v.....vv>v..vvv..>v......>.vvv..>v.v.>....v..vv>\nv...v...vvvv>>..v..vvvv..v>v>>..v>..>v>...v.>>>.....v>.v>.v>>v>vv>...v...v.>.>>v>v.....>..>.v>>>.>>>.v...>..>vvvv........v...>.v>vv.>.v....\n>>>..vvv..v>vv..v>....vv>.vv.v.v...>>..>>.v..>..>.>v....>v>>v.v>>..v.v.v..>vv....vvv.>>.>vv.v.......v.>>.>>....v>.....vvvv.>>.>v.>v.vvv>.>>\n....v>>.vv..>...v.v>.>.....>..v>v.>>>>.>.v.v....>..>>v.v>...v>..v..v..v>v.>v.v....>..>>.vv..v...v..v>..>v.vvv..>>>v..>.>>v>v.v.>>......v...\nv>....v>v>v.>.v>>....vvv>>v..>..>v>v......>v.>>vv>.vvv.>.v..v.v>.vv.>.>v>>v.v.>v..>vv.v...>>>.v....>.v.>.>>.v>>v..v>v..v.v.>>>>....vv.v..vv\nv..v>.v......>.v.v>...>v..v>.>.>..v..>>.v.....>>>>v.>>.>....>.>.v..>vv.>v.v>vv>.>v.vvvv...vvv>v.>..>....>>vvv>.vv>>v>.v>.>vvv.>>.....v..>.>\n>>.vv.>.v..v>...v>..v.v>>v>..>>vvv.....v.v.>>>.>....v.v>.>v.>.vv>v...>>>v.v.....v...vvv>..>>>v.....>v...v....>....>>v..>v.vv..>.....>...>.v\n>>>.v>.v..>..v.>.v.v>..>>...v.>v.v.>.vv...v>...v.v>...v...v>........>v>...v...vv>>v>v...v>.>vv.......vv...>>vvvv.>....v.vv.v.v>v.>v..vvvvvv\n..>>...>.v.vv>v..v.......v>.....v>>..v.v.v>vv.>>....v>>>v.v.v...v>.>.>vv.v>......vv..v.v..v.v.......>>.>..v.>..>.vv>.>..v.>.v>..v>..>.v>..>\nv...>.........v..>.>...v.>.>.>.>>>..v>.v.>.v.>.>>>.....>.>.>..v.>.v...>>v.vv...>v>..>v>.v.v>>...v.v>v>v....v>.vv>v..v.>.>>>v>>v.>v>.v...v>v\n>>.v>>>.>..>v>.>v.v....>.v..>....>vv>..>vvv.>>..>..>..vv..>v>vv>>.>v...>..v>>v.v...>v.v.v.....>.....>..vvv.>..v.v..v..>....>vv.>>v.>v.v...v\n>.v>..>v>vv.>>vv.>.v>.v..vvv>>..v.>.>>..vv>>>......>.vv.>.vv.....>.v>.vv.v>.>v..v>.v.v..vv.>.>>>v.v>>.v.v>.>..>v.>v>.>..vvvv..>.v..>>>>>...\nv>>.vv.>>.v>vv..v>.>vv...v..vv.>..v.>vv....v..v..v>>>>...>v.>.v.>v.>v..v.v>.vv>>>v.v>v>.....v>.v....>>....vvvv.v>.....vv..>v.>.>..>.v..>.v.\n>.>>.....>>v..>....>.v.>v>..>v.v.......>.>v...>........>>v.>>>.>>>v..v>.....v....>v>>.v.vv>>>.v..>.>>..>>vv>v...v..>v....>>vv.vv.>>..>>.>vv\n..>.>>..v.>>>...>.v.v>.v.....vv.......>v>vvvv....v.>>>.....>v.>.>v..v....>>v>.>..v>.>...v>.>>...v.v>v>>v.>>.>v>..v.v.>v>v>v>...v>.>.vvvv>.>\n....v>v>.>.>...v.v...v......v...>>>.v>>.......>....vv..v..>v.>.v...........vv.>.>.>...v.....>....>.....v>v>.>>.....v>v.>>>..vv....v>..v..v>\nvv..>...>>vv..v...v>.>..>.vv.v....v>>v.......>.vvv.vv>>>....>.vv...>...>v.v>>.>>>.>..v>v>>.>.....v.v.vvvv.vv.v..vv>>>...v>>>.vv...v>v..>...\n..>>..v>..>>.v.>v..>.v.>...v>v>vv.>.......>>..>.v.v>.v.v>>>.>.vv..>>v>..vv.v..v....>v>...v>.v.>..>v>..vv>.>v..>.>v.vv.>v.>.......v.vv.v.>.v\nv>>>>..v.v>.>vvvvvv.>.vv.>>.v.v.v..v..>.vvv>.>v..>..v....>vvv>.>v.>..vvv..>.>..>v.>...v.>>v.>.v..>...>..>....v..>v....v.v....vv...>.vv>.v..\nv.>........vv>..v>.>v..v.>>.>..v..v.v>>v.....v>....v>...vvvv>.>.>>.....>>vv.>..>>v.>..vvvv>>>v.....>>>v..v.>>..>.>..vv........vv>v.v...>...\n.v>>.vv.>vv>v.>...>.vv...v.>v>.>vv..v..v.>>v.>vv.>....>>>>......vvv>>v..vvvv.vv..v>>v>......v.v...>.v..v>v>v.>>v...v.v.>.v..v..v.>>....v.v>\n.v>>.vv..>v..v.>.>>..v.>v......>..v>v...vv..>.v>..>..>.v.>..>.>vvv.v.....v.v.v>vvv.v>.....v>.v.vv>.v>>..v...>v.v.>>.v.v.>>..>vv>.....>..>..\n..>>v>>.vv>.....v>vv.v..>>v..>v>v.>.v.v.>v.......v.v>v>.v.v.v.v.>vvv>...v..>..v>vvv>>.>v>.vv.v.>.>v>v.v>>vv>.>.....v....>>>v....>.>>..>..vv\n.>.>v>.v....>......vv........vv.v.v.....v>>>v........>......v....>.v>v.v..>v>.v>vv>.>>..v.vv>......v....>>..>.>.v>vv>>>.vv.>.>..>....v>>..v\nv.v>v.>...>>vv>>>.>...v>v..>>v...v>.>.....vv.....v..v...v.v..vv..>.>v.vv>..v.>.v>.v>.....v>>vv.>>v.vv..v..>....vv.>v.....v>.v..v...vvv>..>>\n>>.>..v.....v>>.>>vv>.>vv..v..>vv..v...v.v.>.>vvv.v>...>v..v>>v>v......v.vv.vvv..v.>vv.vvvvv>>.>>v..>.vvvv.....vvv..vv.>v>>.>>>.>.>.>..v...\nvv..v>v..v>....v>.vv>>.....>>.>>.v.>.v..>....>v>>...>.v....>.>v.>>.>>>v.v>.vv>.>.v.v.>>..v..>>>>.vv..v.v...>...>..>.>.>>vv...>....v...>vv.>\n>..v.v.vv>.v.v.>.....vv.v.vv.v>.>.v>...>..v.>>vv.vvvv>.>.>.>....v.vv>v..vv>v.>>.>.v>.v.v.>.>vv>>..vv>v>.vv........v.>.v>.v.v.v>v...v>.>>...\n>>.v.>>>v.>.>v.>v>>v>>....v>.vv>..>v.>.v...v>.>.vvv....v>..v..>v.vv.v.vvv.v>>.v.>.v..>....v..v....>v..>>.>..>.v........>v.>....>>..>>v.....\nv.v>.>>>..>>>.>.v>.>.>>.>v.vvvv.>...vvv...vv...>>>.....vvvv>.>...v.v>v...>v>>v.vvv.v.>.v.>>vvvvv...>>v.>>..v>vv>>v..>>v..>..vv.>v.v...>....\n.>.v...v..>.v..v......>...v....>>.>v>vvv>.>....v.>.v.>.v.v...v>>.vv..>>.>.v.>v.v>....vv>.v...>>v>>.......>....v.v..v.v.v.v>>...>>..>>v....>\n.....vv.>..v.>>v..>>..v.>>.>.>>>>v...vv...v>..v...v...vv>.>>vv>.>v..>...>>>>>..vv...>v>>.>..v>..>.vv>.>.v.v..>vv>>..>vvv>.v.>..v>vv>v.v>v>.\nvv>v>vv>>....>v>vvv..v......>>..v>v.v>.>>v.>.v.v..>>.vv....>>vvv..v.v>>.>.....vv..>>.>vvv..>..v.>v>.>.....>.>vv.v.>v..>vv.>>>.v.v...>v...>>\nv..>..>..vv....>>.v...>......>.......v>..v>v.>vv.v>...>>>........>v.v.>>.....v...v>>vv..>..v..v........v.>.v>.>v..vvv.v>.vvv>>..v.v>>.>>>.v\n.vvv.vvv>.vvv...v>>.>v..>..v>v>v....vv>v>..>.v...>>>>..v>.vv.>>v...>>.v.vv>.>v.>.vv>>...>v.>.>.v.>v>..v>v.v....v>>...vv....v>...v.....>....\n.>......>.>v.vvv..>>..vv.v.vv>..v....v>.v>.>v..v.vvv.>vv.>..vv>.>.>>v.v>...>...>...v..>....>.v..>.v.v>.v>>.v.v>...vv.>.>.vv.>..v.v>v>v..v..\nvvvv>v.v.v.v.vvv>v.v>>vv>.>>>.>>>v...v..>v......>vv...>v.v.>.>..v>.v>>.v>vvv....>>.>.vv..>...>v.v..v...v>v>v.>...v>>v.>....v.>v...v....>>.>\nv>>.vv>>>..v>vv..v...>..v>.....>.>vv........v...v>..>>.v>..>v...>..vvvv...>>v....v>v>.vv........>vvvv..>.......v.>..>.>>.>>>.>..>..vvv>....\n>.>...v....vv..>>.v>v..>v>>v>..>>.....>>>.>>...v.>vvv>...>.v..v.v..........vv...v..v>>.>v.>>>.v..v..>>>>..>.>..v.v>..>vv>>>.>.....v..>v..v.\n.....vv.>>.vvv....v...>...>...>>.>v>v>v..v>v...>>..vv..>.>v..>..>v..>>..>>>.vv>vv>vv>>...v..>..v.v>>.>v.......v.v.v...v>.>.>...>>..vv>>v>v.\nvv>>v.v.>>..>.>v>.>.v..>v..v...>v>.>>vv>v..v>..v>..>..v>..>v>.v.>.v>.v...v.>>.>v.>.v..>>>>.v..>...>.>.v....v>vv..>>..>..v.>..>..vv>...v>>.v\n.v...v>.....v>.vv......v>>.>>.vv.v..v.>>..>>.>>>.v...vv......v.>v.>v..>...v.>.>>>>.>.v.....v>...>..v.>..>.v...>v>>..>...>.v>>>>...vv>..>.v>\n.v>v>..>>.>vvv.v.........>>..v.>...>>.v>>>..v.v.>v.v.v.>..v.v...v>.v.>.>..v.>.v.v.v.>>.>.v>.>>v>>.>.v>>..v>..>v>.v..>>..vvv.>>>>>v.>>.>.>.>\nv..>>..>vv.vv>.>..>>>v>>.vvv.>v>.>>>v>.>...>...v.>..>.>.......v.>>......>.......vv>.>..>v>>v.>v....>>>..v.>..v>.>>.vv.>v..>v.>...v>.>.>v.vv\n...>v.>v.vv.>>v...>..v..vv.v.v>.v.>......>.v.>.v..>.v..v..>.vv>>.v>v...>.>>.....v.v..v.v.vv>v.v>..v.....v.>vv>....v..>>>>>v....>v...>>.v.>v\n.....v>.>>.>v>...>.>>.....v..vv>v..vv>v.>..vvvv..v..v.>>.>>....>>>.vv.>vv>.>vv>...v.>>..vv.>>.>.>.vv.v>.vvv>.....v>..v.>vv>vv>.>.v...v.>...\n>.vvvv.>..>>.v..>vv>.>vv>v.>.v..v.>>.vv>>>.vvv>.vv..>>.>>>>v.v.>.v>..vv>>...vv..v..vv..>.>...vv..>.....>..v..>v.>v...v>vv>.>v.>..>v..>.>.vv\n..vv.>vv>>v...>vvvvvv.>>..>v......>>.....v.>>vv>>>.......>.>v>........vv>.v...vv.v..>.v>..v...v>v>>.v>v.v.>v.v.>vvv>.v>>......v...vv....>.v\n.>..vv>.....>>.>v.....vv.>v.vv>>.>>>>..v..v>..v>.>>.....v.>.>>v>>.>v.v>.v.vv>.>>...>v>v..>...>.>>.v.>...v>..v.v.v....v..v...>.v>v>v>.v.v...\n.v>.>...>v...>>>..v.v>>.v>>v...>v..>v>vv>.>v>.>>v.vv>.>>v.>.>..vv>>>v>.>.v.......v..v>>.>......vv..>>.v..>>...>.>..vv.>.>.v>v>..>v>>..vv>>v\nv>...vv.vvv....>..>>..v..>v.....>>v>v>>>.vv..>.v....>v...>vv.v.vvvvv.v.vv..>.....>.>v..>v>v...>>>..v....v>>>vv.>>vv..>v>.v>.>.>.>.v.>.vv>>.\n.>>>.v.v..>v...>.>>.vv....>>>....vv.v>>..v>.v..>.>>...>.v>>.vv>vv...>.v..>v>>...v>>...vv>vv.v.v>>v>..>>v.vv.>vv.>>>>>.vvv>..>>...v.>>.v.>v.\n.v.......>.v>>...>...v.vv>>.........vvv.......>v..v..>vv..>>.>.>vv.v.v.v...v.>vv>...>>...v.v.vv>v.v>v..>.v.v..v...v>>vvv.v..>.>>.v>v..vvv..\n....v.v>.>>.>>vv.v>>..v..vv.>>..vv.v..>>>v.v.v.>.>..>.v.v>v.v>v....>...v....>>>..>...>vv.v.>>.>v..>vv>.v>>vvv..>...>..v.>>vv.v>..vv..v.>>>v\n..>.>.v.v>>v..>.......v>.....vv.>..vvv>.>>..>.v.v.v..vv.>>..>.v.>v>>.vv.>.>.>v..>v>.>...v...v>>>>v...>.vv>>v.>>vv>...>.>>>>.>v>>.v.>v>.v..v\n.v...v.>>.>>...v.>.>>>.>.v.>>.>.v>>v>>.....>vv.>.>.v..........>.v.v.>.v..vvv.>.>..v>>vv.v..v>..>v.....>.>>.>.v>..>v.>v..>..>..>.vv>>......v\nv.v>>..>.>>>.vv..>v.>..>..>v.>v..v>..>v.>v.v.>.>>.>...>>v>v.....v..>...>.>>.>..v.>.v.>>.>...v>..>...v.vv>>..>..v.>>v.....>.v.>.>>>...>>>v..\nv....>.v>v.v.>v...v.>vv>v.vvv...v..vv>>.....>v.>v.>>.v..v.v.v.>v>...v.v...vv..v.....v.....>v....>..>vv>.v.v>>.>>.>.v..v>vv...v>...>>v.>>vvv\nv>v>.....>>..>v.....v..>.>..v.......v>>.>..>>....>.>>>.>.....v>>v.>v..>.v>.>.>.v>vv...v>vv>v>.>v>v.vv...>vvvv..vvv>vvv.v..>..v>vv....v...>>\n.v.vv..v>v...>>v.>..>>>>.>.>vv......vv>....vv.v..>.....>vv>>>..v>>v..>...v.vv.....>.>....v..v.>.>...>>.>.v>.>...v>v>>v>v.>..>>.....>.>v..v.\nvv..>.v>>>>.>>..v..>>.>.>vv...>..vvv....>vvv>v.v..v.>..v..vvv>.>v>>..v.v...v...v..v.vv...v.>v>.>..v....v.v>vv..>..v>>...>v.>.v...>v...>.v>.\n...>...>...>.>vv..>.vv..>>..v.>...>>...>.v>.>...>...>..vv.>>v..>.vv.vv>v.>v.v....>.>.>v.>>.>..v>.>.>.>>v.>..>..>v>....vv.>..>.....>v.v.>..v\n.>.v.v>.>.>vv>>v..>..>.vv...v..vv>..vv.v.>..v..>.v>>>>v..vvv...v...v..v.v>..vvv.>vv>>..>>....v.vv...>...v>>v..v>v....v>.v.>>v>v.v.v.>v.>v.>\n......>.>>.v.>>..v>v..>v..>>.v>>.>v>.vv>>..vv>vv...>.vv.v..v...>..>..>>vvv.>...>...v...>>..vv.v>.>>>vv....v.v.>.>vv>v>..>.>v.v>>.....>.>.>.\n......>v>v>.>.>..>v>vv..v.>>>.v>....vv.v>v...>v.>..>v>.v.....>.v>v>vv>..>v>>...v>...vv.>..v...v.v..>v...>...>..>>v>>>>vv.>>.vv>v>v>.v.>....\n..v.>vv.v>v.>.>v.v.>vv>...>v>>>.>...v..>>.>vvv..v..vvv...>..>.v...v..>..v..v..>v>>.v..>v.>.>>..>..v.v....>>.>.v.>v>.v>>v.v.v.>.>>v.....>>>>\n.....>>vv.>>..>vvvv>.vv.vv.>>...>..>>>v...vv.>>v.vv.>.>....vv.>v.>..vvv.>..>>..>>v..v>>>>.>>.>.v>>>....v>.v>.>.>..v.....v>>.v>......>.vv...\nv..v.>>.v.v..>v>.>v..v.v>.>v..v.>vv.v.>>..>....>vv.>v..>.>v>>v>.>v.......v>v.vv.v.>.>v.>..>.>....>..>v.....v..>.>.vv.vv..>v.v.>>...v......v\nv>v.....>..v..>.v..v.vv...>v>...v..>>.v..v>..v>..>.....v.....>...vv..>.vvvv.v>.vv>v....>..>>>.v.....v.>v.>.vv>.>.v.>>...vv.....v.>vv.....>>\n>>v>>.v.>v..v>.>...>>.v....>.......v>vv..>>vv.vv.v>>>>v.>>>...>..vv...vv>v>>v.v.>.>v.>v>..v>>.vv.>.v.>v......>>....>...>v......>v.v.v>v.>>.\nv.v.vv>.>.>...>>.>>>..>.vv..>.v.v>>.>>v.v.v..>...>vv..vvv..>.v.>>>.>>..v.v...>>.>.v.......v>>v>>....>..v..vv.vvv...vvvv.>v..v.v>>>....v.>>.\nv..>.>..>v.v.>.......>.v.>..vv.v>>.>v...v>...v>....v>...v.v.v......v>v..vv.>v.v......v>vv......v.>v>.v..v..>>vv>..v>..v...>.>v>vv.v>v...v..\n.>.........v......v..>.v.vv.v>...>>.vvv>>>vv>>.>v.v.v>..>>..v..vvv.v>>>.v.>.>v...>.v.>vv.vvv>>.>v.v.>..>vv>.>>v.....>..>..v>v>.>>>.>.v>>.>.\n..>>v.v.>v...v..vv.vv........v>>.>>v......>>..v.>>.>v..v....>..v...>....>v>..>>.v...>.v.v.>v...>v>.vvv>>.>v.v.v.>v>>.>.v>>vv.v.>.>.v.>>>v..\n>....v..>..v>...v....>vv..v>...>v>>.......v..>.v..vvv>....>>v>......v.....v>vv.v....>...>v>>>>..v>.>...v...v....v.....>.>.>vv...>v>.>.v>..>\n...>v..>>>v.v..>.v.>...vv.>>>>>v>..v..>..v....vv...>.>.v>.vvv...>>vv...v>v>..>.vv.>.vv>.....v..v>>vvvv>..vv.>v>v>...>>v.v>...>.v.v>>v>vvv..\n..>.vv.v.>.v>.>..v>.>.v.>>>>.>.v.>...vv..>>v>>v..vv>>...v..vv>vv>>..>v...>>...>>.>.>....v>>>.v.>..>v>.>>.>..>v.......v...v..vv>vv....v..vv.\nv.>.....>>.v>vvv>.......>vvvv.>v.>.>..>.vv..>..>..v...vvv>v>>>..>vv>v>...v>>.v..>.v..v..v>v.>...v.>..vv>..v.>>>..vv......>.>vv..>v........v\n>v.v..>>..>>v...vv..vv>.vv>vvv.>..v..v..>...>..v.....>>......v.vv.>.....>.>...>.>v.v.v>..v.v>>>>>>v.v.>>>vv..>.>.>v...>.>vv>.v.v.vv>...>..v\n..v..vv>v.>.>>vv...v..vvv..>>v>.>....>>.v.>>vv....v>>>v.....>>>.>v.v>>.>...v.v.>>vvv...>v.>.>v>...v>..v.>v.v...v.>v>>...>.>.>v.v.>v.vv...v.\n.>>>.vv..>v>>>.v..vv>..vv.>.>....v.vvv.>..>.v..vv.>v..v.vv......>.vv>>..v>v>>>v.>>>v>vv>>>.>v>..vv...v>.>>..>vv.v...>.....>>v>vv..>.....>..\n>>.>vvv.>.v.v>...v>>.vv..>....v>>.>.v..v...>v..vv.v>....>.v.>vvv.>>.....v..v>>v..>.>>>.>.v...vv>....>>...>...>......vvv.v......v....>...v..\n.>.v.........>v.>>v...v...>>.vv.v..>..vvv>v...>.v>vv..v>vv>..>.>.>.>.......>.....>..>..vv>.>>..>>..>..>..vv....v.v.>>.>>v.v..v..>....vv.>.>\nv...>>>.v>>.v....>..>>.vv.vv....>.>....v....v.v....>....>..>v.vvv>..v>>.vv>..v>...v....v.>v>.v...>>..>.v>..>>vvv.v..>v..v>..>>.>>v..vv>..v>\n")
(def t-input "v...>>.vv>\n.vv>>.vv..\n>>.>v>...v\n>>v>>.>.v.\nv>v.vv.v..\n>.>>..v...\n.vv..>.>v.\nv.v..>>v.v\n....v..v.>\n")

(defn parse-grid [input]
  (mapv vec (str/split-lines input)))

(def grid (parse-grid input))
(def t-grid (parse-grid t-input))

(defn rows [grid] (count grid))
(defn cols [grid] (count (first grid)))
(def dims (juxt rows cols))
(defn coords [grid]
  (for [r (range (rows grid)) c (range (cols grid))] [r c]))

(defn step-cucumbers [grid]
  (let [coords (coords grid)
        by-cucumber (group-by (partial get-in grid) coords)
        [rows cols] (dims grid)
        move (fn [grid [r c] [rd cd]]
               (let [r (mod (+ r rd) rows)
                     c (mod (+ c cd) cols)
                     coord [r c]]
                 (and (#{\.} (get-in grid coord))
                      coord)))
        move-cucumber (fn [ds read-grid write-grid coord]
                        (if-let [coord' (move read-grid coord ds)]
                          (-> write-grid
                              (assoc-in coord' (get-in write-grid coord))
                              (assoc-in coord \.))
                          write-grid))]
    (as-> grid grid
          (reduce (partial move-cucumber [0 1] grid) grid (by-cucumber \>))
          (reduce (partial move-cucumber [1 0] grid) grid (by-cucumber \v)))))

(defn simulate-cucumbers [grid]
  (iterate step-cucumbers grid))

(defn part-1 [grid]
  (->> (simulate-cucumbers grid)
       (partition 2 1)
       (take-while (fn [[a b]] (not= a b)))
       (count)
       (inc)))

(rcf/tests
  (part-1 t-grid) := 58
  (time (part-1 grid)) := 367)  ; => 1230.889333 ms
