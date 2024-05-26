package com.myProject.myProject.validation;

import com.myProject.myProject.model.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class RolesValidator implements ConstraintValidator<ValidRoles, List<Role>> {
    @Override
    public void initialize(ValidRoles constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Role> roles, ConstraintValidatorContext context) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }

        if (roles.size() > 2) {
            return false;
        }

        for (Role role : roles) {
            String roleName = role.getName();
            if (!roleName.equals("1") && !roleName.equals("2") && !roleName.equals("1,2") && !roleName.equals("2,1")) {
                return false;
            }
        }

        return true;
    }
}
