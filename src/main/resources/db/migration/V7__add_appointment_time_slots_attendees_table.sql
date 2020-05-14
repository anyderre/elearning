CREATE TABLE public.appointment_time_slots_attendees (
    time_slot_id bigint NOT NULL,
    attendees_id bigint NOT NULL
);


ALTER TABLE public.appointment_time_slots_attendees OWNER TO cabuser;

ALTER TABLE ONLY public.appointment_time_slots_attendees
    ADD CONSTRAINT uk_212voypq0ggct0r2qhc3ncnin UNIQUE (attendees_id);


ALTER TABLE ONLY public.appointment_time_slots_attendees
    ADD CONSTRAINT fkia0fbuwvudni1bo8o7hmmux21 FOREIGN KEY (time_slot_id) REFERENCES public.appointment_time_slots(id);


ALTER TABLE ONLY public.appointment_time_slots_attendees
    ADD CONSTRAINT fkm3tybjhvx7oqsi5q99j77s6x8 FOREIGN KEY (attendees_id) REFERENCES public.attendees(id);


