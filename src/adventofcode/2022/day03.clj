(ns adventofcode.2022.day03
  "Day 3"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(defn find-duplicate
  [line]
  (let [size (count line)
        [compartment1 compartment2] (split-at (/ size 2) line)
        compartment2-set (set compartment2)]
    (loop [[item & rest] compartment1]
      (if (contains? compartment2-set item)
        item
        (recur rest)))))

(def letter->score
  (apply hash-map (flatten (map-indexed (fn [idx letter] [letter (inc idx)]) "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"))))

(with-test
  (defn part1
    [input]
    (->> input
         (string/split-lines)
         (map find-duplicate)
         (map letter->score)
         (reduce +)))
  (is (= 157 (part1 "vJrwpWtwJgWrhcsFMMfFFhFp\njqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\nPmmdzqPrVvPwwTWBwg\nwMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\nttgJtRGJQctTZtZT\nCrZsJsPPZsGzwwsLwLmpwMDw"))))

(defn find-badge
  [[elf1 elf2 elf3]]
  (loop [[letter & rest] elf1]
    (if (and (contains? (set elf2) letter)
             (contains? (set elf3) letter))
        letter
        (recur rest))))

(with-test
  (defn part2
    [input]
    (->> input
         (string/split-lines)
         (partition 3)
         (map find-badge)
         (map letter->score)
         (reduce +)))
  (is (= 70 (part2 "vJrwpWtwJgWrhcsFMMfFFhFp\njqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\nPmmdzqPrVvPwwTWBwg\nwMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\nttgJtRGJQctTZtZT\nCrZsJsPPZsGzwwsLwLmpwMDw"))))

(defn -main []
  (let [input (slurp "resources/2022/day03.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

