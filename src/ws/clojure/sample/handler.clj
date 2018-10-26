(ns ws.clojure.sample.handler
  (:require [immutant.web.async :as async]
            [ws.clojure.sample.receiver :refer [handler clients]]))

(defn query-string->map
  [request]
  (-> (:query-string request)
      (clojure.string/split #"(&|=)")
      (as-> query-string
            (apply hash-map query-string))
      (clojure.walk/keywordize-keys)))

(defn ws-handler
  [request]
  (let [query-params (query-string->map request)
        type (or (:type query-params) "chat")]
    (async/as-channel request
      {:on-open    (fn [channel]
                     (swap! clients assoc channel true)
                     (println channel "Connection established"))
       :on-close   (fn [chan {:keys [code reason]}]
                     (println "channel closed: " code " " reason))
       :on-error   (fn [chan throwable]
                     (println "error: " (.toString throwable)))
       :on-message (fn [chan message]
                     (handler type message))})))