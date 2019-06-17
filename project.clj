(defproject ws-clojure-sample "0.2.0-SNAPSHOT"
  :description "Clojure WebSocket Sample App"
  :url "http://example.com/ws-clojure-sample"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/data.zip "0.1.2"]
                 [org.clojure/core.async "0.4.474"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [org.immutant/web "2.1.10"]
                 [environ "1.1.0"]]
  :resource-paths ["config" "resources"]
  :profiles {:dev {:dependencies [[ring/ring-devel "1.7.0"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [reloaded.repl "0.2.4"]]
                   :env {:app-config "./config/config.edn"}}}
  :main ws.clojure.sample.core)
