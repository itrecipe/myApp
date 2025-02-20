-- 확장 기능 추가 (PostgreSQL 13 이상)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS boards;

CREATE TABLE boards (
    no          BIGSERIAL   PRIMARY KEY, -- Auto Increment
    id          UUID        NOT NULL DEFAULT gen_random_uuid(), -- UUID 기본값 추가
    title       VARCHAR(100) NOT NULL,
    writer      VARCHAR(100) NOT NULL,
    content     TEXT,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 테이블 주석 달기
COMMENT ON TABLE boards IS '게시판';
COMMENT ON COLUMN boards.no IS 'PK';
COMMENT ON COLUMN boards.id IS 'UK';
COMMENT ON COLUMN boards.title IS '제목';
COMMENT ON COLUMN boards.writer IS '작성자';
COMMENT ON COLUMN boards.content IS '내용';
COMMENT ON COLUMN boards.created_at IS '등록일자';
COMMENT ON COLUMN boards.updated_at IS '수정일자';


-- 샘플 데이터 삽입
TRUNCATE TABLE boards RESTART IDENTITY;

INSERT INTO boards (title, writer, content)
VALUES 
    ('제목1', '작성자1', '내용1'),
    ('제목2', '작성자2', '내용2'),
    ('제목3', '작성자3', '내용3'),
    ('제목4', '작성자4', '내용4'),
    ('제목5', '작성자5', '내용5'),
    ('제목6', '작성자6', '내용6'),
    ('제목7', '작성자7', '내용7'),
    ('제목8', '작성자8', '내용8'),
    ('제목9', '작성자9', '내용9'),
    ('제목10', '작성자10', '내용10');

select * from boards;
