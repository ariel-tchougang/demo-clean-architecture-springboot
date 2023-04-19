# Inventory microservices application
> Using *Hexagonal (Ports & Adapters) Architecture & TDD*

This microservice has 2 modules:
* inventory-domain: where all use cases are described along with all inbound and outbound ports
* inventory-application-springboot: hosting the executable InventoryApplication

## Inbound ports

#### From web interactions
* Check inventory for order

#### From messaging (RabbitMQ) interactions
* Check inventory for order: from OrderCreatedEvent coming from order-microservice

## Outbound ports

#### To a repository (Dummy code here - no repo behind in this version)
* Load stock item quantity

#### To a communication broker (RabbitMQ)
* Publish InsufficientStockForOrderEvent
* Publish SufficientStockForOrderEvent

## Installing / Getting started

#### Depends on
* RabbitMQ
* DynamoDB Local

#### Application properties (with default values)
* server.port=8083
* rabbitmq.host.uri=amqp://localhost:5672
* rabbitmq.host.username=inventory
* rabbitmq.host.password=bitnami
* rabbitmq.inventory.exchangeName=application_exchange
* rabbitmq.inventory.exchangeType=topic
* rabbitmq.inventory.sufficientStockQueueName=sufficient_stock_queue
* rabbitmq.inventory.sufficientStockRoutingKey=sufficient-stock-event
* rabbitmq.inventory.insufficientStockQueueName=insufficient_stock_queue
* rabbitmq.inventory.insufficientStockRoutingKey=insufficient-stock-event
* rabbitmq.order.exchangeName=application_exchange
* rabbitmq.order.exchangeType=topic
* rabbitmq.order.orderCreatedQueueName=order_created_queue
* rabbitmq.order.orderCreatedRoutingKey=order-created-event

#### Executable file
* InventoryApplication

#### Use Swagger-ui to access each microservice
> http://[inventory-microservice-host]:[port]/swagger-ui.html
