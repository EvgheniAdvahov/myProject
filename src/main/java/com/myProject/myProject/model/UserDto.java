package com.myProject.myProject.model;

import com.myProject.myProject.validation.ValidRoles;
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

    private int id;
    @NotEmpty(message = "username is required")
    private String username;
    @NotEmpty(message = "password is required")
    private String password;
    @NotEmpty(message = "full name is required")
    private String fullName;

    @NotEmpty(message = "roles is required")
    @ValidRoles
    private List<Role> roles = new ArrayList<>();

}
