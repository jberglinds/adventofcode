(ns adventofcode.2020.day02
  "Day 2"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(with-test
  (defn parse-entry [input]
    (let [matches (re-matches #"^(\d+)-(\d+) (\w): (\w+)$" input)]
      {:min      (Integer/parseInt (nth matches 1)),
       :max      (Integer/parseInt (nth matches 2)),
       :char     (first (char-array (nth matches 3))),
       :password (nth matches 4)}))
  (is (= {:min 1, :max 2, :char \a, :password "abcde"} (parse-entry "1-2 a: abcde"))))

(with-test
  (defn is-valid-entry-1?
    [{password :password
      minCount :min
      maxCount :max
      char     :char}]
    (let [count (->> password
                     (char-array)
                     (filter #(= char %))
                     (count))]
      (and (<= minCount count) (>= maxCount count))))
  (is (is-valid-entry-1? (parse-entry "1-2 a: baba")))
  (is (not (is-valid-entry-1? (parse-entry "1-1 b: aaaa")))))

(with-test
  (defn part1
    [input]
    (->> input
         (string/split-lines)
         (map #(parse-entry %))
         (filter #(is-valid-entry-1? %))
         (count))))

(with-test
  (defn is-valid-entry-2?
    [{password :password
      idx1     :min
      idx2     :max
      char     :char}]
    (->> [(nth password (- idx1 1)) (nth password (- idx2 1))]
         (filter #(= char %))
         (count)
         (= 1)))
  (is (is-valid-entry-2? (parse-entry "1-3 a: abc")))
  (is (not (is-valid-entry-2? (parse-entry "1-3 a: aba")))))

(with-test
  (defn part2
    [input]
    (->> input
         (string/split-lines)
         (map #(parse-entry %))
         (filter #(is-valid-entry-2? %))
         (count))))

(defn -main []
  (let [input (slurp "resources/2020/day01.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

