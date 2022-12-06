(ns adventofcode.2022.day06
  "Day 6"
  (:use [clojure.test]))

(defn solve
  [input packet-size]
  (->> (partition packet-size 1 input)
       (map set)
       (take-while #(not (= packet-size (count %))))
       (count)
       (+ packet-size)))

(with-test
  (defn part1
    [input]
    (solve input 4))
  (is (= 7 (part1 "mjqjpqmgbljsphdztnvjfqwrcgsmlb")))
  (is (= 5 (part1 "bvwbjplbgvbhsrlpgdmjqwftvncz")))
  (is (= 6 (part1 "nppdvjthqldpwncqszvftbrmjlhg")))
  (is (= 10 (part1 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")))
  (is (= 11 (part1 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))))

(defn part2
  [input]
  (solve input 14))

(defn -main []
  (let [input (slurp "resources/2022/day06.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

