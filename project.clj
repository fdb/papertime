(defproject papertime "0.1.0-SNAPSHOT"
  :description "Show deadlines for upcoming events."
  :url "http://github.com/fdb/papertime"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.1.8"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.2"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler papertime.core/app}
  :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}})
