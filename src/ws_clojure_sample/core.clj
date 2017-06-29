(ns ws-clojure-sample.core
    (:require [clojure.xml :as xml]
              [clojure.zip :as zip]
              [clojure.data.xml :as data.xml]
              [clojure.data.zip.xml :as data.zip.xml]
              [clojure.pprint :refer [pprint]]
              [clojure.core.async :as a
                                  :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]
              [org.httpkit.server :refer :all]
              [compojure.route :refer [files not-found]]
              [compojure.handler :refer [site]] ;; form, query params decode; cookie; session, etc
              [compojure.core :refer [defroutes GET POST DELETE ANY]]))

(defn get-xml
  "I don't do a whole lot."
  [url]
  (let [xml-str (future (slurp url))
        result []]
    (-> @xml-str
                (data.xml/parse-str)
                (zip/xml-zip)
                (data.zip.xml/xml->
                    :slideshow
                    :slide
                    :title
                    data.zip.xml/text))))

(def show-landing-page {:status 200
                        :headers {"Content-Type" "text/html"}
                        :body "<h1>Clojure WebSocket App</h1>

                        <input id='message' name='message' type='text'/>
                        <button name='send-btn'>Send</button>
                        <div id='chat'></div>

                        <script>
                          var msg = document.getElementById('message');
                          var btn = document.getElementsByName('send-btn')[0];
                          var chat = document.getElementById('chat');

                          var socket = new WebSocket('ws://localhost:5000/ws');

                          btn.onclick = function() {
                            console.log('Sending...');
                            socket.send(msg.value);
                          };

                          socket.onopen = function(event) {
                            console.log('Connection opened...');
                          }

                          socket.onmessage = function(event) {
                            console.log(event.data);
                            //var p = document.createElement('p');
                            //p.innerHTML = event.data;
                            //chat.appendChild(p);
                          }

                          socket.onerror = function(e) {
                            socket.close();
                          }
                        </script>"})

(def clients (atom {}))

(def urls ["https://now.httpbin.org" "https://httpbin.org/ip" "https://httpbin.org/stream/2"])

(defn chat-handler [request]
  (with-channel request channel
    (swap! clients assoc channel true)
    (println channel "Connection established")
    (on-close channel (fn [status] (println "channel closed: " status)))
    (on-receive channel (fn [data] ;; echo it back
      (let [resp (map #(future (slurp %)) urls)]
        (doall (map (fn [data]
          (doseq [client (keys @clients)]
            (send! client @data))) resp)))))))


(defroutes all-routes
  (GET "/" [] show-landing-page)
  (GET "/ws" [] chat-handler)     ;; websocket
  (files "/static/") ;; static file url prefix /static, in `public` folder
  (not-found "<p>Page not found.</p>")) ;; all other, return 404


(defn -main
    []
    ;;(pprint (into [] (get-xml "http://httpbin.org/xml")))
    (run-server (site #'all-routes) {:port 5000}))
    ;;(System/exit 0))

