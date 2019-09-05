(ns ws.clojure.sample.core
  (:require [environ.core :refer [env]]
            [com.stuartsierra.component :as component]
            [ws.clojure.sample.system :refer [system]])
  (:gen-class))

(defonce web (atom nil))

(defn -main []
  (reset! web (component/start system))
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(component/stop system))))
