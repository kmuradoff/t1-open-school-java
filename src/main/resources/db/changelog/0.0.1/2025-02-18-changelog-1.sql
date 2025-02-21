CREATE TABLE IF NOT EXISTS task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    user_id VARCHAR(255)
);