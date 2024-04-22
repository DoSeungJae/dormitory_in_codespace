import './App.css';
import LogInPage from './routeComponents/logIn/LogInPage';
import {Routes,Route,BrowserRouter} from "react-router-dom";
import SignInPage from './routeComponents/signIn/SignInPage';
import ArticlePage from './routeComponents/article/ArticlePage';
import {React,createContext,useState} from 'react';
import { ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AlertContext from './components/common/AlertContext';
import {toast} from 'react-toastify';
import PatchPage from './routeComponents/home/PatchPage';
import HomePageSelect from './routeComponents/home/HomePageSelect';
import HomeSelectContext from './components/home/HomeSelectContext';
function App() {
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
  
  
  const [selectComponentIndex,setSelectComponentIndex]=useState(0);

  return (
    <div className="App">
      <div>
        <ToastContainer
          className="toast-position"
          position='top-center'/>


        <AlertContext.Provider value={setAlert}>
        <HomeSelectContext.Provider value={{selectComponentIndex,setSelectComponentIndex}}>
          <BrowserRouter>
            <Routes>
              <Route exact path="/" element={<HomePageSelect/>}></Route> 
              
              <Route  path="/article" element={<ArticlePage/>}></Route> {/* exact가 필요한가 ??*/}
              <Route path="/article/patch"element={<PatchPage/>}></Route>
              
              <Route path="/signIn" element={<SignInPage/>}></Route>
              <Route path="/logIn" element={<LogInPage/>}></Route>
            </Routes>
          </BrowserRouter>
          </HomeSelectContext.Provider>
        </AlertContext.Provider>
      </div>
    </div>


  );
}



export default App;
