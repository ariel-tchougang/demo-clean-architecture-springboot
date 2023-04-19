package com.atn.digital.user.domain.ports.out.persistence;

import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.models.User.UserId;

public class NeverFindUserByIdPort implements FindUserByIdPort {
    public User findByUserId(UserId userId) { return null; }
}
