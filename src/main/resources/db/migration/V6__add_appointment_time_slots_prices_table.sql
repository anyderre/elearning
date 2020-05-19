CREATE TABLE public.appointment_time_slots_prices (
    time_slot_id bigint NOT NULL,
    prices_id bigint NOT NULL
);


ALTER TABLE public.appointment_time_slots_prices OWNER TO cabuser;

ALTER TABLE ONLY public.appointment_time_slots_prices
    ADD CONSTRAINT uk_95c1t1fpuecax97pd75boku5k UNIQUE (prices_id);

ALTER TABLE ONLY public.appointment_time_slots_prices
    ADD CONSTRAINT fk6xmlwa3le4jngsubdd63nop2j FOREIGN KEY (prices_id) REFERENCES public.appointment_price(id);


ALTER TABLE ONLY public.appointment_time_slots_prices
    ADD CONSTRAINT fkjnaqlv7tt0a16vwjt75s4wjqu FOREIGN KEY (time_slot_id) REFERENCES public.appointment_time_slots(id);


