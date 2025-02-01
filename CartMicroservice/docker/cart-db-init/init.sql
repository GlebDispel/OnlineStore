CREATE TABLE IF NOT EXISTS cart  (
                      id SERIAL PRIMARY KEY,
                      user_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_products (
                               cart_id BIGINT NOT NULL,
                               product_id BIGINT NOT NULL,
                               quantity INTEGER NOT NULL,
                               PRIMARY KEY (cart_id, product_id),
                               FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE
);