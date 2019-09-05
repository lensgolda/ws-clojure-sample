(ns ws.clojure.sample.server
  (:require [com.stuartsierra.component :as component]
            [immutant.web :as web]
            [clojure.tools.logging :as log]))



(defrecord WebServer [service]
  component/Lifecycle
  (start [this]
    (log/info ">>> starting web server")
    (if service
      service
      (let [options (get-in this [:config :server] {})
            app-routes (get-in this [:endpoint :routes])]
        (assoc this :service (web/run app-routes options)))))
  (stop [this]
    (if service
      (do
        (log/info "<<< stopping web server")
        (web/stop service)
        (assoc this :service nil))
      (assoc this :service nil))))

(defn create []
  (map->WebServer {}))