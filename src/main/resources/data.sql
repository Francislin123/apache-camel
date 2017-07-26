-- Move it to Liquibase
INSERT INTO partnership VALUES ('COMPARADOR');
INSERT INTO partnership VALUES ('AFILIADO');
INSERT INTO partnership VALUES ('SEM');
INSERT INTO partnership VALUES ('EMAIL_MARKETING');
INSERT INTO partnership VALUES ('B2B');
INSERT INTO partnership VALUES ('RETARGETING');
INSERT into feed (id, reference, name, partner_id, creation_date, update_date, flag_active) VALUES (random_uuid(),'google-full', 'google full', 'AAA111' , CURRENT_DATE(),CURRENT_DATE(), true)
INSERT into feed (id,reference, name, partner_id, creation_date, update_date, flag_active) VALUES (random_uuid(),'google-2', 'google 2', 'AA2222' , CURRENT_DATE(),CURRENT_DATE(), true)
INSERT into feed (id,reference, name, partner_id, creation_date, update_date, flag_active) VALUES (random_uuid(),'google-full', 'google 3', 'AAA333' , CURRENT_DATE(),CURRENT_DATE(), true)
INSERT into feed (id,reference, name, partner_id, creation_date, update_date, flag_active) VALUES (random_uuid(),'google-full', 'google 4', 'AAA333' , CURRENT_DATE(),CURRENT_DATE(), true)
INSERT into feed (id,reference, name, partner_id, creation_date, update_date, flag_active) VALUES (random_uuid(),'google-full', 'google 5', 'AAA333' , CURRENT_DATE(),CURRENT_DATE(), true)
INSERT into feed (id,reference, name, partner_id, creation_date, update_date, flag_active) VALUES (random_uuid(),'google-full', 'google 6', 'AAA333' , CURRENT_DATE(),CURRENT_DATE(), true)
INSERT into feed (id,reference, name, partner_id, creation_date, update_date, flag_active) VALUES (random_uuid(),'google-full', 'google 7', 'AAA777', CURRENT_DATE(),CURRENT_DATE(), false)