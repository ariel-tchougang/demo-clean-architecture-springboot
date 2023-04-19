# Order microservices application
> Using *Hexagonal (Ports & Adapters) Architecture & TDD*

This microservice has 3 modules:
* order-domain: where all use cases are described along with all inbound and outbound ports
* order-adapters-out-persistence-dynamodb: outbound adapter for DynamoDB
* order-application-springboot: hosting the executable OrderApplication

## Inbound ports

#### From web interactions
* Create new order
* Get order current status
* Get order status change history

#### From messaging (RabbitMQ) interactions
* Update order status: from InsufficientStockForOrderEvent coming from inventory-microservice => CANCELLED
* Update order status: from SufficientStockForOrderEvent coming from inventory-microservice => INVENTORY_CHECK_OK
* Update order status: from PaymentFailedEvent coming from payment-microservice => CANCELLED
* Update order status: from PaymentCompletedEvent coming from payment-microservice => PAYMENT_COMPLETED
* Update order status: from ShipmentCreatedEvent coming from shipment-microservice => PROCESSING_IN_PROGRESS
* Update order status: from ShipmentStatusChangedEvent coming from shipment-microservice => PENDING_DELIVERY, DELIVERED

## Outbound ports

#### To a repository (DynamoDB)
* Create new order
* Load order by id
* Update order status

#### To a communication broker (RabbitMQ)
* Publish OrderCreatedEvent
* Publish OrderStatusChangedEvent

#### Different order status
* CREATED 
  * => triggers inventory check
* INVENTORY_CHECK_OK
  * => triggers process payment, 
  * => triggers create order copy on user
* PAYMENT_COMPLETED 
  * => triggers create shipment,
  * => triggers update order status on user
* PROCESSING_IN_PROGRESS
  * => triggers update order status on user
* PENDING_DELIVERY
  * => triggers update order status on user
* DELIVERED
  * => triggers update order status on user
* CANCELLED
  * => triggers update order status on user
* UNKNOWN
  * => triggers update order status on user

## Installing / Getting started

#### Depends on
* RabbitMQ
* DynamoDB Local

#### Application properties (with default values)
* server.port=8082
* dynamodb.orders.uri=http://localhost:8000
* dynamodb.orders.tableName=Orders
* rabbitmq.host.uri=amqp://localhost:5672
* rabbitmq.host.username=user
* rabbitmq.host.password=bitnami
* rabbitmq.order.exchangeName=application_exchange
* rabbitmq.order.exchangeType=topic
* rabbitmq.order.orderCreatedQueueName=order_created_queue
* rabbitmq.order.orderCreatedRoutingKey=order-created-event
* rabbitmq.order.orderStatusChangedQueueName=order_status_changed_queue
* rabbitmq.order.orderStatusChangedRoutingKey=order-status-changed-event
* rabbitmq.inventory.exchangeName=application_exchange
* rabbitmq.inventory.exchangeType=topic
* rabbitmq.inventory.sufficientStockQueueName=sufficient_stock_queue
* rabbitmq.inventory.sufficientStockRoutingKey=sufficient-stock-event
* rabbitmq.inventory.insufficientStockQueueName=insufficient_stock_queue
* rabbitmq.inventory.insufficientStockRoutingKey=insufficient-stock-event
* rabbitmq.payment.exchangeName=application_exchange
* rabbitmq.payment.exchangeType=topic
* rabbitmq.payment.paymentCompletedQueueName=payment_completed_queue
* rabbitmq.payment.paymentCompletedRoutingKey=payment-completed-event
* rabbitmq.payment.paymentFailedQueueName=payment_failed_queue
* rabbitmq.payment.paymentFailedRoutingKey=payment-failed-event
* rabbitmq.shipment.exchangeName=application_exchange
* rabbitmq.shipment.exchangeType=topic
* rabbitmq.shipment.shipmentCreatedQueueName=shipment_created_queue
* rabbitmq.shipment.shipmentCreatedRoutingKey=shipment-created-event
* rabbitmq.shipment.shipmentStatusChangedQueueName=shipment_status_changed_queue
* rabbitmq.shipment.shipmentStatusChangedRoutingKey=shipment-status-changed-event

#### Executable file
* OrderApplication

#### Use Swagger-ui to access each microservice
> http://[order-microservice-host]:[port]/swagger-ui.html
