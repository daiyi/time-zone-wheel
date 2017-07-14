(ns time-zone-wheel-cljs.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [time-zone-wheel-cljs.events]
            [time-zone-wheel-cljs.subs]
            [time-zone-wheel-cljs.views :as views]
            [time-zone-wheel-cljs.config :as config]))

; (defonce app-state (r/atom {:timezones [{ :offset-name "UTC-8"
;                                           :offset      -8
;                                           :labels [{:id   "9289"
;                                                     :name "meimei"}
;                                                    {:id    "sldkjsldf"
;                                                     :name  "olas"}]}]}))

(defonce app-state (reagent/atom {:people [{:name "meimei"
                                            :id   "meimei"
                                            :timezone -7}
                                           {:name "daiyi!"
                                            :id   "daiyi"
                                            :timezone 2}
                                           {:name "nyc"
                                            :id   "nyc"
                                            :timezone -4}]})) 

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel app-state]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
