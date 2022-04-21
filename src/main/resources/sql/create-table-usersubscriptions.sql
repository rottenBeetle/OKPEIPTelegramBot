drop table user_subscriptions;

CREATE TABLE user_subscriptions
(
    user_id    BIGINT NOT NULL PRIMARY KEY,
    chat_id    BIGINT NOT NULL,
    group_name VARCHAR(10)
);