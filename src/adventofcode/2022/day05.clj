(ns adventofcode.2022.day05
  "Day 5"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(defn transpose [m]
  (apply mapv vector m))

(defn parse-input
  [input]
  (let [[stack-input moves-input] (string/split input #"\n\n")
        stacks (->> stack-input
                    (string/split-lines)
                    (drop-last 1)                           ; Drop numbers row
                    (map #(take-nth 4 (drop 1 %)))
                    (transpose)
                    (mapv (fn [stack]
                            (vec (filter #(not (= \space %)) stack)))))
        moves (->> moves-input
                   (string/split-lines)
                   (map #(re-seq #"\d+" %))
                   (map #(map parse-long %))
                   (map #(hash-map :crates (first %)
                                   :from (dec (second %))
                                   :to (dec (nth % 2)))))]
    [stacks moves]))

(with-test
  (defn part1
    [input]
    (let [[stacks moves] (parse-input input)]
      (loop [stacks stacks
             [{amount :crates
               from   :from
               to     :to} & moves] moves]
        (if (nil? amount)
          (string/join (map first stacks))
          (let [crates (vec (reverse (take amount (stacks from))))
                new-from (vec (drop amount (stacks from)))
                new-to (vec (concat crates (stacks to)))]
            (recur (assoc stacks
                     (int from) new-from
                     (int to) new-to)
                   moves))))))
  (is (= "CMZ" (part1 "    [D]    \n[N] [C]    \n[Z] [M] [P]\n 1   2   3 \n\nmove 1 from 2 to 1\nmove 3 from 1 to 3\nmove 2 from 2 to 1\nmove 1 from 1 to 2"))))

(with-test
  (defn part2
    [input]
    (let [[stacks moves] (parse-input input)]
      (loop [stacks stacks
             [{amount :crates
               from   :from
               to     :to} & moves] moves]
        (if (nil? amount)
          (string/join (map first stacks))
          (let [crates (vec (take amount (stacks from)))
                new-from (vec (drop amount (stacks from)))
                new-to (vec (concat crates (stacks to)))]
            (recur (assoc stacks
                     (int from) new-from
                     (int to) new-to)
                   moves))))))
  (is (= "MCD" (part2 "    [D]    \n[N] [C]    \n[Z] [M] [P]\n 1   2   3 \n\nmove 1 from 2 to 1\nmove 3 from 1 to 3\nmove 2 from 2 to 1\nmove 1 from 1 to 2"))))

(defn -main []
  (let [input (slurp "resources/2022/day05.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

