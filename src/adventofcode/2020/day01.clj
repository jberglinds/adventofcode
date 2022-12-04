(ns adventofcode.2020.day01
  "Day 1"
  (:use [clojure.test])
  (:require [clojure.string :as string]
            [clojure.math.combinatorics :as combo]))

(with-test
  (defn part1
    ([input] (part1 input 2))
    ([input k]
     (as-> input $
           (string/split-lines $)
           (map #(Integer/parseInt %) $)
           (combo/combinations $ k)
           (filter #(= 2020 (reduce + %)) $)
           (first $)
           (reduce * $))))
  (is (= (* 2000 20) (part1 "2000\n20"))))

(with-test
  (defn part2
    [input]
    (part1 input 3))
  (is (= (* 2000 19 1) (part2 "2000\n19\n1"))))

(defn -main []
  (let [input (slurp "resources/2020/day01.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

