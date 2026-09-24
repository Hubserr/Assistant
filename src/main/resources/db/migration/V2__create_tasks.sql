CREATE TABLE task (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    completed   BOOLEAN      NOT NULL DEFAULT FALSE,
    until       TIMESTAMP(6),
    created_at  TIMESTAMP(6),
    user_id     BIGINT REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_task_user_id ON task(user_id);