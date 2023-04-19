package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.exceptions.UserNotFoundException;
import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;
import com.atn.digital.user.domain.ports.in.queries.FindUserByIdQuery;
import com.atn.digital.user.domain.ports.out.persistence.FindUserByIdPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class FindUserByIdService implements FindUserByIdQuery {

    private final FindUserByIdPort findUserByIdPort;

    public User findByUserId(UserId userId) {
        User user = findUserByIdPort.findByUserId(userId);

        if (user == null) {
            throw new UserNotFoundException("Couldn't find user with id: " + userId.getId());
        }

        return user;
    }
}
