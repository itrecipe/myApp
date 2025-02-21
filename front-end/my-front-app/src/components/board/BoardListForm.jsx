import { Link } from "react-router-dom";

const BoardList = ({ boardList }) => {

  /* boardList 샘플 데이터 생성 
  (서버로부터 받은 데이터가 없을때 테스트용으로 사용)
  */
  // const boardList = [
  //   { no: 1, title: "글 제목1", writer: "작성자1", "createdAt" : "2025-02-20 1:21:50" },
  //   { no: 2, title: "글 제목2", writer: "작성자2", "createdAt" : "2025-02-20 1:21:50" },
  //   { no: 3, title: "글 제목3", writer: "작성자3", "createdAt" : "2025-02-20 1:21:50" },
  //   { no: 4, title: "글 제목4", writer: "작성자4", "createdAt" : "2025-02-20 1:21:50" },
  //   { no: 5, title: "글 제목5", writer: "작성자5", "createdAt" : "2025-02-20 1:21:50" },
  // ]


  return (
    <div className="container">
      <h1>게시글 제목</h1>
      <Link to="/boards/insert" className="btn">글쓰기</Link>

      <table border={1}>
        <thead>
          <tr>
            <th>번호</th>
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
                          <td align='center'>{ board.createdAt }</td>
                        </tr>
                    )
                )
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
    </div>
  );
};

export default BoardList;
