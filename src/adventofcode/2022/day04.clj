(ns adventofcode.2022.day04
  "Day 4"
  (:use [clojure.test]
        [clojure.set])
  (:require [clojure.string :as string]))

(with-test
  (defn realize-range
    "From a range string, create the range as an array"
    [str]
    (let [[low high] (map #(Integer/parseInt %) (string/split str #"-"))]
      (into [] (range low (inc high)))))
  (is (= [2 3 4 5] (realize-range "2-5"))))

(defn parse-line
  [line]
  (as-> line $
       (string/split $ #",")
       (map realize-range $)))

(defn fully-contains?
  [range1 range2]
  (let [range1 (set range1)
        range2 (set range2)]
    (or (clojure.set/subset? range1 range2)
        (clojure.set/subset? range2 range1))))

(with-test
  (defn part1
    [input]
    (->> input
      (string/split-lines)
      (map parse-line)
      (map #(apply fully-contains? %))
      (filter true?)
      (count)))
  (is (= 2 (part1 "2-4,6-8\n2-3,4-5\n5-7,7-9\n2-8,3-7\n6-6,4-6\n2-6,4-8"))))

(defn overlaps?
  [range1 range2]
  (let [range1 (set range1)
        range2 (set range2)]
    (not (empty? (clojure.set/intersection range1 range2)))))

(with-test
  (defn part2
    [input]
    (->> input
         (string/split-lines)
         (map parse-line)
         (map #(apply overlaps? %))
         (filter true?)
         (count)))
  (is (= 4 (part2 "2-4,6-8\n2-3,4-5\n5-7,7-9\n2-8,3-7\n6-6,4-6\n2-6,4-8"))))

(defn -main []
  (let [input (slurp "resources/2022/day04.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

