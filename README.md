# 🎄 Advent of Code - Clojure

My Clojure solutions for the annual [Advent of Code][aoc]. Currently, only solutions for 2020, although I may backfill my (partial) 2019 solutions.

## 2020

### Goals
In order:
1. 100% the AOC in a timely manner
2. Take minimal Advent-specific help from [r/adventofcode][r-aoc] or the Clojurians advent [channel][clojurians-aoc]
3. Write reasonably idiomatic Clojure code, of reasonable quality

### Postmortem
I finished the AOC on December 30th! It was a tremendously fun ride, and very satisfying to complete.

I took minimal help from the internet:
* I took help from the Clojurians channel -- from [Aleksandr Zhuravlёv][zelark] in particular -- on day 23, and was grateful to learn about [deftype](https://clojuredocs.org/clojure.core/deftype) and how it enables one to define mutable Java types.
* I learned about [Instaparse](https://github.com/Engelberg/instaparse) from the Slack channel, and used it to redo my day 19 solution.
* I learned about `*warn-on-reflection*`, `*unchecked-math*` from Clojurians, and applied where appropriate

I reviewed Clojurian solutions after my own completion; occasionally I applied something I saw to clean up my own code, but for the most part my solutions are unchanged from their original form.

In order to finish on time and leave room for my day job and holiday fun, I sacrificed somewhat on code quality, but not so much that I'm embarrassed to publicize the repo.

In the beginning I played around with abstracting out some boilerplate using a function called `day`; while it helped me move fast in the beginning, it proved to hinder me as the solutions got more verbose.

#### Future goals and ideas
* Create utility functions for repetitive tasks, namely input parsing, working with grids/coordinates
* If optimizing on time-to-star, search for and make use of libraries, clojure.walk, etc.
* Time all solutions, and play with optimizations

[aoc]: https://adventofcode.com/
[r-aoc]: https://www.reddit.com/r/adventofcode/
[clojurians-aoc]: https://clojurians-log.clojureverse.org/adventofcode
[zelark]: https://github.com/zelark/AoC-2020/blob/master/src/zelark/aoc_2020/day_23.clj