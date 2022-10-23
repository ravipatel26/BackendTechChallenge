for ((i=1;i<=500;i++)); do
  curl -X PUT http://localhost:8080/v1/booking/18 -H "Content-Type: application/json" -d '{ "firstName": "firstName", "lastName": "lastName", "email": "test@gmail.com", "arrivalDate" : "2022-10-25", "departureDate" : "2022-10-26" }';
done