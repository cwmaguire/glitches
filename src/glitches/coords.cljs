(ns glitches.coords)

(defn around
  "get the positive coords around x,y that aren't x,y"
  [x y]
  (filter (fn [[a b]] (and
                        (not (neg? a))
                        (not (neg? b))
                        (not (= [a b] [x y]))))
    (for [y0 (range (- y 1) (+ y 2))
          x0 (range (- x 1) (+ x 2))]
          [x0 y0])))
