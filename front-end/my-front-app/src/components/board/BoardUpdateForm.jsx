import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import styles from "./css/BoardUpdateForm.module.css";
import * as Swal from "../../apis/alert";
import * as format from "../../utils/format.js";
import DownloadIcon from "@mui/icons-material/Download";
import DeleteIcon from "@mui/icons-material/Delete";
import Checkbox from "@mui/material/Checkbox";

const BoardUpdateForm = ({
  board,
  onUpdate,
  onDelete,
  onDeleteFile,
  fileList,
  onDownload,
  deleteCheckedFiles,
  mFile,
}) => {
  const { id } = useParams();

  // state 선언
  const [title, setTitle] = useState("");
  const [writer, setWriter] = useState("");
  const [content, setContent] = useState("");
  const [fileIdList, setFileIdList] = useState([]); // 선택 삭제 id 목록
  const [mainFile, setMainFile] = useState(null); // mainfile state 추가
  const [files, setFiles] = useState(null); // files state 추가

  // change에 대한 이벤트 함수 등록
  const changeTitle = (e) => {
    setTitle(e.target.value);
  };
  const changeWriter = (e) => {
    setWriter(e.target.value);
  };
  const changeContent = (e) => {
    setContent(e.target.value);
  };

  /* 수정 이벤트 헨들러 - 1 (기존)
     
      [트러블 슈팅]

      1. 문제 상황 (원인 분석) : 현재 코드에선 빈 값이 있으면 Swal.alert 창이 뜨지만,
                                이후에도 onUpdate 함수가 호출되어 업데이트가 진행되는 
                                문제 발생 (수정시 빈 공백값이 들어가는데도 막아 주질 못해 불편하고 취약함)

      2. 해결 방법 (개선 방법) : 조건에 해당되면 바로 함수를 종료 하도록 return 하기

      3. 결과 : title, writer, content 중 하나라도 입력되지 않으면
                Swal.alert("모든 입력란을 채워 주세요!"); 경고창 출력

      [기존에 문제가 발생된 코드]
      const onSubmit = () => {
       if (!title || !writer || !content) {
         Swal.alert("모든 입력란을 채워 주세요!");
         return;
       }
       onUpdate(id, title, writer, content);
     }; 

     -> 발생된 문제를 개선한 코드는 아래 onSubmit 코드 참조
  */

  /* 수정 버튼을 누를때 내려 받은 onInsert를 호출하며 
       state(id, title, writer, content)를 전달하기
    */

  /* 수정(제출) 이벤트 핸들러 - 2 (개선 완료)

    const onSubmit = () => {
        if (!title || !writer || !content) {
          Swal.alert("모든 입력란을 채워 주세요!");
          return; // 위 조건 만족시 return해서 업데이트 진행이 되지 않도록 처리
        }
        Swal.confirm(
          "수정 할까요?",
          "확인 또는 취소 버튼을 눌러 주세요!",
          "warning",
          (result) => {
            if (result.isConfirmed) {
              onUpdate(id, title, writer, content);
            }
          }
        );
      };
  */

  // 파일 변경 이벤트 핸들러 추가
  const changeMainFile = (e) => {
    // files : []  -> 리스트 형식
    setMainFile(e.target.files[0]);
  };

  const changeFile = (e) => {
    setFiles(e.target.files);
  };

  // 파일 관련 수정(제출) 이벤트 핸들러-3
  const onSubmit = () => {
    if (!title || !writer || !content) {
      Swal.alert("모든 입력란을 채워 주세요!");
      return; // 위 조건 만족시 return해서 업데이트 진행이 되지 않도록 처리
    }
    Swal.confirm(
      "수정 할까요?",
      "확인 또는 취소 버튼을 눌러 주세요!",
      "warning",
      (result) => {
        if (result.isConfirmed) {
          // onUpdate(id, title, writer, content);
          /* Content-Type : application/json 기존 방식에서 
                파일 기능(업로드, 다운로드, 삭제, 조회 등...)
                연결을 위해 multiform-data 형식으로 변경해야 한다.
          
                onInsert(title, writer, content);
              */

          /* 파일 업로드
                 application/json 방식 -> multipart/form-data 구조 변경
               */
          const formData = new FormData();

          // 게시글 정보 세팅
          formData.append("id", id); //
          formData.append("title", title);
          formData.append("writer", writer);
          formData.append("content", content);

          // 파일 데이터 세팅
          if (mainFile) {
            formData.append("mainFile", mainFile);
          }
          if (files) {
            // 파일 체크
            for (let i = 0; i < files.length; i++) {
              const file = files[i];
              formData.append("files", file);
            }
          }

          // 헤더
          const headers = {
            "Content-Type": "multipart/form-data",
          };

          if (!title || !writer || !content) {
            Swal.alert("빈칸 없이 모두 입력 해주세요!");
            return;
          }
          // onUpdate(title, writer, content) -> application/json (기존 게시판에서 쓰던 방식)
          onUpdate(formData, headers); // multipart/form-data로 구조변경
        }
      }
    );
  };

  /* 
    [트러블 슈팅]
    1. 문제 상황 (원인 분석) : 기존 코드가 Swal.confirm을 동기적으로 처리했기 때문에 반환값이 boolean 이었음.
    2. 해결 방법 (개선 방안) : SweetAlert2는 Promise 기반이므로, 결과(result.isConfirmed)를 .then() 콜백에서 처리
    3. 결과 : 확인 시 onDelete(id)가 정상 호출 되게 로직 변경함

    [문제가 발생했던 삭제 이벤트 핸들러 - 기존코드]
      const handleDelete = () => {
        const check = Swal.confirm("정말 삭제 할래요?");
        console.log('check : ', check);
        if (check) onDelete(id);
      }; 
    
    -> 문제를 개선한 코드는 아래 handleDelete 함수 참조
    */

  // 게시글 삭제 이벤트 핸들러 - 2 (개선 완료)
  const handleDelete = () => {
    Swal.confirm(
      "정말 삭제 할까요?",
      "신중하게 결정 했나요?",
      "warning",
      (result) => {
        if (result.isConfirmed) {
          onDelete(id);
        }
      }
    );
  };

  // 파일 삭제 이벤트 핸들러
  const handleFileDelete = (id) => {
    Swal.confirm(
      "파일 삭제 할까요?",
      "신중하게 결정 했나요?",
      "warning",
      (result) => {
        if (result.isConfirmed) {
          onDeleteFile(id);
        }
      }
    );
  };

  /*
      [트러블 슈팅]
      1. 문제상황 (원인분석): 선택된 파일이 없을 때에도 삭제 요청이 실행되어 서버에 500 에러 발생.
      2. 해결방법 (개선방안): 파일이 선택되지 않은 경우 Swal.alert를 띄우고, 즉시 함수 종료(return) 처리.
      3. 결과: 파일 선택 없이 삭제 버튼을 누르면 경고창이 뜨고, 선택된 파일이 있을 때만 삭제 확인 후 onDelete 실행.
      
      [선택 삭제 이벤트 핸들러 - 기존 문제 발생 코드]
      const handleCheckedFileDelete = (id) => {
        Swal.confirm(
          `선택한 ${fileIdList.length} 개 파일들 삭제 할까요?`,
          "신중하게 결정 했나요?",
          "warning",
          (result) => {
            if (result.isConfirmed) {
              deleteCheckedFiles(fileIdList);
              setFileIdList([]) // 삭제할 id 리스트 초기화
            }
          }
        );
      } 
    */

  // 선택 삭제 이벤트 핸들러 (개선한 로직)
  const handleCheckedFileDelete = () => {
    // 1. 체크된 파일이 없을 경우 경고창 띄우기
    if (fileIdList.length === 0) {
      Swal.alert("삭제할 파일을 선택하세요!", "", "warning");
      return;
    }

    // 2. 파일이 선택된 경우만 삭제 컨펌 창 띄우기
    Swal.confirm(
      `선택한 ${fileIdList.length} 개 파일들 삭제 할까요?`,
      "신중하게 결정 하셨죠?",
      "warning",
      (result) => {
        if (result.isConfirmed) {
          deleteCheckedFiles(fileIdList);
          setFileIdList([]); // 삭제할 ID 리스트를 초기화
        }
      }
    );
  };

  /* 선택(개별) 삭제시 체크된 파일 id 값을 검증하는 이벤트 핸들러   
    
    const checkFileId = (id) => {
      console.log(`checkFileId() -> id값 확인 : ${id}`)

      let checked = false

      // 체크 여부 확인
      for (let i = 0; i < fileIdList.length; i++) {
        const fileId = fileIdList[i];
        // 체크 된 것 : 체크박스 해제 -> 제거
        if( fileId == id) {
          fileIdList.splice(i, 1) // splice 메서드로 i번째 인덱스의 1개 요소를 제거
          checked = true
        }
      }

      // 체크가 안된것 : 체크박스를 지정
      if( !checked ) {
        // 체크한 아이디 추가
        fileIdList.push(id)
      }
      console.log(`checkFileId() -> fileIdList -> 체크한 ID값 확인 : ${fileIdList}`)

      setFileIdList(fileIdList)
      위 작업들이 정리가 좀 되면 setFileIdList(fileIdList)로 정리 해준다 
      (즉, 체크된 여부가 바뀐다는 의미)
      
    } 
     */

  // 선택(개별) 삭제시 체크된 파일 id 값을 검증하는 이벤트 핸들러 (개선한 로직)
  const checkFileId = (id) => {
    console.log(`checkFileId() -> id값 확인 : ${id}`);

    setFileIdList((prevList) => {
      if (prevList.includes(id)) {
        // 이미 선택되어 있다면 제거
        return prevList.filter((fileId) => fileId !== id);
      } else {
        // 선택되어 있지 않다면 추가
        return [...prevList, id];
      }
    });
  };

  /*   
  - 전체 선택/해제 버튼 이벤트 핸들러 (개선한 로직-1)

  const handleSelectAll = () => {
    // 파일 목록이 없으면 작업을 처리 하지 않음
    if (fileList.length === 0) return;
    console.log('fileList.length === 0 값 : ', fileList.length === 0)

    if (fileIdList.length === fileList.length) {
      console.log('fileIdList.length === fileList.length : 값 확인', fileIdList.length === fileList.length)
      // 이미 전체 선택된 상태 -> 전체 해제
      setFileIdList([]);
      console.log('handleSelectAll -> setFileIdList 값 확인 :', setFileIdList)
    } else {
      // 전체 선택되지 않은 경우 -> 전체 선택
      const allIds = fileList.map((file) => file.id);
      setFileIdList(allIds);
    }
  } */

  // 전체 선택/해제 버튼 이벤트 핸들러 (개선한 로직-2)
  const handleSelectAll = () => {
    if (fileList.length === 0) return;

    setFileIdList((prevList) => {
      if (prevList.length === fileList.length) {
        // 전체 선택 상태 -> 전체 해제
        return [];
      } else {
        // 전체 선택되지 않은 상태 -> 모든 파일의 id 선택
        return fileList.map((file) => file.id);
      }
    });
  };

  useEffect(() => {
    console.log("Updated fileIdList:", fileIdList);
  }, [fileIdList]);

  /* BoardUpdateForm이 마운트될 때,
   board 객체가 존재하면 해당 객체의 title, writer, content 값을
   각각 setTitle, setWriter, setContent 함수를 통해 상태에 설정한다.
  */
  useEffect(() => {
    if (board) {
      setTitle(board.title);
      setWriter(board.writer);
      setContent(board.content);
      console.log("useEffect()에 board 값이 있을때 mFile 확인 : ", mFile);
    }
  }, [board]);

  return (
    <div className="container">
      <h1 className="title">게시글 수정</h1>
      {/* <h3>id: {id} </h3> */}
      <table className={styles.table}>
        <tr>
          <th>제목</th>
          <td>
            {/* <input type="text" value={board.title} onChange={changeTitle} /> 
              ex) value={board.title} 형식으로 화면에 뿌리면 부모에게서 내려 받은 고정값이 되기 때문에
              값이 정상적으로 변경 되지 않는다.

              아래에서 value 값을 state 형식으로 변경해줘야 한다. (writer, content 동일)
              ex) value={title}
          */}
            <input
              type="text"
              value={title}
              onChange={changeTitle}
              className={styles["form-input"]}
            />
          </td>
        </tr>
        <tr>
          <th>작성자</th>
          <td>
            <input
              type="text"
              value={writer}
              onChange={changeWriter}
              className={styles["form-input"]}
            />
          </td>
        </tr>
        <tr>
          <td colSpan={2}>
            <textarea
              cols={40}
              rows={10}
              value={content}
              onChange={changeContent}
              className={styles["form-input"]}
            ></textarea>
          </td>
        </tr>
        {/* mFile(대표파일) 없을 때, 파일 첨부 UI를 출력
            mFile(대표파일) 있을 때, 파일 첨부 UI를 숨김
        */}
        { mFile 
          ? 
          <></>
          : 
         (
          <tr>
            <td>대표 파일</td>
            <td>
              <input type="file" onChange={changeMainFile} />
            </td>
          </tr>
        )}
        <tr>
          <td>서브 파일</td>
          <td>
            <input type="file" multiple onChange={changeFile} />
          </td>
        </tr>
        <tr>
          <td colSpan={2}>
            {fileList.map((file) => (
              <div className="flex-box" key={file.id}>
                <div className="item">
                  {/* <input type="checkbox" onClick={ () => checkFileId( file.id ) } /> 게시판에서 사용하던 코드 */}

                  {/* 파일 기능 구현 후 개선한 코드-1 (체크박스 전체선택/해제)
                      <input 
                          type="checkbox" 
                          onChange={ () => checkFileId(file.id) } 
                          checked={ fileIdList.includes(file.id) }
                      /> 
                  */}

                  {/*   [트러블 슈팅]
                          1. 문제 상황: CheckBox 아이콘을 사용해 체크박스 기능을 구현했으나, 
                              onChange 이벤트가 발생하지 않아 fileIdList 상태 업데이트가 감지되지 않음.

                          2. 원인 분석: '@mui/icons-material/CheckBox'는 단순 아이콘 컴포넌트로,
                                       인터랙티브 체크박스 기능이 없음.

                          3. 해결 방법: Material UI의 인터랙티브한 Checkbox 컴포넌트('@mui/material/Checkbox')를
                                      사용하여 onChange와 checked 속성이 정상 작동하도록 수정.

                          4. 결과: 사용자가 체크박스를 클릭하면 fileIdList가 올바르게 업데이트되어 전체 선택/해제 기능이 정상 동작함. 

                          * 코드는 아래 개선 코드-2 참조할것.
                  */}

                  {/* 파일 기능 구현 후 개선한 코드-2 (체크박스 전체선택/해제) */}
                  <Checkbox
                    onChange={() => checkFileId(file.id)}
                    checked={fileIdList.includes(file.id)}
                  />

                  <div className="item-img">
                    {file.type == "MAIN" && ( <span className="badge">대표 이미지</span> ) }
                    
                    {/* 썸네일 이미지 적용 */}
                    <img src={`/api/files/img/${file.id}`} alt={file.originName} className="file-img" />
                  </div>
             
                  <span>
                    {file.originName} ( {format.byteToUnit(file.fileSize)})
                  </span>
                </div>
                <div className="item">
                  {/* <button className="btn">Download</button> 게시판 이전 코드 */}
                  {/* <button className="btn" onClick={() => onDownload(file.id, file.originName)}>
                      Download
                    </button> 
                    매트리얼 ui 적용 전 코드 (삭제도 동일)
                  */}
                  <button
                    className="btn"
                    onClick={() => onDownload(file.id, file.originName)}
                  >
                    <DownloadIcon />
                  </button>
                  <button
                    className="btn"
                    onClick={() => handleFileDelete(file.id)}
                  >
                    <DeleteIcon />
                  </button>
                  {/* onClick={ () => onDownload(file.no, file.originName) }
                                  매개변수가 있을때 함수 호출 구조가 아닌 정의구조로 작성해야 한다. 
                              */}
                </div>
              </div>
            ))}
          </td>
        </tr>
      </table>
      <div className="btn-box">
        <Link to="/boards" className="btn">
          목록
        </Link>

        <button className="btn" onClick={handleSelectAll}>
          전체 선택 / 해제
        </button>

        {/* <button className='btn'>선택 삭제</button> -> 이전 게시글 코드 */}
        {/* <button className='btn' onClick={ () => deleteCheckedFiles( fileIdList ) }>선택 삭제</button> -> 파일 관련 기능 구현할때 만든 코드*/}
        <button className="btn" onClick={handleCheckedFileDelete}>
          선택 삭제
        </button>

        {/* <Link to={`/boards/update/${id}`} className="btn">수정</Link> 이전 게시글 코드 */}
        <div>
          <button onClick={onSubmit} className="btn">
            수정
          </button>
          <button onClick={handleDelete} className="btn">
            삭제
          </button>
        </div>
        {/*
         1. Link to={`/boards/update/${id}`} 여기서 id 번호(게시글 번호)를
          받아와야 하는데 useParams를 써서 값을 넘겨야 한다.
          상단에 useParams를 const로 선언 해주고 임포트 시키면
          정상적으로 값이 넘어가는걸 알 수 있다.

          2. TIP
          JSX에서는 자바스크립트 표현식을 사용시 중괄호 `{}`로 감싸야 한다.
          백틱(``)을 사용한 템플릿 리터럴로 변수 값을 문자열에 포함시킴
         `${id}` -> id 값을 문자열 안에 동적으로 삽입 한다.
          <Link to={`/boards/update/${id}`} className="btn">수정</Link>
      */}
      </div>
    </div>
  );
};

export default BoardUpdateForm;
