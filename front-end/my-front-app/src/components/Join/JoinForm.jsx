import './JoinForm.css'
// import { useState } from "react";

const JoinForm = ({ join }) => {

  // const [selectedRoles, setSelectedRoles] = useState(["ROLE_USER"]) // 기본 권한 : ROLE_USER를 포함

  //역할 선택 시 상태를 업데이트 (추가기능)
  // const handleRoleChange = (e) => {
  //   const options = e.target.options;
  //   console.log('options 값 : ', options)

  //   const selected = [];
  //   console.log("selected 값 : ", selected)
    
  //   for(let i = 0; i < options.length; i++) {
  //     if (options[i].selected) {
  //       selected.push(options[i].value);
  //     }
  //   }
  //   setSelectedRoles(selected);
  // };

  // 회원 가입하기 클릭
  const onJoin = (e) => {
    e.preventDefault() // submit의 기본 동작을 방지(제거) 한다.

    // 폼 태그 안에 들어갈 정보 값을 꺼내오기
    const form = e.target // 폼태그를 지정해서 들고오자
    const username = form.username.value
    const password = form.password.value
    const name = form.name.value
    const email = form.email.value

    // console.log(username, password, name, email, selectedRoles);
    console.log(username, password, name, email);

    // join( { username, password, name, email, 
    // authList: selectedRoles.map((role)=> ({auth: role}))} )
    
    // 객체로 묶어 요청 날리기
    join( { username, password, name, email })
  }

  return (
    <div className="form">
        <h2 className="login-title">회원가입</h2>
        <form className='login-form' onSubmit={ (e) => onJoin(e) }>
            {/* username 입력 창*/}
            <div>
                <label htmlFor="username">username</label>
                <input type="text" 
                       id='username'
                       placeholder='username'
                       name='username'
                       autoComplete="username"
                       required />
            </div>
            {/* password 입력 창 */}
            <div>
                <label htmlFor="password">password</label>
                <input type="password" 
                       id='password'
                       placeholder='password'
                       name='password'
                       autoComplete='password'
                       required />
            </div>
            {/* name 입력 창 */}
            <div>
                <label htmlFor="name">name</label>
                <input type="text" 
                       id='name'
                       placeholder='name'
                       name='name'
                       autoComplete='name'
                       required />
            </div>
            {/* email 입력 창 */}
            <div>
                <label htmlFor="email">email</label>
                <input type="text" 
                       id='email'
                       placeholder='email'
                       name='email'
                       autoComplete='email'
                       required />
            </div>

            {/* 역할 선택 드롭다운 - 추가기능 */}
            {/* <div>
              <label htmlFor='role'></label>
              <select name="role" id="role" multiple onChange={handleRoleChange}>
                <option value="ROLE_USER" selected>
                  일반 사용자
                </option>
                <option value="ROLE_ADMIN" selected>
                  관리자
                </option>
              </select>
            </div> */}

            {/* 회원가입 버튼 */}
            <button type='submit' className="btn btn--form btn-login">
                회원가입
            </button>
        </form>
    </div>
  )
};

export default JoinForm;
