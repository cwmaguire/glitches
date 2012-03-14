(ns glitches.canvas
  (:require [goog.dom :as dom]
            [glitches.coords :as coords]))

(defn data-seq
  "Uses raw JavaScript to convert a CanvasPixelArray to a vector; ignores alpha"
  [d] [(js* "~{}[0], ~{}[1], ~{}[2]" d d d)])

(defn px
  "Return a vector of RGB values in context ctx at point [x,y]"
  [ctx [x y]]
  (data-seq (.-data (.getImageData ctx x y 1 1))))

(defn draw-px
  "Draw a random color pixel on the canvas context ctx at [x,y]"
  [ctx [x y] color]
  (set! (.-fillStyle ctx) color)
  (.fillRect ctx x y 1 1))

(defn ctx
  "Get a context for 'canvas1'"
  []
  (let [ctx (.getContext (dom/getElement "canvas1") "2d")]
    ctx))

(defn pxs
  "Retrieve a map of coordinates to summed RGB values for surrounding pixels"
  [ctx x y]
  (let [coords (coords/around x y)
        pixels (map (partial px ctx) coords)]
    (zipmap
       coords
      (map (fn [d] (apply + d)) pixels))))