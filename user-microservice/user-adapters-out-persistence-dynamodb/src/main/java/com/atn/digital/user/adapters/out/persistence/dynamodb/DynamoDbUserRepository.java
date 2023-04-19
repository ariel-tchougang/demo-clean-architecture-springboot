package com.atn.digital.user.adapters.out.persistence.dynamodb;

import com.atn.digital.user.adapters.out.persistence.UserRepositoryAdapter;
import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.UUID;

public class DynamoDbUserRepository extends UserRepositoryAdapter {

    private final DynamoDbClient client;

    private final UserEntityMapper mapper = new UserEntityMapper();

    public DynamoDbUserRepository(DynamoDbClient client) {
        this.client = client;
    }

    public UserId registerNewUser(User user) {
        DynamoDbTable<UserEntity> table = getUserEntityDynamoDbTable();
        var userId = new UserId(UUID.randomUUID().toString());
        User dbUser = User.withId(
                userId,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());
        table.putItem(mapper.toUserEntity(dbUser));
        return userId;
    }

    UserEntity findByUserId(String uuid) {
        DynamoDbTable<UserEntity> table = getUserEntityDynamoDbTable();
        return table.getItem(Key.builder()
                .partitionValue(uuid)
                .build());
    }

    public User findByUserId(UserId userId) {
        UserEntity userEntity = findByUserId(userId.getId());
        return mapper.toUser(userEntity);
    }

    private DynamoDbTable<UserEntity> getUserEntityDynamoDbTable() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build()
                .table(System.getProperty("USER_TABLE"), TableSchema.fromBean(UserEntity.class));
    }
}
