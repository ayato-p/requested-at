(ns ring.middleware.requested-at
  (:require [clojure.test :as t]
            [ring.middleware.requested-at :as sut]
            [ring.mock.request :as mock])
  (:import java.time.OffsetDateTime))

(t/deftest test-requested-at-middleware
  (t/testing "prod mode"
    (let [handler (sut/wrap-requested-at identity)
          req (mock/request :get "/ping")
          now (OffsetDateTime/now)]
      (with-redefs [sut/now (constantly now)]
        (t/is (= now
                 (get (handler req) ::sut/requested-at))))))

  (t/testing "dev mode"
    (let [handler (sut/wrap-requested-at identity {:dev true})
          req (-> (mock/request :get "/ping")
                  (assoc-in [:headers "x-requested-at"] "2019-01-01T00:00:00Z"))
          now (OffsetDateTime/now)]
      (t/is (.equals (OffsetDateTime/parse "2019-01-01T00:00:00Z")
                     (get (handler req) ::sut/requested-at))))

    (let [handler (sut/wrap-requested-at identity {:dev true})
          req (-> (mock/request :get "/ping")
                  (assoc-in [:headers "X-Requested-At"] "2019-01-01T00:00:00Z"))
          now (OffsetDateTime/now)]
      (t/is (.equals (OffsetDateTime/parse "2019-01-01T00:00:00Z")
                     (get (handler req) ::sut/requested-at))))

    (let [handler (sut/wrap-requested-at identity {:dev true})
          req (-> (mock/request :get "/ping")
                  (assoc-in [:headers "X-requested-at"] "2019-01-01T00:00:00Z"))
          now (OffsetDateTime/now)]
      (t/is (.equals (OffsetDateTime/parse "2019-01-01T00:00:00Z")
                     (get (handler req) ::sut/requested-at)))))

  )
