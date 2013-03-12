(ns example.tmpls
  (:use [example.config :only [cfg]]
        [me.shenfeng.mustache :only [gen-tmpls-from-resources]]))

(defn add-info [data]
  (assoc data
    :dev? (= (cfg :profile) :dev)
    :prod? (= (cfg :profile) :prod)))

;;; generate clojure functions from src/templates folder
(gen-tmpls-from-resources "templates" [".tpl"] add-info)
