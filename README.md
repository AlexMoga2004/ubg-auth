# University of Bahr al ghazal class registration system backend 

## setup guide: 

- mongodb database setup (on localhost port 27017)
```use ubgregistrationdb;
db.createUser(
  {
    user: "ubgregistrationuser",
    pwd: "mongoftw",
    roles: [ { role: "readWrite", db: "ubgregistrationdb" } ]
  }
);
```

- use java 17
