DROP TABLE IF EXISTS users;

CREATE TABLE users (
    no          BIGSERIAL   PRIMARY KEY,
    id          VARCHAR(100) NOT NULL,
    username    VARCHAR(100) NOT NULL,
    password    VARCHAR(100) NOT NULL,
    name        VARCHAR(100),
    email       VARCHAR(100),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    enabled     BOOLEAN DEFAULT TRUE
);

SELECT * FROM users;

DROP TABLE IF EXISTS user_auth;

CREATE TABLE user_auth (
    no          BIGSERIAL   PRIMARY KEY,
    username    VARCHAR(100) NOT NULL,
    auth        VARCHAR(100) NOT NULL
);

select * from user_auth;

-- COMMENT 추가
COMMENT ON TABLE users IS '회원';
COMMENT ON COLUMN users.no IS 'PK';
COMMENT ON COLUMN users.id IS 'UK';
COMMENT ON COLUMN users.username IS '아이디';
COMMENT ON COLUMN users.password IS '비밀번호';
COMMENT ON COLUMN users.name IS '이름';
COMMENT ON COLUMN users.email IS '이메일';
COMMENT ON COLUMN users.created_at IS '등록일자';
COMMENT ON COLUMN users.updated_at IS '등록일자';
COMMENT ON COLUMN users.enabled IS '활성화여부';

COMMENT ON TABLE user_auth IS '회원권한';
COMMENT ON COLUMN user_auth.no IS 'PK';
COMMENT ON COLUMN user_auth.username IS '아이디';
COMMENT ON COLUMN user_auth.auth IS '권한';
