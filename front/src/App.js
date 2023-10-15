
import './App.css';
import LogInPage from './routeComponents/logIn/LogInPage';
import {Routes,Route,BrowserRouter} from "react-router-dom";
import SignInPage from './routeComponents/signIn/SignInPage';
import HomePage from './routeComponents/home/HomePage';

function App() {
  
  return (

    <div className="App">
      
      <BrowserRouter>
        <Routes>
          <Route exact path="/" element={<HomePage/>}></Route> {/*홈 주소 : /home*/ }
          <Route path="/oreum1"></Route> {/* /home/oreum1 ... */}
          <Route path="/oreum2"></Route>
          <Route path="/oreum3"></Route>

          <Route path="/preum1"></Route>
          <Route path="/preum2"></Route>
          <Route path="/preum3"></Route>
          <Route path="/preum4"></Route>

          {/* 기숙사 페이지는 로그인 없이 이동할 수 있도록 함, 단
            해당 페이지 안에 있는 글을 조회하거나 홈 메뉴를 제외한 하단 메뉴로 이동할 때에는
            로그인이 필요하도록 하여 서버에서 jwt 유효성 검사가 수행됨..*/}

          <Route path="/myWriting"></Route>
          <Route path="/newWriting"></Route>
          <Route path="/alarm"></Route>


          <Route path="/signIn" element={<SignInPage/>}></Route>
        </Routes>
      </BrowserRouter>  
      
    </div>


  );
}



export default App;
