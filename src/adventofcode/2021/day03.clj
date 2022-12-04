(ns adventofcode.2021.day03
  "Day 3"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(with-test
  (defn parse-input
    [input]
    (->> input
         (string/split-lines)
         (map (fn [line]
                (as-> line $
                      (string/split $ #"")
                      (map #(Integer/parseInt %) $))))))
  (is (= '((1 0 1 0) (0 1 0 1)) (parse-input "1010\n0101"))))

(defn transpose [m]
  (apply mapv vector m))

(with-test
  (defn find-most-common-element
    [items]
    (->> items
         (frequencies)
         (sort-by (juxt val key))
         (last)
         (first)))
  (is (= 1 (find-most-common-element '(0 0 0 1 1 1 1))))
  (is (= 1 (find-most-common-element '(1 1 1 0 0 0))))) ; equally common

(with-test
  (defn find-least-common-element
    [items]
    (->> items
         (frequencies)
         (sort-by (juxt val key))
         (first)
         (first)))
  (is (= 0 (find-least-common-element '(0 0 0 1 1 1 1))))
  (is (= 0 (find-least-common-element '(1 1 1 0 0 0))))) ; equally common

(with-test
  (defn invert-binary-string
    [binary-string]
    (->> binary-string
         (seq)
         (map #(if (= \1 %)
                 \0
                 \1))
         (apply str)))
  (is (= "1010" (invert-binary-string "0101"))))

(with-test
  (defn part1
    [input]
    (let [binary (->> input
                      (parse-input)
                      (transpose)
                      (map #(find-most-common-element %))
                      (apply str))
          inverted (invert-binary-string binary)]
      (* (Integer/parseInt binary 2) (Integer/parseInt inverted 2))))
  (is (= 198 (part1 "00100\n11110\n10110\n10111\n10101\n01111\n00111\n11100\n10000\n11001\n00010\n01010"))))

(defn find-rating
  [rows bit-criteria-fn]
  (as-> rows $
        (loop [rows $
               bitIndex 0]
          (let [bitsForIndex (nth (transpose rows) bitIndex)
                bitCriteria (bit-criteria-fn bitsForIndex)
                filteredRows (filter #(= bitCriteria (nth % bitIndex)) rows)]
            (if (= (count filteredRows) 1)
              (first filteredRows)
              (recur filteredRows (inc bitIndex)))))
        (apply str $)
        (Integer/parseInt $ 2)))

(with-test
  (defn part2
    [input]
    (let [rows (parse-input input)
          o-rating (find-rating rows find-most-common-element)
          co2-rating (find-rating rows find-least-common-element)]
      (* o-rating co2-rating)))
  (is (= 230 (part2 "00100\n11110\n10110\n10111\n10101\n01111\n00111\n11100\n10000\n11001\n00010\n01010"))))

(defn -main []
  (let [input (slurp "resources/2021/day03.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))
