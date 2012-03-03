(ns glitches.glitch
  (:require [goog.dom :as dom]))

(def color-digits (concat (range 0 10) "ABCDEF"))

(defn data-seq
  "Uses raw JavaScript to convert a CanvasPixelArray to a vector; ignores alpha"
  [d] [(js* "~{}[0], ~{}[1], ~{}[2]" d d d)])

(defn px
  "Return a vector of RGB values in context ctx at point [x,y]"
  [ctx [x y]]
  (data-seq (.-data (.getImageData ctx x y 1 1))))

(defn coords-around
  "get the positive coords around x,y that aren't x,y"
  [x y]
  (filter (fn [[a b]] (and
                        (not (neg? a))
                        (not (neg? b))
                        (not (= [a b] [x y]))))
    (for [y0 (range (- y 1) (+ y 2))
          x0 (range (- x 1) (+ x 2))]
          [x0 y0])))

(defn coords-pxs
  "Return the RGB pixel values for coords"
  [coords]
  (map (partial px ctx) coords))

(defn pxs
  "Retrieve a map of coordinates to summed RGB values for surrounding pixels"
  [ctx x y]
  (let [coords (coords-around x y)
        pixels (map (partial px ctx) coords)]
    (zipmap
       coords
      (map (fn [d] (apply + d)) pixels))))

(defn val-sort
  "Sort map elements by highest value"
  [m]
  (sort (fn [a b] (> (second a) (second b))) m))

(defn lightest
  "Return the key for the map with the highest value"
  [m]
  (ffirst (val-sort m)))

(defn ctx
  "Get a context for 'canvas1' and draw it all white."
  []
  (let [ctx (.getContext (dom/getElement "canvas1") "2d")]
    (set! (.-fillStyle ctx) "white")
    (.fillRect ctx 0 0 300 300)
    ctx))

(defn rand-color-digit
  "Get a random hex value as a number or string; e.g. 1, 'A'"
  []
  (nth color-digits (.floor js/Math (* 15 (.random js/Math)))))

(defn rand-color
  "Create a random HTML hex colour"
  []
  ; force rand-color-digit to be called sequentially, not reused
  (apply str "#" (take 6 (for [x (range 0 6)] (rand-color-digit)))))

(defn random-px
  "Draw a random color pixel on the canvas context ctx at [x,y]"
  [ctx [x y]]
  (set! (.-fillStyle ctx) (rand-color))
  (.fillRect ctx x y 1 1))

(defn random
  "Get a random number between zero and max"
  [max]
  (.floor js/Math (* max (.random js/Math))))

(defn random-pairs
  [n max]
  (for [x (range 0 (+ n 1))]
    [(random max) (random max)]))

(defn go
  "Start a bunch of glitches glitching"
  ([]
    (go (ctx)))
  ([ctx]
    (go ctx (random-pairs 100 300)))
  ([ctx xys]
    (doseq [xy xys]
      (crawl ctx (next-px ctx xy) 400))))

(defn crawl
  "Draw a random color pixel at the next coord in sequence s, n times, with a 100ms pause"
  [ctx s n]
  (random-px ctx (first s))
  (if (> n 0)
    (.setTimeout (dom/getWindow) (fn [] (crawl ctx (rest s) (- n 1))) 100)))

(defn next-px
  [ctx [x y]]
  ((fn f [[x y]] (lazy-seq (cons (lightest (pxs ctx x y)) (f (lightest (pxs ctx x y)))))) [x y]))
