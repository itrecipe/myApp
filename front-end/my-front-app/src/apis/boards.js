import axios from 'axios';

// 기본 경로 URL 설정
axios.defaults.baseURL = "/api"

// 목록
export const list = () => axios.get("/boards")

// 조회
export const select = (id) => axios.get(`/boards/${id}`)

// 등록
// export const insert = (title, writer, content) => axios.post("/boards", {title, writer, content} ) 기존 게시판 등록 api
export const insert = (formData, headers) => axios.post("/boards", formData, headers ) // 파일 관련 기능 api

// 수정
export const update = (id, title, writer, content) => axios.put("/boards", {id, title, writer, content} )

// 삭제
export const remove = (id) => axios.delete(`/boards/${id}`)