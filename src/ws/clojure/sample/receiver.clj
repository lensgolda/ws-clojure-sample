(ns ws.clojure.sample.receiver
  (:require [immutant.web.async :as async]
            [clojure.data.json :as json]))

(def ^:private urls ["https://httpbin.org/ip" "https://httpbin.org/stream/1"])

(def ^:private responses
  (let [http-data (promise)]
    (future
      (let [responses-all (map #(slurp %) urls)]
        (deliver http-data responses-all)))
    http-data))

(def clients (atom {}))

(defn- send-clients!
  [{:keys [key message name data]}]
  (let [msg-data (cond-> {}
                   key (assoc :key key)
                   name (assoc :name name)
                   message (assoc :message message)
                   data  (assoc :data data))]
    (doseq [client (keys @clients)]
      (async/send! client (json/write-str msg-data)))))

(defn handler
  [key data]
  (let [{:keys [message name]} (json/read-json data)
        response {:key key :message message :name name}]
    (println response)
    (case key
      "chat" (send-clients! response)
      "data" (->> (map #(json/read-str %) @responses)
                  (assoc response :data)
                  (send-clients!)))))
