(ns adventofcode.2020.day06
  "Day 6"
  (:use [clojure.test])
  (:require [clojure.string :as string]
            [clojure.set :as set]))

(with-test
  (defn group-answers-any
    [group-input]
    (->> group-input
        (string/split-lines)
        (map set)
        (apply set/union)))
  (is (= #{\a \b \c \x \y \z} (group-answers-any "abcx\nabcy\nabcz"))))

(defn part1
  [input]
  (->> (string/split input #"\n\n")
       (map group-answers-any)
       (map count)
       (reduce +)))

(with-test
  (defn group-answers-every
    [group-input]
    (->> group-input
         (string/split-lines)
         (map set)
         (apply set/intersection)))
  (is (= #{\a \b \c} (group-answers-every "abcx\nabcy\nabcz"))))

(defn part2
  [input]
  (->> (string/split input #"\n\n")
       (map group-answers-every)
       (map count)
       (reduce +)))

(defn -main []
  (let [input (slurp "resources/2020/day06.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))
