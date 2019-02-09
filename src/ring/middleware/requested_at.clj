(ns ring.middleware.requested-at
  (:import java.time.LocalDateTime
           java.time.format.DateTimeFormatter
           java.util.Locale)
  (:require [clojure.string :as str]))

(defn- now []
  (LocalDateTime/now))

(defn- new-formatter
  ([^String pattern]
   (formatter pattern (Locale/getDefault))))

(defn- parse-datetime-string
  [s pattern]
  (if-let [formatter (when (string? pattern) (new-formatter pattern))]
    (LocalDateTime/parse s formatter)
    (LocalDateTime/parse s)))

(defn- parse-header [req]
  (get-in req [:headers "x-requested-at"]))

(defn- assoc-requested-at [req {:keys [pattern]}]
  (assoc req ::requested-at
         (or (some-> (parse-header req)
                     (parse-datetime-string pattern))
             (now))))

(defn wrap-requested-at
  ([handler]
   (wrap-requested-at handler {}))

  ([handler {:keys [pattern] :as options}]
   (fn wrap-requested-at* [req]
     (handler (assoc-requested-at req options)))))
