# Demo e-commerce microservices application
> Using *Hexagonal (Ports & Adapters) Architecture & TDD*

This application is composed of 5 microservices serving specific purposes:
* user-microservice: with REST endpoints for user management (add new user, fetch user details, get orders by userId)
* order-microservice: with REST endpoints for order management (create new order, get status, get status change history)
* inventory-microservice: with REST endpoint for inventory management (check stock for a list of items)
* payment-microservice: to process orders payment
* shipment-microservice: with REST endpoint for shipment management (create a new shipment, check shipment status, check status change history, update shipment status)

Technologies used:
* Java 17
* Spring boot 3.0.5
* Lombok
* Maven
* DynamoDB Local (docker container amazon/dynamodb-local:latest)
* RabbitMQ as communication broker (docker container bitnami/rabbitmq:latest)
* org.testcontainers
* Junit 5

## Installing / Getting started

#### Start RabbitMQ
```console
$ docker pull bitnami/rabbitmq:latest
$ docker run --name rabbitmq bitnami/rabbitmq:latest
``` 
![Bitnami RabbitMQ](https://hub.docker.com/r/bitnami/rabbitmq)

Default web interface: http://localhost:15672/
username: user
password: bitnami

#### Start DynamoDB Local
```console
$ docker pull amazon/dynamodb-local:latest
$ docker run --name dynamodb-local -p 8000:8000 amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb
```

#### Run each application separately
* For user-microservice: UserApplication
* For order-microservice: OrderApplication
* For inventory-management: InventoryApplication
* For payment-microservice: PaymentApplication
* For shipment-microservice: ShipmentApplication

#### Use Swagger-ui to access each microservice
> http://[microservice-host]:[port]/swagger-ui.html
