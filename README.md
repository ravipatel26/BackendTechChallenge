# Reservation System

## Setup
This service uses MySQL as a database as defined in application.properties
Replace the following values with correct values
```
spring.datasource.url=jdbc:mysql://{URL}:{PORT}/booking
spring.datasource.username={USERNAME}
spring.datasource.password={PASSWORD}
```

## API Documentation
The reservation service had the following endpoints
- **POST /v1/booking** This endpoint will create a booking
- **PUT /v1/booking/{id}** This endpoint will update a booking by id
- **DELETE /v1/booking/{id}** This endpoint will delete a booking by id
- **GET /v1/booking/{id}** This endpoint will fetch a booking by id
- **GET /v1/booking** This endpoint will fetch all bookings

Sample postman collection with input and outputs to the service can be found here

## Load testing
This section contains two tests scripts

- **load_test.sh** which does a quick and basic laod testing
- **perf_test.jmx** which does a performance test using Jmeter