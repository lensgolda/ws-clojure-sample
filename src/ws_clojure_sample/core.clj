(ns ws-clojure-sample.core
    (:require [org.httpkit.server :refer [run-server]]                  ;; http-kit server
              [compojure.core :refer [defroutes GET POST DELETE ANY]]   ;; defroutes, и методы
              [compojure.route :refer [resources files not-found]]      ;; маршруты для ресурсов и статики, а также страница not-found
              [ring.middleware.defaults :refer :all]                    ;; middleware
              [ws-clojure-sample.views.index :refer [index-page]]
              [ws-clojure-sample.handler :refer [ws-handler]]))         ;; Добавляем представление index-page

(defroutes app-routes
  (GET "/" [] index-page)                       ;; Нам нужна будет главная страница.
  (GET "/ws" [] ws-handler)                     ;; здесь будем "ловить" веб-сокеты. Обработчик.
  (resources "/")                               ;; директория ресурсов
  (files "/static/")                            ;; префикс для статических файлов в папке `public`
  (not-found "<h3>Страница не найдена</h3>"))   ;; все остальные, возвращает 404)

(defn -main
  "Точка входа в приложение"
  []
  (run-server (wrap-defaults #'app-routes site-defaults) {:port 5000}))