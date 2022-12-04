(ns adventofcode.2020.day08
  "Day 8"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(with-test
  (defn parse-instruction
    [input]
    (let [[_ operation value] (re-matches #"^(acc|nop|jmp) ([+-]\d+)$" input)]
      [operation (Integer/parseInt value)]))
  (is (= ["acc" 5] (parse-instruction "acc +5")))
  (is (= ["nop" 0] (parse-instruction "nop +0")))
  (is (= ["jmp" -3] (parse-instruction "jmp -3"))))

(defn run-program
  ([instructions] (run-program instructions 0 0 []))
  ([instructions position acc visited]
   (cond
     (contains? (set visited) position) (throw (ex-info "Infinite loop" {:acc acc}))
     (>= position (count instructions)) acc
     :else (let [[operation value] (nth instructions position)]
             (cond
               (= operation "acc") (run-program instructions (inc position) (+ acc value) (conj visited position))
               (= operation "nop") (run-program instructions (inc position) acc (conj visited position))
               (= operation "jmp") (run-program instructions (+ position value) acc (conj visited position)))))))

(with-test
  (defn part1
    [input]
    (as-> input $
          (string/split-lines $)
          (map parse-instruction $)
          (try (run-program $)
               (catch Exception e ((ex-data e) :acc)))))
  (is (= 5 (part1 "nop +0\nacc +1\njmp +4\nacc +3\njmp -3\nacc -99\nacc +1\njmp -4\nacc +6"))))

(defn generate-mutations
  "From a program, generates a list of mutated programs"
  [program]
  (reduce (fn [acc [index [operation value]]]
            (cond
              (= "jmp" operation) (conj acc (assoc program index ["nop" value]))
              (= "nop" operation) (conj acc (assoc program index ["jmp" value]))
              :else acc))
          [program] (map-indexed vector program)))

(defn run-programs
  "Runs through a list of programs, stopping if a program returns"
  [[program & rest]]
  (try (run-program program)
       (catch Exception _ (run-programs rest))))

(with-test
  (defn part2
    [input]
    (as-> input $
          (string/split-lines $)
          (map parse-instruction $)
          (generate-mutations (vec $))
          (run-programs $)))
  (is (= 8 (part2 "nop +0\nacc +1\njmp +4\nacc +3\njmp -3\nacc -99\nacc +1\njmp -4\nacc +6"))))

(defn -main []
  (let [input (slurp "resources/2020/day08.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))
