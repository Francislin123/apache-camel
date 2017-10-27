-- promotion

INSERT INTO PARTNER(id, name, slug, partnerships, flag_active, creation_date, user_login) VALUES (sys_guid(), 'Zoom', 'zoom', 'comparadores', 1, CURRENT_TIMESTAMP, 'user');

INSERT INTO FEED(id, name, slug, partner_id, template_id, "TYPE", notification_method, notification_format, flag_active, creation_date, user_login) VALUES (
sys_guid(),
'promotion',
'promotion',
(SELECT id FROM PARTNER WHERE slug='zoom'),
(SELECT id FROM FEED_TEMPLATE WHERE slug='default-template'),
'FULL', 'FILE', 'XML',
1, CURRENT_TIMESTAMP, 'user'
);

Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'promotion'), 'utm_source','feed-source');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'promotion'), 'utm_medium','feed-medium');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'promotion'), 'utm_campaign','feed-campaign');

-- collection

INSERT INTO FEED(id, name, slug, COLLECTION_ID, partner_id, template_id, "TYPE", notification_method, notification_format, flag_active, creation_date, user_login) VALUES (
sys_guid(),
'collection',
'collection',
7380,
(SELECT id FROM PARTNER WHERE slug='zoom'),
(SELECT id FROM FEED_TEMPLATE WHERE slug='default-template'),
'FULL', 'FILE', 'XML',
1, CURRENT_TIMESTAMP, 'user'
);

Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'collection'), 'utm_source','feed-source');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'collection'), 'utm_medium','feed-medium');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'collection'), 'utm_campaign','feed-campaign');


-- Taxonomy

Insert into FEEDS_ADMIN.PARTNER_TAXONOMY (ID,SLUG,NAME,PARTNER_ID,FILE_NAME,STATUS,CREATION_DATE,USER_LOGIN) values (
SYS_GUID(), 'zoom','zoom', (SELECT id FROM PARTNER WHERE slug='zoom'),'anyfilename','PROCESSED', CURRENT_TIMESTAMP, 'teste');

Insert into FEEDS_ADMIN.TAXONOMY_MAPPING (ID,PARTNER_TAXONOMY_ID, PARTNER_PATH_ID, WALMART_PATH, PARTNER_PATH, CREATION_DATE, USER_LOGIN) values (
SYS_GUID(), (SELECT id FROM PARTNER_TAXONOMY WHERE slug = 'zoom'), '123','Armas', 'Armas > Rifles', CURRENT_TIMESTAMP,'teste');
Insert into FEEDS_ADMIN.TAXONOMY_MAPPING (ID,PARTNER_TAXONOMY_ID, PARTNER_PATH_ID, WALMART_PATH,PARTNER_PATH,CREATION_DATE, USER_LOGIN) values (
SYS_GUID(), (SELECT id FROM PARTNER_TAXONOMY WHERE slug = 'zoom'), '123','Computadores > NoteBooks','PC > Notebooks', CURRENT_TIMESTAMP,'teste');
Insert into FEEDS_ADMIN.TAXONOMY_MAPPING (ID,PARTNER_TAXONOMY_ID, PARTNER_PATH_ID, WALMART_PATH,PARTNER_PATH,CREATION_DATE, USER_LOGIN) values (
SYS_GUID(), (SELECT id FROM PARTNER_TAXONOMY WHERE slug = 'zoom'), '123','Games > Playstation 3 > Jogos para PS3', 'Consoles > Playstation > Jogos PS3', CURRENT_TIMESTAMP,'teste');

Insert into FEEDS_ADMIN.TAXONOMY_BLACKLIST (ID,NAME,SLUG,CREATION_DATE,UPDATE_DATE,USER_LOGIN) values (
SYS_GUID(),'armas','armas', CURRENT_TIMESTAMP, null,'123e80c6-ee55-48ff-9d1a-f6a7cf902465');
Insert into FEEDS_ADMIN.TAXONOMY_BLACKLIST_MAPPING (ID,TAXONOMY,OWNER,TAXONOMY_BLACKLIST_ID) values (
'589A1DE80B9B40AE9D5CF6F706B34F7C','Armas > Rifles','PARTNER', (SELECT id FROM TAXONOMY_BLACKLIST WHERE slug='armas'));

INSERT INTO FEED(id, name, slug, TAXONOMY_BLACKLIST_ID, partner_id, template_id, "TYPE", notification_method, notification_format, flag_active, creation_date, user_login) VALUES (
sys_guid(),
'taxonomy-blacklist',
'taxonomy-blacklist',
(SELECT id FROM TAXONOMY_BLACKLIST WHERE id = 'zoom'),
(SELECT id FROM PARTNER WHERE slug='zoom'),
(SELECT id FROM FEED_TEMPLATE WHERE slug='default-template'),
'FULL', 'FILE', 'XML',
1, CURRENT_TIMESTAMP, 'user'
);

Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'taxonomy-blacklist'), 'utm_source','feed-source');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'taxonomy-blacklist'), 'utm_medium','feed-medium');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'taxonomy-blacklist'), 'utm_campaign','feed-campaign');

-- Tems

Insert into FEEDS_ADMIN.TERMS_BLACKLIST (ID,SLUG,NAME,CREATION_DATE,UPDATE_DATE,USER_LOGIN) values (
SYS_GUID(),'armas','Armas', CURRENT_TIMESTAMP, null, '123e80c6-ee55-48ff-9d1a-f6a7cf902465');
Insert into FEEDS_ADMIN.TERMS_BLACKLIST_ITEMS (BLACKLIST_ID, TERM) values (
(SELECT id FROM TERMS_BLACKLIST WHERE slug = 'armas'), 'Armas');


INSERT INTO FEED(id, name, slug, partner_id, template_id, "TYPE", notification_method, notification_format, flag_active, creation_date, user_login) VALUES (
sys_guid(),
'terms-blacklist',
'terms-blacklist',
(SELECT id FROM PARTNER WHERE slug='zoom'),
(SELECT id FROM FEED_TEMPLATE WHERE slug='default-template'),
'FULL', 'FILE', 'XML',
1, CURRENT_TIMESTAMP, 'user'
);

Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'terms-blacklist'), 'utm_source','feed-source');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'terms-blacklist'), 'utm_medium','feed-medium');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'terms-blacklist'), 'utm_campaign','feed-campaign');

Insert into FEEDS_ADMIN.TERMS_FEEDS (TERMS_BLACKLIST_ID,FEED_ID) values ((SELECT id FROM TERMS_BLACKLIST WHERE slug = 'armas'), (SELECT id FROM FEED WHERE slug = 'terms-blacklist'));

-- Full Queries

INSERT INTO FEED(id, name, slug, COLLECTION_ID, TAXONOMY_BLACKLIST_ID, partner_id, template_id, "TYPE", notification_method, notification_format, flag_active, creation_date, user_login) VALUES (
sys_guid(),
'full',
'full',
7380,
(SELECT id FROM TAXONOMY_BLACKLIST WHERE id = 'zoom'),
(SELECT id FROM PARTNER WHERE slug='zoom'),
(SELECT id FROM FEED_TEMPLATE WHERE slug='default-template'),
'FULL', 'FILE', 'XML',
1, CURRENT_TIMESTAMP, 'user'
);

Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'full'), 'utm_source','feed-source');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'full'), 'utm_medium','feed-medium');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'full'), 'utm_campaign','feed-campaign');

Insert into FEEDS_ADMIN.TERMS_FEEDS (TERMS_BLACKLIST_ID,FEED_ID) values ((SELECT id FROM TERMS_BLACKLIST WHERE slug = 'armas'), (SELECT id FROM FEED WHERE slug = 'full'));

-- GOOGLE

INSERT INTO FEED(id, name, slug, PARTNER_TAXONOMY_ID, partner_id, template_id, "TYPE", notification_method, notification_format, flag_active, creation_date, user_login) VALUES (
sys_guid(),
'google',
'google',
(SELECT id FROM PARTNER_TAXONOMY WHERE slug='zoom'),
(SELECT id FROM PARTNER WHERE slug='zoom'),
(SELECT id FROM FEED_TEMPLATE WHERE slug='google-xml-template'),
'FULL', 'FILE', 'XML',
1, CURRENT_TIMESTAMP, 'user'
);

Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'google'), 'utm_source','google-pla');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'google'), 'utm_medium','feed-medium');
Insert into FEEDS_ADMIN.FEED_UTMS (FEED_ID,UTM_TYPE,UTM_VALUE) values ((SELECT id FROM FEED WHERE slug = 'google'), 'utm_campaign','feed-campaign');