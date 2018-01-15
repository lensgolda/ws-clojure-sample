(ns ws-clojure-sample.core
    (:require [clojure.pprint :refer [pprint]]
              [org.httpkit.server :refer [run-server]]
              [ring.middleware.reload :as reload]
              [compojure.route :refer [resources files not-found]]
              [compojure.handler :refer [site]] ;; form, query params decode; cookie; session, etc
              [compojure.core :refer [defroutes GET POST DELETE ANY]]
              [ws-clojure-sample.views.index :as views]
              [ws-clojure-sample.handler :refer [ws-handler]]))


;; Main app routes, using Compojure
(defroutes all-routes
  (GET "/" [] views/index-page) ;; index page
  (GET "/ws" [] ws-handler)     ;; websocket
  (resources "/")
  (files "/static/") ;; static file url prefix /static, in `public` folder
  (not-found "<h3>Page not found.</h3>")) ;; all other, return 404

;; Main function. App entry point.
;; Server runs on http://127.0.0.1:5000 by default
(defn -main-dev
    []
    (-> #'all-routes
        site
        reload/wrap-reload
        (run-server {:port 5000})))

(defn -main
    []
    (-> #'all-routes
        site
        (run-server {:port 5000})))
    ;;(System/exit 0))

