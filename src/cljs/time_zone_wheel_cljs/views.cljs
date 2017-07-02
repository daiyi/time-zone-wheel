(ns time-zone-wheel-cljs.views
  (:require [re-frame.core :as re-frame]))


(defn get-theta
  [hour]
  (* hour 2 (/ Math/PI 24)))

(defn svg-masks
  [r1 r2]
  [:defs
    [:mask#donut
      [:circle {:cx 0
                :cy 0
                :r r2
                :stroke "white"
                :stroke-width "100"}]]
    [:mask#donut-hole
      [:rect {:x -300
              :y -300
              :width 900
              :height 900
              :fill "white"}]
      [:circle {:cx 0
                :cy 0
                :r r1
                :fill "black"}]]])


(defn get-rotation
  [hour]
  ; offset text rotation by 90 deg, because we want upright text to start from due east
  ; TODO flip text depending on wheel-rotation
  (let [theta (get-theta hour)]
    (+ (* (- theta (/ Math/PI 2))
          (/ 180 Math/PI))
       (if (>= theta Math/PI) -180 0))))

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



(defn wheel-slice
  [r start end fill]
  (let [x1 (get-x r (- start 0.5)) ; hours are inclusive. this highlights the hour text.
        x2 (get-x r (+ end 0.5))
        y1 (get-y r (- start 0.5))
        y2 (get-y r (+ end 0.5))
        largeArcFlag (if (> (- end start) 12) 1 0)]
    [:path {:d (str "M " x1 " " y1 " A " r " " r " 0 " largeArcFlag " 1 " x2 " " y2 " L 0 0 Z")
            :fill fill}]))

(defn sundial
  []
  [:svg.clock {:viewBox "-300 -300 600 600" :preserveAspectRatio "xMidYMid meet"}
    (svg-masks 80 140)
    ;; hour marks on the clock
    [:g.wheel
      {:mask "url(#donut)"}
      (wheel-slice 140 7 24 "rgba(225, 225, 225, 0.3)")
      (wheel-slice 140 8 22 "rgba(180, 204, 5, 0.3)")
      (wheel-slice 140 10 18 "rgba(90, 0, 0, 0.4)")
      ; (for [index (range 24)]
      ;   (get-clock-tick index 130 15))
      (for [index (range 24)]
        (get-clock-hour index 115))]
    [:g.wheel-locations {
                          ; :mask "url(#donut-hole)"
                          :transform (str "rotate(" (get-wheel-rotation @(re-frame/subscribe [:rotation])) " 0 0)")}
      (get-location-label 2  150 "daiyi!")
      (get-clock-tick     2  130 15)
      (get-location-label -7 150 "meimei & olas")
      (get-clock-tick     -7 130 15)
      (get-location-label 12 150 "new zealand")
      (get-clock-tick     12 130 15)]])

(defn intro
  []
  (let [name (re-frame/subscribe [:name])
        instructions (re-frame/subscribe [:instructions])]
    (fn []
      [:div.intro
        [:h1 @name]
        [:p [:i @instructions]]
        [:p "a more visual way of doing timezone conversions" [:br]
         "and mess around for a window when everyone is awake!"]
        [:p "by "
         [:a {:href "https://twitter.com/daiyitastic"} "daiyi"]
         " | "
         [:a {:href "https://github.com/daiyi/time-zone-wheel-cljs"} "source"]]])))


(defn main-panel
  []
  (set! (.-onkeydown js/document) #(handle-keys %))
  (fn []
    [:div.page
      [intro]
      [:div.wheel-box
        [sundial]]]))
