
CREATE TABLE notification (
    id BIGINT PRIMARY KEY,
    message TEXT,
    type_id BIGINT,
    user_id BIGINT
);