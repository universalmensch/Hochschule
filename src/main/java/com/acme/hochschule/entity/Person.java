package com.acme.hochschule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import static com.acme.hochschule.entity.Person.STUDIENGANG_GRAPH;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

/**
 * Daten einer Person an der Hochschule.
 * <img src="../../../../../asciidoc/Person.svg" alt="Klassendiagramm">
 */
@Entity
@Table(name = "person")
@NamedEntityGraph(name = STUDIENGANG_GRAPH, attributeNodes = @NamedAttributeNode("fakultaet"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
@SuppressWarnings("ClassFanOutComplexity")
public class Person {
    /**
     * NamedEntityGraph für das Attribut "fakultaet".
     */
    public static final String STUDIENGANG_GRAPH = "Person.fakultaet";

    static final String NACHNAME_PATTERN = "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    static final int MAXSIZE = 40;

    /**
     * ID der Person.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @NotNull
    @Pattern(regexp = NACHNAME_PATTERN)
    @Size(max = MAXSIZE)
    private String nachname;

    @Email
    @NotNull
    @Size(max = MAXSIZE)
    private String email;

    @Past
    private LocalDate geburtsdatum;

    @Enumerated(STRING)
    private GeschlechtType geschlecht;

    @ManyToOne(optional = false, fetch = LAZY)
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(updatable = false)
    private Studiengang fakultaet;

    @Enumerated(STRING)
    private BerufType beruf;

    @CreationTimestamp
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    private LocalDateTime aktualisiert;

    @Version
    private int version;

    @Column(name = "leser_id")
    private UUID leser;

    /**
     * Personendaten überschreiben.
     *
     * @param person Neuer Personendatensatz.
     */
    public void set(final Person person) {
        nachname = person.nachname;
        email = person.email;
        geburtsdatum = person.geburtsdatum;
        geschlecht = person.geschlecht;
        beruf = person.beruf;
        leser = person.leser;
    }
}
