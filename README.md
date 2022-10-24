# Reservation System

## Setup
This service uses MySQL as a database as defined in [application.properties](https://github.com/ravipatel26/ReservationSystem/blob/testing_service/src/main/resources/application.properties).

Replace the following values with correct values:
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

The Postman collection for these endpoints with input payloads to the service can be found [here](https://github.com/ravipatel26/ReservationSystem/blob/testing_service/src/test/resources/postman_collection/Booking.postman_collection.json)

## Load testing
This section contains two tests scripts

- **[load_test.sh](https://github.com/ravipatel26/ReservationSystem/blob/testing_service/src/test/resources/load_testing/load_test.sh)** which does a quick and basic load testing
- **[perf_test.jmx](https://github.com/ravipatel26/ReservationSystem/blob/testing_service/src/test/resources/load_testing/perf_test.jmx)** which does a performance test using Jmeter
