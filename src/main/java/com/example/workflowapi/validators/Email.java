package com.example.workflowapi.validators;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
    String message() default "Invalid email format";
    Class<?>[] groups() default {};
}
