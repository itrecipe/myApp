import BoardInsertForm from '../../components/board/BoardInsertForm'
import * as boards from '../../apis/boards'
import { useNavigate } from 'react-router-dom';
import * as Swal from '../../apis/alert'

const InsertContainer = () => {

  // 페이지 목록으로 이동하기
  const navigate = useNavigate()

  // 게시글 등록 요청 이벤트 헨들러 생성
  const onInsert = async (title, writer, content) => {
    try {
      const response = await boards.insert(title, writer, content) // 등록 응답 요청
      const data = await response.data // 데이터 응답 받기
      console.log('onInsert_data: ', data);

      Swal.alert("등록 완료!")

      // 게시글 목록으로 이동
      navigate('/boards')

    } catch(error) {
      console.log('onInsert_error: ', error);
    }
  }

  return (
    <>
      <div>InsertContainer</div>
      <BoardInsertForm onInsert={onInsert} />
    </>
  );
};

export default InsertContainer;
