ALTER TABLE ONLY public."user"
    DROP CONSTRAINT uk_sb8bbouer5wak8vyiiy4pf2bx;
ALTER TABLE ONLY public."user"
    DROP CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe;

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email, deleted);
ALTER TABLE ONLY public."user"
    ADD CONSTRAINT uk_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (username, deleted);

