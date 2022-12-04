(ns adventofcode.2020.day05
  "Day 5"
  (:use [clojure.test])
  (:require [clojure.string :as string]
            [clojure.set :as set]))


(with-test
  (defn decode-seat-row
    [code]
    (as-> code $
          (char-array $)
          ; Convert to binary
          (map #(if (= \F %) 0 1) $)
          (apply str $)
          ; Parse binary string to decimal
          (Integer/parseInt $ 2)))
  (is (= 70 (decode-seat-row "BFFFBBF")))
  (is (= 14 (decode-seat-row "FFFBBBF")))
  (is (= 102 (decode-seat-row "BBFFBBF"))))

(with-test
  (defn decode-seat-column
    [code]
    (as-> code $
          (char-array $)
          ; Convert to binary
          (map #(if (= \L %) 0 1) $)
          (apply str $)
          ; Parse binary string to decimal
          (Integer/parseInt $ 2)))
  (is (= 7 (decode-seat-column "RRR")))
  (is (= 4 (decode-seat-column "RLL"))))

(with-test
  (defn decode-seat
    [code]
    (+ (* 8 (decode-seat-row (subs code 0 7)))
       (decode-seat-column (subs code 7))))
  (is (= 567 (decode-seat "BFFFBBFRRR")))
  (is (= 119 (decode-seat "FFFBBBFRRR")))
  (is (= 820 (decode-seat "BBFFBBFRLL"))))

(defn part1
  [input]
  (->> input
       (string/split-lines)
       (map decode-seat)
       (apply max)))

(defn part2
  [input]
  (let [lastSeatNo (part1 input)]
    (->> input
         (string/split-lines)
         (map decode-seat)
         (set)
         (set/difference (set (range lastSeatNo)))
         (seq)
         (sort)
         (last))))

(defn -main []
  (let [input (slurp "resources/2020/day05.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

