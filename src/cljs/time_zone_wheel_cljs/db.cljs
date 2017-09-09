(ns time-zone-wheel-cljs.db
  (:require [cljsjs.moment]
            [time-zone-wheel-cljs.timezone :as tz]))

(def default-labels {(tz/loc->keyword "America/Los_Angeles") ["meimei" "olas"]
                     (tz/loc->keyword "Europe/Berlin") ["jiejie"]
                     (tz/loc->keyword (tz/user-loc)) ["you, right now."]})

(def default-db
  {:name "time zone wheel"
   :instructions "use arrow keys to spin the wheel of time"
   :rotation (tz/loc->hour (tz/user-loc))
   :labels (merge default-labels)})
