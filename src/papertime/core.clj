(ns papertime.core
  (:use [compojure.core :only [defroutes GET POST]]
        [hiccup.page :only [include-css include-js html5]]
        [papertime.events :only [events]])
  (:require [compojure.route :as route]))

(defn layout [title & content]
  (html5
   [:head
    [:title title]
    (include-css "/css/bootstrap.min.css")
    (include-css "/css/papertime.css")]
   [:body
    [:div.container
     content]
    (include-js "/js/bootstrap.min.js")]))

(defn show-event [c]
  [:tr.event
   [:td (:date c)]
   [:td [:a {:href (:url c)} (:name c)]]])

(defn index []
  (let [ordered-events (sort-by :date events)]
    (layout "Papertime"
            [:h1 "Papertime"]
            [:table.table.table-striped
             [:thead
              [:tr [:th "Date"] [:th "Event"]]]
             [:tbody
              (map show-event ordered-events)]])))

(defroutes app
  (GET "/" [] (index))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))
