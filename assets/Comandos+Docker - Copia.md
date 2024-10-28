### Comandos Docker:

## Container Auth-DB:

docker run --name auth-db -p 5432:5432 -e POSTGRES_DB=auth-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 postgres

## Container Product-DB:

docker run --name product-db -p 5433:5432 -e POSTGRES_DB=product-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 postgres

## Container Sales-DB:

docker run --name sales-db -p 27017:27017 -p 28017:28017 -e MONGODB_USER="admin" -e MONGODB_DATABASE="sales" -e MONGODB_PASS="123456" tutum/mongodb

docker run --name sales-db -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=igor -e MONGO_INITDB_ROOT_PASSWORD=e296cd9f mongo

## Conexão no Mongoshell: 

mongosh "mongodb://igor:e296cd9f@localhost:27017/admin"
use sales-db
db.createUser({  user: "igor",  pwd: "e296cd9f",  roles: [{ role: "dbAdmin", db: "sales-db" }, { role: "userAdmin", db: "sales-db" }, { role: "readWrite", db: "sales-db" }]})
mongosh "mongodb://igor:e296cd9f@localhost:27017/sales-db"

## Container RabbitMQ:

docker run --name sales_rabbit -p 5672:5672 -p 25676:25676 -p 15672:15672 rabbitmq:management