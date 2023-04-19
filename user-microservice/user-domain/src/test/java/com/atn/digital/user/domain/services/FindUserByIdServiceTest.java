package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.exceptions.UserNotFoundException;
import com.atn.digital.user.domain.models.User.UserId;
import com.atn.digital.user.domain.ports.in.queries.FindUserByIdQuery;
import com.atn.digital.user.domain.ports.out.persistence.AlwaysFindUserByIdPort;
import com.atn.digital.user.domain.ports.out.persistence.FindUserByIdPort;
import com.atn.digital.user.domain.ports.out.persistence.NeverFindUserByIdPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FindUserByIdServiceTest {
    private final FindUserByIdPort alwaysFindUserByIdPort = new AlwaysFindUserByIdPort();
    private final FindUserByIdQuery alwaysFindUserByIdQuery = new FindUserByIdService(alwaysFindUserByIdPort);
    private final FindUserByIdPort neverFindUserByIdPort = new NeverFindUserByIdPort();
    private final FindUserByIdQuery neverFindUserByIdQuery = new FindUserByIdService(neverFindUserByIdPort);

    @Test
    void shouldReturnUserWhenUserIdExists() {
        Assertions.assertNotNull(alwaysFindUserByIdQuery.findByUserId(new UserId("id")));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIdDoesntExist() {
        Assertions.assertThrows(UserNotFoundException.class,
                () -> neverFindUserByIdQuery.findByUserId(new UserId("id")));
    }
}
