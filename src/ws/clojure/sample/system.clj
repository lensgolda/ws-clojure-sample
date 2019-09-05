(ns ws.clojure.sample.system
  (:require [clojure.edn :as edn]
            [environ.core :refer [env]]
            [com.stuartsierra.component :as component]
            [ws.clojure.sample.server :as server]
            [ws.clojure.sample.endpoint :as endpoint]))

(def config
  (edn/read-string (slurp "config/config.edn")))

(defn system
  [_]
  (component/system-using
    (component/system-map
      :config config
      :server (server/create)
      :endpoint (endpoint/create))
    {:server [:config :endpoint]
     :endpoint [:config]}))