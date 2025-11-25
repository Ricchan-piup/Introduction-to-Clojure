(ns app
  (:require db)
  (:require [clojure.string :as str]))

(def customers (db/load-cust-data))
(def items (db/load-item-data))
(def sales (db/load-sales-data))

(println customers)
(println items)
(println sales)
