(ns glitches.build
  (:require [cljs.closure :as cljsc]))

(defn build-home []
  (cljsc/build
  "d:/dev_gh/glitches/src/"
  {;:optimizations :advanced
   ;:optimizations :simple
   :output-dir "D:/dev_gh/glitches/src/public"
   :output-to "D:/dev_gh/glitches/src/public/glitches.js"}))

(build-home)