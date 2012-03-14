(ns glitches.random)

(defn rand-up-to
  "Get a random number between zero and max"
  [max]
  (.floor js/Math (* max (.random js/Math))))

(defn random-pairs
  [n max]
  (for [x (range 0 (+ n 1))]
    [(rand-up-to max) (rand-up-to max)]))
