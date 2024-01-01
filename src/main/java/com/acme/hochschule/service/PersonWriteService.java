package com.acme.hochschule.service;

import com.acme.hochschule.entity.Person;
import com.acme.hochschule.repository.HochschuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;
import java.util.Objects;
import java.util.UUID;

/**
 * Schreibservice des Repositorys.
 * <img src="../../../../../asciidoc/PersonWriteService.svg" alt="Klassendiagramm">
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonWriteService {
    private final HochschuleRepository repo;

    private final Validator validator;

    /**
     * Legt eine neue Person an.
     *
     * @param person anzulegende Person.
     * @return neu angelegte Person.
     */
    public Person create(final Person person) {
        log.debug("create: {}", person);

        final var violations = validator.validate(person);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        if (repo.existsByEmail(person.getEmail())) {
            throw new EmailExistsException(person.getEmail());
        }

        final var personDB = repo.save(person);
        log.debug("create: {}", personDB);
        return personDB;
    }

    /**
     * Aktualisiert eine Person.
     *
     * @param person neue Werte des Datensatzes.
     * @param id ID des zu aktualisierenden Datensatzes.
     * @param version des Datensatzes.
     */
    public void update(final Person person, final UUID id, final int version) {
        log.debug("update: {}", person);
        log.debug("update: id={}", id);

        final var violations = validator.validate(person);
        if (!violations.isEmpty()) {
            log.debug("update: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        final var personDbOptional = repo.findById(id);
        if (personDbOptional.isEmpty()) {
            throw new NotFoundException(id);
        }

        final var personDb = personDbOptional.get();
        log.trace("update: version={}, kundeDb={}", version, personDb);
        if (version != personDb.getVersion()) {
            throw new VersionOutdatedException(version);
        }

        final var email = person.getEmail();
        if (!Objects.equals(email, personDb.getEmail()) && repo.existsByEmail(email)) {
            log.debug("update: email {} existiert", email);
            throw new EmailExistsException(email);
        }

        personDb.set(person);
        repo.save(personDb);
        log.debug("update: {}", personDb);
    }
}
