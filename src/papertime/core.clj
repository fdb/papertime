(ns papertime.core
  (:use [compojure.core :only [defroutes GET POST]]
        [hiccup.page :only [include-css include-js html5]]
        [papertime.events :only [events]]
        [clj-time.core :only [interval before? now]]
        [clj-time.format :only [parse formatter]])
  (:require [compojure.route :as route])
  (:import org.joda.time.PeriodType))

; ==============================================================================
; Date parsing

(def date-format (formatter "yyyy-MM-dd"))

(defn parse-date
  "Convert the date string to a DateTime object."
  [e]
  (merge e {:date (parse date-format (:date e))}))

(defn format-date
  "Convert the DateTime object to a date string."
  [d]
  (.print date-format d))

(defn filter-past-events
  "Filter events that are in the past."
  [events]
  (filter #(before? (now) (:date %)) events))

(defn add-days-left
  "Add number of days left to event."
  [e]
  (if (before? (now) (:date e))
    (let [i (interval (now) (:date e))]
      (merge e {:days-left (.. i (toPeriod (PeriodType/dayTime)) getDays)}))
    (merge e {:days-left 0})))

(defn relevant-events
  "Show only relevant events, sorted."
  [events]
  (let [events-with-dates (map parse-date events)
        future-events (filter-past-events events-with-dates)
        events-with-days-left (map add-days-left future-events)]
    (sort-by :date events-with-days-left)))

; ==============================================================================
; Views

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

(defn show-event [e]
  [:tr.event
   [:td (format-date (:date e))]
   [:td (:days-left e)]
   [:td [:a {:href (:url e)} (:name e)]]])

(defn index []
  (layout "Papertime"
          [:h1 "Papertime"]
          [:table.table.table-striped
           [:thead
            [:tr
             [:th "Date"]
             [:th "Days Left"]
             [:th "Event"]]]
           [:tbody
            (map show-event (relevant-events events))]]))

(defroutes app
  (GET "/" [] (index))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))
