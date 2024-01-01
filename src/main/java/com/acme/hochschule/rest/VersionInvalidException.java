package com.acme.hochschule.rest;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

/**
 * Exception, falls die Versionsnummer im ETag fehlt oder syntaktisch ungültig ist.
 */
class VersionInvalidException extends ErrorResponseException {
    VersionInvalidException(final HttpStatusCode status, final String message) {
        this(status, message, null);
    }

    VersionInvalidException(
        final HttpStatusCode status,
        final String message,
        final Throwable cause
    ) {
        super(status, asProblemDetail(status, message), cause);
    }

    private static ProblemDetail asProblemDetail(final HttpStatusCode status, final String detail) {
        return ProblemDetail.forStatusAndDetail(status, detail);
    }
}
