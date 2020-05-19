CREATE TABLE public.appointment_time_slots (
    id bigint NOT NULL,
    approved_attendees bigint,
    book_before_minutes integer,
    date_from timestamp without time zone,
    date_to timestamp without time zone,
    max_attendees bigint,
    min_minutes bigint,
    status character varying(255),
    type character varying(255),
    video_conference_link character varying(255),
    agreed_price_id bigint,
    suggested_price_id bigint,
    teacher_id bigint
);


ALTER TABLE public.appointment_time_slots OWNER TO cabuser;


CREATE SEQUENCE public.appointment_time_slots_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.appointment_time_slots_id_seq OWNER TO cabuser;


ALTER SEQUENCE public.appointment_time_slots_id_seq OWNED BY public.appointment_time_slots.id;


ALTER TABLE ONLY public.appointment_time_slots ALTER COLUMN id SET DEFAULT nextval('public.appointment_time_slots_id_seq'::regclass);


SELECT pg_catalog.setval('public.appointment_time_slots_id_seq', 1, false);


ALTER TABLE ONLY public.appointment_time_slots
    ADD CONSTRAINT appointment_time_slots_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.appointment_time_slots
    ADD CONSTRAINT fkcdv6cohr8861v8yjl8m7u6h4y FOREIGN KEY (teacher_id) REFERENCES public."user"(id);


ALTER TABLE ONLY public.appointment_time_slots
    ADD CONSTRAINT fkrm9l3rw9t1s47o98lm6odlg1e FOREIGN KEY (suggested_price_id) REFERENCES public.appointment_price(id);


ALTER TABLE ONLY public.appointment_time_slots
    ADD CONSTRAINT fks5ivjyhopk0onwk9d54k81yin FOREIGN KEY (agreed_price_id) REFERENCES public.appointment_price(id);

