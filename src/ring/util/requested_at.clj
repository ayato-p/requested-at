(ns ring.util.requested-at
  (:require [ring.middleware.requested-at :as middleware]))

(defn requested-at [req]
  (::middleware/requested-at req))
