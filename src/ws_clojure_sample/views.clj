(ns ws-clojure-sample.views)

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
                                p.innerHTML = response.data;
                                chat.appendChild(p);
                            }

                          }

                          socket.onerror = function(e) {
                            socket.close();
                          }
                        </script>"})