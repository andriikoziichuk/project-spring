package com.eproject.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {

    @Size(min = 3, max = 15, message = "First name should have 3-15 characters!")
    private String firstName;
    @Size(min = 3, max = 15, message = "Second name should have 3-15 characters!")
    private String lastName;
    private String username;
    @Size(min = 5, max = 20, message = "Password should have 5-20 characters!")
    private String password;
    private String repeatPassword;
}
