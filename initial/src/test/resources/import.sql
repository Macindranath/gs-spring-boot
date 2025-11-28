-- Initial roles
insert into role (`name`) values ('ROLE_MANAGER');
insert into role (`name`) values ('ROLE_MEMBER');

-- Initial super user
replace into manager (`name`, `email`, `password`, `phone_number`) values ('Super User', 'sadmin@tenniscms.com', '$2a$10$U/OcYwXe1OvJB2cQjzVDHO/P0krLAwzY1xpvbUkPE4vsHMPPNK7WK', '000-000-0000');

-- Initial test member
replace into member (`name`, `email`, `password`, `phone_number`) values ('Test Member', 'member@tenniscms.com', '$2a$10$U/OcYwXe1OvJB2cQjzVDHO/P0krLAwzY1xpvbUkPE4vsHMPPNK7WK', '000-000-0000');

update manager set role_id = 1;

update member set role_id = 2;

