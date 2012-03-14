(ns glitches.build
  (:require [cljs.closure :as cljsc]))

(defn build-home []
  (cljsc/build
  "d:/dev_gh/glitches/src/"
  {;:optimizations :advanced
   ;:optimizations :simple
   ;:output-dir "D:/dev_gh/glitches/src/public"
   ;:output-to "D:/dev_gh/glitches/src/public/glitches.js"}))

   ; changed to put in jetty folder
   :output-dir "D:/dev_gh/jetty/gh/"
   :output-to "D:/dev_gh/jetty/gh/glitches.js"}))

(build-home)