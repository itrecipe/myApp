import { useContext, useEffect } from 'react'
import Header from '../components/Header/Header'
import UserForm from '../components/User/UserForm'
import * as auth from '../apis/auth'
import * as Swal from '../apis/alert'
import { useNavigate } from 'react-router-dom'
import { LoginContext } from '../contexts/LoginContextProvider'

const User = () => {

  // context
  const { isLoading, isLogin, roles, logout, userInfo } = useContext(LoginContext)

  const navigate = useNavigate()

  // 회원 정보 수정
  const updateUser = async ( form ) => {
    console.log(form)

    let response
    let data

    try {
      response = await auth.update(form)
    } catch (error) {
      console.error(`error : 회원정보 수정 중 에러 발생!`, error.message);
      return
    }

    data = response.data
    const status = response.status
    console.log(`data : ${data}`);
    console.log(`status : ${status}`);

    if( status == 200 ) {
      console.log(`회원정보 수정 성공!`);
      Swal.alert("회원정보 수정 성공!", "로그아웃 이후 다시 로그인 해주세요!", "success",
        // 로그아웃 처리
        () => { logout(true) }
    )
  } else {
    console.log(`회원정보 수정 실패!`);
    Swal.alert("회원정보 수정 실패!", "회원정보 수정을 실패 하였습니다!", "error")
  }
}

  // 회원 탈퇴
  const deleteUser = async (username) => {
      console.log(username);

      let response
      let data
      try {
        response = await auth.remove(username)
      } catch (error) {
        console.error("에러 메시지 확인 : ", error.message);
        console.error(`회원 탈퇴 처리 중 에러 발생!`);
      }

      data = response.data
      const status = response.status
      console.log(`data : ${data}`);
      console.log(`status : ${status}`);

      if( status == 200) {
        Swal.alert("회원 탈퇴 성공!", "이용해 주셔서 감사합니다!", "success",
          () => logout(true)
        )
      }
    else {
      Swal.alert("회원 탈퇴 실패!", "들어올 땐 자유, 나갈땐 아님", "error")
    }
  
  }

  useEffect( () => {
    // 로딩중...
    if( isLoading ) return

    // 사용자 정보가 로딩완료 되었을 때만, 로그인 여부 체크
    if( !isLogin || !roles.isUser ) {
      navigate("/login")
    }
  }, [isLoading, isLogin, roles, navigate])

  // 개선 작업중
  // useEffect( () => {
  //   // 로딩중...
  //   if( isLoading ) return

  //   // 사용자 정보가 로딩완료 되었을때만 로그인 여부를 체크한다.
  //   // 두 권한이 모두 없는 경우 로그인 페이지로 리다이렉트 한다.
  //   if( !isLogin || !roles.isUser || !roles.isAdmin ) {
  //     navigate("/login")
  //   }
  // }, [isLoading, isLogin, roles, navigate]);

  return (
    <>
        <Header />
        <div className="container">
            <UserForm userInfo={userInfo} updateUser={updateUser} deleteUser={deleteUser} />
        </div>
    </>
  )
}

export default User