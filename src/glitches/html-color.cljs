(ns glitches.html-color
  (:require [glitches.random :as random]))

(def color-digits (concat (range 0 10) "ABCDEF"))

(defn to-hex-str
  "Convert a number to a two digit hexidecimal string"
  [x]
  (let [num-digits (count color-digits)
        x (max 0 (min 255 x))]
    (apply str (map #(nth color-digits %) [(/ x num-digits) (rem x num-digits)]))))

(defn rand-color-digit
  "Get a random hex value as a number or string; e.g. 1, 'A'"
  []
  (nth color-digits (random/rand-up-to (count color-digits))))

(defn rand-color
  "Create a random HTML hex colour"
  []
  ; force rand-color-digit to be called sequentially, not reused
  (apply str "#" (for [x (range 0 6)] (rand-color-digit))))

(defn rand-darker-color
  "Create a random HTML hex colour where red green and blue are all darker than they were originally"
  [rgb]
  (apply str "#" (map #(-> % random/rand-up-to to-hex-str) rgb)))
