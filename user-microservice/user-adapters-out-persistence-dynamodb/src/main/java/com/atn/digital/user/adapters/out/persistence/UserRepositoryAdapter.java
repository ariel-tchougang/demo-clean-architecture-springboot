package com.atn.digital.user.adapters.out.persistence;

import com.atn.digital.user.domain.ports.out.persistence.FindUserByIdPort;
import com.atn.digital.user.domain.ports.out.persistence.RegisterNewUserPort;

public abstract class UserRepositoryAdapter implements RegisterNewUserPort, FindUserByIdPort { }
