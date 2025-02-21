import { createContext, useEffect, useState } from "react";
import * as auth from "../apis/auth";
import Cookies from "js-cookie";
import * as Swal from "../apis/alert";
import api from "../apis/api";
import { useNavigate } from "react-router-dom";

// 컨텍스트 생성
export const LoginContext = createContext();

const LoginContextProvider = ({ children }) => {
  
  // 로딩중
  const [isLoading, setIsLoading] = useState(true);

  // 로그인 여부
  const [isLogin, setIsLogin] = useState(() => {
    const savedIsLogin = localStorage.getItem("isLogin");
    return savedIsLogin ?? false;
  });

  // 사용자 정보
  const [userInfo, setUserInfo] = useState(() => {
    const savedUserInfo = localStorage.getItem("userInfo");
    return savedUserInfo ? JSON.parse(savedUserInfo) : null;
  });

  // 권한 정보
  const [roles, setRoles] = useState(() => {
    const savedRoles = localStorage.getItem("roles");
    return savedRoles
      ? JSON.parse(savedRoles)
      : { isUser: false, isAdmin: false };
  });

  // 페이지 이동
  const navigate = useNavigate();

  // 로그인 함수
  const login = async (username, password) => {
    console.log(`username : ${username}`);
    console.log(`password : ${password}`);

    try {
      const response = await auth.login(username, password);
      const data = response.data; // {user}
      const status = response.status;
      const headers = response.headers;
      const authorization = headers.authorization;
      const jwt = authorization.replace("Bearer ", "");

      console.log(`data : ${data}`);
      console.dir(data);
      console.log(`status : ${status}`);
      console.log(`headers : ${headers}`);
      console.log(`authorization : ${authorization}`);
      console.log(`jwt : ${jwt}`);

      // 로그인 성공
      if (status == 200) {
        // JWT를 쿠키에 등록하기
        Cookies.set("jwt", jwt, { expires: 5 }); // 5일 후 만료되게 설정

        // 로그인 세팅 - loginSetting(JWT 인증 토큰, 사용자)
        loginSetting(authorization, data);

        // 로그인 성공 alert
        Swal.alert("로그인 성공", "메인 화면으로 이동중...", "success", () => navigate("/"));
      }
    } catch (error) {
      // 로그인 실패 alert
      Swal.alert("로그인 실패", "ID or PW가 일치하지 않음!", "error");
      // 기존 디버깅 코드 console.log("로그인 실패! 및 이유 : "); 트러블 슈팅 :catch 블록에 error 정의 해두고 안써서 eslint 경고 및 오류 났었음.
      console.log("로그인 실패! 및 이유 : " , error.message); // 개선한 디버깅 코드
    }
  };

    /* 로그인 세팅
     @param {*} authorization : Bearre {jwt}
     @param {*} data          : { user }
  */
     const loginSetting = (authorization, data) => {
      // JWT를 Authorization 헤더에 등록하기
      api.defaults.headers.common.Authorization = authorization;
  
      // 로그인 여부
      setIsLogin(true);
      localStorage.setItem("isLogin", "true"); // localStorage에 등록
  
      // 사용자 정보
      setUserInfo(data);
      localStorage.setItem("userInfo", JSON.stringify(data)); // localStorage에 등록
  
      // 권한 정보
      const updateRoles = { isUser: false, isAdmin: false };
      data.authList.forEach((obj) => {
        if (obj.auth == "ROLE_USER") updateRoles.isUser = true;
        if (obj.auth == "ROLE_ADMIN") updateRoles.isAdmin = true;
      });
      setRoles(updateRoles);
      localStorage.setItem("roles", JSON.stringify(updateRoles)); // localStorage에 등록
    };


  //  로그아웃 함수
  const logout = (force = false) => {
    // 강제 로그아웃
    if (force) {
      // 로딩중
      setIsLoading(true);
      // 로그아웃 세팅
      logoutSetting();
      // 페이지 이동 -> "/" 메인 화면으로 이동
      navigate("/");
      setIsLoading(false);
      return;
    }

    // 로그아웃 확인
    Swal.confirm(
      "정말 로그아웃 하시나요?",
      "로그아웃 진행중...",
      "warning",
      (result) => {
        if (result.isConfirmed) {
          Swal.alert("로그아웃 성공!", "로그아웃 되었습니다.", "success");
          
          // 로그아웃 세팅
          logoutSetting();

          // 페이지 이동 -> "/" 메인화면 으로 이동하기
          navigate("/");
          return
        }
      }
    )
  };

  // 로그아웃 세팅
  const logoutSetting = () => {

    // Authorization 헤더 초기화
    api.defaults.headers.common.Authorization = undefined

    // 로그인 여부 : false
    setIsLogin(false)
    localStorage.removeItem("isLogin")

    // 유저 정보 초기화
    setUserInfo(null)
    localStorage.removeItem("userInfo")

    // 권한 정보 초기화
    setRoles( {isUser: false, isAdmin: false} )
    localStorage.removeItem("roles");

    // 쿠키 제거
    Cookies.remove("jwt")
  }

  /* 자동 로그인
    1. 쿠키에서 JWT 가져오기
    2. jwt가 있으면, 사용자 정보를 요청
    3. 로그인 세팅 (로그인 여부, 사용자 정보, 권한)
    - 쿠키에 저장된 JWT 토큰을 읽어와서 로그인 처리
  */
const autoLogin = async () => {
  // 쿠키에서 jwt를 가져오기
  const jwt = Cookies.get("jwt")

  // jwt 토큰이 없으
  if( !jwt ) {
    // TODO: 로그아웃 세팅
    return
  }

  // 
  console.log(`jwt : ${jwt}`);
  const authorization = `Bearer ${jwt}`

  // JWT를 Authorization 헤더에 등록
  api.defaults.headers.common.Authorization = authorization

  // 사용자 정보 요청
  let response
  let data

  try {
    response = await auth.info()
  } catch (error) {
    console.error(`error : ${error}`);
    console.log(`status : ${response.status}`);
    return
  }

  // 인증 실패
  if( response.data == 'UNAUTHORIZED' || response.status == 401 ) {
    console.error(`jwt 토큰이 만료 되었거나 인증에 실패 했습니다!`);
    return
  }

  // 인증 성공
  console.log(`jwt로 자동 로그인 성공!`);

  data = response.data

  // 로그인 세팅 - loginbSetting(jwt 토큰, 사용자 정보)
  loginSetting(authorization, data)
}

  useEffect(() => {
    const savedIsLogin = localStorage.getItem("isLogin")
    if( !savedIsLogin || savedIsLogin == false ) {
      autoLogin().then(() => {
        console.log(`로딩 완료!`);

        // 로딩 완료
        setIsLoading(false)
      })
    }
    else {
      // 로딩 완료
      setIsLoading(false)
    }
  }, []);

  return (
    // 컨텍스트 값 지정 -> value={ ?, ? }

    // 로그인 여부, 로그아웃, 로그인, 유저정보, 권한 상태를 등록
    <LoginContext.Provider value={{ isLoading, isLogin, logout, login, userInfo, roles }}>
      {children}
    </LoginContext.Provider>
  );
};

export default LoginContextProvider;
