CREATE TABLE kit_recipe (

id                  BIGSERIAL PRIMARY KEY,
name                VARCHAR(150) NOT NULL,
description         VARCHAR(500),
servings            INTEGER NOT NULL  CHECK (servings>0),
prep_time_minutes   INTEGER      CHECK (prep_time_minutes>0),
calories            INTEGER      CHECK (calories>=0),
protein             NUMERIC(6,1)    CHECK (protein>=0),
fat                 NUMERIC(6,1)    CHECK (fat>=0),
carbs               NUMERIC(6,1)    CHECK (carbs>=0),
created_at          TIMESTAMP(6)    NOT NULL,
user_id             BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE

);

CREATE INDEX idx_kit_recipe_user ON kit_recipe(user_id);

CREATE TABLE kit_recipe_ingredient (

    id          BIGSERIAL PRIMARY KEY,
    recipe_id   BIGINT NOT NULL REFERENCES kit_recipe(id)  ON DELETE CASCADE,
    product_id  BIGINT NOT NULL REFERENCES kit_product(id) ON DELETE RESTRICT,
    amount      NUMERIC(10,2) NOT NULL CHECK (amount>0)

);
CREATE INDEX idx_kit_recipe_ingredient ON kit_recipe_ingredient(recipe_id);

CREATE TABLE kit_recipe_steps (
    recipe_id      BIGINT   NOT NULL REFERENCES kit_recipe(id) ON DELETE CASCADE,
    step_order     INTEGER  NOT NULL,
    content        VARCHAR(1000) NOT NULL,
    PRIMARY KEY (recipe_id,step_order)
);


