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

(defn option1 []
  (println "** Customer Table **")
  (doseq [k (keys customers)]
    (println k ":" (mapv #(str "\"" % "\"") (vals (get customers k))))))

(defn option2
  []
  (println "** Product Table **")
  (doseq [k (keys products)]
    (println k ":" (mapv #(str "\"" % "\"") (vals (get products k))))))


(defn option3
  []
  (println "** Sales Table **")
  (doseq [k (keys sales)]
    (let [cust-ID (get-in sales [k :cust-id])
          prod-ID (get-in sales [k :prod-id])
          count (get-in sales [k :count])]
      (def saleInfo [(#(str "\"" (get-in customers [cust-ID :name]) "\"")), (#(str "\"" (get-in products [prod-ID :description]) "\"")), (#(str "\"" count "\""))]))

    (println k ":" saleInfo)))

(defn findCustomer [name n]
  (if (contains? customers n)
    (if (= (get-in customers [n :name]) name)
      n
      (recur name (inc n)))
    nil))

(defn compute-total-sales [cust-id]
  (let [customer-sales
        (for [v (vals sales)
              :when (= (:cust-id v) cust-id)]
          (let [product (products (:prod-id v))
                price   (:cost product)
                count   (:count v)]
            (* price count)))]
    (reduce + 0 customer-sales)))

(defn option4
  []
  (print "\nPlease enter a customer name => ")
  (flush)
  (let [name (read-line)]
    (def cust-ID (findCustomer name 1)) 
    (if (not (= cust-ID nil)) 
      (println name":"(compute-total-sales cust-ID))
      (println "The client is not in the database"))))

(defn option5
  []
  (print "\nPlease enter a product type => ")
  (flush)
  (let [item (read-line)]
    (println "now display the total sales count for this product")))



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




; ------------------------------
; Run the program. Note that your assignment will run from app.clj
; and then call the code in this file
(menu)
