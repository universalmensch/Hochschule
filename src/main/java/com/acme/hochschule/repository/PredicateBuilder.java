package com.acme.hochschule.repository;

import com.acme.hochschule.entity.BerufType;
import com.acme.hochschule.entity.QPerson;
import com.acme.hochschule.entity.GeschlechtType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static java.util.Locale.GERMAN;

/**
 * Singleton-Klasse, um Prädikate durch QueryDSL für eine WHERE-Klausel zu bauen.
 */
@Component
@Slf4j
public class PredicateBuilder {
    /**
     * Prädikate durch QueryDSL für eine WHERE-Klausel zu bauen.
     *
     * @param queryParams als MultiValueMap
     * @return Predicate in QueryDSL für eine WHERE-Klausel
     */
    @SuppressWarnings("ReturnCount")
    public Optional<Predicate> build(final Map<String, ? extends List<String>> queryParams) {
        log.debug("build: queryParams={}", queryParams);

        final var qPerson = QPerson.person;
        final var booleanExprList = queryParams
            .entrySet()
            .stream()
            .map(entry -> toBooleanExpression(entry.getKey(), entry.getValue(), qPerson))
            .toList();
        if (booleanExprList.isEmpty() || booleanExprList.contains(null)) {
            return Optional.empty();
        }

        final var result = booleanExprList
            .stream()
            .reduce(booleanExprList.get(0), BooleanExpression::and);
        return Optional.of(result);
    }

    @SuppressWarnings({"CyclomaticComplexity", "UnnecessaryParentheses"})
    private BooleanExpression toBooleanExpression(
        final String paramName,
        final List<String> paramValues,
        final QPerson qPerson
    ) {
        log.trace("toSpec: paramName={}, paramValues={}", paramName, paramValues);

        if (paramValues == null) {
            return null;
        }

        final var value = paramValues.get(0);
        return switch (paramName) {
            case "nachname" -> nachname(value, qPerson);
            case "email" ->  email(value, qPerson);
            case "geschlecht" -> geschlecht(value, qPerson);
            case "beruf" -> beruf(value, qPerson);
            default -> null;
        };
    }

    private BooleanExpression nachname(final String teil, final QPerson qPerson) {
        return qPerson.nachname.toLowerCase().matches("%" + teil.toLowerCase(GERMAN) + '%');
    }

    private BooleanExpression email(final String teil, final QPerson qPerson) {
        return qPerson.email.toLowerCase().matches("%" + teil.toLowerCase(GERMAN) + '%');
    }

    private BooleanExpression geschlecht(final String geschlecht, final QPerson qPerson) {
        return qPerson.geschlecht.eq(GeschlechtType.of(geschlecht).orElse(null));
    }

    private BooleanExpression beruf(final String beruf, final QPerson qPerson) {
        return qPerson.beruf.eq(BerufType.of(beruf).orElse(null));
    }
}
