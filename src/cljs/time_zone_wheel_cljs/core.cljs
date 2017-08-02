(ns time-zone-wheel-cljs.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [time-zone-wheel-cljs.events]
            [time-zone-wheel-cljs.subs]
            [time-zone-wheel-cljs.views :as views]
            [time-zone-wheel-cljs.config :as config]))
            ; [re-frisk.core :refer [enable-re-frisk!]]))

(defonce app-state (reagent/atom {:timezones
                                   {:-7 {:labels #{"meimei" "olas"}}
                                    :2  {:labels #{"jiejie"}}}}))


; (defonce app-state (reagent/atom {:labels [{:name "meimei"
;                                             :id   "meimei"
;                                             :timezone -7}
;                                            {:name "daiyi!"
;                                             :id   "daiyi"
;                                             :timezone 2}
;                                            {:name "olas"
;                                             :id   "olas"
;                                             :timezone -7}]}))

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
  ; (enable-re-frisk!)
  (dev-setup)
  (mount-root))
