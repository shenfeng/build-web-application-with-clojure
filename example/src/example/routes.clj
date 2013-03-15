(ns example.routes
  (:use [compojure.core :only [defroutes GET POST DELETE ANY context]]
        (ring.middleware [keyword-params :only [wrap-keyword-params]]
                         [params :only [wrap-params]]
                         [session :only [wrap-session]])
        [example.middleware :only [wrap-failsafe wrap-request-logging-in-dev
                                   wrap-req-bind json-response
                                   wrap-reload-in-dev JGET JPUT JPOST JDELETE]])
  (:require [example.handlers.app :as app]
            [org.httpkit.dbcp :as db]
            [example.handlers.api :as api]
            [compojure.route :as route]))

(defn test-handler [req]
  (json-response {:headers {}
                  :status 200
                  :body (dissoc req :async-channel)
                  ;; :session {:a 1 :b 2 :c "sfds"}
                  :cookies {:name {:value "value"}
                            :name2 {:value "value2"
                                    :max-age -13123
                                    :http-only true
                                    ;; :domain ".what.com"
                                    ;; :path "/what"
                                    }}}))

(defn data-from-jdbc [req]
  (-> (db/query "select * from feed_data where id = ?" 1690134)
      first :summary))

;; define mapping here
(defroutes server-routes*
  (GET "/" [] app/show-landing)
  (GET "/test" [] test-handler)
  (GET "/jdbc" [] data-from-jdbc)
  (JGET "/request-map" [] (fn [req] {:body (dissoc req :async-channel)}))
  (context "/api" []
           ;; JGET returns json encoding of the response
           (JGET "/time" [] api/get-time))
  ;; static files under ./public folder, prefix /static
  ;; like /static/css/style.css
  (route/files "/static")
  ;; 404, modify for a better 404 page
  (route/not-found "<p>Page not found.</p>" ))

(defn app []
  (-> #'server-routes*
      wrap-session
      wrap-keyword-params
      wrap-params
      wrap-request-logging-in-dev
      wrap-reload-in-dev
      wrap-req-bind
      wrap-failsafe))
