(ns adventofcode.2020.day11
  "Day 11"
  (:use [clojure.test])
  (:require [clojure.string :as string]
            [clojure.math.combinatorics :as combo]))

(defn parse-input
  [input]
  (->> input
       (string/split-lines)
       (map char-array)
       (map vec)
       (vec)))

(defn get-adjacent-positions
  [seats [y x]]
  (let [maxX (count (first seats))
        maxY (count seats)
        rangeX (->> (range (dec x) (+ 2 x))
                    (filter #(and (<= 0 %) (> maxX %))))
        rangeY (->> (range (dec y) (+ 2 y))
                    (filter #(and (<= 0 %) (> maxY %))))]
    (->> (combo/cartesian-product rangeY rangeX)
         (filter #(not (= [y x] %))))))


(defn occupied?
  [seats position]
  (= \# (get-in seats position)))

(defn count-adjacent-occupied
  [seats position]
  (->> (get-adjacent-positions seats position)
       (filter #(occupied? seats %))
       (count)))

(defn step
  [seats]
  (for [y (range (count seats))]
    (for [x (range (count (nth seats y)))
          :let [ch (get-in seats [y x])]]
      (cond
        (= \L ch) (if (= 0 (count-adjacent-occupied seats [y x])) \# \L)
        (= \# ch) (if (<= 4 (count-adjacent-occupied seats [y x])) \L \#)
        :else ch))))

(defn simulate
  ([seats] (simulate [] seats))
  ([prev-seats seats]
   (if (= prev-seats seats)
     seats
     (simulate seats (step (vec (map vec seats)))))))

(with-test
  (defn part1
    [input]
    (->> input
         (parse-input)
         (simulate)
         (flatten)
         (filter #(= \# %))
         (count)))
  (is (= 37 (part1 "L.LL.LL.LL\nLLLLLLL.LL\nL.L.L..L..\nLLLL.LL.LL\nL.LL.LL.LL\nL.LLLLL.LL\n..L.L.....\nLLLLLLLLLL\nL.LLLLLL.L\nL.LLLLL.LL"))))

(defn -main []
  (let [input (slurp "resources/2020/day11.txt")]
    (println "Part 1: " (part1 input))))
;(println "Part 2: " (part2 input))))
