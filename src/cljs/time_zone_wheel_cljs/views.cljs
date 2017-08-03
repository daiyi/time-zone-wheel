(ns time-zone-wheel-cljs.views
  (:require [re-frame.core :as r]
            [reagent.core :as reagent]
            [clojure.string :as string]
            [cljsjs.moment]
            [cljsjs.moment-timezone]
            [time-zone-wheel-cljs.timezone :as tz]))


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

(defn clock-tick
  [hour r l]
  ^{:key hour} [:line {:x1 (get-x r hour)
                       :x2 (get-x (+ r l) hour)
                       :y1 (get-y r hour)
                       :y2 (get-y (+ r l) hour)}])

(defn clock-hour
 [hour r]
 ^{:key (+ 100 hour)} [:text {:x (get-x r hour) :y (get-y r hour)} hour])

(defn location-label
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
    (= (.-keyCode e) 37) (r/dispatch [:rotate-hour -1])
    (= (.-keyCode e) 39) (r/dispatch [:rotate-hour 1])))

(defn label [labels offset]
  ^{:key offset} [location-label
                   offset
                   150
                   (string/join ", " (get labels (-> offset
                                                   (- 11)
                                                   (str)
                                                   (keyword))))])
    ; ^{key (str offset "_2")} [clock-tick     offset 130 15])


(defn wheel-slice
  [r start end fill]
  (let [x1 (get-x r (- start 0.5)) ; hours are inclusive. this highlights the hour text.
        x2 (get-x r (+ end 0.5))
        y1 (get-y r (- start 0.5))
        y2 (get-y r (+ end 0.5))
        largeArcFlag (if (> (- end start) 12) 1 0)]
    [:path {:d (str "M " x1 " " y1 " A " r " " r " 0 " largeArcFlag " 1 " x2 " " y2 " L 0 0 Z")
            :fill fill}]))

(defn sundial []
  [:svg.clock {:viewBox "-300 -300 600 600"
               :preserveAspectRatio "xMidYMid meet"}
    [svg-masks 80 140]
    ;; hour marks on the clock
    [:g.wheel
      {:mask "url(#donut)"}
      [wheel-slice 140 7 24 "rgba(225, 225, 225, 0.3)"]
      [wheel-slice 140 8 22 "#4d3c67"]
      [wheel-slice 140 10 18 "#1f7c81"]
      ; (for [index (range 24)]
      ;   [clock-tick index 130 15]))
      (for [index (range 24)]
        ^{:key (str index "_hour")} [clock-hour index 115])]
    [:g.wheel-locations {
                          ; :mask "url(#donut-hole)"
                          :transform (str "rotate(" (get-wheel-rotation @(r/subscribe [:rotation])) " 0 0)")}
      (doall
        (for [i (range 24)]
          ^{:key (str "label-" i)} [label @(r/subscribe [:labels]) i]))]])

(defn location-form []
  [:form#form-add-location
    [:div.form-group
      [:label {:for "input-timezone-utc" :hidden "true"}
        "timezone"]
      [:select#input-timezone-utc {:name "offset" :type "select" :defaultValue 1}
        [:option {:value -4} "UTC-4"]
        [:option {:value 0} "UTC+0"]
        [:option {:value 1} "UTC+1"]]]
    [:div.form-group
      [:label {:for "input-timezone-label" :hidden "true"}
        "label"]
      [:input#input-timezone-label {:name "label" :placeholder "label" :type "text"}]]
    [:div.form-group
      [:button#button-add-location.button-submit
        {:on-click
          (fn [event]
            (.preventDefault event)
            (r/dispatch [:add-location (.. event -target -form -label -value)
                                       (.. event -target -form -offset -value)]))}
        "add"]]])

(defn intro
  []
  (let [name (r/subscribe [:name])
        instructions (r/subscribe [:instructions])]
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


(defn main-panel []
  (set! (.-onkeydown js/document) #(handle-keys %))
  (fn []
    [:div.page
      [intro]
      [:div.wheel-box
        [sundial]]
      [location-form]]))
