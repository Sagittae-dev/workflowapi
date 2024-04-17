package com.example.workflowapi.validators;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotBlank{
    String message() default "This field can't be empty";
    Class<?>[] groups() default {};
}
