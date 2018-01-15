(defproject ws-clojure-sample "0.1.0-SNAPSHOT"
  :description "Clojure WebSocket Sample Chat App"
  :url "http://example.com/ws-clojure-sample"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                [org.clojure/data.xml "0.0.8"]
                [org.clojure/data.json "0.2.6"]
                [org.clojure/data.zip "0.1.2"]
                [org.clojure/core.async "0.4.474"]
                [http-kit "2.2.0"]
                [compojure "1.6.0"]
                [hiccup "1.0.5"]]
  :profiles {:dev {:dependencies [[ring/ring-devel "1.6.3"]
                                  [javax.servlet/servlet-api "2.5"]]
                   :main ws-clojure-sample.core/-main-dev}}
  :main ws-clojure-sample.core)
