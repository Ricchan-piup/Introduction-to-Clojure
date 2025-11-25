(ns db
  (:require [clojure.string :as str]))

(defn readFile [fileName]
  (let [rawString (slurp fileName)]
    (str/split-lines rawString)))

;; --- 1. Customer Entry Mapping (custEntry->map) ---
(defn custEntry->map [entry]
  (let [data (str/split entry #"\|") ; Sépare par le caractère pipe '|'
        cust-ID (first data)
        cust-Name (nth data 1)
        cust-Address (nth data 2)
        cust-Number (nth data 3)
        cust-map {:name cust-Name, :address cust-Address, :number cust-Number}]
    {cust-ID cust-map}))

;; --- 2. Item/Product Entry Mapping (itemEntry->map) ---
(defn itemEntry->map [entry]
  (let [data (str/split entry #"\|") ; Sépare par l'espace ' ' (selon l'exemple prod.txt)
        prod-ID (first data)
        item-Description (nth data 1)
        unit-Cost (nth data 2)
        ;; Convertit le coût en nombre pour les calculs (read-string)
        item-map {:description item-Description, :cost (read-string unit-Cost)}]
    {prod-ID item-map}))

;; --- 3. Sales Entry Mapping (salesEntry->map) ---
(defn salesEntry->map [entry]
  (let [data (str/split entry #"\|") ; Sépare par l'espace ' ' (selon l'exemple sales.txt)
        sales-ID (first data)
        cust-ID (nth data 1)
        prod-ID (nth data 2)
        item-Count (nth data 3)
        ;; Convertit le compte en nombre pour les calculs (read-string)
        sales-map {:cust-id cust-ID, :prod-id prod-ID, :count (read-string item-Count)}]
    {sales-ID sales-map}))

;; --- 4. Generic Data Processing Function (processData) ---
(defn processData [data entry-mapper-fn]
  ;;Takes raw line data and a mapping function, returning a single merged map {ID -> entry-map}.
  (into {} (map entry-mapper-fn data)))

;; --- 5. Specific Loading Functions (Exposées à app.clj) ---

(defn load-cust-data []
  (processData (readFile "cust.txt") custEntry->map))

(defn load-item-data []
  (processData (readFile "prod.txt") itemEntry->map))

(defn load-sales-data []
  (processData (readFile "sales.txt") salesEntry->map))