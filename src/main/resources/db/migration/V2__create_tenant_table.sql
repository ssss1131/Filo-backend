CREATE TABLE tenant (
    id           UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    name         TEXT           NOT NULL UNIQUE,
    display_name TEXT           NOT NULL,
    quota_megabytes  BIGINT     NOT NULL DEFAULT 500,
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ    NOT NULL DEFAULT NOW()

);