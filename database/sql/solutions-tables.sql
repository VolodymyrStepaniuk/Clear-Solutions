DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    id               bigint                                              NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    email            character varying(255) COLLATE pg_catalog."default",
    first_name       character varying(255) COLLATE pg_catalog."default" NOT NULL,
    last_name        character varying(255) COLLATE pg_catalog."default" NOT NULL,
    birth_date       date                                                NOT NULL,
    address          character varying(255) COLLATE pg_catalog."default",
    phone_number     character varying(255) COLLATE pg_catalog."default",
    created_at       timestamp(6) with time zone                         NOT NULL DEFAULT NOW(),
    last_modified_at timestamp(6) with time zone                         NOT NULL DEFAULT NOW(),
    CONSTRAINT users_pkey PRIMARY KEY (id)
    )
    TABLESPACE pg_default;

ALTER SEQUENCE IF EXISTS public.users_id_seq
    OWNED by public.users.id;