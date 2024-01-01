package com.acme.hochschule.repository;

import com.acme.hochschule.entity.FakultaetType;
import com.acme.hochschule.entity.Person;
import com.acme.hochschule.entity.Studiengang;
import com.querydsl.core.types.Predicate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import static com.acme.hochschule.entity.Person.STUDIENGANG_GRAPH;

/**
 * Repository für den DB-Zugriff.
 */
@Repository
public interface HochschuleRepository extends JpaRepository<Person, UUID>, QuerydslPredicateExecutor<Person> {
    @Override
    List<Person> findAll(Predicate predicate);

    @EntityGraph(STUDIENGANG_GRAPH)
    @Override
    Optional<Person> findById(UUID id);

    /**
     * Liefert einen gesuchten Studiengang zurück.
     *
     * @param fakultaet des Studiengangs.
     * @return gesuchter Studiengang.
     */
    @Query("""
        SELECT s
        FROM   Studiengang s
        WHERE  s.fakultaet = :fakultaet
        """)
    Optional<Studiengang> findbyfakultaet(FakultaetType fakultaet);

    /**
     * Person zu gegebener E-Mail aus der DB ermitteln.
     *
     * @param email E-Mail für die Suche
     * @return Optional mit dem gefundenen Kunde oder leeres Optional
     */
    @Query("""
        SELECT p
        FROM   Person p
        WHERE  lower(p.email) LIKE concat(lower(:email), '%')
        """)
    @EntityGraph(STUDIENGANG_GRAPH)
    Optional<Person> findByEmail(String email);

    /**
     * Abfrage, ob es eine Person mit gegebener E-Mail gibt.
     *
     * @param email E-Mail für die Suche
     * @return true, falls es eine solche Person gibt, sonst false
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean existsByEmail(String email);

    /**
     * Person anhand des Nachnamens suchen.
     *
     * @param nachname Der (Teil-) Nachname der gesuchten Person
     * @return Die gefundenen Personen oder eine leere Collection
     */
    @Query("""
        SELECT   p
        FROM     Person p
        WHERE    lower(p.nachname) LIKE concat('%', lower(:nachname), '%')
        ORDER BY p.id
        """)
    @EntityGraph(STUDIENGANG_GRAPH)
    Collection<Person> findByNachname(CharSequence nachname);
}
