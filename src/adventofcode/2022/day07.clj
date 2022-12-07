(ns adventofcode.2022.day07
  "Day 7"
  (:use [clojure.test])
  (:require [clojure.string :as string]
            [clojure.core.match :as match]))

(def example-input
  "$ cd /\n$ ls\ndir a\n14848514 b.txt\n8504156 c.dat\ndir d\n$ cd a\n$ ls\ndir e\n29116 f\n2557 g\n62596 h.lst\n$ cd e\n$ ls\n584 i\n$ cd ..\n$ cd ..\n$ cd d\n$ ls\n4060174 j\n8033020 d.log\n5626152 d.ext\n7214296 k")

(with-test
  (defn calculate-dir-size
    [input]
    (loop [dir->size {}
           cwd []
           [line & lines] (string/split-lines input)]
      (if (nil? line)
        dir->size
        (match/match (string/split line #"\s")
          ["$" "cd" "/"] (recur dir->size ["/"] lines)
          ["$" "cd" ".."] (recur dir->size (pop cwd) lines)
          ["$" "cd" path] (recur dir->size (conj cwd (str (last cwd) path "/")) lines)
          ["$" "ls"] (recur dir->size cwd lines) ; do nothing
          ["dir" _] (recur dir->size cwd lines)  ; do nothing
          [size _] (recur (merge-with +
                                      dir->size
                                      (into {} (map #(vector % (parse-long size)) cwd)))
                          cwd lines)))))
  (is (= {"/"     48381165
          "/a/"   94853
          "/d/"   24933642
          "/a/e/" 584}
         (calculate-dir-size example-input))))

(with-test
  (defn part1
    [input]
    (->> (calculate-dir-size input)
         (vals)
         (filter #(>= 100000 %))
         (reduce +)))
  (is (= 95437 (part1 example-input))))

(with-test
  (defn part2
    [input]
    (let [dir->size (calculate-dir-size input)
          root-size (dir->size "/")
          to-delete (- 30000000 (- 70000000 root-size))]
      (->> (vals dir->size)
           (sort)
           (drop-while #(< % to-delete))
           (first))))
  (is (= 24933642 (part2 example-input))))

(defn -main []
  (let [input (slurp "resources/2022/day07.txt")]
    (println "Part 1: " (part1 input))
    (println "Part 2: " (part2 input))))

