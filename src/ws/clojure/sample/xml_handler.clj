(ns ws.clojure.sample.xml-handler
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.xml :as data.xml]
            [clojure.data.zip.xml :as data.zip.xml]))

;; Use it fo parsing XML if needed
(defn get-xml
  "Example of parse xml function using zipper and xml libraries"
  [url]
  (let [xml-str (future (slurp url))]
    (-> @xml-str
        (data.xml/parse-str)
        (zip/xml-zip)
        (data.zip.xml/xml->
            :slideshow
            :slide
            :title
            data.zip.xml/text))))
