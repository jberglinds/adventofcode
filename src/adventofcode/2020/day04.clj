(ns adventofcode.2020.day04
  "Day 4"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(with-test
  (defn extract-value-from-string
    "Extracts the value for a key in the string"
    [key string]
    (nth (re-matches (re-pattern (str ".*" key ":([^\\s+]+).*")) string) 1 nil))
  (is (= "abc"
         (extract-value-from-string "byr" "byr:abc")))
  (is (= "#ffffff"
         (extract-value-from-string "hcl" "ecl:gry hcl:#ffffff"))))

(with-test
  (defn parse-row
    [row]
    (as-> row $
          (string/replace $ #"\n" " ")
          ; Extract the value for each key into map
          {:byr (extract-value-from-string "byr" $)
           :iyr (extract-value-from-string "iyr" $)
           :eyr (extract-value-from-string "eyr" $)
           :hgt (extract-value-from-string "hgt" $)
           :hcl (extract-value-from-string "hcl" $)
           :ecl (extract-value-from-string "ecl" $)
           :pid (extract-value-from-string "pid" $)}
          ; Remove keys without value
          (into {} (filter val $))))
  (is (= {:ecl "lol"}
         (parse-row "ecl:lol")))
  (is (= {:ecl "lol" :hcl "#ffffff"}
         (parse-row "hcl:#ffffff\necl:lol"))))

(defn parse-input
  [input]
  (as-> input $
        (string/split $ #"\n{2}")
        (map parse-row $)))

(defn valid-passport-1?
  "Returns true if the passport contains all "
  [passport]
  (every? passport '(:byr :iyr :eyr :hgt :hcl :ecl :pid)))

(defn part1
  [input]
  (->> input
       (parse-input)
       (filter valid-passport-1?)
       (count)))

(defn valid-passport-2?
  [passport]
  (and (valid-passport-1? passport)
       (let [byr (Integer/parseInt (passport :byr))]
         (and (<= 1920 byr)
              (>= 2002 byr)))
       (let [iyr (Integer/parseInt (passport :iyr))]
         (and (<= 2010 iyr)
              (>= 2020 iyr)))
       (let [eyr (Integer/parseInt (passport :eyr))]
         (and (<= 2020 eyr)
              (>= 2030 eyr)))
       (let [[_ height unit] (re-matches #"^(\d+)(cm|in)$" (passport :hgt))]
         (and height unit
              (as-> (Integer/parseInt height) height
                    (if (= unit "cm")
                      (and (<= 150 height)
                           (>= 193 height))
                      (and (<= 59 height)
                           (>= 76 height))))))
       (re-matches #"^#[0-9a-f]{6}$" (passport :hcl))
       (re-matches #"^(amb|blu|brn|gry|grn|hzl|oth)$" (passport :ecl))
       (re-matches #"^\d{9}$" (passport :pid))))

(defn part2
  [input]
  (->> input
       (parse-input)
       (filter valid-passport-2?)
       (count)))

(defn -main []
  (let [input (slurp "resources/2020/day04.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

