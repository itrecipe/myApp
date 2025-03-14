import axios from 'axios';

// 기본 경로 URL 설정
axios.defaults.baseURL = "/api"

// export const list = () => axios.get("/boards") -> 기존 게시판 목록 조회 api
// export const list = (page, size) => axios.get(`/boards?page=${page}&size=${size}`)  // 쿼리 파라미터 세팅, 페이지 네이션 api (확장)
// 목록 조회 (검색 기능 확장)
export const list = (page, size, keyword = "", searchType = "") => 
    axios.get(`/boards?page=${page}&size=${size}&keyword=${keyword}&searchType=${searchType}&password2=asdfsdf`);

// 조회
export const select = (id) => axios.get(`/boards/${id}`)

// 등록
export const insert = (title, writer, content) => axios.post("/boards", {title, writer, content} ) //-> 기존 게시판 등록 api
// export const insert = (formData, headers) => axios.post("/boards", formData, headers ) // 파일 관련 등록 api (확장)

// 수정
// export const update = (id, title, writer, content) => axios.put("/boards", {id, title, writer, content} ) -> 기존 게시판 수정 api
export const update = (formData, headers) => axios.put("/boards", formData, headers ) // 파일 관련 수정 api (확장)

// 삭제
export const remove = (id) => axios.delete(`/boards/${id}`)