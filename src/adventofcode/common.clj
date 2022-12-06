(ns adventofcode.common
  "Commons"
  (:use [clojure.test])
  (:require [clojure.string :as string]))

(defn transpose [m]
  (apply mapv vector m))
