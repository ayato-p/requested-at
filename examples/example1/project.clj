(defproject example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://choosealicense.com/licenses/mit"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring "1.7.1"]
                 [org.panchromatic/requested-at "0.1.0-SNAPSHOT"]
                 ;; [integrant "0.7.0"]
                 ;; [ring "1.7.1"]
                 ;; [ring/ring-json "0.4.0"]
                 ;; [clojure.java-time "0.3.2"]
                 ]
  :main ^:skip-aot example.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
