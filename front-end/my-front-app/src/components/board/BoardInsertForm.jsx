import { useState } from 'react'
import { Link } from 'react-router-dom'

const BoardInsertForm = ({ onInsert }) => {

  // state 선언
  const [title, setTitle] = useState('')
  const [writer, setWriter] = useState('')
  const [content, setContent] = useState('')

  // change에 대한 이벤트 함수 등록
  const changeTitle = (e) => { setTitle( e.target.value ) }
  const changeWriter = (e) => { setWriter( e.target.value ) }
  const changeContent = (e) => { setContent( e.target.value ) }

  /* 등록 버튼을 누를때 내려 받은 onInsert를 호출하며 
     state(title, writer, content)를 전달하기
  */
  const onSubmit = () => {
    onInsert(title, writer, content)
  }

  return (
    <div className="container">
      <h1 className="title">게시글 작성</h1>
      <table>
        <tr>
          <td>제목</td>
          <td>
            <input type="text" onChange={changeTitle} />
          </td>
        </tr>
        <tr>
          <td>작성자</td>
          <td>
            <input type="text" onChange={changeWriter} />
          </td>
        </tr>
        <tr>
          <td colSpan={2}>
            <textarea cols={40} rows={10} onChange={changeContent}></textarea>
          </td>
        </tr>
      </table>
      <div className="btn-box">
        <Link to="/boards" className="btn">목록</Link>
        <button className="btn" onClick={ onSubmit }>등록</button>
      </div>
    </div>
  )
}

export default BoardInsertForm