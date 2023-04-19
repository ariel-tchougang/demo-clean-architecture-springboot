package com.atn.digital.user.adapters.in.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterNewUserWeb {
    private String firstName;
    private String lastName;
    private String email;
}
