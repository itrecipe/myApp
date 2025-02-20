import { Link, useParams } from 'react-router-dom'

const BoardUpdateFrom = () => {

  const { id } = useParams()

  return (
    <div className="div container">
    <h1 className="title">게시글 수정</h1>
    <h3>번호: {id} </h3>
    <table>
      <tr>
        <td>제목</td>
        <td>
          <input type="text" value={"제목1"} />
        </td>
      </tr>
      <tr>
        <td>작성자</td>
        <td>
          <input type="text" value={"작성자1"} />
        </td>
      </tr>
      <tr>
        <td colSpan={2}>
          <textarea cols={40} rows={10} value={"내용1"}></textarea>
        </td>
      </tr>
    </table>
    <div className="btn-box">
      <Link to="/boards" className="btn">목록</Link>
      <Link to={`/boards/update/${id}`} className="btn">수정</Link>
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
  )
}

export default BoardUpdateFrom