(ns ws-clojure-sample.handler
  (:require [org.httpkit.server :refer [with-channel on-receive on-close]]
            [ws-clojure-sample.receiver :refer [receiver clients]]))

;; Главный обработчик (handler)
(defn ws-handler
  "Main WebSocket handler"
  [request]                                                              ;; Принимает запрос
    (with-channel request channel                                          ;; Получает канал
      (swap! clients assoc channel true)                                   ;; Сохраняем пул клиентов с которыми установлено соединение в атом clients и ставим флаг true
      (println channel "Connection established")
      (on-close channel (fn [status] (println "channel closed: " status))) ;; Устанавливает обработчик при закрытии канала
      (on-receive channel (get receiver :chat))))                          ;; Устаналивает обработчик данных из канала (его создадим далее)