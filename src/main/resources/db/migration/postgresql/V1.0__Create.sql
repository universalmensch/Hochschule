-- docker compose exec postgres bash
-- psql --dbname=hochschule --username=hochschule --file=/sql/V1.0__Create.sql

-- https://www.postgresql.org/docs/current/sql-createtype.html
-- https://www.postgresql.org/docs/current/datatype-enum.html
-- CREATE TYPE geschlecht AS ENUM ('MAENNLICH', 'WEIBLICH', 'DIVERS');

CREATE TABLE IF NOT EXISTS studiengang (
    id            uuid PRIMARY KEY USING INDEX TABLESPACE hochschulespace,
    fakultaet     varchar(25) CHECK (fakultaet ~ 'WIRTSCHAFTSINFORMATIK|INFORMATIK|BETRIEBSWIRTSCHAFTSLEHRE|VOLKSWIRTSCHAFTSLEHRE') UNIQUE
) TABLESPACE hochschulespace;

CREATE TABLE IF NOT EXISTS person (
    id            uuid PRIMARY KEY USING INDEX TABLESPACE hochschulespace,
    nachname      varchar(40) NOT NULL,
    email         varchar(40) NOT NULL UNIQUE USING INDEX TABLESPACE hochschulespace,
    geburtsdatum  date CHECK (geburtsdatum < current_date),
    geschlecht    varchar(9) CHECK (geschlecht ~ 'MAENNLICH|WEIBLICH'),
    fakultaet_id  uuid REFERENCES studiengang(id),
    beruf         varchar(11) CHECK (beruf ~ 'STUDENT|DOZENT|HAUSMEISTER'),
    erzeugt       timestamp NOT NULL,
    aktualisiert  timestamp NOT NULL,
    version       integer NOT NULL DEFAULT 0,
    leser_id      uuid unique not null
) TABLESPACE hochschulespace;

CREATE INDEX IF NOT EXISTS person_nachname_idx ON person(nachname) TABLESPACE hochschulespace;
