(ns dev
  (:require
    [com.stuartsierra.component :as component]
    [com.stuartsierra.component.repl
      :refer [reset set-init start stop system]]
    [ws.clojure.sample.system :as ->system]))

(set-init ->system/system)
