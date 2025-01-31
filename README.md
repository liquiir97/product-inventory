# product-inventory
##Project is stored in folder .inventory

## Advanced Features

### 1. Sorting
Soritng is enabled via <i>GET `/api/products`</i> which will retrieve a list of all products with optional sorting and pagination.
#### Example of endpoint
http://localhost:8080/api/products?page=0&size=10&sort=description,desc&sort=price,desc
