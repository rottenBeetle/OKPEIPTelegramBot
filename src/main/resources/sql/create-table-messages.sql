drop table messages;

CREATE TABLE messages
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    group_name VARCHAR(10),
    message    text
);