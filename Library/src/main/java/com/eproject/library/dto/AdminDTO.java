package com.eproject.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data @NoArgsConstructor @AllArgsConstructor
public class AdminDTO {
    @Size(min = 3, max = 10, message = "invalid first name!(3-10 characters)")
    private String firstName;
    @Size(min = 3, max = 10, message = "invalid last name!(3-10 characters)")
    private String lastName;

    private String username;
    @Size(min = 5, max = 15, message = "invalid password!(3-10 characters)")
    private String password;

    private String repeatPassword;
}
