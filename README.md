# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.9/maven-plugin/reference/html/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.9/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [MySQL]()
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.7.9/reference/htmlsingle/#howto.data-initialization.migration-tool.liquibase)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.9/reference/htmlsingle/#web)
* [Spring security]()
* [lombok]()
* [mapstructure]()

### Guides

The following guides illustrate how to use some features concretely:

* At first, you must run docker-compose file with command
    - `docker-compose -f docker-compose.yml up -d --build`
    - with this command you run mysql server for the application
* Then build the project with command
    - `mvn clean install`
    - with this command the project built and mapper classes are built
* Then run the project 
    - At first step liquibase build the tables and indexes in mysql-server
    - Then liquibase add `ROLE_USER` to the role table
    - Then liquibase add some product records to the product table
* After running complete you must run bellow cUrls:
    - This curl for create a user with role `ROLE_USER`:
    - `curl --location 'localhost:8085/api/shopping/v1/users' \
      --header 'Content-Type: application/json' \
      --data '{
      "name": "UserName",
      "phoneNumber": "09183121750",
      "password": "123"
      }'`
    - Then with this curl login user and get token:
    - `curl --location 'localhost:8085/api/shopping/login' \
      --header 'Content-Type: application/x-www-form-urlencoded' \
      --data-urlencode 'phoneNumber=09183121750' \
      --data-urlencode 'password=123'`
    - After this you get the user token and can do some activity like
        - Add product to user cart:
        - `curl --location 'localhost:8085/api/shopping/v1/carts/add/{productId}' \
          --header 'Authorization: Bearer TOKEN' \
          --header 'Content-Type: application/json' \
          --data '{
          "count": 1
          }'`
        - Remove product from user cart:
        - `curl --location --request DELETE 'localhost:8085/api/shopping/v1/carts/remove/1' \
          --header 'Authorization: Bearer TOKEN' \
          --header 'Content-Type: application/json' \
          --data '{
          "count": 1
          }'`
        - Get user invoice:
        - `curl --location 'localhost:8085/api/shopping/v1/invoice' \
          --header 'Authorization: Bearer TOKEN'`
        - Pay the invoice (But without any cost just pay and remove invoice)
        - `curl --location --request PUT 'localhost:8085/api/shopping/v1/invoice/pay' \
          --header 'Authorization: Bearer TOKEN'`
        - By the way you can get product list without any token with this cUrl:
        - `curl --location 'localhost:8085/api/shopping/v1/products?page=0&size=10'`
    - Data base design:
      - [[https://github.com/ArminNrz/shop-pragma/blob/master/DATABASECHANGELOG.png?raw=true|alt=octocat]]
