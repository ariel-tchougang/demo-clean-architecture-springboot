# Shipment microservices application
> Using *Hexagonal (Ports & Adapters) Architecture & TDD*

This microservice has 3 modules:
* shipment-domain: where all use cases are described along with all inbound and outbound ports
* shipment-adapters-out-persistence-dynamodb: outbound adapter for DynamoDB
* shipment-application-springboot: hosting the executable ShipmentApplication

## Inbound ports

#### From web interactions
* Create new shipment
* Update shipment status
* Get shipment current status
* Get shipment status change history

#### From messaging (RabbitMQ) interactions
* Add new order to shipment: from OrderStatusChangedEvent coming from order-microservice
* Update order status: from OrderStatusChangedEvent coming from order-microservice

## Outbound ports

#### To a repository (DynamoDB)
* Create new shipment
* Load shipment by id
* Update shipment status

#### To a communication broker (RabbitMQ)
* Publish ShipmentCreatedEvent
* Publish ShipmentStatusChangedEvent

#### Different shipment status
* CREATED
    * => triggers update order status
* ON_THE_WAY
    * => triggers update order status
* DELIVERED
    * => triggers update order status
* LOST
    * => not yet handled
* UNKNOWN

## Installing / Getting started

#### Depends on
* RabbitMQ
* DynamoDB Local

#### Application properties (with default values)
* server.port=8085
* dynamodb.shipments.uri=http://localhost:8000
* dynamodb.shipments.tableName=Shipments
* rabbitmq.host.uri=amqp://localhost:5672
* rabbitmq.host.username=shipment
* rabbitmq.host.password=bitnami
* rabbitmq.shipment.exchangeName=application_exchange
* rabbitmq.shipment.exchangeType=topic
* rabbitmq.shipment.shipmentCreatedQueueName=shipment_created_queue
* rabbitmq.shipment.shipmentCreatedRoutingKey=shipment-created-event
* rabbitmq.shipment.shipmentStatusChangedQueueName=shipment_status_changed_queue
* rabbitmq.shipment.shipmentStatusChangedRoutingKey=shipment-status-changed-event
* rabbitmq.order.exchangeName=application_exchange
* rabbitmq.order.exchangeType=topic
* rabbitmq.order.orderStatusChangedQueueName=order_status_changed_queue
* rabbitmq.order.orderStatusChangedRoutingKey=order-status-changed-event

#### Executable file
* ShipmentApplication

#### Use Swagger-ui to access each microservice
> http://[shipment-microservice-host]:[port]/swagger-ui.html
