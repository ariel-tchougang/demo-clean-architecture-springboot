package com.atn.digital.user.domain.ports.out.persistence;

import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;

public class AlwaysFindUserByIdPort implements FindUserByIdPort {
    public User findByUserId(UserId userId) {
        return User.withId(userId, "firstName", "lastName", "email@unit.test");
    }
}
