(ns ws.clojure.sample.handler
  (:require [immutant.web.async :as async]
            [ws.clojure.sample.receiver :refer [receiver clients]]))

(defn ws-handler
  [request]
  (clojure.pprint/pprint request)
  (async/as-channel request
    {:on-open    (fn [channel]
                   (swap! clients assoc channel true)
                   (println channel "Connection established"))
     :on-close   (fn [chan {:keys [code reason]}]
                   (println "channel closed: " code " " reason))
     :on-error   (fn [chan throwable]
                   (println "error: " (.toString throwable)))
     :on-message (fn [chan message]
                   (let [chat-receiver (get receiver :chat)
                         data-receiver (get receiver :data)]
                     (chat-receiver message)))}))