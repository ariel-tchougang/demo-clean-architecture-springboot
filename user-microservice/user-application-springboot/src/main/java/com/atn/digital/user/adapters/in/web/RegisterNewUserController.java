package com.atn.digital.user.adapters.in.web;

import com.atn.digital.user.domain.models.User.UserId;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserCommand;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterNewUserController {

    private final RegisterNewUserUseCase useCase;

    @PostMapping("/api/v1/users")
    public ResponseEntity<UserIdDto> registerNewUser(@RequestBody RegisterNewUserWeb newUserWeb) {
        RegisterNewUserCommand newUserCommand = new RegisterNewUserCommand(
                newUserWeb.getFirstName(),
                newUserWeb.getLastName(),
                newUserWeb.getEmail());
        UserId userId = useCase.handle(newUserCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserIdDto(userId.getId()));
    }
}
