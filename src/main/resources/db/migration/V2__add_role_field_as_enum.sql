ALTER TABLE rol ADD COLUMN role VARCHAR(255);
update rol set role='ROLE_ADMIN' where description='ROLE_ADMIN';

update rol set role='ROLE_ADMIN' where description='ROLE_ADMIN';
update rol set role='ROLE_SCHOOL' where description='ROLE_SCHOOL';
update rol set role='ROLE_PROFESSOR' where description='ROLE_PROFESSOR';

update rol set role='ROLE_STUDENT' where description='ROLE_STUDENT';
update rol set role='ROLE_FREELANCER' where description='ROLE_FREELANCER';
update rol set role='ROLE_FREE_STUDENT' where description='ROLE_FREE_STUDENT';

update rol set role='ROLE_ORGANIZATION' where description='ROLE_ORGANIZATION';
update rol set role='ROLE_EMPLOYEE' where description='ROLE_EMPLOYEE';

update rol set role='ROLE_SUPER_ADMIN' where description='ROLE_SUPER_ADMIN';
update rol set role='ROLE_INSTRUCTOR' where description='ROLE_INSTRUCTOR';