import './App.css';
import LogInPage from './routeComponents/logIn/LogInPage';
import {Routes,Route,BrowserRouter} from "react-router-dom";
import SignInPage from './routeComponents/signIn/SignInPage';
import HomePage from './routeComponents/home/HomePage';
import WritingPage from './routeComponents/home/WritingPage';
import MyWritingPage from './routeComponents/home/MyWritingPage';
import AlarmPage from './routeComponents/home/AlarmPage';
import ArticlePage from './routeComponents/article/ArticlePage';
import React,{useState,useContext} from 'react';
import { ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AlertContext from './components/common/AlertContext';
import {toast} from 'react-toastify';
import PatchPage from './routeComponents/home/PatchPage';

function App() {
  const testFunction= () => {
    console.log("전역 함수");
  };

  const setAlert= (location) => {
    if(!location.state){
      return 
    }
    else if(!location.state.type){
      toast(location.state.message);
    }
    else if(location.state.type=="success"){
      toast.success(location.state.message);
    }
    else if(location.state.type=="info"){
      toast.info(location.state.message);
    }
    else if(location.state.type=="warning"){
      toast.warning(location.state.message);
    }
    else if(location.state.type=="error"){
      toast.error(location.state.message);
    }
    location.state=0;
  }
  

    //서비스 배포시 유의
  
  
  
  return (
    <div className="App">
      <div>
        <ToastContainer
          className="toast-position"
          position='top-center'/>


        <AlertContext.Provider value={setAlert}>
          <BrowserRouter>
            <Routes>
              <Route exact path="/" element={<HomePage/>}></Route> 
              <Route path="/newWriting" element={<WritingPage/>}></Route>
              <Route path="/myWriting" element={<MyWritingPage/>}></Route>
              <Route path="/alarm" element={<AlarmPage/>}></Route>

              <Route  path="/article" element={<ArticlePage/>}></Route> {/* exact가 필요한가 ??*/}
              <Route path="/article/patch"element={<PatchPage/>}></Route>
              
              <Route path="/signIn" element={<SignInPage/>}></Route>
              <Route path="/logIn" element={<LogInPage/>}></Route>
            </Routes>
          </BrowserRouter>
        </AlertContext.Provider>
      </div>
    </div>


  );
}



export default App;
