(ns mini-rails-server.db_spec)


(def db_redis (atom {}))

(defprotocol RedisPrtcl
  (get-data-key [this])
  (save-data! [this])
  (get-all [this]))



(defrecord RedisStore [obj]
  RedisPrtcl
  (save-data! [this]
              ((let [data (:content @db_redis)])
              (swap! db_redis update-in [:content] conj obj))

  (get-data-key [this]
                (let [data (:content @db_redis)]
                   (let [content (filter #(obj %) data)]
                     content)))
  (get-all [this]
           (:content (deref db_redis)))))


(save-data! (->RedisStore {:book {:title "The power of habbit" :author {:name "Charles Duhrigg" :age 39}}}))
(get-all (->RedisStore {}))
(get-data-key (->RedisStore :book))


