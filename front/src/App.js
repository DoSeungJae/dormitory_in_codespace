import './App.css';
import {Routes,Route,BrowserRouter} from "react-router-dom";;
import {React,useState} from 'react';
import { ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import AlertContext from './components/common/AlertContext';
import {toast} from 'react-toastify';
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
    //위 함수는 곧 삭제될 예정임 
  
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
            </Routes>
          </BrowserRouter>
          </HomeSelectContext.Provider>
        </AlertContext.Provider>
      </div>
    </div>


  );
}



export default App;
