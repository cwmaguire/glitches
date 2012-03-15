(ns glitches.pixel)

(defn brightness
  [{[r g b a] :colors}]
  (+ r g b a))

(defn distance-from-xy
  "Returns the distance from px to [x,y] such that the farther away px is, the higher the number"
  [[x y] {[px-x px-y] :coords}]
  (+ x y px-x px-y))

(defn distance-to-xy-clj
  "Returns the distnace from px to [x,y] such that the closer px is, the higher the number"
  [[x y] {[px-x px-y] :coords}]
  (let [xd (- x px-x)
        yd (- y px-y)
        neg-x (if (neg? xd) xd (- xd))
        neg-y (if (neg? yd) yd (- yd))]
    (+ neg-x neg-y)))

(defn to-string
  [{[x y] :coords [r g b] :colors}]
  (str "[" x "," y "] [" r " / " g " / " b "]"))

(defn dxy
  [x1 y1 x2 y2]
  (let [xd (- x1 x2)
        yd (- y1 y2)
        neg-xd (if (pos? xd) (- xd) xd)
        neg-yd (if (pos? yd) (- yd) yd)]
        (+ neg-xd neg-yd)))