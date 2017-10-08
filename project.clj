(defproject ws-clojure-sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]                     ;; Подключаем http-kit
                 [compojure "1.6.0"]                    ;; Подключаем compojure (роутинг/маршрутизация)
                 [ring/ring-defaults "0.3.1"]           ;; Джентльменский набор middleware по умолчанию [ring-defaults][7] 
                 [hiccup "1.0.5"]
                 [org.clojure/data.json "0.2.6"]]                      ;; HTML на Clojure. Template langugage.
  :profiles                                             ;; Профили для запуска lein with-profile <имя профиля>
  {:dev                                                 ;; Профиль разработки
    {:dependencies [[javax.servlet/servlet-api "2.5"]   ;; пригодится если вы будете устанавливать ring/ring-core
                    [ring/ring-devel "1.6.2"]]}}        ;; пригодится для горячей перезагрузки
  :main ws-clojure-sample.core)                         ;; пространство имен в котором находится функция -main(точка входа в приложение)
