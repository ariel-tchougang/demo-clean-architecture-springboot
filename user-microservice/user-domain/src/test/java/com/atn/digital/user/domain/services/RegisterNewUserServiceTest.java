package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.models.User.UserId;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserCommand;
import com.atn.digital.user.domain.ports.out.persistence.DummyRegisterNewUserPort;
import com.atn.digital.user.domain.ports.out.persistence.RegisterNewUserPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegisterNewUserServiceTest {

    private RegisterNewUserPort port = new DummyRegisterNewUserPort();
    private final RegisterNewUserService service = new RegisterNewUserService(port);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.handle(null));
    }

    @Test
    void shouldReturnNewRegisteredUserId() {
        RegisterNewUserCommand registerNewUserCommand = new RegisterNewUserCommand("First", "Last",
                "first.last@unit.test");
        UserId bean = service.handle(registerNewUserCommand);
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(bean.getId());
    }
}
