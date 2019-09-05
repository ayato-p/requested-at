(ns ring.middleware.requested-at
  (:require [clojure.string :as str])
  (:import java.time.format.DateTimeFormatter
           java.time.OffsetDateTime
           java.util.Locale))

(defn- now []
  (OffsetDateTime/now))

(defn- new-formatter
  ([^String pattern]
   (DateTimeFormatter/ofPattern pattern (Locale/getDefault))))

(defn- parse-datetime-string
  [s pattern]
  (if-let [formatter (when (string? pattern) (new-formatter pattern))]
    (OffsetDateTime/parse s formatter)
    (OffsetDateTime/parse s)))

(def ^:private requested-at-header-patterns
  ["x-requested-at"
   "X-requested-at"
   "X-Requested-At"])

(defn- normarize-requested-at-header [req]
  (update req :headers
          (fn [headers]
            (reduce #(update %1 %2 (fnil str/lower-case ""))
                    headers
                    requested-at-header-patterns))))

(defn- parse-header [req]
  (get-in req [:headers "x-requested-at"]))

(defn- assoc-requested-at [req {:keys [pattern dev]}]
  (assoc req ::requested-at
         (or (when dev
               (some-> (normarize-requested-at-header req)
                       parse-header
                       (parse-datetime-string pattern)))
             (now))))

(defn wrap-requested-at
  ([handler]
   (wrap-requested-at handler {:dev false}))

  ([handler {:keys [pattern dev] :as options}]
   (fn wrap-requested-at* [req]
     (handler (assoc-requested-at req options)))))
