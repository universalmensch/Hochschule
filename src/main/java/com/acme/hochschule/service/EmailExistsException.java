package com.acme.hochschule.service;

import lombok.Getter;

/**
 * Exception, falls die E-Mail-Adresse bereits existiert.
 */
@Getter
public class EmailExistsException extends RuntimeException {
    /**
     * Bereits vorhandene E-Mail-Adresse.
     */
    private final String email;

    EmailExistsException(@SuppressWarnings("ParameterHidesMemberVariable") final String email) {
        super("Die E-Mail-Adresse " + email + " existiert bereits");
        this.email = email;
    }
}
