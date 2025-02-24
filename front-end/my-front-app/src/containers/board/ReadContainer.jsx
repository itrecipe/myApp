import { useEffect, useState } from "react";
import { useParams } from 'react-router-dom'
import * as boards from "../../apis/boards"
import BoardReadForm from "../../components/board/BoardReadForm";

const ReadContainer = () => {

  // 객체 구조 분해 할당 사용
  const { id } = useParams() // id 가져오기

  // state 셋팅
  const [board, setBoard] = useState({}) // 기본값 빈 객체 {}

  // 게시글 데이터 요청
/*   const getBoard = async () => {
    const response = await boards.select(id)
    const data = await response.data
    setBoard(data)

  } */

    // 디버깅 코드
    const getBoard = async () => {
      try {
        const response = await boards.select(id);
        console.log('response', response);
        const data = response.data;
        setBoard(data);
      } catch (error) {
        console.error("Error fetching board data: ", error);
        // 필요 시 사용자에게 오류 메시지 표시
      }
    };

  useEffect( () => {
    getBoard()
    console.log("조회 되고 있나?", board);
  }, [])

  return (
    <>
      <div>ReadContainer</div>
      <BoardReadForm board={board} />
    </>
  );
};

export default ReadContainer;
