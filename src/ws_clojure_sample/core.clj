(ns ws-clojure-sample.core
    (:require [clojure.pprint :refer [pprint]]
              [org.httpkit.server :refer [run-server]]
              [ring.middleware.reload :as reload]
              [compojure.route :refer [files not-found]]
              [compojure.handler :refer [site]] ;; form, query params decode; cookie; session, etc
              [compojure.core :refer [defroutes GET POST DELETE ANY]]
              [ws-clojure-sample.views :refer [show-landing-page]]
              [ws-clojure-sample.handler :refer [ws-handler]]))






;; Main app routes, using Compojure
(defroutes all-routes
  (GET "/" [] show-landing-page) ;; index page
  (GET "/ws" [] ws-handler)     ;; websocket
  (files "/static/") ;; static file url prefix /static, in `public` folder
  (not-found "<h3>Page not found.</h3>")) ;; all other, return 404

;; Main function. App entry point.
;; Server runs on http://127.0.0.1:5000 by default
(defn -main
    []
    (run-server (reload/wrap-reload (site #'all-routes)) {:port 5000}))
    ;;(System/exit 0))

