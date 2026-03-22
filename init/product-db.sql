CREATE TABLE IF NOT EXISTS tb_products (
    id                  UUID PRIMARY KEY,
    product_id          UUID NOT NULL,
    version             INTEGER NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT true,
    name                VARCHAR(255) NOT NULL,
    description         TEXT,
    product_type        VARCHAR(50) NOT NULL,
    risk_level          VARCHAR(50) NOT NULL,
    status              VARCHAR(50) NOT NULL,
    interest_rate       NUMERIC(10,4) NOT NULL,
    minimum_investment  NUMERIC(19,4) NOT NULL,
    grace_period_days   INTEGER NOT NULL,
    liquidity_days      INTEGER NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL,
    updated_at          TIMESTAMPTZ
    );

CREATE INDEX IF NOT EXISTS idx_products_product_id ON tb_products(product_id);
CREATE INDEX IF NOT EXISTS idx_products_active ON tb_products(active);
CREATE UNIQUE INDEX IF NOT EXISTS idx_products_product_id_active
    ON tb_products(product_id) WHERE active = true;

CREATE TABLE IF NOT EXISTS product_tb_outbox (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    topic           VARCHAR(255) NOT NULL,
    event_key       VARCHAR(255) NOT NULL,
    payload         TEXT NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL,
    processed_at    TIMESTAMPTZ,
    attempts        INTEGER DEFAULT 0,
    last_error      TEXT
    );