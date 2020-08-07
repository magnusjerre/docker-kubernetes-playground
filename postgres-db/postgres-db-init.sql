CREATE TABLE public.person
(
    id integer NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.person
    OWNER to postgres;


INSERT INTO person VALUES(1, 'Sofia');
INSERT INTO person VALUES(2, 'Magnus');
