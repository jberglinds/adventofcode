(ns adventofcode.2022.day01
  "Day 1"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(with-test
  (defn part1
    [input]
    (as-> input $
        (string/split $ #"\n\n")
        (map (fn [snack-list] (->> snack-list
                                   (string/split-lines)
                                   (map #(Integer/parseInt %))
                                   (reduce +)))
             $)
        (sort $)
        (last $)))
  (is (= 24000 (part1 "1000\n2000\n3000\n\n4000\n\n5000\n6000\n\n7000\n8000\n9000\n\n10000"))))

(with-test
  (defn part2
    [input]
    (as-> input $
          (string/split $ #"\n\n")
          (map (fn [snack-list] (->> snack-list
                                     (string/split-lines)
                                     (map #(Integer/parseInt %))
                                     (reduce +)))
               $)
          (sort $)
          (take-last 3 $)
          (reduce + $)))
  (is (= 45000 (part2 "1000\n2000\n3000\n\n4000\n\n5000\n6000\n\n7000\n8000\n9000\n\n10000"))))

(defn -main []
  (let [input (slurp "resources/2022/day01.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

