(ns ws.clojure.sample.receiver
  (:require [immutant.web.async :as async]
            [clojure.data.json :as json]))

(def clients (atom {}))
(def urls ["https://now.httpbin.org" "https://httpbin.org/ip" "https://httpbin.org/stream/1"])

(def responses
  (let [http-data (promise)]
    (future
      (let [responses-all (map #(slurp %) urls)]
        (deliver http-data responses-all)))
    http-data))

(defn- send-clients!
  [{:keys [key message data]}]
  (let [msg-data (cond-> {}
                   true  (assoc :key (or key "chat"))
                   true  (assoc :message (or message "¯\\_(ツ)_/¯"))
                   data  (assoc :data data))]
    (doseq [client (keys @clients)]
      (async/send! client (json/write-str msg-data)))))

(defn chat-receiver
  "Chat receiver"
  [message]
  (let [msg-data {:key "chat" :message message}]
    (send-clients! msg-data)))

(defn data-receiver
  "Data receiver"
  [message]
  (let [http-data (map #(json/read-str %) @responses)
        msg-data  {:key "data"
                   :message message
                   :data http-data}]
    (send-clients! msg-data)))

(def receiver {:chat chat-receiver
               :data data-receiver})
