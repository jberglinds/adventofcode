(ns adventofcode.2021.day04
  "Day 4"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(defn transpose [m]
  (apply mapv vector m))

(defn parse-board
  [input]
  (as-> input $
        (string/split $ #"\n\s*")
        (map #(string/split % #"\s+") $)
        (map (fn [x] (map #(Integer/parseInt %) x)) $)
        (concat $ (map seq (transpose $)))))

(defn parse-input
  [input]
  (let [[numbers-input & board-inputs] (string/split input #"\n\n\s*")]
    {:numbers (->> (string/split numbers-input #",")
                   (map #(Integer/parseInt %)))
     :boards  (map parse-board board-inputs)}))

(defn bingo?
  [board]
  (some #(empty? %) board))

(defn calculate-score
  [board]
  (reduce + (set (flatten board))))

(defn play-board
  ([numbers board] (play-board numbers board 0))
  ([numbers board round]
   (let [number (nth numbers round)
         updated-board (map (fn [row] (remove #(= number %) row)) board)]
     (if (bingo? updated-board)
       {:round-won  round
        :number-won number
        :score      (calculate-score updated-board)}
       (play-board numbers updated-board (inc round))))))

(with-test
  (defn part1
    [input]
    (let [{numbers :numbers
           boards  :boards} (parse-input input)]
      (as-> boards $
            (map #(play-board numbers %) $)
            (sort-by :round-won $)
            (first $)
            (* (:number-won $) (:score $)))))
  (is (= 4512 (part1 "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1\n\n22 13 17 11  0\n 8  2 23  4 24\n21  9 14 16  7\n 6 10  3 18  5\n 1 12 20 15 19\n\n 3 15  0  2 22\n 9 18 13 17  5\n19  8  7 25 23\n20 11 10 24  4\n14 21 16 12  6\n\n14 21 17 24  4\n10 16 15  9 19\n18  8 23 26 20\n22 11 13  6  5\n 2  0 12  3  7"))))

(with-test
  (defn part2
    [input]
    (let [{numbers :numbers
           boards  :boards} (parse-input input)]
      (as-> boards $
            (map #(play-board numbers %) $)
            (sort-by :round-won $)
            (last $)
            (* (:number-won $) (:score $)))))
  (is (= 1924 (part2 "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1\n\n22 13 17 11  0\n 8  2 23  4 24\n21  9 14 16  7\n 6 10  3 18  5\n 1 12 20 15 19\n\n 3 15  0  2 22\n 9 18 13 17  5\n19  8  7 25 23\n20 11 10 24  4\n14 21 16 12  6\n\n14 21 17 24  4\n10 16 15  9 19\n18  8 23 26 20\n22 11 13  6  5\n 2  0 12  3  7"))))

(defn -main []
  (let [input (slurp "resources/2021/day04.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

