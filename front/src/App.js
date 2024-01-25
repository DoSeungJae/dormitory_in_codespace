import './App.css';
import LogInPage from './routeComponents/logIn/LogInPage';
import {Routes,Route,BrowserRouter} from "react-router-dom";
import SignInPage from './routeComponents/signIn/SignInPage';
import HomePage from './routeComponents/home/HomePage';
import WritingPage from './routeComponents/home/WritingPage';
import MyWritingPage from './routeComponents/home/MyWritingPage';
import AlarmPage from './routeComponents/home/AlarmPage';
import ArticlePage from './routeComponents/article/ArticlePage';
import React,{useState} from 'react';
import AlertContext from './components/common/AlertContext';
import Alert from './components/common/Alert';

function App() {
  const [alert,setAlert]=useState(null);
  
  const showAlert=(message)=>{
    setAlert({message});
  }

  const closeAlert=()=>{
    setAlert(null);
  }

  return (

    <div className="App">

        <AlertContext.Provider value={showAlert}>
          {alert && <Alert message={alert.message} onClose={closeAlert} />}

          <BrowserRouter>
        <Routes>
          <Route exact path="/" element={<HomePage/>}></Route> 
          <Route path="/newWriting" element={<WritingPage/>}></Route>
          <Route path="/myWriting" element={<MyWritingPage/>}></Route>
          <Route path="/alarm" element={<AlarmPage/>}></Route>

          <Route path="/article" element={<ArticlePage/>}></Route>
          
          <Route path="/signIn" element={<SignInPage/>}></Route>
          <Route path="/logIn" element={<LogInPage/>}></Route>
        </Routes>
      </BrowserRouter>  
        </AlertContext.Provider>
     

      
    </div>


  );
}



export default App;
