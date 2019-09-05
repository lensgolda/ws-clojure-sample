(defproject ws-clojure-sample "0.2.0-SNAPSHOT"
  :description "Clojure WebSocket Sample App"
  :url "http://example.com/ws-clojure-sample"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/data.zip "0.1.3"]
                 [org.clojure/core.async "0.4.500"]
                 [org.clojure/tools.logging "0.5.0"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [org.immutant/web "2.1.10"]
                 [environ "1.1.0"]]
  :resource-paths ["config" "resources"]
  :source-paths ["src"]
  :profiles {:dev {:dependencies [[ring/ring-devel "1.7.1"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [com.stuartsierra/component.repl "0.2.0"]]
                   :source-paths ["src" "dev"]
                   :env {:app-config "./config/config.edn"}}}
  :repl-options {:init-ns user}
  :main ws.clojure.sample.core)
