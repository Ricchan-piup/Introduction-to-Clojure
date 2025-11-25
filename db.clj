(ns db
  (:require [clojure.string :as str]))

; reads the whole file into a string
(defn readFile [fileName]
  (let [rawString (slurp fileName)]
    (str/split-lines rawString)))

; Customer Entry Mapping (custEntry->map)
(defn custEntry->map [entry]
  (let [data (str/split entry #"\|")
        cust-ID (read-string (first data))
        cust-Name (nth data 1)
        cust-Address (nth data 2)
        cust-Number (nth data 3)
        cust-map {:name cust-Name, :address cust-Address, :number cust-Number}]
    {cust-ID cust-map}))

; Item/Product Entry Mapping (itemEntry->map) 
(defn itemEntry->map [entry]
  (let [data (str/split entry #"\|")
        prod-ID (read-string (first data))
        item-Description (nth data 1)
        unit-Cost (read-string (nth data 2))
        item-map {:description item-Description, :cost unit-Cost}]
    {prod-ID item-map}))

; Sales Entry Mapping (salesEntry->map) 
(defn salesEntry->map [entry]
  (let [data (str/split entry #"\|")
        sales-ID (read-string (first data))
        cust-ID (read-string (nth data 1))
        prod-ID (read-string (nth data 2))
        item-Count (read-string (nth data 3))
        sales-map {:cust-id cust-ID, :prod-id prod-ID, :count item-Count}]
    {sales-ID sales-map}))

; formats the data into a a map of map key {ID -> entry-map} 
(defn processData [data entry-mapper-fn]
  (into (sorted-map) (map entry-mapper-fn data)))

; Specific Loading Functions 
(defn load-cust-data []
  (processData (readFile "cust.txt") custEntry->map))


(defn load-item-data []
  (processData (readFile "prod.txt") itemEntry->map))

(defn load-sales-data []
  (processData (readFile "sales.txt") salesEntry->map))

; loads the data into memory
(def customers (load-cust-data))
(def items (load-item-data))
(def sales (load-sales-data))