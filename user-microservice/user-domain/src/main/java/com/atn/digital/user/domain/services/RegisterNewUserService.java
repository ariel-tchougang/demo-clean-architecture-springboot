package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;
import com.atn.digital.user.domain.ports.out.persistence.RegisterNewUserPort;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserUseCase;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserCommand;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RegisterNewUserService implements RegisterNewUserUseCase {

    private final RegisterNewUserPort registerNewUserPort;

    public UserId handle(RegisterNewUserCommand registerNewUserCommand) {

        if (registerNewUserCommand == null) {
            throw new IllegalArgumentException("Expected registerNewUserCommand to be not null!");
        }

        return registerNewUserPort.registerNewUser(
                User.withoutId(
                    registerNewUserCommand.getFirstName(),
                    registerNewUserCommand.getLastName(),
                    registerNewUserCommand.getEmail()
                ));
    }
}
