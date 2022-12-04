(ns adventofcode.2022.day05
  "Day 5"
  (:use [clojure.test]
        [clojure.set])
  (:require [clojure.string :as string]))

(with-test
  (defn part1
    [input]
    input)
  (is (= nil (part1 ""))))

(defn -main []
  (let [input (slurp "resources/2022/day05.txt")]
    (println "Part 1: " (part1 input))))
    ;(println "Part 2: " (part2 input))))

