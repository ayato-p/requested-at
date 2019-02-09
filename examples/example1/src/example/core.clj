(ns example.core
  (:gen-class)
  (:require [clojure.pprint :as pp]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.requested-at :as requested-at]
            [ring.util.requested-at :as util]))

(defonce ^:private server (atom nil))

(defn- echo-reqeusted-at-handler [req]
  (prn req)
  {:status 200
   :body (str (util/requested-at req))})

(def ^:private app
  (requested-at/wrap-requested-at echo-reqeusted-at-handler))

(defn start
  ([] (start false))
  ([join?]
   (when-not @server
     (reset! server (jetty/run-jetty #'app {:port 3000 :join? join?})))))

(defn stop []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart []
  (stop)
  (start))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (start true))
