import Header from '../components/Header/Header'
import JoinForm from '../components/Join/JoinForm'
import * as auth from '../apis/auth'
import * as Swal from '../apis/alert'
import { useNavigate } from 'react-router-dom'


const Join = () => {

  // useNavigate 훅을 이용해 화면 이동을 위한 navigate 객체를 생성
  const navigate = useNavigate() 

  // 회원 가입 요청
  const join = async ( form ) => {
    console.log(form);

    let response
    let data
    try {
      response = await auth.join(form)

    } catch(error) {
      console.log(error);
      console.error(`회원가입 중 에러가 발생!`);
      return
    }

    data = response.data
    const status = response.status
    // 응답 받은 상태와 메시지(데이터)를 확인하기
    console.log(`data : ${data}`);
    console.log(`status : ${status}`);

    if ( status == 200) {
      console.log('회원가입 성공!');
      // alert('회원가입 성공!')
      Swal.alert(
        '회원가입 성공!', '로그인 화면으로 이동중...', 'success',
        () => { navigate('/login') }
      )

    } else {
      console.log('회원가입 실패!');
      // alert('회원가입 실패!')
      Swal.alert('회원가입 실패!', '회원가입에 실패했습니다', 'error')

    }
  }
  return (
    <>
        <Header />
        <div className="container">
            <JoinForm join={ join } />
        </div>
    </>
  )
}

export default Join