package com.atn.digital.user.adapters.out.persistence.dynamodb;

import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;

public class UserEntityMapper {

    public User toUser(UserEntity userEntity) {
        return userEntity == null ? null : User.withId(
                new UserId(userEntity.getId()),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail());
    }

    public UserEntity toUserEntity(User user) {
        if (user == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId().get().getId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        return userEntity;
    }
}
