package com.myProject.myProject.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for Validation user form
 */
@Getter
@Setter
public class UserDto {
    @NotEmpty(message = "username is required")
    private String username;
    @NotEmpty(message = "password is required")
    private String password;
    @NotEmpty(message = "full name is required")
    private String fullName;
    @NotEmpty(message = "roles is required")
    private List<Role> roles = new ArrayList<>();

}
