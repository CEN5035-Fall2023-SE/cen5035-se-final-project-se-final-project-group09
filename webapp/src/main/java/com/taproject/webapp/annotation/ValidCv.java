package com.taproject.webapp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.taproject.webapp.validator.CvFileValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CvFileValidator.class)
public @interface ValidCv {
    String message() default "Invalid File!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
