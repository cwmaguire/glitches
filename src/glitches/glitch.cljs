(ns glitches.glitch
  (:require [goog.dom :as dom]
            [glitches.canvas :as canvas]
            [glitches.html-color :as html-color]
            [glitches.random :as random]
            [glitches.pixel :as pixel]))

(defn multi-compare
  "Compare two elements using a list of functions; return true/false for first non-zero compare value"
  [fns a b]
  (if (seq fns)
    (let [f (first fns)
          result (compare (f a) (f b))]

      ;(js/alert (str "Comparing: " (pixel/to-string a) ", " (pixel/to-string b) ", result = " result "\n\nfn = \n" f))

      (cond
        (> result 0) true
        (< result 0) false
        :default (recur (rest fns) a b)))
    true))

(defn next-px
  "lazy-sequence that calculates the lightest neighbouring pixel"
  [ctx px fns]
  ; create an anony function so that we don't hold on to px through a gazzilion iterations
  ((fn f
     [{[x y] :coords :as px1}]

     (let [surrounding-pxs (canvas/pxs ctx x y)
           px-next (first (sort (partial multi-compare fns) surrounding-pxs))] ; I think pre-calc'ing px-next is going to cause us to hold on to each px we find

       (lazy-seq (cons px-next (f px-next))))) px))

(defn lower-alpha
  [ctx px]
  (canvas/lower-alpha ctx px 40))

(defn darken
  [ctx {[x y] :coords rgba :colors :as px}]
  (let [clr-str (html-color/rand-darker-color rgba)]
    (canvas/draw-px ctx px clr-str)))

(defn crawl
  "Draw a random color pixel at the next coord in sequence s, n times, with a 100ms pause"
  [ctx s f n]
  (f ctx (first s))
  (if (> n 0) (.setTimeout (dom/getWindow) (fn [] (crawl ctx (rest s) f (- n 1))) 100)))

(defn white [canv] (canvas/white canv))

(defn load-img
  [canv img-id]
  (let [ctx (.getContext canv "2d")
        img (dom/getElement img-id)
        w (.-width canv)
        h (.-height canv)]
    (.drawImage ctx img 0 0 w h)))

(defn go
  "Start a bunch of glitches glitching"
  ([]
    (go (dom/getElement "canvas1")))
  ([canv]
    (go canv (random/random-pairs 100 (min (.-width canv) (.-height canv)))))
  ([canv xys]
    ;(canvas/white canv)
    (let [ctx (canvas/get-ctx canv)
          w (.-width canv)
          h (.-height canv)
          iters 500]

      (doseq [xy xys]
        (crawl ctx
               (next-px ctx {:coords xy} [pixel/brightness (partial pixel/distance-to-xy-clj [(/ w 2) (/ h 2)])])
               lower-alpha
               iters)))))
