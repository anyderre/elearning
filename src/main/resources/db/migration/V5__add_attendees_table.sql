CREATE TABLE public.attendees (
    id bigint NOT NULL,
    approval_uid character varying(255),
    decline_uid character varying(255),
    status character varying(255),
    time_slot_id bigint,
    user_id bigint
);


ALTER TABLE public.attendees OWNER TO cabuser;

CREATE SEQUENCE public.attendees_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attendees_id_seq OWNER TO cabuser;

ALTER SEQUENCE public.attendees_id_seq OWNED BY public.attendees.id;

ALTER TABLE ONLY public.attendees ALTER COLUMN id SET DEFAULT nextval('public.attendees_id_seq'::regclass);

SELECT pg_catalog.setval('public.attendees_id_seq', 1, false);


ALTER TABLE ONLY public.attendees
    ADD CONSTRAINT attendees_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.attendees
    ADD CONSTRAINT fkaupjpgmyynue74v336j79npf6 FOREIGN KEY (time_slot_id) REFERENCES public.appointment_time_slots(id);



ALTER TABLE ONLY public.attendees
    ADD CONSTRAINT fkdh3vh2fpan817svs5xafsplv7 FOREIGN KEY (user_id) REFERENCES public."user"(id);


