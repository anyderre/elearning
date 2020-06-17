# elearning  

Technologies used:
1. Java 8
2. Maven 3.6.0
3. Spring Boot 1.5.4
4. Postgres 10
5. Flyway Db Migration Plugin
6. Lombok
7. Map Struct 

### I. Init db schema
1. create 'cabacademy' db in postgres under 'cabuser'
2. in `application-<ENV>.properties` file check db.url, db.user, db.pass etc settings
3. in project root run `mvn -Dflyway.configFiles=src/main/resources/application-<YOUR_ENV>.properties flyway:migrate`

### II. Build project

1. From project root run `mvn clean install -P YOUR_ENV_NAME` from cmd

**YOUR_ENV_NAME**  - is target environment name: dev, stage, prod.

### III. Run project

2. Move to ./target dir and run `java -jar elearning-<verion>.jar` from cmd

OR

*2. Run CabAcademieApplication.java from IDE

### IV. Stripe

login/password: cabacadamie@gmail.com/sS08062020