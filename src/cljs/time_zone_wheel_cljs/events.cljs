(ns time-zone-wheel-cljs.events
  (:require [re-frame.core :as re-frame]
            [time-zone-wheel-cljs.db :as db]
            [time-zone-wheel-cljs.timezone :as tz]))

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
    [db [_ label location]]
    ;; moment.tz("America/Los_Angeles").format("ZZ")
    ;; TODO don't allow locations that aren't on the location list
    (if (not-any? clojure.string/blank? [label location])
      (update-in db [:labels (tz/loc->keyword location)]
                    (fnil conj [])
                    label)
      db)))

(re-frame/reg-event-db
  :remove-label
  (fn
    [db [_ label hour]]
    (update-in db [:labels (tz/hour->keyword hour)]
                  disj
                  label)))
