(ns ws-clojure-sample.core
    (:require [clojure.xml :as xml]
              [clojure.data.json :as json]
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

;; Use it fo parsing XML if needed
(defn get-xml
  "Example of parse xml function using zipper and xml libraries"
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

;; Landing page
;; todo: change to hiccup or enlive template engine
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

                          var socket = new WebSocket('ws://localhost:5000/ws?foo=clojure');

                          btn.onclick = function() {
                            console.log('Sending...');
                            socket.send(msg.value);
                          };

                          socket.onopen = function(event) {
                            console.log('Connection opened...');
                          }

                          socket.onmessage = function(event) {
                            console.log(event.data);
                            var response = JSON.parse(event.data);

                            if (response.key == 'chat') {
                                var p = document.createElement('p');
                                p.innerHTML = event.data;
                                chat.appendChild(p);
                            }

                          }

                          socket.onerror = function(e) {
                            socket.close();
                          }
                        </script>"})


(def clients (atom {})) ;; atom for saving connecting clients

;; list of urls, if app needs to get a lot of data from external resources
(def urls ["https://now.httpbin.org" "https://httpbin.org/ip" "https://httpbin.org/stream/2"]) ;; example

;; on-receive channel handler, for app chat example
(defn chat-receiver
    "Chat receiver"
    [data]
    (doseq [client (keys @clients)]
            (send! client (json/write-str {:key "chat" :data data})))) ;; example: return received data back to client

;; on-receive channel handler, for app data getter example
(defn data-receiver
    "Data receiver"
    [data]
    (let [responses (map #(future (slurp %)) urls)]
        (doall (map (fn [data]
          (doseq [client (keys @clients)]
            (send! client @data))) responses))))

;; Receivers map, for WebSocket handler (see ws-handler)
;; for new one, write new receiver and add it to map
(def receiver {:chat chat-receiver
               :data data-receiver })

;; Main WebSocket handler
(defn ws-handler
    "Main WebSocket handler"
    [request]
  (with-channel request channel
    (swap! clients assoc channel true)
    (println channel "Connection established")
    (on-close channel (fn [status] (println "channel closed: " status)))
    (on-receive channel (get receiver :chat :data))))


;; Main app routes, using Compojure
(defroutes all-routes
  (GET "/" [] show-landing-page) ;; index page
  (GET "/ws" [] ws-handler)     ;; websocket
  (files "/static/") ;; static file url prefix /static, in `public` folder
  (not-found "<h3>Page not found.</h3>")) ;; all other, return 404

;; Main function. App entry point.
;; Server runs on http://127.0.0.1:5000 by default
(defn -main
    []
    (run-server (site #'all-routes) {:port 5000}))
    ;;(System/exit 0))

