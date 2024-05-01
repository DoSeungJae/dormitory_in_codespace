import './App.css';
import {Routes,Route,BrowserRouter} from "react-router-dom";;
import {React,useState} from 'react';
import { ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {toast} from 'react-toastify';
import HomePageSelect from './routeComponents/home/HomePageSelect';
import HomeSelectContext from './components/home/HomeSelectContext';

function App() {
  const [selectComponentIndex,setSelectComponentIndex]=useState(0);

  return (
    <div className="App">
      <div>
        <ToastContainer
          className="toast-position"
          position='top-center'/>

        <HomeSelectContext.Provider value={{selectComponentIndex,setSelectComponentIndex}}>
          <BrowserRouter>
            <Routes>
              <Route exact path="/" element={<HomePageSelect/>}></Route> 
            </Routes>
          </BrowserRouter>
          </HomeSelectContext.Provider>
      </div>
    </div>


  );
}



export default App;
