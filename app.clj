;; This file just serves as starting point for the program
;; When the program is launched, the database is loaded in the menu.cjl file using the functions of db/clj.
;; Then the menu table is generated and the user can interact with it
(ns app
  (:require db)
  (:require menu))

(menu/menu) ;; calls the menu function of menu