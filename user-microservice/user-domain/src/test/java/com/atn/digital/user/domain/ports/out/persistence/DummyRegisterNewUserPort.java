package com.atn.digital.user.domain.ports.out.persistence;

import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;

import java.util.Random;
import java.util.UUID;

public class DummyRegisterNewUserPort implements RegisterNewUserPort {

    public UserId registerNewUser(User user) {
        return User.withId(
                new UserId(UUID.randomUUID().toString()),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        ).getId().get();
    }
}
