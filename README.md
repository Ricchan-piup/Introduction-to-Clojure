# Introduction to Clojure — Sales Menu App

Small command-line Clojure project that loads customer, product, and sales data from text files and provides an interactive menu to view and analyze sales.

## Features

- Display all customers
- Display all products
- Display all sales (with resolved customer/product names)
- Compute total sales amount for a customer
- Compute total units sold for a product

## Project Structure

- `app.clj` — entry point that launches the menu
- `menu.clj` — interactive CLI menu + reporting logic
- `db.clj` — file parsing and in-memory data loading
- `cust.txt` — customer data
- `prod.txt` — product data
- `sales.txt` — sales transaction data
- `deps.edn` — Clojure dependency/config file

## Requirements

- Clojure CLI installed (`clojure` or `clj` command)

## Run the App

From the project root:

```bash
clojure app.clj
```

If your environment uses `clj`, you can also run:

```bash
clj app.clj
```

## Data File Format

The app expects pipe-separated (`|`) text files.

### `cust.txt`

```text
customer_id|name|address|phone
```

### `prod.txt`

```text
product_id|description|unit_cost
```

### `sales.txt`

```text
sale_id|customer_id|product_id|count
```

## Notes

- Data is loaded into memory when the app starts.
- IDs are parsed as numbers and used as map keys.
- This project is intended as a beginner-level Clojure learning exercise.
