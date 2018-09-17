(ns mini-rails-server.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :refer [response]]
            [cheshire.core :as json]
            [mini-rails-server.home :as home])
  (use [ring.util.response :only [response]]))

(defroutes app-routes
  (GET "/" []  (home/get-all-data []))
  (POST "/save" req (home/save (:body req)))
  (GET "/data" [_key]  (home/get-data-Key _key))
  (DELETE "/delete" [_key] (home/delete-data-key _key))
  (POST "/append/last" req (home/append-last (:body req)))
  (GET "/pop/last" [_key] (home/pop-last _key))
  (GET "/map/:_key1/:_key2" [_key1 _key2] (home/map-get-keys _key1 _key2))
  (PUT "/map/:_key1/:_key2" req (home/map-update-key (:_key1 (:params req)) (:_key2 (:params req)) (:body req)))
  (DELETE "/map/:_key1/:_key2" [_key1 _key2] (home/map-delete-key _key1 _key2))
  (route/not-found "Not Found"))


(def app
  (->( handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
     (middleware/wrap-json-response )
     ))

;;(home/save_data (json/generate-string))
