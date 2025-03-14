import BoardList from "../../components/board/BoardListForm";
import * as boards from '../../apis/boards';
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

const ListContainer = () => {
  const [boardList, setBoardList] = useState([]);
  const [pagination, setPagination] = useState({});
  const location = useLocation();

  useEffect(() => {
    const query = new URLSearchParams(location.search);
    const newPage = Number(query.get("page")) || 1;
    const newSize = Number(query.get("size")) || 10;
    const keyword = query.get("keyword") || ""; // 검색어를 추가
    
    console.log(`updatePage() -> newPage: ${newPage}`);
    console.log(`updatePage() -> newSize: ${newSize}`);
    console.log(`updatePage() -> keyword: ${keyword}`); // 검색어 로그 출력
    
    const getList = async (pageParam, sizeParam) => {
      try {
        const response = await boards.list(pageParam, sizeParam, keyword, "title");
        const data = response.data;
        console.dir('getList() -> data 확인 : ', data);
        setBoardList(data.list);
        setPagination(data.pagination);
      } catch (error) {
        console.error("게시글 목록 가져오는 중 오류 발생: ", error);
      }
    };
    
    getList(newPage, newSize, keyword); // keyword 추가
  }, [location.search]);

  return (
    <>
      <div>ListContainer</div>
      <BoardList boardList={boardList} pagination={pagination} />
    </>
  );
};

export default ListContainer;
