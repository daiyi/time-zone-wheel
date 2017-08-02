(ns time-zone-wheel-cljs.events
  (:require [re-frame.core :as re-frame]
            [time-zone-wheel-cljs.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
  :rotate-hour
  (fn
    [db [_ hour-change]]
    (assoc db :rotation (+ hour-change (:rotation db)))))

(re-frame/reg-event-db
  :add-location
  (fn
    [db [_ label offset]]
    (if (pos? (count label))
      (update-in db [:labels (keyword offset)] (fnil conj #{}) label)
      db)))
