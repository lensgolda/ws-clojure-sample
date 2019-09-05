(ns ws.clojure.sample.endpoint
  (:require [com.stuartsierra.component :as component]
            [compojure.core :refer [defroutes GET POST DELETE ANY]]
            [compojure.route :refer [resources files not-found]]
            [ws.clojure.sample.views.index :as views]
            [ws.clojure.sample.handler :refer [ws-handler]]
            [clojure.tools.logging :as log]
            [ring.util.response :refer [response]]))

(defroutes app-routes
  (GET "/" [] views/index-page)
  (GET "/ws" [] ws-handler)
  (GET "/encoding/utf-8" [] (fn [_] (slurp "https://httpbin.org/encoding/utf8")))
  (resources "/")
  (files "/static/")
  (not-found "<h3>Страница не найдена</h3>"))

(defn- wrap-handler-type
  [handler handler-type]
  (fn [request]
    (handler
      (assoc request :app-type handler-type))))


(defrecord Endpoint []
  component/Lifecycle
  (start [this]
    (log/info ">>> starting endpoint component")
    (let [app-type (get-in this [:config :app-type])]
      (assoc this :routes (wrap-handler-type app-routes app-type))))
  (stop [this]
    (log/info "<<< stopping endpoint component")
    (assoc this :routes nil)))

(defn create []
  (map->Endpoint {}))