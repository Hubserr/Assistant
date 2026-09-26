CREATE TABLE fin_transaction (
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(100)   NOT NULL,
    description      VARCHAR(255),
    amount           NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    type             VARCHAR(20)    NOT NULL,
    transaction_date DATE           NOT NULL,
    creation_date       TIMESTAMP(6)   NOT NULL,
    category_id      BIGINT REFERENCES fin_category(id) ON DELETE SET NULL,
    user_id          BIGINT         NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_fin_transaction_user_date ON fin_transaction (user_id, transaction_date);