(ns ws.clojure.sample.views.index
  (:use [hiccup.page :only (html5 include-css include-js)]))

(def index-page
  (html5
    [:head
      (include-css "https://unpkg.com/bootstrap@3.3.7/dist/css/bootstrap.min.css")]
    [:body {:style "padding-top: 50px;"}
      [:div.container
        [:div.form-group [:input#message.form-control {:name "message" :type "text"}]]
        [:button.btn.btn-primary {:name "send-btn"} "Send"]]
      [:hr]
      [:div.container
        [:div#chat]]
      (include-js "https://unpkg.com/jquery@3.2.1/dist/jquery.min.js")
      (include-js "https://unpkg.com/bootstrap@3.3.7/dist/js/bootstrap.min.js")
      (include-js "https://cdnjs.cloudflare.com/ajax/libs/Faker/3.1.0/faker.min.js")
      (include-js "js/ws-client.js")]))
