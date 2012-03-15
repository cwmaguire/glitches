(ns glitches.canvas
  (:require [goog.dom :as dom]
            [glitches.coords :as coords]))

(defn px-array-to-vec
  "Uses raw JavaScript to convert a CanvasPixelArray to a vector; ignores alpha"
  [d] [(js* "~{}[0], ~{}[1], ~{}[2], ~{}[3]" d d d d)])

(defn rgba
  "Return a vector of RGB values in context ctx at point [x,y]"
  [ctx [x y]]
  (px-array-to-vec (.-data (.getImageData ctx x y 1 1))))

(defn pxs
  "Retrieve a map of coordinates to RGB values for surrounding pixels"
  [ctx x y]
  (let [coords (coords/around x y)
        clrs (map (partial rgba ctx) coords)]
    ; output all coords with their colors
    ;(js/alert (apply str "colors: " (interleave (map (fn [c] (str "[" (first c) "," (second c) "],\n")) coords) (map (fn [c] (str "[" (first c) "," (second c) "," (nth c 2) "],\n")) clrs))))
    (map hash-map (repeat :coords) coords (repeat :colors) clrs)))

(defn draw-px
  "Draw a random color pixel on the canvas context ctx at [x,y]"
  [ctx {[x y] :coords} color]
  (set! (.-fillStyle ctx) color)
  (.fillRect ctx x y 1 1))

(defn lower-alpha
  "Draw a random color pixel on the canvas context ctx at [x,y]"
  [ctx {[x y] :coords [r g b a] :colors} d]
  (let [img-data (.createImageData ctx 1 1)]
    (js* "~{}.data[0] = ~{}; ~{}.data[1] = ~{}; ~{}.data[2] = ~{}; ~{}.data[3] = ~{};" img-data r img-data g img-data b img-data (max 0 (- a d)))
    (.putImageData ctx img-data x y)))

(defn get-ctx
  "Get a context for a canvas; uses 'canvas1' if none specified."
  [canv]
    (.getContext canv "2d"))

(defn white
  "Paints a canvas completely white"
  [canv]
  (let [w (.-width canv)
        h (.-height canv)
        ctx (get-ctx canv)]
    (set! (.-fillStyle ctx) "white")
    (.fillRect ctx 0 0 w h)))