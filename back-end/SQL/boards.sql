-- 확장 기능 추가 (PostgreSQL 13 이상)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS boards;

-- boards 테이블 생성
CREATE TABLE boards (
    no BIGSERIAL PRIMARY KEY,
    id VARCHAR(64) NOT NULL,
    title VARCHAR(100) NOT NULL,
    writer VARCHAR(100) NOT NULL,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 각 컬럼에 주석(comment) 달아주기
COMMENT ON TABLE boards IS '게시판';
COMMENT ON COLUMN boards.no IS 'PK';
COMMENT ON COLUMN boards.id IS 'UK';
COMMENT ON COLUMN boards.title IS '제목';
COMMENT ON COLUMN boards.writer IS '작성자';
COMMENT ON COLUMN boards.content IS '내용';
COMMENT ON COLUMN boards.created_at IS '등록일자';
COMMENT ON COLUMN boards.updated_at IS '수정일자';

TRUNCATE TABLE boards;

INSERT INTO boards (id, title, writer, content)
VALUES 
	/* PostgreSQL의 gen_random_uuid() 함수를 사용해 UUID 생성
	   ::text 캐스팅을 통해 문자열로 저장한다.
	 */
	
    (gen_random_uuid()::text, '제목1', '작성자1', '내용1'), 
    (gen_random_uuid()::text, '제목2', '작성자2', '내용2'),
    (gen_random_uuid()::text, '제목3', '작성자3', '내용3'),
    (gen_random_uuid()::text, '제목4', '작성자4', '내용4'),
    (gen_random_uuid()::text, '제목5', '작성자5', '내용5'),
    (gen_random_uuid()::text, '제목6', '작성자6', '내용6'),
    (gen_random_uuid()::text, '제목7', '작성자7', '내용7'),
    (gen_random_uuid()::text, '제목8', '작성자8', '내용8'),
    (gen_random_uuid()::text, '제목9', '작성자9', '내용9'),
    (gen_random_uuid()::text, '제목10', '작성자10', '내용10');

select * from boards;

-- uuid 조회 쿼리 (테스트)
select *
from boards
where id = '86bb0ee1-2164-4fd7-a7a5-d9b9a4a229a3';


select * from boards where id;
