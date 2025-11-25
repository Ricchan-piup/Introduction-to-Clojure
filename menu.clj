;; -------------
;; This is the menu template for the database app. You can use it as a starting
;; point for the assignment
;; ------------

(ns menu
  (:require [clojure.string :as stringy])
  (:require db))

;; Loads the database into memory
(def customers (db/load-cust-data))
(def products (db/load-item-data))
(def sales (db/load-sales-data))

;;-------------------
;; THE MENU FUNCTIONS
;; ------------------

;; Display the menu and ask the user for the option
(defn showMenu
  []
  (println "\n\n*** Sales Menu ***")
  (println "------------------\n")
  (println "1. Display Customer Table")
  (println "2. Display Product Table")
  (println "3. Display Sales Table")
  (println "4. Total Sales for Customer")
  (println "5. Total Count for Product")
  (println "6. Exit")
  (do
    (print "\nEnter an option? ")
    (flush)
    (read-line)))

;; Handle option 1
(defn option1 []
  (println "** Customer Table **")
  (doseq [k (keys customers)]
    (println k ":" (mapv #(str "\"" % "\"") (vals (get customers k))))))

;; Handle option2 
(defn option2
  []
  (println "** Product Table **")
  (doseq [k (keys products)]
    (println k ":" (mapv #(str "\"" % "\"") (vals (get products k))))))

;; Handle option 3
(defn option3
  []
  (println "** Sales Table **")
  (doseq [k (keys sales)]
    (let [cust-ID (get-in sales [k :cust-id])
          prod-ID (get-in sales [k :prod-id])
          count (get-in sales [k :count])]
      (def saleInfo [(#(str "\"" (get-in customers [cust-ID :name]) "\"")), (#(str "\"" (get-in products [prod-ID :description]) "\"")), (#(str "\"" count "\""))]))

    (println k ":" saleInfo)))

;; Helper function for option 4
;; Returns the ID of the client if the client was found in the database, else it returns nil 
(defn findCustomer [name n]
  (if (contains? customers n)
    (if (= (get-in customers [n :name]) name)
      n
      (recur name (inc n)))
    nil))

;; Help function for option 4
;; Computes the total expenses of the client with ID cust-id
(defn compute-total-sales [cust-id]
  (let [customer-sales
        (for [v (vals sales)
              :when (= (:cust-id v) cust-id)]
          (let [product (products (:prod-id v))
                price   (:cost product)
                count   (:count v)]
            (* price count)))]
    (reduce + customer-sales)))

;; Handle option 4
(defn option4
  []
  (print "\nPlease enter a customer name => ")
  (flush)
  (let [name (read-line)]
    (def cust-ID (findCustomer name 1)) 
    (if (not (= cust-ID nil)) 
      (println name":"(compute-total-sales cust-ID))
      (println "The client is not in the database"))))

;; Helper function for option 5
;; Returns the ID of the item if it was found in the data base, else it returns nil
(defn findProduct [item n]
  (if (contains? products n)
    (if (= (get-in products [n :description]) item)
      n
      (recur item (inc n)))
    nil))

;; Helper function for option 5
;; computes the total count of sellings of the item with ID product-id
(defn compute-total-count [product-id]
  (let [total-count
        (for [v (vals sales)
              :when (= (:prod-id v) product-id)]
          (get v :count))]
    (reduce + total-count)))

;; Handle option 5
(defn option5
  []
  (print "\nPlease enter a product type => ")
  (flush)
  (let [item (read-line)]
    (def product-ID (findProduct item 1))
  (if (not (= product-ID nil))
    (println item":"(compute-total-count product-ID))
    (println "The item is not in the database"))))


; If the menu selection is valid, call the relevant function to 
; process the selection
(defn processOption
  [option]
  (println "\n")
  (if (= option "1")
    (option1)
    (if (= option "2")
      (option2)
      (if (= option "3")
        (option3)
        (if (= option "4")
          (option4)
          (if (= option "5")
            (option5)
            (println "Invalid Option, please try again")))))))


; Display the menu and get a menu item selection. Process the
; selection and then loop again to get the next menu selection
(defn menu []
  (let [option (stringy/trim (showMenu))]
    (if (= option "6")
      (println "\nGood Bye\n")
      (do
        (processOption option)
        (flush)
        (println "\n")
        (print "Press enter to continue...")
        (flush) ; 
        (read-line) ; clear screen
        (recur)))))