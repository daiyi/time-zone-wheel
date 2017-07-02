(ns time-zone-wheel-cljs.views
  (:require [re-frame.core :as re-frame]))


(defn get-theta
  [hour]
  (* hour 2 (/ Math/PI 24)))


(defn get-rotation
  [hour]
  ; offset text rotation by 90 deg, because we want upright text to start from due east
  ; TODO flip text depending on wheel-rotation
  (let [theta (get-theta hour)]
    (+ (* (- theta (/ Math/PI 2))
          (/ 180 Math/PI))
       (if (> theta Math/PI) -180 0))))

(defn get-wheel-rotation
  [hour]
  (let [theta (get-theta hour)]
    (* theta (/ 180 Math/PI))))

(defn get-x
  [r hour]
  (* r (Math/sin (get-theta hour))))

(defn get-y
  [r hour]
  (* -1 r (Math/cos (get-theta hour))))

(defn get-clock-tick
  [hour r l]
  ^{:key hour} [:line {:x1 (get-x r hour)
                       :x2 (get-x (+ r l) hour)
                       :y1 (get-y r hour)
                       :y2 (get-y (+ r l) hour)}])

(defn get-clock-hour
 [hour r]
 ^{:key (+ 100 hour)} [:text {:x (get-x r hour) :y (get-y r hour)} hour])

(defn get-location-label
  [hour r label]
  ^{:key (+ 200 hour)} [:text {:x (get-x r hour)
                               :y (get-y r hour)
                               :class "location"
                               :transform (str "rotate(" (get-rotation hour) " " (get-x r hour) " " (get-y r hour) ")")
                               :text-anchor (if (> Math/PI (get-theta hour)) "start" "end")}
                              label])

(defn handle-keys
  [e]
  (cond
    (= (.-keyCode e) 37) (re-frame/dispatch [:rotate-hour -1])
    (= (.-keyCode e) 39) (re-frame/dispatch [:rotate-hour 1])))

(defn sundial
  []
  [:svg.clock {:viewBox "-300 -300 600 600" :preserveAspectRatio "xMidYMid meet"}
    ;; hour marks on the clock
    [:g.wheel
      (for [index (range 24)]
        (get-clock-tick index 130 15))
      (for [index (range 24)]
        (get-clock-hour index 115))]
    [:g.wheel-locations
      {:transform (str "rotate(" (get-wheel-rotation @(re-frame/subscribe [:rotation])) " 0 0)")}
      (get-location-label 1 150 "potato friend")
      (get-location-label 6 150 "a birb")
      (get-location-label 20 150 "bloop")]])



(defn wheel
  []
  [:div])


(defn main-panel
  []
  (let [name (re-frame/subscribe [:name])
        instructions (re-frame/subscribe [:instructions])]
    (set! (.-onkeydown js/document) #(handle-keys %))
    (fn []
      [:div.page
        [:div.intro
          [:h1 @name]
          [:p [:i @instructions]]
          [:p "a more visual way of doing timezone conversions" [:br]
           "and mess around for a window when everyone is awake!"]
          [:p "by "
           [:a {:href "http://daiyi.co"} "daiyi"]
           " | "
           [:a {:href "https://github.com/daiyi/time-zone-wheel-cljs"} "source"]]]
        [:div.wheel-box
          [sundial]]])))
