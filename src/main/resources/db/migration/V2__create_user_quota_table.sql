CREATE TABLE user_quota(
    user_id BIGINT PRIMARY KEY REFERENCES users,
    used_bytes BIGINT NOT NULL DEFAULT 0,
    quota_bytes BIGINT NOT NULL
);