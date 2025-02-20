import { BrowserRouter, Route, Routes } from 'react-router-dom' // 1. 기본적으로 react-router-dom 먼저 임포트 해주기
import './App.css'
import Home from './pages/Home'
import Login from './pages/Login'
import Join from './pages/Join'
import User from './pages/User'
import About from './pages/About'
import LoginContextProvider from './contexts/LoginContextProvider'
import List from './pages/board/List'
import Read from './pages/board/Read'
import Insert from './pages/board/Insert'
import Update from './pages/board/Update'

function App() {

  return (
    // 2. BrowserRouter : 라우팅 할 수 있도록 활성화
    <BrowserRouter> 
      <LoginContextProvider>
        {/* login 컴포넌트 라우팅 */}
        <Routes> {/* 3. 이동할 라우터들 구성하기 */}

          <Route path="/" element={<Home />}></Route>
          <Route path="/login" element={<Login />}></Route>
          <Route path="/join" element={<Join />}></Route>
          <Route path="/user" element={<User />}></Route>
          <Route path="/about" element={<About />}></Route>
          
          {/* board 컴포넌트 라우팅 */}
          <Route path='/' element={ <Home /> }></Route>
          <Route path='/boards' element={ <List /> }></Route>
          <Route path='/boards/:id' element={ <Read /> }></Route> {/* Read는 path 경로에 /boards/:id 파라미터를 같이 넘겨주기 */}
          <Route path='/boards/insert' element={ <Insert /> }></Route>
          <Route path='/boards/update/:id' element={ <Update /> }></Route> {/* Update도 path 경로에 /boards/update/:id 파라미터를 같이 넘겨주기 */}

        </Routes>
      </LoginContextProvider>
    </BrowserRouter>    
  )
}

export default App
