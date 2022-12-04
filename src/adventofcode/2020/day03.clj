(ns adventofcode.2020.day03
  "Day 3"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(with-test
  (defn parse-map-row
    "Parses a map row into a seq where true=tree"
    [row]
    (->> row
         (char-array)
         (map #(if (= \# %) true false))))
  (is (= [true false false true false true true] (parse-map-row "#..#.##"))))

(defn parse-map
  [input]
  (->> input
       (string/split-lines)
       (map parse-map-row)
       (map cycle)))

(defn part1
  ([input] (part1 input 3 1))
  ([input dX dY]
   (let [tree-map (parse-map input)]
     (loop [x dX y dY trees 0]
       (if (>= y (count tree-map))
         trees
         (recur (+ x dX) (+ y dY) (if (nth (nth tree-map y) x) (inc trees) trees)))))))

(defn part2
  [input]
  (* (part1 input 1 1)
     (part1 input 3 1)
     (part1 input 5 1)
     (part1 input 7 1)
     (part1 input 1 2)))

(defn -main []
  (let [input (slurp "resources/2020/day03.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

