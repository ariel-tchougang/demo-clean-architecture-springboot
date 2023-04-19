# User microservices application
> Using *Hexagonal (Ports & Adapters) Architecture & TDD*

This microservice has 3 modules:
* user-domain: where all use cases are described along with all inbound and outbound ports
* user-adapters-out-persistence-dynamodb: outbound adapter for DynamoDB
* user-application-springboot: hosting the executable UserApplication

## Inbound ports

#### From web interactions
* Register new user
* Find user by id
* Get the list of orders from a given user

#### From messaging (RabbitMQ) interactions
* Add new order to user: from OrderCreationEvent coming from order-microservice
* Update order status: from OrderStatusChangedEvent coming from order-microservice

## Outbound ports

#### To a repository (DynamoDB)
* Register new user
* Find user by id
* Add new order to user
* Update order status
* Get the list of orders from a given user

## Installing / Getting started

#### Depends on
* RabbitMQ
* DynamoDB Local

#### Application properties (with default values)
* server.port=8081
* dynamodb.users.uri=http://localhost:8000
* dynamodb.users.tableName=Users
* rabbitmq.host.uri=amqp://localhost:5672
* rabbitmq.host.username=user
* rabbitmq.host.password=bitnami
* rabbitmq.order.exchangeName=application_exchange
* rabbitmq.order.exchangeType=topic
* rabbitmq.order.orderCreatedQueueName=order_created_queue
* rabbitmq.order.orderCreatedRoutingKey=order-created-event
* rabbitmq.order.orderStatusChangedQueueName=order_status_changed_queue
* rabbitmq.order.orderStatusChangedRoutingKey=order-status-changed-event

#### Executable file
* UserApplication

#### Use Swagger-ui to access each microservice
> http://[user-microservice-host]:[port]/swagger-ui.html
