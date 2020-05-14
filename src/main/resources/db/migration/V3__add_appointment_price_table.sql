DROP TABLE IF EXISTS appointment_slots CASCADE;

CREATE TABLE public.appointment_price (
    id bigint NOT NULL,
    currency character varying(255),
    minutes integer,
    price_cents bigint
);


ALTER TABLE public.appointment_price OWNER TO cabuser;


CREATE SEQUENCE public.appointment_price_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.appointment_price_id_seq OWNER TO cabuser;


ALTER SEQUENCE public.appointment_price_id_seq OWNED BY public.appointment_price.id;


ALTER TABLE ONLY public.appointment_price ALTER COLUMN id SET DEFAULT nextval('public.appointment_price_id_seq'::regclass);


SELECT pg_catalog.setval('public.appointment_price_id_seq', 1, false);


ALTER TABLE ONLY public.appointment_price
    ADD CONSTRAINT appointment_price_pkey PRIMARY KEY (id);

