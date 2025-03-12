import { useEffect, useState } from "react";
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import * as boards from "../../apis/boards";
import * as files from "../../apis/files";
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

    // 게시글 데이터 요청 (파일 기능 확장)
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


      /*  게시판 기존 수정 로직
      const onUpdate = async (id, title, writer, content) => { // 파일 기능 구현 후 개선한 코드
        try {
          // const response = await boards.update(id, title, writer, content) // 게시글 등록 응답 요청
          const data = await response.data // 데이터 응답 받기
          console.log('onUpdate_data: ', data);
    
          Swal.alert("수정 완료!")
    
          // 게시글 목록으로 이동
          navigate('/boards')
    
        } catch(error) {
          console.log('onUpdate_error: ', error);
        }
      } 
        */

  // 게시판 + 파일 관련 기능 확장 후 수정 로직
  // const onUpdate = async (id, title, writer, content) => {   <- 게시판에서 쓰던 코드
  const onUpdate = async (formData, headers) => { // 파일 기능 구현 후 개선한 코드
    try {
      // const response = await boards.update(id, title, writer, content) // 게시글 등록 응답 요청
      const response = await boards.update(formData, headers) // 게시글 등록 응답 요청
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

   // 파일 다운로드
    const onDownload = async (id, fileName) => {
      // API 요청
      const response = await files.download(id);
      console.log("onDownload() -> response 확인 : ", response);
      /* 작업할 내용
        1. 서버에서 응답받는 파일 데이터를 받아 Blob로 변환
        2. 브라우저를 통해 a태그로 등록
        3. a태그의 다운로드 기능으로 요청
       */
      const url = window.URL.createObjectURL(new Blob( [response.data] ))
      // <a href="파일(data)" download="파일명.png">  <- 왼쪽 코드는 a태그 형식으로 만들어 주는 과정
      const link = document.createElement('a')  // a태그 생성
      link.href = url
      link.setAttribute('download', fileName)
      document.body.appendChild(link)
      link.click()  // 다운로드 기능을 가진 a태그를 클릭
      document.body.removeChild(link)
    }

  // 파일 삭제
  const onDeleteFile = async (fileId) => {
      try {
        // 파일 삭제 요청
        const fileResponse = await files.remove(fileId)
        console.log("onDeleteFile -> fileResponse.data 요청 응답 확인 :  ", fileResponse.data);

        // 요청 성공 여부 체크

        // 파일 목록 갱신
        const boardResponse = await boards.select(id)
        const data = boardResponse.data
        const fileList = data.fileList
        setFileList(fileList)

      } catch (error) {
        console.log("onDeleteFile 에러 로그 확인 : ", error)
      }
  }

  // 파일 선택 삭제 요청
  const deleteCheckedFiles = async (idList) => {
    const fileIdList = idList.join(",")
    console.log('deleteCheckedFiles() -> fileIdList 확인 :', fileIdList);
    
    try {
      // 파일 선택 삭제 요청
      const response = await files.removeFiles( fileIdList )
      console.log('deleteCheckedFiles() response.data -> 파일 선택 삭제 요청 응답확인 : ', response.data);

      // 파일 목록 갱신
      const boardResponse = await boards.select(id)
      const data = boardResponse.data
      const fileList = data.fileList
      setFileList(fileList)

    } catch (error) {
      console.log('deleteCheckedFiles -> error 로그 확인', error);
    }

  }


  useEffect( () => {
    getBoard()
    console.log("getBoard() - update_log", board);
  }, [])

  return (
    <>
      <div>UpdateContainer</div>
      <BoardUpdateForm 
          board={board} 
          fileList={fileList} 
          onUpdate={onUpdate} 
          onDelete={onDelete}
          onDeleteFile={onDeleteFile}
          onDownload={onDownload}
          deleteCheckedFiles={deleteCheckedFiles}
        />
    </>
  );
};

export default UpdateContainer;