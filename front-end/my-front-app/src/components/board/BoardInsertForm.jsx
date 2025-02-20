import { Link } from 'react-router-dom'

const BoardInsertForm = () => {
  return (
    <div className="div container">
      <h1 className="title">게시글 작성</h1>
      <table>
        <tr>
          <td>제목</td>
          <td>
            <input type="text" />
          </td>
        </tr>
        <tr>
          <td>작성자</td>
          <td>
            <input type="text" />
          </td>
        </tr>
        <tr>
          <td colSpan={2}>
            <textarea cols={40} rows={10}></textarea>
          </td>
        </tr>
      </table>
      <div className="btn-box">
        <Link to="/boards" className="btn">목록</Link>
        <button className="btn">등록</button>
      </div>
    </div>
  )
}

export default BoardInsertForm