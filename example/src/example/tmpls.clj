(ns example.tmpls
  (:use [example.config :only [cfg]]
        [me.shenfeng.mustache :only [gen-tmpls-from-resources]]))

(def ^{:dynamic true} *req*)

(defn add-info [data]
  (assoc data
    :dev? (= (cfg :profile) :dev)
    :server-time (- (System/currentTimeMillis) (:start-time *req*))
    :prod? (= (cfg :profile) :prod)))

;;; generate clojure functions from src/templates folder
(gen-tmpls-from-resources "templates" [".tpl"] add-info)
