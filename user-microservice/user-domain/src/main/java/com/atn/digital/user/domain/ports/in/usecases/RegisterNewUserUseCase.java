package com.atn.digital.user.domain.ports.in.usecases;

import com.atn.digital.user.domain.models.User.UserId;

public interface RegisterNewUserUseCase {
    UserId handle(RegisterNewUserCommand registerNewUserCommand);
}
