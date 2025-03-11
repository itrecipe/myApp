import axios from 'axios'

// 파일 다운로드
export const download = (id) => axios.get(`/files/download/${id}`, {responseType: 'blob'} )

// 파일 삭제
export const remove = (id) => axios.delete(`/files/${id}`)

// 파일 선택 삭제
export const removeFiles = (idList) => axios.delete(`/files?idList=${idList}`)
// (`/files?idList=${idList}`) idLIist가 컴마로 구분되어 넘어가면 서버측에선 컬렉션 리스트 형태로 전달 받을 수 있다.