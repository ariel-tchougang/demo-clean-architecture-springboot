package com.atn.digital.user.domain.models;

import com.atn.digital.commons.validation.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private final UserId id;

    @Getter
    private final String firstName;

    @Getter
    private final String lastName;

    @Getter
    private final String email;

    public static User withoutId(String firstName, String lastName, String email) {
        return new User(null, firstName, lastName, email);
    }

    public static User withId(UserId id, String firstName, String lastName, String email) {
        return new User(id, firstName, lastName, email);
    }

    public Optional<UserId> getId(){
        return Optional.ofNullable(id);
    }

    public static class UserId extends SelfValidating<UserId> {

        @NotNull
        @NotBlank
        @Getter
        private final String id;

        public UserId(String id) {
            this.id = id;
            this.validateSelf();
        }
    }
}
