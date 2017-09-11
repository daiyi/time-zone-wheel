(ns time-zone-wheel-cljs.db
  (:require [cljsjs.moment]
            [time-zone-wheel-cljs.timezone :as tz]))

(def default-labels {(tz/loc->keyword "America/Los_Angeles") #{"LAX"}
                     (tz/loc->keyword "America/Bogota") #{"Medellín"}
                     (tz/loc->keyword "Europe/Berlin") #{"Berlin"}
                     (tz/loc->keyword (tz/user-loc)) #{"you \\o/"}})

(def default-db
  {:name "time zone wheel"
   :instructions "use arrow keys to spin the wheel of time"
   :rotation (tz/loc->hour (tz/user-loc))
   :labels (merge default-labels)})
