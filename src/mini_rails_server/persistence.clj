(ns mini-rails-server.persistence
  (:require  [cheshire.core :as json]))

(def db (atom {}))

(defprotocol RedisProtocol
  (save! [this])
  (get-by-key [this])
  (get-all [this])
  (search [store])
  (delete-by-key [this])
  (append-value-end [this])
  (pop-last-element [this])
  (map-key-get [this])
  (map-key-update [this])
  (map-key-delete [this]))

(defrecord StoreAtomLib [_key _value]
  RedisProtocol
  (save! [this]
              (swap! db assoc _key _value)
         (json/generate-string {:result @db}))
  (append-value-end [this]
                    (swap! db assoc _key (conj (_key @db) _value))
                    (json/generate-string {:result (_key @db)})))


(defrecord GetAtomLib [_key]
  RedisProtocol
  (get-by-key [this]   (json/generate-string {:result (_key @db)}))
  (get-all [this]  (json/generate-string {:result  @db}))
  (delete-by-key [this]
                 (swap! db dissoc _key)
                 (json/generate-string {:result @db}))
  (pop-last-element [this]
                    (let [items (_key @db)]
                      (let [last-element (last items)]
                        (let [new-element (drop-last items)]
                          (swap! db assoc _key new-element))
                      (json/generate-string {:result last-element})))))


(defrecord MapAtomLib [_keys obj]
  RedisProtocol
  (map-key-get [this]
           (let [results (get-in @db _keys)]
             (json/generate-string {:result results})))

  (map-key-update [this]
              (swap! db assoc-in _keys obj)
              (json/generate-string {:result @db}))

  (map-key-delete [this]
              (let [items (_keys @db)]
                (let [results (dissoc items obj)]
                  (swap! db assoc _keys results)))
              (json/generate-string {:result @db})))



(save! (->StoreAtomLib :book {:author {:name "Sroda" :age "34"} :title "Mathew"}))
(get-all (->GetAtomLib []))
(get-by-key (->GetAtomLib :tags))
(delete-by-key (->GetAtomLib :library))
(append-value-end (->StoreAtomLib :tags "violence"))
(pop-last-element (->GetAtomLib :tags))
(map-key-get (->MapAtomLib [:book :author] {}))
(map-key-update (->MapAtomLib [:book :title] "The power of habits"))
(map-key-delete (->MapAtomLib :book :title))
