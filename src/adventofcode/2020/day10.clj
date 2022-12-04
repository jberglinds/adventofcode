(ns adventofcode.2020.day10
  "Day 10"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(with-test
  (defn part1
    [input]
    (let [numbers (->> input
                       (string/split-lines)
                       (map #(Integer/parseInt %))
                       (cons 0)
                       (sort))
          differences (as-> numbers $
                            (map vector (conj (vec (drop 1 numbers)) (+ 3 (last numbers))) $)
                            (map #(apply - %) $))]
      (* (count (filter #(= 1 %) differences))
         (count (filter #(= 3 %) differences)))))
  (is (= 35 (part1 "16\n10\n15\n5\n1\n11\n7\n19\n6\n12\n4")))
  (is (= 220 (part1 "28\n33\n18\n42\n31\n14\n46\n20\n48\n47\n24\n23\n49\n45\n19\n38\n39\n11\n1\n32\n25\n35\n8\n17\n7\n9\n4\n2\n34\n10\n3"))))

(defn find-combinations
  ([numbers] (find-combinations numbers {0 1} 1))
  ([numbers combinations-up-to-number index]
   (if (>= index (count numbers))
     (combinations-up-to-number (last numbers))
     (let [number (nth numbers index)
           prev-3 (take-last 3 (take index numbers))
           value (->> prev-3
                      (filter #(>= 3 (- number %)))
                      (map #(combinations-up-to-number %))
                      (reduce +))]
       (find-combinations numbers (assoc combinations-up-to-number number value) (inc index))))))

(with-test
  (defn part2
    [input]
    (let [numbers (as-> input $
                        (string/split-lines $)
                        (map #(Integer/parseInt %) $)
                        (cons 0 $)                          ; add start
                        (cons (+ 3 (apply max $)) $)        ; add goal
                        (sort $))]
      (find-combinations numbers)))
  (is (= 8 (part2 "16\n10\n15\n5\n1\n11\n7\n19\n6\n12\n4")))
  (is (= 19208 (part2 "28\n33\n18\n42\n31\n14\n46\n20\n48\n47\n24\n23\n49\n45\n19\n38\n39\n11\n1\n32\n25\n35\n8\n17\n7\n9\n4\n2\n34\n10\n3"))))

(defn -main []
  (let [input (slurp "resources/2020/day10.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))
