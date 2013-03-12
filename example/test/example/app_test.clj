(ns example.app-test
  (:use example.test-common
        clojure.test))

(deftest test-landing-page
  (let [resp (test-app {:request-method :get
                        :uri "/"})]
    (is (= 200 (:status resp)))))

(deftest test-get-time
  (let [resp ((site all-routes) {:request-method :get
                                 :uri "/api/time"})]
    (is (= 200 (:status resp)))
    (is (re-find #"important" (:body resp)))))
