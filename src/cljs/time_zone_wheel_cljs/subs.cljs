(ns time-zone-wheel-cljs.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :instructions
 (fn [db _]
   (-> db
     :instructions)))

(re-frame/reg-sub
  :rotation
  (fn [db _]
    (-> db
      :rotation)))

(re-frame/reg-sub
  :labels
  (fn [db _]
    (-> db
      :labels)))
