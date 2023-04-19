package com.atn.digital.user.domain.ports.in.usecases;

import com.atn.digital.commons.validation.SelfValidating;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class RegisterNewUserCommand extends SelfValidating<RegisterNewUserCommand> {

    @NotNull
    @NotBlank
    private final String firstName;

    @NotNull
    @NotBlank
    private final String lastName;

    @NotNull
    @NotEmpty
    @Email
    private final String email;

    public RegisterNewUserCommand(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.validateSelf();
    }
}
