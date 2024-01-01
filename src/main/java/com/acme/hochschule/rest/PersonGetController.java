package com.acme.hochschule.rest;

import com.acme.hochschule.repository.Leser;
import com.acme.hochschule.service.PersonReadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import static com.acme.hochschule.rest.PersonGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

/**
 * Schnittstelle für Get-Anfragen.
 * <img src="../../../../../asciidoc/PersonGetController.svg" alt="Klassendiagramm">
 */
@Controller
@RequestMapping(REST_PATH)
@ResponseBody
@OpenAPIDefinition(info = @Info(title = "Hochschule API", version = "v1"))
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ClassFanOutComplexity")
public class PersonGetController {

    static final String REST_PATH = "/rest";

    static final String ID_PATTERN =
        "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";

    private final PersonReadService service;
    private final UriHelper uriHelper;

    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit der Personen-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Person gefunden")
    @ApiResponse(responseCode = "404", description = "Person nicht gefunden")
    PersonModel findById(@PathVariable final UUID id, final HttpServletRequest request) {
        log.debug("findById: id={}", id);
        log.debug("findById: Thread={}", Thread.currentThread().getName());

        final var person = service.findById(id);

        final var model = new PersonModel(person);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + person.getId();
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);

        log.debug("findById: {}", model);
        return model;
    }

    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Collection mit den gefundenen Personen")
    @ApiResponse(responseCode = "404", description = "Keine Personen gefunden")
    CollectionModel<? extends PersonModel> find(
        @RequestParam @NonNull final MultiValueMap<String, String> suchkriterien,
        final HttpServletRequest request
    ) {
        log.debug("find: suchkriterien={}", suchkriterien);

        final var baseUri = uriHelper.getBaseUri(request).toString();

        final var models = service.find(suchkriterien)
            .stream()
            .map(person -> {
                final var model = new PersonModel(person);
                model.add(Link.of(baseUri + '/' + person.getId()));
                return model;
            })
            .toList();

        log.debug("find: {}", models);
        return CollectionModel.of(models);
    }

    @GetMapping(path = "/leser/{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit der Leser-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Leser gefunden")
    @ApiResponse(responseCode = "404", description = "Leser nicht gefunden")
    Leser findLeserById(@PathVariable final UUID id) {
        log.debug("findById: id={}", id);
        log.debug("findById: Thread={}", Thread.currentThread().getName());

        final var leser = service.findLeserById(id);

        log.debug("findById: {}", leser);
        return leser;
    }
}
