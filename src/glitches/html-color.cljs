(ns glitches.html-color
  (:require [glitches.random :as random]))

(def color-digits (concat (range 0 10) "ABCDEF"))

(defn rand-color-digit
  "Get a random hex value as a number or string; e.g. 1, 'A'"
  []
  (nth color-digits (random/rand-up-to (count color-digits))))

(defn rand-color
  "Create a random HTML hex colour"
  []
  ; force rand-color-digit to be called sequentially, not reused
  (apply str "#" (take 6 (for [x (range 0 6)] (rand-color-digit)))))
