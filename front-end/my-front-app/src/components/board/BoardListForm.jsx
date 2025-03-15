import { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import * as format from "../../utils/format";
// import './css/BoardListForm.css' -> 기존까지 사용했던 일반적인 css 사용 방법
import styles from "./css/BoardListForm.module.css"; // css를 모듈화해서 사용하는 방법
// import Header from '../../components/Header/Header';
import noImage from '../../assets/react.svg'
import KeyboardArrowLeftIcon from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardDoubleArrowLeftIcon from '@mui/icons-material/KeyboardDoubleArrowLeft';
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';
import KeyboardDoubleArrowRightIcon from '@mui/icons-material/KeyboardDoubleArrowRight';

const BoardList = ({ boardList, pagination }) => {
  console.log("BoardList() -> boardList 값 : ", boardList);
  console.log("BoardList() -> pagination 값 : ", pagination);

  const navigate = useNavigate();
  const location = useLocation();
  const query = new URLSearchParams(location.search);
  const [searchTerm, setSearchTerm] = useState(query.get("keyword") || ""); // URL에서 검색어 유지

  const handleSearch = () => {
    navigate(`/boards?page=${pagination.page}&size=${pagination.size}&keyword=${searchTerm}`)
  }

  /* boardList 샘플 데이터 생성 
  (서버로부터 받은 데이터가 없을때 테스트용으로 사용)
  */
 /* 샘플 데이터
  const boardList = [
    { no: 1, title: "글 제목1", writer: "작성자1", "createdAt" : "2025-02-20 1:21:50" },
    { no: 2, title: "글 제목2", writer: "작성자2", "createdAt" : "2025-02-20 1:21:50" },
    { no: 3, title: "글 제목3", writer: "작성자3", "createdAt" : "2025-02-20 1:21:50" },
    { no: 4, title: "글 제목4", writer: "작성자4", "createdAt" : "2025-02-20 1:21:50" },
    { no: 5, title: "글 제목5", writer: "작성자5", "createdAt" : "2025-02-20 1:21:50" },
  ]
  */

// 페이지 리스트를 state로 관리
const [pageList, setPageList] = useState([])

// ?파라미터 = 값 가져오는 법
/*   const location = useLocation()
  const query = new URLSearchParams(location.search)
  const page = query.get("page")  //"page" 파라미터 값을 가져온다.
  const size = query.get("size")  */

  const createPageList = () => {
    let newPageList = []

    for (let i = pagination.start; i <= pagination.end; i++) {
      newPageList.push(i)
    }
    setPageList(newPageList) // 셋팅된 리스트를 넣어주기
  }

  useEffect(() => {
    createPageList()  // 리스트가 변하면
  }, [pagination])  // 페이지네이션을 적용

  return (
    <div className="container">
      <h1 className="title">게시글 제목</h1>
      <Link to="/boards/insert" className="btn">
        글쓰기
      </Link>

    {/* 검색 입력창 추가 */}
      <input
        type="text"
        placeholder="검색어 입력"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      <button onClick={handleSearch}>검색</button>

      {/* 게시글 목록 */}
      {/* <table border={1} className='table'> */}
      <table border={1} className={`${styles.table}`}>
        <thead>
          <tr>
            {/* <th>번호</th> */}
            <th>이미지</th>
            <th>제목</th>
            <th>작성자</th>
            <th>등록일자</th>
          </tr>
        </thead>
        <tbody>
          {/* 반복문 작성 : 테스트용 샘플데이터 반복을 
              시키기 위해 map으로 기존 tr태그에 적힌
              값들을 끌어옴 
          */}
          
          {/* 화살표 함수의 내용이 한 문장이면, {}, return 생략 가능
             () => 
             () => ()

             {} 안에서 함수 내용을 작성 - return 선택
             () => { return ? }
            
             즉, 반환값이 있으면 return을 쓰고 없으면 안씀
             반환값이 있어서 중괄호로 묶었으면 ()로 감싸서 리턴을 해줘야
             값이 추출된다.
          */}
          
          {/* 표현해야 하는 값이 여러줄이라 중괄호로 묶은 경우
              (즉, return을 해야 하는 경우) 방법-1
          */}

          {/* {
              boardList.map( (board) => {
                  return (
                      <tr key={board.no}>
                        <td align='center'>{ board.no }</td>
                        <td align='left'>{ board.title }</td>
                        <td align='center'>{ board.writer }</td>
                        <td align='center'>{ board.createdAt }</td>
                      </tr>
                  )
              })
          } */}
          
          {/* 중괄호를 안쓰고 그냥 소괄호로 묶어 표현하는 경우, 방법-2 */}
          {/* 게시글 목록 title에 Link 걸기 전 코드
              {
                boardList.map( (board) => 
                  (
                        <tr key={board.no}>
                          <td align='center'>{ board.no }</td>
                          <td align='left'>{ board.title }</td>
                          <td align='center'>{ board.writer }</td>
                          <td align='center'>{ board.createdAt }</td>
                        </tr>
                    )
                )
              }  
            */}

          {/* 조건부 렌더링 추가 전 코드
              {
                boardList.map( (board) => 
                  (
                    <tr key={board.no}>
                          <td align='center'>{ board.no }</td>
                          <td align='left'>
                            <Link to={`/boards/${board.no}`}>
                              { board.title }
                            </Link>
                          </td>
                          <td align='center'>{ board.writer }</td>
                              날짜 format 적용 이전 코드
                              <td align='center'>{ board.createdAt }</td> 
                          
                          <td align='center'>{ format.formatDate( board.createdAt ) }</td>
                        </tr>
                    )
                )
              }  
              */}
          {/* 조건부 렌더링 코드 추가 후 : 조회된 데이터가 없을시 */}
          {
            boardList.length === 0 
            ? 
            <tr>
              <td colSpan={5} align="center">조회된 데이터가 없어요!</td>
            </tr>
             : 
            boardList.map( (board) => {
              return (
              <tr key={board.no}>
                {/* <td align='center'>{ board.no }</td> */}
                <td>
                  {
                  board.file == null
                   ? 
                    // <span>이미지 없음</span>
                    <img src={noImage} />
                   : 
                    <img src={`/api/files/img/${board.file.id}`}
                      style={ { width: "100px" } } alt={board.file.originName} />
                  }
                </td>
                <td align="left">
                  {/* <Link to={`/boards/${board.no}`}> 게시글 번호를 받아오던 이전 코드 */}
                  <Link to={`/boards/${board.id}`}>
                    { board.title }
                  </Link>
                </td>
                <td align="center">{ board.writer }</td>
                {/* 날짜 format 적용 이전 코드
                    <td align='center'>{ board.createdAt }</td> 
                  */}
                <td align="center">{ format.formatDate(board.createdAt) }</td>
              </tr>
            )
          })
        }
          
          {/* 반복시키기 전에 작성한 코드(샘플)
                <tr>
                  <td align='center'>1</td>
                  <td align='left'>게시글 제목</td>
                  <td align='center'>작성자</td>
                  <td align='center'>2025-02-20 1:21:50</td>
                </tr>
            */}
        </tbody>
      </table>

      {/* 페이지네이션 - a 태그 방식 */}

      {/* 
        <div className="pagination">
             <a href={`/boards?page=${pagination.first}`} className='btn-page'>처음</a> 
             <a href={`/boards?page=${pagination.prev}`} className='btn-page'>이전</a>

            {
              pageList.map( page => (

                  // - active 클래스 추가 하는 방법 (현재 페이지)
                // eslint-disable-next-line react/jsx-key : prop이 필요할 가능성이 있는 요소 
                //  key (예: 배열 리터럴 또는 화살표 함수 표현식에 있는 요소)에 대해 경고합니다.)
                   <a href='' className={page == pagination.page ? 'btn-page active' : 'btn-page' }>{page}</a>
                   <a href={`/boards?page=${page}`} className={'btn-page ' + ( page == pagination.page && 'active' ) }>{page}</a>

              ))
            }  
                  <a href={`/boards?page=${pagination.next}`} className='btn-page'>다음</a>
                  <a href={`/boards?page=${pagination.last}`} className='btn-page'>마지막</a> 
      </div> 
      */}

      {/* 페이지네이션 - Link 태그 방식 (사수께서 수정 해주신 코드) */}
      {/*   <div className="pagination">
          <Link to={`/boards?page=${pagination.first}`} className='btn-page'>처음</Link>
          <Link to={`/boards?page=${pagination.prev}`} className='btn-page'>이전</Link>
          {
            pageList.map( page => (

              // eslint-disable-next-line react/jsx-key
              <Link to={`/boards?page=${page}`} className={'btn-page ' + ( page == pagination.page && 'active' ) }>{page}</Link>
            ))
          }
          { pagination.end >= pagination.next && (
            <Link to={`/boards?page=${pagination.next}`} className="btn-page">다음</Link>
          )
          }
          { pagination.end == pagination.next && (
            <Link to={`/boards?page=${pagination.last}`} className='btn-page'>마지막</Link>
          )
          }
        </div> 
        */}

      {/* 페이지네이션 - Link 태그 방식 */}

      {/* 페이지네이션 - 검색 처리 적용*/}
      {pagination.total > 0 && (
        <div className="pagination">
          <Link to={`/boards?page=${pagination.first}&size=${pagination.size}&keyword=${searchTerm}`} className="btn-page">
            <KeyboardDoubleArrowLeftIcon />
          </Link>
          {pagination.page > pagination.first && (
            <Link to={`/boards?page=${pagination.prev}&size=${pagination.size}&keyword=${searchTerm}`} className="btn-page">
              <KeyboardArrowLeftIcon />
            </Link>
          )}
          {Array.from({ length: pagination.end - pagination.start + 1 }, (_, i) => pagination.start + i).map(page => (
            <Link key={page} to={`/boards?page=${page}&size=${pagination.size}&keyword=${searchTerm}`} className={`btn-page ${page === pagination.page ? 'active' : ''}`}>
              {page}
            </Link>
          ))}
          {pagination.page < pagination.last && (
            <Link to={`/boards?page=${pagination.next}&size=${pagination.size}&keyword=${searchTerm}`} className="btn-page">
              <KeyboardArrowRightIcon />
            </Link>
          )}
          <Link to={`/boards?page=${pagination.last}&size=${pagination.size}&keyword=${searchTerm}`} className="btn-page">
            <KeyboardDoubleArrowRightIcon />
          </Link>
        </div>
      )}
    </div>
  );
};

export default BoardList;
