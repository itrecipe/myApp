import BoardList from "../../components/board/BoardListForm";

const ListContainer = () => {
  return (
    // <></> 리액트 프래그먼트 문법이자 축약형 문법 : 여러 요소를 하나의 부모로 묶을 수 있도록 해줌 (그룹화)
    <>
      <div>ListContainer</div>
      <BoardList />
    </>
  );
};

export default ListContainer;
