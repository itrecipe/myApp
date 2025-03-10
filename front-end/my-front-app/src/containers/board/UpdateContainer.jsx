import { useEffect, useState } from "react";
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import * as boards from "../../apis/boards";
import BoardUpdateForm from "../../components/board/BoardUpdateForm";
import * as Swal from "../../apis/alert";

const UpdateContainer = () => {

  // 객체 구조 분해 할당 사용
  const { id } = useParams() // id 가져오기

  // 페이지 목록으로 이동하기
  const navigate = useNavigate()

  // state 셋팅
  const [board, setBoard] = useState({}) // 기본값 빈 객체 {}
  const [fileList, setFileList] = useState([])

    
/* 게시글 데이터 요청 (디버깅 코드 추가) - 이전 게시판 코드
   const getBoard = async () => {
      try {
        const response = await boards.select(id);
        console.log('update_response: ', response);
        
        const data = await response.data;
        console.log('update_data: ', data)
        
        // setBoard(data); 이전 게시글 코드
        setBoard(data.board); 
        // 파일 데이터와 게시글의 응답을 같이 받기 위해 구조 변경
        
      } catch (error) {
        console.error("update_error: ", error);
        // 필요 시 사용자에게 오류 메시지 표시
      }
    }; */

     const getBoard = async () => {
        try {
          const response = await boards.select(id);
          console.log("read_response: ", response);
    
          const data = await response.data;   // data 구조 => Board + fileLIst가 같이 오는 구조
          console.log("read_data: ", data);
    
          setBoard(data.board);
          
          setFileList(data.fileList);
          console.log("data.fileList 확인 : ", data.fileList);
    
        } catch (error) {
          console.error("read_error: ", error);
          // 필요 시 사용자에게 오류 메시지 표시
        }
      };
    

  // 게시글 수정 요청 이벤트 헨들러 생성
  const onUpdate = async (id, title, writer, content) => {
    try {
      const response = await boards.update(id, title, writer, content) // 등록 응답 요청
      const data = await response.data // 데이터 응답 받기
      console.log('onUpdate_data: ', data);

      Swal.alert("수정 완료!")

      // 게시글 목록으로 이동
      navigate('/boards')

    } catch(error) {
      console.log('onUpdate_error: ', error);
    }
  }

  // 게시글 삭제 요청 이벤트 헨들러 생성
  const onDelete = async (id) => {
    try {
      const response = await boards.remove(id) // 등록 응답 요청
      const data = await response.data // 데이터 응답 받기
      console.log('onDelete_data: ', data);

      Swal.alert("삭제 완료!")

      // 게시글 목록으로 이동
      navigate('/boards')

    } catch(error) {
      console.log('onDelete_error: ', error);
    }
  }

  useEffect( () => {
    getBoard()
    console.log("getBoard() - update_log", board);
  }, [])

  return (
    <>
      <div>UpdateContainer</div>
      <BoardUpdateForm board={board} fileList={fileList} onUpdate={onUpdate} onDelete={onDelete} />
    </>
  );
};

export default UpdateContainer;