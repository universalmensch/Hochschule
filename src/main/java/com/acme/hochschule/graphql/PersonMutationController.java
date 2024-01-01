package com.acme.hochschule.graphql;

import com.acme.hochschule.entity.Person;
import com.acme.hochschule.service.ConstraintViolationsException;
import com.acme.hochschule.service.EmailExistsException;
import com.acme.hochschule.service.PersonWriteService;
import com.acme.hochschule.service.StudiengangReadService;
import graphql.GraphQLError;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;

/**
 * Eine Controller-Klasse für das Schreiben mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
class PersonMutationController {
    private final PersonWriteService service;
    private final StudiengangReadService studiengangservice;

    /**
     * Eine neue Person anlegen.
     *
     * @param input Die Eingabedaten für eine neue Person
     * @return Die generierte ID für die neue Person als Payload
     */
    @MutationMapping
    CreatePayload create(@Argument final PersonInput input) {
        log.debug("create: input={}", input);
        final var personf = input.toPerson();
        personf.setFakultaet(studiengangservice.findbyfakultatet(personf.getFakultaet().getFakultaet()));
        final var id = service.create(personf).getId();
        log.debug("create: id={}", id);
        return new CreatePayload(id);
    }

    @GraphQlExceptionHandler
    GraphQLError handleEmailExists(final EmailExistsException ex) {
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message("Die Emailadresse " + ex.getEmail() + " existiert bereits.")
            .build();
    }

    @GraphQlExceptionHandler
    GraphQLError handleDateTimeParseException(final DateTimeParseException ex) {
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message("Das Datum " + ex.getParsedString() + " ist nicht korrekt.")
            .build();
    }

    @GraphQlExceptionHandler
    Collection<GraphQLError> handleConstraintViolations(final ConstraintViolationsException ex) {
        return ex.getViolations()
            .stream()
            .map(this::violationToGraphQLError)
            .collect(Collectors.toList());
    }

    private GraphQLError violationToGraphQLError(final ConstraintViolation<Person> violation) {
        final List<Object> path = new ArrayList<>(5);
        path.add("input");
        for (final Path.Node node: violation.getPropertyPath()) {
            path.add(node.toString());
        }
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message(violation.getMessage())
            .path(path)
            .build();
    }
}
