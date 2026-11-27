--liquibase formatted sql

--changeset anyuruf:202611260001
CREATE TABLE anyuruf_geneology.member (
    father_id uuid,
    mother_id uuid,
    id uuid,
    first_name text,
    last_name text,
    description text,
    gender text,
    dob date,
    PRIMARY KEY ((father_id), mother_id, id)
);