(ns time-zone-wheel-cljs.db
  (:require [cljsjs.moment]))

(def default-db
  {:name "time zone wheel"
   :instructions "use arrow keys to spin the wheel of time"
   ;; TODO I just noticed the clock isn't rotated properly
   :rotation (js->clj (.. (js/moment.) (format "H")))
   :labels {:-7 #{"meimei" "olas"}
            :2  #{"jiejie"}}})
