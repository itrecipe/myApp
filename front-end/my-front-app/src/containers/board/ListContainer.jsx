import BoardList from "../../components/board/BoardListForm";
import * as boards from '../../apis/boards'
import { useEffect, useState } from "react";

const ListContainer = () => {

  // TODO: boardlist state 선언
  // state 선언
  const [boardList, setBoardList] = useState([])

  // 게시글 목록 데이터 가져오기
  const getList = async () => {
    const response = await boards.list()
    const data = await response.data
    const list = data.list
    // const pagination = data.pagination

    // 디버깅 코드 - 2
    console.dir(data)
    console.dir(data.list)
    console.dir(data.pagination)

    // 디버깅 코드 - 1
    // console.log(`data : ${data}`)
    // console.log(`list : ${list}`)
    // console.log(`pagination : ${pagination}`)

    // TODO: boardList state 업데이트
    setBoardList( list )
  }

  useEffect( () => {
    getList()
  }, [])

  return (
    // <></> 리액트 프래그먼트 문법이자 축약형 문법 : 여러 요소를 하나의 부모로 묶을 수 있도록 해줌 (그룹화)
    <>
      <div>ListContainer</div>
      <BoardList boardList={boardList} />
    </>
  );
};

export default ListContainer;