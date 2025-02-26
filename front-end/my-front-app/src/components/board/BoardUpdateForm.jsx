import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import styles from './css/BoardUpdateForm.module.css'
import * as Swal from "../../apis/alert";

const BoardUpdateFrom = ({ board, onUpdate, onDelete }) => {
  const { id } = useParams();

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

  /* 수정 버튼을 누를때 내려 받은 onInsert를 호출하며 
       state(id, title, writer, content)를 전달하기
    */
  // 수정 이벤트 헨들러
  const onSubmit = () => {
    if (!title || !writer || !content) {
      Swal.alert("모든 입력란을 채워 주세요!");
      return;
    }
    onUpdate(id, title, writer, content);
  };

  // 삭제 이벤트 핸들러
/*   const handleDelete = () => {
    const check = Swal.confirm("정말 삭제 할래요?");
    console.log('check : ', check);
    if (check) onDelete(id);
  }; */

  const handleDelete = () => {
    Swal.confirm({
      title: '정말 삭제 할까요?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, 삭제',
      cancelButtonText: 'No, 취소'
    }).then((result) => {
      if(result.isconfirmed) {
        onDelete(id);
      }
    });

  /* BoardUpdateForm이 마운트될 때,
   board 객체가 존재하면 해당 객체의 title, writer, content 값을
   각각 setTitle, setWriter, setContent 함수를 통해 상태에 설정한다.
  */
  useEffect( () => {
    if( board ) {
      setTitle(board.title)
      setWriter(board.writer)
      setContent(board.content)
    }
  }, [board])

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
            <input type="text" value={title} onChange={changeTitle} className={styles['form-input']} />
          </td>
        </tr>
        <tr>
          <th>작성자</th>
          <td>
            <input type="text" value={writer} onChange={changeWriter} className={styles['form-input']} />
          </td>
        </tr>
        <tr>
          <td colSpan={2}>
            <textarea
              cols={40}
              rows={10}
              value={content}
              onChange={changeContent}
              className={styles['form-input']}
            ></textarea>
          </td>
        </tr>
      </table>
      <div className="btn-box">
        <Link to="/boards" className="btn">
          목록
        </Link>
        {/* <Link to={`/boards/update/${id}`} className="btn">수정</Link> 이전 코드 */}
        <button onClick={onSubmit} className='btn'>수정</button>
        <button onClick={handleDelete} className='btn'>삭제</button>

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

export default BoardUpdateFrom;
