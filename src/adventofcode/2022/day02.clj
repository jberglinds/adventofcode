(ns adventofcode.2022.day02
  "Day 2"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(def input->shape
  {"A" :rock "B" :paper "C" :scissors
   "X" :rock "Y" :paper "Z" :scissors})
(def shape->score
  {:rock 1
   :paper 2
   :scissors 3})
(def outcome->score
  {:win  6
   :tie  3
   :lose 0})

(defn parse-round
  [line]
  (as-> line $
        (string/split $ #"\s")
        (map input->shape $)))

(defn get-outcome
  [[s1 s2]]
  (cond
    (= s1 s2) :tie
    (and (= s1 :rock)
         (= s2 :paper)) :win
    (and (= s1 :paper)
         (= s2 :scissors)) :win
    (and (= s1 :scissors)
         (= s2 :rock)) :win
    :else :lose))

(defn get-score
  [[s1 s2 :as round]]
  (+ (outcome->score (get-outcome round))
     (shape->score s2)))

(with-test
  (defn part1
    [input]
    (->> input
        (string/split-lines)
        (map parse-round)
        (map get-score)
        (reduce +)))
  (is (= 15 (part1 "A Y\nB X\nC Z"))))

(def input->outcome
  {"X" :lose
   "Y" :tie
   "Z" :win})
(def shape->winning-shape
  {:rock :paper
   :paper :scissors
   :scissors :rock})
(def shape->losing-shape
  {:rock :scissors
   :paper :rock
   :scissors :paper})

(defn parse-round-2
  [line]
  (let [[first second] (string/split line #"\s")]
    [(input->shape first) (input->outcome second)]))

(defn select-shape
  [[ s1 outcome]]
  (cond
    (= outcome :tie) s1
    (= outcome :win) (shape->winning-shape s1)
    (= outcome :lose) (shape->losing-shape s1)))

(with-test
  (defn part2
    [input]
    (->> input
         (string/split-lines)
         (map parse-round-2)
         (map #(assoc % 1 (select-shape %)))
         (map get-score)
         (reduce +)))
  (is (= 12 (part2 "A Y\nB X\nC Z"))))

(defn -main []
  (let [input (slurp "resources/2022/day02.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

