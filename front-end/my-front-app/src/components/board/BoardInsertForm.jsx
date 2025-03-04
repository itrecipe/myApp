import { useState } from "react";
import { Link } from "react-router-dom";
// import './css/BoardInsertForm.css'
import styles from "./css/BoardInsertForm.module.css";
import * as Swal from "../../apis/alert";

const BoardInsertForm = ({ onInsert }) => {
  // state 선언
  const [title, setTitle] = useState("");
  const [writer, setWriter] = useState("");
  const [content, setContent] = useState("");

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

  /* 등록 버튼을 누를때 내려 받은 onInsert를 호출하며
     state(title, writer, content)를 전달하기

      [트러블 슈팅]
      
      문제 상황 : 제목(title), 작성자(writer), 내용(content)이
                비어 있어도 게시글이 등록되는 문제 발생

      원인 분석: 입력값 검증 없이 onInsert 함수가 실행됨

      해결 방법: if 조건문을 추가하여 모든 필드가 입력된 경우에만 등록이 가능하도록 수정
      
      결과: 필수 입력값을 채우지 않으면 alert 창이 뜨고, 등록이 방지됨

       [기존 문제 발생 코드]
       const onSubmit = () => {
        Swal.alert("빈칸 없이 모두 입력 해주세요!");
        }
        onInsert(title, writer, content)
        }

        -> 문제를 개선한 코드는 아래 onSubmit() 참조
    
    */
  const onSubmit = () => {
    if (!title || !writer || !content) {
      Swal.alert("빈칸 없이 모두 입력 해주세요!");
      return;
    }
    onInsert(title, writer, content);
  };

  return (
    <div className="container">
      <h1 className="title">게시글 작성</h1>
      {/* <table className='table'> */}
      {/* <table className={`${styles.table}`}>
          `` 백틱으로 자바스크립트 표현식 형태로 작성해야 될때는 
          클래스네임이 여러개가 나열되어야 할때 사용한다.
      */}
      <table className={styles.table}>
        <tr>
          <th>제목</th>
          <td>
            {/* <input type="text" onChange={changeTitle} className='form-input' /> 
                CSS module 적용 전 코드*/}
            {/* 
                CSS modules의 클래스 선택자는 카멜케이스로 쓰는 것이 관례
                                  CSS         JavaScript
                * 카멜케이스 : .formInput -> { styles.formInput } 문법적으로 가능
                * 케밥케이스 : .form-input -> { style.form-input } 문법적으로 불가능
                  그래서 이런 경우에는 { style['form-input'] } 이런식으로 가져올 수도 있음.
            */}

            {/* <input type="text" onChange={changeTitle} className={`${styles.formInput}`} /> */}
            <input
              type="text"
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
              onChange={changeContent}
              className={styles["form-input"]}
            ></textarea>
          </td>
        </tr>
      </table>
      <div className="btn-box">
        <Link to="/boards" className="btn">
          목록
        </Link>
        <button className="btn" onClick={onSubmit}>
          등록
        </button>
      </div>
    </div>
  );
};

export default BoardInsertForm;
