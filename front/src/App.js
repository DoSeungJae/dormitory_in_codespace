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
          <Route exact path="/" element={<WritingPage/>}></Route> {/*홈 주소 : /home*/ }
          
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
