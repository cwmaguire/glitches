(ns glitches.glitch
  (:require [goog.dom :as dom]
            [glitches.canvas :as canvas]
            [glitches.html-color :as html-color]
            [glitches.random :as random]))

(defn val-sort
  "Sort map elements by highest value"
  [m]
  (sort (fn [a b] (> (second a) (second b))) m))

(defn lightest
  "Return the key for the map with the highest value"
  [m]
  (ffirst (val-sort m)))

(defn next-px
  "lazy-sequence that calculates the lightest neighbouring pixel"
  [ctx [x y]]
  ((fn f [[x y]] (lazy-seq (cons (lightest (canvas/pxs ctx x y)) (f (lightest (canvas/pxs ctx x y)))))) [x y]))

(defn crawl
  "Draw a random color pixel at the next coord in sequence s, n times, with a 100ms pause"
  [ctx s n]
  (canvas/draw-px ctx (first s) (html-color/rand-color))
  (if (> n 0)
    (.setTimeout (dom/getWindow) (fn [] (crawl ctx (rest s) (- n 1))) 100)))

(defn go
  "Start a bunch of glitches glitching"
  ([]
    (go (canvas/ctx)))
  ([ctx]
    (go ctx (random/random-pairs 100 300)))
  ([ctx xys]
    (doseq [xy xys]
      (crawl ctx (next-px ctx xy) 400))))
