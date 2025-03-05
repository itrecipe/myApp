-- 기존 테이블 삭제
DROP TABLE IF EXISTS files;

-- 기존 ENUM 타입 삭제 (이미 있는 경우)
DROP TYPE IF EXISTS file_type;

-- ENUM 타입 생성 (파일 타입)
CREATE TYPE file_type AS ENUM ('MAIN', 'SUB');

-- 테이블 생성
CREATE TABLE files (
    no BIGSERIAL PRIMARY KEY,           -- 자동 증가 (BIGSERIAL)
    id VARCHAR(64) NOT NULL,             -- 문자열로 처리 (원하는 경우 UUID로 변경 가능)
    p_table VARCHAR(100) NOT NULL,       -- 부모테이블
    p_no BIGINT NOT NULL,                -- 부모 PK
    type file_type NOT NULL DEFAULT 'SUB',  -- ENUM 타입, 기본값 'SUB'
    file_name TEXT NOT NULL,             -- 파일명
    origin_name TEXT NOT NULL,           -- 원본파일명
    file_path TEXT NOT NULL,             -- 파일경로
    file_size BIGINT DEFAULT 0,          -- 용량 (기본값 0)
    seq BIGINT DEFAULT 0,                -- 순서 (기본값 0)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 등록일자
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP   -- 수정일자
);

-- 테이블 및 컬럼 주석 추가
COMMENT ON TABLE files IS '파일';
COMMENT ON COLUMN files.no IS 'PK';
COMMENT ON COLUMN files.id IS 'UK';
COMMENT ON COLUMN files.p_table IS '부모테이블';
COMMENT ON COLUMN files.p_no IS '부모PK';
COMMENT ON COLUMN files.type IS '타입';
COMMENT ON COLUMN files.file_name IS '파일명';
COMMENT ON COLUMN files.origin_name IS '원본파일명';
COMMENT ON COLUMN files.file_path IS '파일경로';
COMMENT ON COLUMN files.file_size IS '용량';
COMMENT ON COLUMN files.seq IS '순서';
COMMENT ON COLUMN files.created_at IS '등록일자';
COMMENT ON COLUMN files.updated_at IS '수정일자';

select * from boards;
select * from files;