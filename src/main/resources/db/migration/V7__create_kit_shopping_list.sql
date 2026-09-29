CREATE TABLE kit_shopping_list (
id BIGSERIAL PRIMARY KEY,
product_id BIGINT NOT NULL REFERENCES kit_product(id) ON DELETE CASCADE,
amount NUMERIC(10,2) NOT NULL CHECK(amount>0),
user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE




);
CREATE UNIQUE INDEX uq_kit_shopping_list_user_product_id ON kit_shopping_list(user_id,product_id);