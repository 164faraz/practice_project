# Student Management

Spring Boot student management app with a Bootstrap interface served from `http://localhost:8080/`.

## Run

Configure the MySQL connection in `src/main/resources/application.properties`, then run:

```sh
./mvnw spring-boot:run
```

The application expects a MySQL database named `springdb` and a user with access to it. The REST API is available at `/api/students`.
