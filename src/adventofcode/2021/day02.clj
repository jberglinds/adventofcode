(ns adventofcode.2021.day02
  "Day 2"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(defn parse-line
  [line]
  (as-> line $
        (re-find #"(forward|down|up) (\d+)" $)
        (rest $)
        (list (first $) (Integer/parseInt (last $)))))

(defn parse-input
  [input]
  (->> input
       (string/split-lines)
       (map parse-line)))

(with-test
  (defn part1
    [input]
    (as-> input $
          (parse-input $)
          (reduce (fn [current-position [direction value]]
                    (case direction
                      "forward" (update current-position :horizontal #(+ % value))
                      "down" (update current-position :depth #(+ % value))
                      "up" (update current-position :depth #(- % value))))
                  {:horizontal 0 :depth 0} $)
          (* (:horizontal $) (:depth $))))
  (is (= 150 (part1 "forward 5\ndown 5\nforward 8\nup 3\ndown 8\nforward 2"))))

(with-test
  (defn part2
    [input]
    (as-> input $
          (parse-input $)
          (reduce (fn [current-position [direction value]]
                    (case direction
                      "forward" (-> current-position
                                    (update :horizontal #(+ % value))
                                    (update :depth #(+ % (* (:aim current-position) value))))
                      "down" (update current-position :aim #(+ % value))
                      "up" (update current-position :aim #(- % value))))
                  {:horizontal 0 :depth 0 :aim 0} $)
          (* (:horizontal $) (:depth $))))
  (is (= 900 (part2 "forward 5\ndown 5\nforward 8\nup 3\ndown 8\nforward 2"))))

(defn -main []
  (let [input (slurp "resources/2021/day01.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

