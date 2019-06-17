(ns ws.clojure.sample.receiver
  (:require [immutant.web.async :as async]
            [clojure.data.json :as json]))

(def ^:private urls ["https://now.httpbin.org" "https://httpbin.org/ip" "https://httpbin.org/stream/1"])

(def ^:private responses
  (let [http-data (promise)]
    (future
      (let [responses-all (map #(slurp %) urls)]
        (deliver http-data responses-all)))
    http-data))

(def clients (atom {}))

(defn- send-clients!
  [{:keys [key message data]}]
  (let [msg-data (cond-> {}
                   true  (assoc :key (or key "chat"))
                   true  (assoc :message (or message "¯\\_(ツ)_/¯"))
                   data  (assoc :data data))]
    (doseq [client (keys @clients)]
      (async/send! client (json/write-str msg-data)))))

(defn handler
  [key message]
  (let [msg-data {:key key :message message}]
    (cond
      (= key "chat") (send-clients! msg-data)
      (= key "data") (->> (map #(json/read-str %) @responses)
                          (assoc msg-data :data)
                          (send-clients!)))))
