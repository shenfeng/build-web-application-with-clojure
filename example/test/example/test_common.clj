(ns example.test-common
  (:use [example.routes :only [server-routes]]
        [clojure.data.json :only [read-str]]))

(def test-app server-routes)

(defn read-json [str] (read-str str :key-fn keyword))
