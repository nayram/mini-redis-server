(ns mini-rails-server.home
  (:require [clojure.core :refer :all]
            [mini-rails-server.persistence :refer :all]
           ;; [mini-rails-server.db_spec :refer :all]
            [cheshire.core :as json]))

(defn save [obj]
   (let [key_obj (:key obj)]
    (let [value_obj (:value obj)]
        (save! (->StoreAtomLib (keyword key_obj) value_obj)))))

(defn get-data-Key [_key]
    ( let [k (keyword _key)]
      (get-by-key (->GetAtomLib k))))

(defn delete-data-key [_key]
  (let [k (keyword _key)]
    (delete-by-key (->GetAtomLib k))))

(defn get-all-data [obj]
 (get-all (->GetAtomLib [])))

(defn append-last [obj]
     (let [key_obj (:key obj)]
      (let [value_obj (:value obj)]
        (append-value-end (->StoreAtomLib (keyword key_obj) value_obj)))))

(defn pop-last [_key]
      ( let [k (keyword _key)]
        (pop-last-element (->GetAtomLib k))))

(defn map-get-keys [_key1 _key2]
  (map-key-get (->MapAtomLib [(keyword _key1) (keyword _key2)] {})))

(defn map-update-key [_key1 _key2 value1]
  (let [val_obj (:value value1)]
  (map-key-update (->MapAtomLib [(keyword _key1) (keyword _key2)] val_obj))))

(defn map-delete-key [_key1 _key2]
   (map-key-delete (->MapAtomLib _key1 _key2)))


