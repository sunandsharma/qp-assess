# qp-assess
Grocery Booking API

Authentication: Basic
Authorization: Role-Based

   Database Used: PostgreSQL
    **Database Name**: grocery_db
    **SQL Queries to create tables**:
>     CREATE TABLE users (
>     id SERIAL PRIMARY KEY,
>     username VARCHAR(50) UNIQUE NOT NULL,
>     password VARCHAR(255) NOT NULL,
>     role VARCHAR(20) NOT NULL, -- Role column directly in the users table
>     enabled BOOLEAN NOT NULL
>       );

      
>    INSERT INTO users (username, password, role, enabled)
>    VALUES
>    ('admin', '{bcrypt}$2a$10$zUwTDC4ZTOfp/xED1.ZoXePxKYqALso.6qpkEfi6OK8EyMTyl.Qsi', 'ROLE_ADMIN', true), -- Password: admin123
>    ('user', '{bcrypt}$2a$10$JXsftUs2/9zXBLbBkaRVnecyUCeYmMYMY5Io58NAn3FTff7RtFv7C', 'ROLE_USER', true);  -- Password: user123


>    CREATE TABLE grocery_items (
>     id SERIAL PRIMARY KEY,
>     name VARCHAR(255) NOT NULL,
>     price DECIMAL(10, 2) NOT NULL,
>     stock INT NOT NULL
>    );

>    CREATE TABLE orders (
>     id SERIAL PRIMARY KEY,
>     user_id BIGINT REFERENCES users(id),
>     total_price DECIMAL(10, 2)
>    );

>    CREATE TABLE order_items (
>     id SERIAL PRIMARY KEY,
>     order_id BIGINT REFERENCES orders(id),
>     grocery_item_id BIGINT REFERENCES grocery_items(id),
>     quantity INT NOT NULL
>    );

To **Build Docker Image**: docker build -t grocery-booking-app .
To **run the container**: docker run -p 8080:8080 grocery-booking-app


CURL Requests for ADMIN:
      1) To view Grocery items
>          curl --location 'http://localhost:8080/grocery-service/admin/items' \
>          --header 'Content-Type: application/json' \
>          --header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
>          --data ''
      2) To Add Grocery Item
>          curl --location 'http://localhost:8080/grocery-service/admin/items' \
>          --header 'Content-Type: application/json' \
>          --header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
>          --data '{
>              "id":1,
>              "name":"Apple",
>              "price":50.0,
>              "stock":3
>          }'
    3) To update Inventory
>          curl --location --request PUT 'http://localhost:8080/grocery-service/admin/items/1/inventory?stock=2' \
>          --header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' 

   4) To update item
>    curl --location --request PUT 'http://localhost:8080/grocery-service/admin/items/1' \
>    --header 'Content-Type: application/json' \
>    --header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
>    --header 'Cookie: JSESSIONID=62344F6FEBB756CEB715F19D9C3AAB52' \
>    --data '{
>     "id":1,
>     "name": "Apple",
>     "price": 60.0,
>     "stock":1
>       }'

   5) To delete grocery item
>      curl --location --request DELETE 'http://localhost:8080/grocery-service/admin/items/1/' \
>       --header 'Authorization: Basic YWRtaW46YWRtaW4xMjM='


Curl Requests for User
   1) View Items

>      curl --location 'http://localhost:8080/grocery-service/user/items' \
>    --header 'Authorization: Basic dXNlcjp1c2VyMTIz' \
>    --data ''

   2) Place Order
>       curl --location 'http://localhost:8080/grocery-service/user/orders' \
>       --header 'Content-Type: application/json' \
>       --header 'Authorization: Basic dXNlcjp1c2VyMTIz' \
>       --data '{
>        "userId":2,
>        "items": [
>            {
>                "groceryItemId":2,
>                "quantity":2
>            }
>        ]
>       }'



