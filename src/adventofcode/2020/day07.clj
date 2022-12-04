(ns adventofcode.2020.day07
  "Day 7"
  (:use [clojure.test])
  (:require [clojure.string :as string]
            [clojure.set :as set]))

(with-test
  (defn parse-rule
    [rule]
    (let [[description contents] (string/split rule #" bags contain ")
          contents (string/split contents #", ")
          contents (filter #(not (= "no other bags." %)) contents)]
      {description (reduce (fn [acc content]
                             (let [[_ amount description] (re-matches #"^(\d+) ([\s\w]+) bags?\.?$" content)]
                               (assoc acc description (Integer/parseInt amount))))
                           {} contents)}))
  (is (= {"light red" {"bright white" 1
                       "muted yellow" 2}}
         (parse-rule "light red bags contain 1 bright white bag, 2 muted yellow bags."))))

(defn find-contained
  [rules bag]
  (let [bags (or (keys (rules bag)) '())]
    (->> bags
         (map #(find-contained rules %))
         (apply set/union)
         (set/union (set bags)))))

(defn part1
  [input]
  (let [rules (->> input
                   (string/split-lines)
                   (map parse-rule)
                   (apply merge))]
    (->> (keys rules)
         (map #(find-contained rules %))
         (filter #(contains? % "shiny gold"))
         (count))))

(with-test
  (defn find-amount-contained
    [rules bag]
    (let [rule (rules bag)]
      (if (empty? rule)
        0
        (->> rule
             (map #(let [[bag amount] %]
                     (+ amount (* amount (find-amount-contained rules bag)))))
             (apply +)))))
  (is (= 2 (find-amount-contained {"lol" {}
                                   "test"     {"lol"  2}} "test"))))

(with-test
  (defn part2
    [input]
    (let [rules (->> input
                     (string/split-lines)
                     (map parse-rule)
                     (apply merge))]
      (find-amount-contained rules "shiny gold")))
  (is (= 126 (part2 "shiny gold bags contain 2 dark red bags.\ndark red bags contain 2 dark orange bags.\ndark orange bags contain 2 dark yellow bags.\ndark yellow bags contain 2 dark green bags.\ndark green bags contain 2 dark blue bags.\ndark blue bags contain 2 dark violet bags.\ndark violet bags contain no other bags.")))
  (is (= 32 (part2 "light red bags contain 1 bright white bag, 2 muted yellow bags.\ndark orange bags contain 3 bright white bags, 4 muted yellow bags.\nbright white bags contain 1 shiny gold bag.\nmuted yellow bags contain 2 shiny gold bags, 9 faded blue bags.\nshiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.\ndark olive bags contain 3 faded blue bags, 4 dotted black bags.\nvibrant plum bags contain 5 faded blue bags, 6 dotted black bags.\nfaded blue bags contain no other bags.\ndotted black bags contain no other bags."))))

(defn -main []
  (let [input (slurp "resources/2020/day07.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))
