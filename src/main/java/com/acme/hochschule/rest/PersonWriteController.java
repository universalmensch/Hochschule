package com.acme.hochschule.rest;

import com.acme.hochschule.service.ConstraintViolationsException;
import com.acme.hochschule.service.EmailExistsException;
import com.acme.hochschule.service.PersonWriteService;
import com.acme.hochschule.service.StudiengangReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.acme.hochschule.rest.PersonGetController.ID_PATTERN;
import static com.acme.hochschule.rest.PersonGetController.REST_PATH;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.HttpStatus.PRECONDITION_REQUIRED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/**
 * Legt Datensätze an.
 * <img src="../../../../../asciidoc/PersonWriteController.svg" alt="Klassendiagramm">
 */
@Controller
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ClassFanOutComplexity")
public class PersonWriteController {

    private static final String VERSIONSNUMMER_FEHLT = "Versionsnummer Fehlt";
    private final PersonWriteService service;
    private final StudiengangReadService studiengangservice;

    private final UriHelper uriHelper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Eine neue Person anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Person neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    ResponseEntity<Void> create(@RequestBody final PersonDTO personDTO, final HttpServletRequest request) {
        log.debug("create: {}", personDTO);

        final var personf = personDTO.toPerson();
        personf.setFakultaet(studiengangservice.findbyfakultatet(personf.getFakultaet().getFakultaet()));
        final var person = service.create(personf);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(baseUri + '/' + person.getId());
        return created(location).build();
    }

    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Eine Person mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Person nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    void update(@PathVariable final UUID id, @RequestBody final PersonDTO personDTO, @RequestHeader("If-Match") final Optional<String> version
    ) {
        log.debug("update: id={}, {}", id, personDTO);
        final int versionInt = getVersion(version);
        service.update(personDTO.toPerson(), id, versionInt);
    }

    @SuppressWarnings({"MagicNumber", "RedundantSuppression"})
    private int getVersion(final Optional<String> versionOpt) {
        log.trace("getVersion: {}", versionOpt);
        if (versionOpt.isEmpty()) {
            throw new VersionInvalidException(
                PRECONDITION_REQUIRED,
                VERSIONSNUMMER_FEHLT);
        }

        final var versionStr = versionOpt.get();
        if (versionStr.length() < 3 ||
            versionStr.charAt(0) != '"' ||
            versionStr.charAt(versionStr.length() - 1) != '"') {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                "Ungueltiges ETag " + versionStr
            );
        }

        final int version;
        try {
            version = Integer.parseInt(versionStr.substring(1, versionStr.length() - 1));
        } catch (final NumberFormatException ex) {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                "Ungueltiges ETag " + versionStr,
                ex
            );
        }

        log.trace("getVersion: version={}", version);
        return version;
    }

    @ExceptionHandler
    ProblemDetail onConstraintViolations(final ConstraintViolationsException ex) {
        log.debug("onConstraintViolations: {}", ex.getMessage());

        final var personViolations = ex.getViolations()
            .stream()
            .map(violation -> violation.getPropertyPath() + ": " +
                violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName() + " " +
                violation.getMessage())
            .toList();
        log.trace("onConstraintViolations: {}", personViolations);
        final String detail;
        if (personViolations.isEmpty()) {
            detail = "N/A";
        } else {
            final var violationsStr = personViolations.toString();
            detail = violationsStr.substring(1, violationsStr.length() - 2);
        }

        return ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
    }

    @ExceptionHandler
    ProblemDetail onEmailExists(final EmailExistsException ex) {
        log.debug("onEmailExists: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
    }
}
