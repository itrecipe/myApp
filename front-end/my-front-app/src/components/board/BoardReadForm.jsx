import { Link, useParams } from "react-router-dom";
import styles from "./css/BoardReadForm.module.css";
import * as format from "../../utils/format.js"

// const BoardReadForm = ({ board }) => {  -> 게시글 조회 까지 로직
const BoardReadForm = ({ board, fileList }) => {
  const { id } = useParams();

  // board가 아직 정의되지 않았으면 Loading 메시지 표시
  if (!board || Object.keys(board).length === 0) {
    return <div>Loading...</div>;
  }

  return (
    <div className="container">
      <h1 className="title">게시글 조회</h1>
      {/* <h3>id: {id} </h3> */}
      <table className={styles.table}>
        <tbody>
          <tr>
            <th>제목</th>
            <td>
              {/* <input type="text" value={"제목1"} /> value값 받기 전 코드*/}
              
              {/* value vs defaultValue의 차이점

                 - Controllered Component (상태관리를 하는 컴포넌트)
                  * 상태들이 변경되면 UI에 업데이트 한다.
                  * 그래서 value 값을 사용하며 값의 변경을 UI로 업데이트가 가능하다.

                 - UnControllered Component (상태관리를 하지 않는 컴포넌트)
                  * 상태 변경 감지를 하지 않는다.
                  * 그래서 defaultValue 값을 사용하며, 초기에만 세팅된다.
                    (업데이트를 할 수 없다.)
              */}
              <input
                type="text"
                readOnly
                // value={board.title} -> 파일 기능 구현 이전 코드 (title writer, content) 모두 변경 전
                defaultValue={board.title ?? ''}
                className={styles["form-input"]}
              />
            </td>
          </tr>
          <tr>
            <th>작성자</th>
            <td>
              <input
                type="text"
                readOnly
                defaultValue={board.writer ?? ''}
                className={styles["form-input"]}
              />
            </td>
          </tr>
          <tr>
            <td colSpan={2}>
              <textarea
                cols={40}
                rows={10}
                readOnly
                defaultValue={board.content ?? ''}
                className={styles["form-input"]}
              ></textarea>
            </td>
          </tr>
          <tr>
            <td colSpan={2}>
              {
                fileList.map( (file) => (
                  <div className="flex-box" key={file.id}>
                    <div className="item">
                      {/* 썸네일 이미지 적용 */}
                      <img src={`/api/files/img/${file.id}`} alt={file.originName}
                        className="file-img" />
                      <span>{file.originName} ( { format.byteToUnit( file.fileSize ) })</span>
                    </div>
                    <div className="item">
                      <button className="btn">Download</button>
                    </div>
                  </div>
                ))
              }
            </td>
          </tr>
        </tbody>
      </table>
      <div className="btn-box">
        <Link to="/boards" className="btn">
          목록
        </Link>
        <Link to={`/boards/update/${id}`} className="btn">
          수정
        </Link>
        {/* 
           1. Link to={`/boards/update/${id}`} 여기서 id 번호(게시글 번호)를
            받아와야 하는데 useParams를 써서 값을 넘겨야 한다.
            상단에 useParams를 const로 선언 해주고 임포트 시키면
            정상적으로 값이 넘어가는걸 알 수 있다.

            2. TIP
            JSX에서는 자바스크립트 표현식을 사용시 중괄호 `{}`로 감싸야 한다.
            백틱(``)을 사용한 템플릿 리터럴로 변수 값을 문자열에 포함 시킨다.
           `${id}` -> id 값을 문자열 안에 동적으로 삽입 한다.
            <Link to={`/boards/update/${id}`} className="btn">수정</Link>
        */}
      </div>
    </div>
  );
};

export default BoardReadForm;
