(ns adventofcode.2021.day01
  "Day 1"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(defn count-increments
  "Given a sequence of numbers, counts the number of times the next number is an increment from the one before"
  [numbers]
  (->> numbers
       (partition 2 1)
       (filter #(< (first %) (second %)))
       (count)))

(with-test
  (defn part1
    [input]
    (->> input
         (string/split-lines)
         (map #(Integer/parseInt %))
         (count-increments)))
  (is (= 7 (part1 "199\n200\n208\n210\n200\n207\n240\n269\n260\n263"))))

(with-test
  (defn part2
    [input]
    (->> input
         (string/split-lines)
         (map #(Integer/parseInt %))
         (partition 3 1)
         (map #(apply + %))
         (count-increments)))
  (is (= 5 (part2 "199\n200\n208\n210\n200\n207\n240\n269\n260\n263"))))

(defn -main []
  (let [input (slurp "resources/2021/day01.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

