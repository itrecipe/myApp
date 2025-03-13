import BoardList from "../../components/board/BoardListForm";
import * as boards from '../../apis/boards'
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

const ListContainer = () => {

  // TODO: boardlist state 선언
  // state 선언
  const [boardList, setBoardList] = useState([])
  const [pagination, setPagination] = useState({}) // useState({}) 여기서는 객체로 넘어오기 떄문에 빈객체로 넣어둠
  const [page, setPage] = useState(1)
  const [size, setSize] = useState(10)

  /* 페이지 네이션 초기 코드
    ?파라미터 = 값 가져오는 법
    const location = useLocation()
    const query = new URLSearchParams(location.search)
    const page = query.get("page") ?? 1 //"page" 파라미터 값을 가져온다. 기본값이 없을 경우 ?? 1 or ?? 10 이런식으로 값을 임의로 세팅하기
    const size = query.get("size") ?? 10
  */

 // ?파라미터 = 값 가져오는 법
 const location = useLocation() // 전역에서 사용할 수 있도록 분리
 
 const updatePage = () => {
    const query = new URLSearchParams(location.search)
    const newPage = query.get("page") ?? 1
    const newSize = query.get("size") ?? 10
    
    console.log(`updatePage() -> newPage : ${newPage}`)
    console.log(`updatePage() -> newSize : ${newSize}`)
    
    setPage(newPage)
    setSize(newSize)
    // getList()
  }

  // 게시글 목록 데이터 요청 (기존 게시판 코드)
/*   const getList = async () => {
    const response = await boards.list()
    const data = await response.data
    const list = data.list
    const pagination = data.pagination

    // 디버깅 코드 - 2
    console.dir('getList() -> data 확인 : ', data)
    console.dir('getList() -> data.list 확인 : ', data.list)
    console.dir('getList() -> data.pagination 확인 : ', data.pagination)

    // 디버깅 코드 - 1
    // console.log(`data : ${data}`)
    // console.log(`list : ${list}`)
    // console.log(`pagination : ${pagination}`)

    // TODO: boardList state 업데이트

    setBoardList( list )
    setPagination( pagination )
  } 
  */

  // 게시글 목록 데이터 요청 (페이지네이션 기능 확장)
  const getList = async () => {
    const response = await boards.list(page, size)
    const data = await response.data
    const list = data.list
    const pagination = data.pagination

    // 디버깅 코드 - 2
    console.dir('getList() -> data 확인 : ', data)
    console.dir('getList() -> data.list 확인 : ', data.list)
    console.dir('getList() -> data.pagination 확인 : ', data.pagination)

    // 디버깅 코드 - 1
    // console.log(`data : ${data}`)
    // console.log(`list : ${list}`)
    // console.log(`pagination : ${pagination}`)

    // TODO: boardList state 업데이트

    setBoardList( list )
    setPagination( pagination )
  }

  useEffect( () => {
    getList()
  }, [page, size])

  useEffect( () => {
    updatePage()
  }, [location.search])

  return (
    // <></> 리액트 프래그먼트 문법이자 축약형 문법 : 여러 요소를 하나의 부모로 묶을 수 있도록 해줌 (그룹화)
    <>
      <div>ListContainer</div>
      <BoardList boardList={boardList} pagination={pagination} />
    </>
  );
};

export default ListContainer;