package com.atn.digital.user.domain.ports.in.queries;

import com.atn.digital.user.domain.exceptions.UserNotFoundException;
import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;

import java.util.Optional;

public interface FindUserByIdQuery {
    User findByUserId(UserId userId) throws UserNotFoundException;
}
