(defproject time-zone-wheel-cljs "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.854"]
                 [reagent "0.7.0"]
                 [re-frame "0.9.4"]
                 [cljsjs/moment "2.17.1-1"]
                 [cljsjs/moment-timezone "0.5.11-0"]]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-sassy "1.0.8"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :sass {:src "sass"
         :dst "resources/public/css/"}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.8.2"]
                   [day8.re-frame/trace "0.1.0"]]

    :plugins      [[lein-figwheel "0.5.9"]]}}


  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs" "checkouts/re-frame-trace/src"]
     :figwheel     {:on-jsload "time-zone-wheel-cljs.core/mount-root"}
     :compiler     {:main                 time-zone-wheel-cljs.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true}
                    :preloads             [devtools.preload day8.re-frame.trace.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}}}


    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            time-zone-wheel-cljs.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :whitespace ;; uhh todo, externs
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}]})
