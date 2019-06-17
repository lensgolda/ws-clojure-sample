(ns ws.clojure.sample.core
  (:require [compojure.core :refer [defroutes GET POST DELETE ANY]]
            [compojure.route :refer [resources files not-found]]
            [ws.clojure.sample.views.index :as views]
            [ws.clojure.sample.handler :refer [ws-handler]]
            [environ.core :refer [env]]
            [clojure.edn :as edn]
            [immutant.web :as web])
  (:gen-class))

(defonce server (atom nil))

(defn stop-server
  []
  (when-not (nil? @server)
    (web/stop @server)
    (reset! server nil)))

(defroutes app-routes
  (GET "/" [] views/index-page)
  (GET "/ws" [] ws-handler)
  (resources "/")
  (files "/static/")
  (not-found "<h3>Страница не найдена</h3>"))

(defn -main []
  (let [server-options (-> (slurp (env :app-config))
                           (edn/read-string)
                           (get :server))]
    (reset! server (web/run #'app-routes server-options))))

