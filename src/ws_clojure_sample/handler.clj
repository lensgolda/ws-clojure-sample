(ns ws-clojure-sample.handler
    (:require [org.httpkit.server :refer [with-channel on-receive on-close]]
        [ws-clojure-sample.receiver :refer [receiver clients]]))


;; Main WebSocket handler
(defn ws-handler
    "Main WebSocket handler"
    [request]
  (with-channel request channel
    (swap! clients assoc channel true)
    (println channel "Connection established")
    (on-close channel (fn [status] (println "channel closed: " status)))
    (on-receive channel (get receiver :chat :data))))