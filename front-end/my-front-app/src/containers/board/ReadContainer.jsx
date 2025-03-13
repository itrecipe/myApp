import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import * as boards from "../../apis/boards";
import * as files from "../../apis/files";
import BoardReadForm from "../../components/board/BoardReadForm";

const ReadContainer = () => {
  // 객체 구조 분해 할당 사용
  const { id } = useParams(); // id 가져오기

  // state 셋팅
  const [board, setBoard] = useState({}); // 기본값 빈 객체 {}
  const [fileList, setFileList] = useState([]);
  const [mainFile, setMainFile] = useState();

  // 게시글 데이터 요청 (기존 코드)
  /*   const getBoard = async () => {
    const response = await boards.select(id)
    const data = await response.data
    setBoard(data)

  } */

  // 게시글 데이터 요청 및 업데이트(변경된 값을 반영하는 부분) (디버깅 코드 추가)
  const getBoard = async () => {
    try {
      const response = await boards.select(id);
      console.log("read_response: ", response);

      const data = await response.data; // data 구조 => Board + fileLIst가 같이 오는 구조
      console.log("read_data: ", data);

      setBoard(data.board);

      setFileList(data.fileList);
      console.log("data.fileList 확인 : ", data.fileList);

      const no = await data.board.no;
      getMainFile(no); // 메인 파일
    } catch (error) {
      console.error("read_error: ", error);
      // 필요 시 사용자에게 오류 메시지 표시
    }
  };

  // 다운로드
  const onDownload = async (id, fileName) => {
    // API 요청
    const response = await files.download(id);
    console.log("onDownload() -> response 확인 : ", response);
    /* 작업할 내용
      1. 서버에서 응답받는 파일 데이터를 받아 Blob로 변환
      2. 브라우저를 통해 a태그로 등록
      3. a태그의 다운로드 기능으로 요청
     */
    const url = window.URL.createObjectURL(new Blob([response.data]));
    // <a href="파일(data)" download="파일명.png">  <- 왼쪽 코드는 a태그 형식으로 만들어 주는 과정
    const link = document.createElement("a"); // a태그 생성
    link.href = url;
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click(); // 다운로드 기능을 가진 a태그를 클릭
    document.body.removeChild(link);
  };

  // 메인 파일 조회
  const getMainFile = async (no) => {
    const response = await files.fileByType("boards", no, "MAIN");
    const file = await response.data;
    setMainFile(file);

    // console.log(`mainFile : ${response.data}`);
  };

  useEffect(() => {
    getBoard(); // 게시글 정보 (게시글 + 파일목록)
    console.log("getBoard() - read 조회", board);
  }, []);

  return (
    <>
      <div>ReadContainer</div>
      <BoardReadForm
        board={board}
        mainFile={mainFile}
        fileList={fileList}
        onDownload={onDownload}
      />

      {/* TIP : 상단에 useState로 상태를 정의 했다면 컴포넌트를
          출력하는 return문에 props(매개변수)로 전달받아야
          되기 때문에 내려줘야 한다. 
          
          board, fileList
          (2가지 매개변수를 위에서 useState로 상태 정의를 하고 여기서 내려 받는다.)
      */}
    </>
  );
};

export default ReadContainer;
