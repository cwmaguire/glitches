(ns glitches.goofy
  (:require [goog.dom :as dom]))

(defn circle [ctx {:keys [x y]} r s e cw? c]
  (.beginPath ctx)
  (.arc ctx x y r s e cw?)
  (set! (.-fillStyle ctx) c)
  (.fill ctx))

(defn goofy
  [canvas-name]
  (if-let [ctx (.getContext (dom/getElement canvas-name) "2d")]
    (let [xy (for [x (range 20 420 20) y (range 20 420 20)] {:x x :y y})
          r 8
          s 0
          e (* 2 Math/PI)
          cw? true
          colors (color-seq)]
      (doall (map circle (repeat ctx) xy (cycle [6 15 9]) (repeat s) (repeat e) (repeat cw?) colors)))))

(defn circ1
  [canvas-name]
  (if-let [ctx (.getContext (dom/getElement canvas-name) "2d")]
    (circle ctx {:x 10 :y 10} 10 0 (* 2 Math/PI) true "#FF00FF")))
