(ns ws-clojure-sample.receiver
    (:require [clojure.data.json :as json]
              [org.httpkit.server :refer [send!]]))


(def clients (atom {})) ;; atom for saving connecting clients

;; list of urls, if app needs to get a lot of data from external resources
(def urls ["https://now.httpbin.org" "https://httpbin.org/ip" "https://httpbin.org/stream/2"]) ;; example

;; on-receive channel handler, for app chat example
(defn chat-receiver
    "Chat receiver"
    [data]
    (doseq [client (keys @clients)]
            (send! client (json/write-str {:key "chat" :data data})))) ;; example: return received data back to client

;; on-receive channel handler, for app data getter example
(defn data-receiver
    "Data receiver"
    [data]
    (let [responses (map #(future (slurp %)) urls)]
        (doall (map (fn [resp]
          (doseq [client (keys @clients)]
            (send! client @resp))) responses))))

;; Receivers map, for WebSocket handler (see ws-handler)
;; for new one, write new receiver and add it to map
(def receiver {:chat chat-receiver
               :data data-receiver })
