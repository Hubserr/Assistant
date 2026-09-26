CREATE TABLE fin_category (
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    user_id BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX uq_fin_category_user_name ON fin_category (user_id, LOWER(name));