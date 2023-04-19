package com.atn.digital.user.adapters.in.web;

import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;
import com.atn.digital.user.domain.ports.in.queries.FindUserByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindUserByIdController {

    private final FindUserByIdQuery query;

    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<UserDto> findByUserId(@PathVariable("userId") String userId) {
        User user = query.findByUserId(new UserId(userId));
        UserDto userDto = new UserDto(
                user.getId().get().getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }
}
