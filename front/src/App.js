import './App.css';
import LogInPage from './routeComponents/logIn/LogInPage';
import {Routes,Route,BrowserRouter} from "react-router-dom";
import SignInPage from './routeComponents/signIn/SignInPage';
import HomePage from './routeComponents/home/HomePage';
import WritingPage from './routeComponents/home/WritingPage';
import MyWritingPage from './routeComponents/home/MyWritingPage';
import AlarmPage from './routeComponents/home/AlarmPage';

function App() {
  
  return (

    <div className="App">
      
      <BrowserRouter>
        <Routes>
          <Route exact path="/" element={<HomePage/>}></Route> {/*홈 주소 : /home*/ }

          {/* 기숙사 페이지는 로그인 없이 이동할 수 있도록 함, 단
            해당 페이지 안에 있는 글을 조회하거나 홈 메뉴를 제외한 하단 메뉴로 이동할 때에는
            로그인이 필요하도록 하여 서버에서 jwt 유효성 검사가 수행됨..*/}

          <Route path="/myWriting" element={<MyWritingPage/>}></Route>
          <Route path="/alarm" element={<AlarmPage/>}></Route>

          <Route path="/signIn" element={<SignInPage/>}></Route>
          <Route path="/logIn" element={<LogInPage/>}></Route>
          <Route path="/newWriting" element={<WritingPage/>}></Route>

        </Routes>
      </BrowserRouter>  
      
    </div>


  );
}



export default App;
