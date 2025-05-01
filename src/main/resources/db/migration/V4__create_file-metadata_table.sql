CREATE EXTENSION IF NOT EXISTS ltree;

CREATE TABLE file_metadata (
    id            UUID           PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id     UUID           NOT NULL,
    user_id       UUID           NOT NULL,
    logical_path  ltree          NOT NULL,
    storage_key   TEXT           NOT NULL
);

CREATE INDEX idx_file_logical_path_tree
  ON file_metadata
  USING GIST (logical_path);
