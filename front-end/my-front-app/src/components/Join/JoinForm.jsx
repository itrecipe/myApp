import './JoinForm.css'

const JoinForm = ({ join }) => {

  // 회원 가입하기 클릭
  const onJoin = (e) => {
    e.preventDefault() // submit의 기본 동작을 방지(제거) 한다.

    // 폼 태그 안에 들어갈 정보 값을 꺼내오기
    const form = e.target // 폼태그를 지정해서 들고오자
    const username = form.username.value
    const password = form.password.value
    const name = form.name.value
    const email = form.email.value

    console.log(username, password, name, email);

    // 객체로 묶어 요청 날리기
    join( { username, password, name, email } )
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

            {/* 회원가입 버튼 */}
            <button type='submit' className="btn btn-form btn-login">
                회원가입
            </button>
        </form>
    </div>
  )
};

export default JoinForm;
