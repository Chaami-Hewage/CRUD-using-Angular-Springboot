insert into tags (id, tag) values (0, 'UNDEFINED');
insert into tags (id, tag) values (1, 'TECHNOLOGY');
insert into tags (id, tag) values (2, 'BUSINESS');
insert into tags (id, tag) values (3, 'MUSIC');
insert into tags (id, tag) values (4, 'ENGINEERING');
insert into tags (id, tag) values (5, 'AGRICULTURE');
insert into tags (id, tag) values (6, 'SPACE');
insert into tags (id, tag) values (7, 'SCIENCE');
insert into tags (id, tag) values (8, 'POLITICS');
insert into tags (id, tag) values (9, 'MEDICINE');
insert into tags (id, tag) values (10, 'TRAVELLING');

insert into accounts (id, first_name, last_name, email, password, phone, date_created, image_url, role, tag_id) values (1, 'Admin', 'Admin', 'admin@admin.com', '$2a$10$QZ2GJNCiLgx8RhUEXUbWje5BGkBvHGOqLe7JtRVs8ZZm4hjlIBVDe', '+94771234567', '09/05/2023', null, 'ADMIN', 0);
insert into accounts (id, first_name, last_name, email, password, phone, date_created, image_url, role, tag_id) values (2, 'Regular', 'User One', 'regularuser1@gmail.com', '$2a$10$jW1t8pqOStmxhQIB.T4XaOJoWodCZqCYNIib9X4cunCv98nAmdU7e', '+94771111111', '09/05/2023', null, 'REGULAR_USER', 1);
insert into accounts (id, first_name, last_name, email, password, phone, date_created, image_url, role, tag_id) values (3, 'Premium', 'User One', 'premiumuser1@gmail.com', '$2a$10$jW1t8pqOStmxhQIB.T4XaOJoWodCZqCYNIib9X4cunCv98nAmdU7e', '+94772222222', '09/05/2023', null, 'PREMIUM_USER', 0);
