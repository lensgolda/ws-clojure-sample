(ns ws-clojure-sample.core
    (:require [org.httpkit.server :refer [run-server]]                  ;; http-kit server
              [compojure.core :refer [defroutes GET POST DELETE ANY]]   ;; defroutes, и методы
              [compojure.route :refer [resources files not-found]]      ;; маршруты для статики, и not-found
              [ring.middleware.defaults :refer :all]                    ;; middleware
              [ws-clojure-sample.views.index :refer [index-page]]       ;; Добавляем представление index-page
              [ws-clojure-sample.handler :refer [ws-handler]]))         ;; Добавляем обработчик для веб-сокетов

(defroutes app-routes
  (GET "/" [] index-page)                       ;; Нам нужна будет главная страница.
  (GET "/ws" [] ws-handler)                     ;; здесь будем "ловить" веб-сокеты. Обработчик.
  (resources "/")                               ;; директория ресурсов
  (files "/static/")                            ;; префикс для статических файлов в папке `public`
  (not-found "<h3>Страница не найдена</h3>"))   ;; все остальные, возвращает 404)

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

