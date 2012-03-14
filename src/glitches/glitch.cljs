(ns glitches.glitch
  (:require [goog.dom :as dom]
            [glitches.canvas :as canvas]
            [glitches.html-color :as html-color]
            [glitches.random :as random]))

(defn brightest-origin
  "return the key for the map element with the highest summed value or lowest-summed key if value sums are equal"
  ([m]
    (ffirst (sort brightest-origin m)))
  ([me1 me2]
    (let [clrs1 (second me1)
          clrs2 (second me2)
          brightness1 (apply + clrs1)
          brightness2 (apply + clrs2)]
      (if (not (= brightness1 brightness2))
        (> brightness1 brightness2)
        (< (apply + (first me1)) (apply + (first me2)))))))

(defn next-px
  "lazy-sequence that calculates the lightest neighbouring pixel"
  [ctx [x y]]
  ((fn f [[x1 y1]] (lazy-seq (cons (brightest-origin (canvas/pxs ctx x1 y1)) (f (brightest-origin (canvas/pxs ctx x1 y1)))))) [x y]))

(defn crawl
  "Draw a random color pixel at the next coord in sequence s, n times, with a 100ms pause"
  [ctx s n]
  (canvas/draw-px ctx (first s) (html-color/rand-darker-color (canvas/rgb ctx (first s))))
  (if (> n 0)
    (.setTimeout (dom/getWindow) (fn [] (crawl ctx (rest s) (- n 1))) 100)))

(defn go
  "Start a bunch of glitches glitching"
  ([]
    (go (dom/getElement "canvas1")))
  ([canv]
    (go canv (random/random-pairs 50 300)))
  ([canv xys]
    (canvas/white canv)
    (let [ctx (canvas/get-ctx canv)]
      (doseq [xy xys]
        (crawl ctx (next-px ctx xy) 400)))))
