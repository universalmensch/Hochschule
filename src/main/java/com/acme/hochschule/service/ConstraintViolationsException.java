package com.acme.hochschule.service;

import com.acme.hochschule.entity.Person;
import jakarta.validation.ConstraintViolation;
import java.util.Collection;
import lombok.Getter;

/**
 * Exception, falls es mindestens ein verletztes Constraint gibt.
 */
@Getter
public class ConstraintViolationsException extends RuntimeException {
    /**
     * Die verletzten Constraints.
     */
    private final Collection<ConstraintViolation<Person>> violations;

    ConstraintViolationsException(
        final Collection<ConstraintViolation<Person>> violations
    ) {
        super("Constraints sind verletzt");
        this.violations = violations;
    }
}
