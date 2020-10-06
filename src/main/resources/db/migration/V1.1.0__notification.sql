
CREATE TABLE notification (
    id BIGINT PRIMARY KEY,
    message TEXT,
    type_id INT,
    user_id BIGINT
);