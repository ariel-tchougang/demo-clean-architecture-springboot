# Payment microservices application
> Using *Hexagonal (Ports & Adapters) Architecture & TDD*

This microservice has 3 modules:
* payment-domain: where all use cases are described along with all inbound and outbound ports
* payment-adapters-out-persistence-dynamodb: outbound adapter for DynamoDB
* payment-application-springboot: hosting the executable PaymentApplication

## Inbound ports

#### From messaging (RabbitMQ) interactions
* Process payment: from OrderStatusChangedEvent coming from order-microservice

## Outbound ports

#### To a repository (DynamoDB)
* Save Payment details
* Load Payment details

#### To a communication broker (RabbitMQ)
* Publish PaymentCompletedEvent
* Publish PaymentFailedEvent

## Installing / Getting started

#### Depends on
* RabbitMQ
* DynamoDB Local

#### Application properties (with default values)
* server.port=8084
* dynamodb.payments.uri=http://localhost:8000
* dynamodb.payments.tableName=Payments
* rabbitmq.host.uri=amqp://localhost:5672
* rabbitmq.host.username=payment
* rabbitmq.host.password=bitnami
* rabbitmq.payment.exchangeName=application_exchange
* rabbitmq.payment.exchangeType=topic
* rabbitmq.payment.paymentCompletedQueueName=payment_completed_queue
* rabbitmq.payment.paymentCompletedRoutingKey=payment-completed-event
* rabbitmq.payment.paymentFailedQueueName=payment_failed_queue
* rabbitmq.payment.paymentFailedRoutingKey=payment-failed-event
* rabbitmq.order.exchangeName=application_exchange
* rabbitmq.order.exchangeType=topic
* rabbitmq.order.orderStatusChangedQueueName=order_status_changed_queue
* rabbitmq.order.orderStatusChangedRoutingKey=order-status-changed-event

#### Executable file
* PaymentApplication

#### Use Swagger-ui to access each microservice
> http://[payment-microservice-host]:[port]/swagger-ui.html
