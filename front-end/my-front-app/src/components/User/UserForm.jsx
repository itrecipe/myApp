const UserForm = ({ userInfo, updateUser, deleteUser }) => {

// 정보 수정
const onUpdate = (e) => {
    e.preventDefault() // 해당 이벤트 기본동작 막기(제거)

    const form = e.target
    const username = form.username.value
    const password = form.password.value
    const name = form.name.value
    const email = form.email.value
    console.log( username, password, name, email );

    updateUser( { username, password, name, email } )
}

  return (
    <div className="form">
        <h2 className="login-title">회원 정보</h2>
        
        <form className='login-form' onSubmit={ (e) => onUpdate(e) }>
            {/* username 입력 창*/}
            <div>
                <label htmlFor="username">username</label>
                <input type="text" 
                       id='username'
                       placeholder='username'
                       name='username'
                       autoComplete="username"
                       required 
                       readOnly
                       defaultValue={ userInfo?.username }/>
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
                       required 
                       defaultValue={ userInfo?.name }
                       />
            </div>
            
            {/* email 입력 창 */}
            <div>
                <label htmlFor="email">email</label>
                <input type="text" 
                       id='email'
                       placeholder='email'
                       name='email'
                       autoComplete='email'
                       required
                       defaultValue={ userInfo?.email }
                       />
            </div>

            {/* 회원 정보수정 버튼 */}
            <button type='submit' className="btn btn--form btn-login">
                정보 수정
            </button>

            {/* 회원 탈퇴 버튼 */}
            <button type='submit' className="btn btn--form btn-login"
                    onClick={ () => deleteUser( userInfo.username ) }>
                회원 탈퇴
            </button>
        </form>
    </div>
  )
}

export default UserForm