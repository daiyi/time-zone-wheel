(ns time-zone-wheel-cljs.events
  (:require [re-frame.core :as re-frame]
            [time-zone-wheel-cljs.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
