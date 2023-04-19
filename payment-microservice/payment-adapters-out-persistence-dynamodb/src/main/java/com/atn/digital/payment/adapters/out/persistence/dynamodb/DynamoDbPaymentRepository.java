package com.atn.digital.payment.adapters.out.persistence.dynamodb;

import com.atn.digital.payment.adapters.out.persistence.PaymentRepositoryAdapter;
import com.atn.digital.payment.domain.models.PaymentDetails;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DynamoDbPaymentRepository extends PaymentRepositoryAdapter {

    private final DynamoDbClient client;

    private final PaymentDetailsMapper mapper = new PaymentDetailsMapper();

    public DynamoDbPaymentRepository(DynamoDbClient client) {
        this.client = client;
    }

    public String save(PaymentDetails paymentDetails) {
        DynamoDbTable<PaymentDetailsItem> table = getPaymentDetailsItemDynamoDbTable();
        PaymentDetailsItem dbEntity = mapper.toPaymentDetailsItem(paymentDetails);
        dbEntity.setId(UUID.randomUUID().toString());
        dbEntity.setInstant(Instant.now().toEpochMilli());
        table.putItem(dbEntity);
        return dbEntity.getId();
    }

    private DynamoDbTable<PaymentDetailsItem> getPaymentDetailsItemDynamoDbTable() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build()
                .table(System.getProperty("PAYMENT_TABLE"), TableSchema.fromBean(PaymentDetailsItem.class));
    }

    public Optional<PaymentDetails> findPaymentDetailsById(String id) {
        DynamoDbTable<PaymentDetailsItem> table = getPaymentDetailsItemDynamoDbTable();
        PaymentDetailsItem dbEntity = table.getItem(Key.builder().partitionValue(id).build());
        return dbEntity == null ? Optional.empty() : Optional.of(mapper.toPaymentDetails(dbEntity));
    }
}
