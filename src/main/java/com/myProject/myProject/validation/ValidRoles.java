package com.myProject.myProject.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RolesValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoles {
    String message() default "1 or 2 or 1,2 - templates";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
