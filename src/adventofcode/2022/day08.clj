(ns adventofcode.2022.day08
  "Day 8"
  (:use [clojure.test])
  (:require
    [adventofcode.common :as common]
    [clojure.string :as string]
    [clojure.math.combinatorics :as combo]))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map (fn [line]
              (map #(parse-long (str %)) line)))))

(defn is-visible?
  [forest x y]
  (let [transposed-forest (common/transpose forest)
        tree-height (nth (nth forest y) x)
        tree-row (nth forest y)
        tree-col (nth transposed-forest x)
        lines (list
                (take x tree-row)
                (drop (inc x) tree-row)
                (take y tree-col)
                (drop (inc y) tree-col))]
    (some (fn [line]
            (every? #(> tree-height %) line)) lines)))

(defn scenic-score
  [forest x y]
  (let [transposed-forest (common/transpose forest)
        tree-height (nth (nth forest y) x)
        tree-row (nth forest y)
        tree-col (nth transposed-forest x)
        lines (list
                (reverse (take x tree-row))
                (drop (inc x) tree-row)
                (reverse (take y tree-col))
                (drop (inc y) tree-col))]
    (->> lines
         (map (fn [line]
                (loop [[x & xs] line
                       distance 0]
                  (cond
                    (nil? x) distance
                    (>= x tree-height) (inc distance)
                    :else (recur xs (inc distance))))))
         (apply *))))

(with-test
  (defn part1
    [input]
    (as-> (parse-input input) $
          (map-indexed (fn [x row] (map-indexed (fn [y _] (is-visible? $ x y)) row)) $)
          (flatten $)
          (filter identity $)
          (count $)))
  (is (= 21 (part1 "30373\n25512\n65332\n33549\n35390"))))

(with-test
  (defn part2
    [input]
    (as-> (parse-input input) $
          (map-indexed (fn [x row] (map-indexed (fn [y _] (scenic-score $ x y)) row)) $)
          (flatten $)
          (apply max $)))
  (is (= 8 (part2 "30373\n25512\n65332\n33549\n35390"))))

(defn -main []
  (let [input (slurp "resources/2022/day08.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

