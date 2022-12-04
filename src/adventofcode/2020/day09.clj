(ns adventofcode.2020.day09
  "Day 9"
  (:use [clojure.test])
  (:require [clojure.string :as string]
            [clojure.math.combinatorics :as combo]))

(with-test
  (defn is-valid?
    [prev-numbers number]
    (as-> prev-numbers $
          (combo/combinations $ 2)
          (filter #(= number (reduce + %)) $)
          (not (empty? $))))
  (is (is-valid? [1 2] 3))
  (is (not (is-valid? [0 1 2 5 9] 4))))

(with-test
  (defn find-first-invalid
    ([preamble-size numbers]
     (loop [idx preamble-size]
       (let [number (nth numbers idx)]
         (if (is-valid? (take-last preamble-size (take idx numbers)) number)
           (recur (inc idx))
           number)))))
  (is (= 99 (find-first-invalid 4 [1 1 2 3 4 5 99]))))

(with-test
  (defn part1
    ([input] (part1 input 25))
    ([input preamble-size]
     (->> input
          (string/split-lines)
          (map #(Long/parseLong %))
          (find-first-invalid preamble-size))))
  (is (= 127 (part1 "35\n20\n15\n25\n47\n40\n62\n55\n65\n95\n102\n117\n150\n182\n127\n219\n299\n277\n309\n576" 5))))

(with-test
  (defn part2
    ([input] (part2 input 25))
    ([input preamble-size]
     (let [step1 (part1 input preamble-size)
           numbers (->> input
                        (string/split-lines)
                        (map #(Long/parseLong %)))]
       (as-> numbers $
             (map #(partition % 1 numbers) (range 2 (count numbers))) ; generate all possible ranges
             (apply concat $)
             (filter #(= step1 (reduce + %)) $)             ; filter out the ones that don't sum to step1 value
             (first $)
             (+ (apply max $) (apply min $))))))
  (is (= 62 (part2 "35\n20\n15\n25\n47\n40\n62\n55\n65\n95\n102\n117\n150\n182\n127\n219\n299\n277\n309\n576" 5))))

(defn -main []
  (let [input (slurp "resources/2020/day09.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))
