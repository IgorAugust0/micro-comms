db = connect("mongodb://igor:e296cd9f@localhost:27017/admin");

db = db.getSiblingDB("sales-db");

db.createUser({
  user: "igor",
  pwd: "e296cd9f",
  roles: [
    { role: "dbAdmin", db: "sales-db" },
    { role: "userAdmin", db: "sales-db" },
    { role: "readWrite", db: "sales-db" },
  ],
});
