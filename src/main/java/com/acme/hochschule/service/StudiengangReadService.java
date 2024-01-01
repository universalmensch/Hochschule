package com.acme.hochschule.service;

import com.acme.hochschule.entity.FakultaetType;
import com.acme.hochschule.entity.Studiengang;
import com.acme.hochschule.repository.HochschuleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Anwendungslogik zum Laden und Übertragen von Datensätzen.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudiengangReadService {
    private final HochschuleRepository repo;

    /**
     * Anwendungslogik zum Lesen eines Datensatzes.
     *
     * @param fakultaet des gewünschten Datensatzes.
     *
     * @return Datensatz eines Studiengangs oder leeres Ergebnis.
     */
    public @NonNull Studiengang findbyfakultatet(final FakultaetType fakultaet) {
        log.debug("findbyfakultatet: id={}", fakultaet);
        final var studiengang = repo.findbyfakultaet(fakultaet)
            .orElseThrow(() -> new NotFoundException(fakultaet));
        log.debug("findbyfakultatet: {}", studiengang);
        return studiengang;
    }
}
