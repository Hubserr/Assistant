CREATE TABLE kit_product (

id      BIGSERIAL PRIMARY KEY,
name    VARCHAR(100) NOT NULL,
unit    VARCHAR(20) NOT NULL,
user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uq_kit_product_user_name ON kit_product(user_id,LOWER(name));