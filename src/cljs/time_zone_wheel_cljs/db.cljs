(ns time-zone-wheel-cljs.db)


;; sigh.
(defn get-hour []
  (-> (js/Date.)
      .toTimeString
      (clojure.string/split " ")
      first
      (clojure.string/split ":")
      first
      (js/parseInt)))

(def default-db
  {:name "time zone wheel"
   :instructions "use arrow keys to spin the wheel of time"
   :rotation (get-hour)
   :labels {:-7 #{"meimei" "olas"}
            :2  #{"jiejie"}}})
