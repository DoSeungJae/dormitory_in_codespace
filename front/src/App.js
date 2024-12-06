import 'react-toastify/dist/ReactToastify.css';
import {toast} from 'react-toastify';
import HomePageSelect from './routeComponents/home/HomePageSelect';
import HomeSelectContext from './components/home/HomeSelectContext';
import { ToastContainer} from 'react-toastify';
import {React,useState} from 'react';
import './App.css';
import {Routes,Route,BrowserRouter} from "react-router-dom";
import ModalContext from './components/common/ModalContext';

function App() {
  const [selectComponentIndex,setSelectComponentIndex]=useState(0);
  const [isOpen, setIsOpen]=useState(false);
  const [content,setContent]=useState({});
  
  const openModal = (modalContent) => {
    if(isOpen){
      return ;
    }
    setContent(modalContent);
    setIsOpen(true);
  };
  const closeModal = () => {
    setIsOpen(false);
    setContent({});
  };

  return (
    <div className="App">
      {
      <div>
        <ToastContainer
          className="toast-position"
          position='top-center'/>

        <HomeSelectContext.Provider value={{selectComponentIndex,setSelectComponentIndex}}>
        <ModalContext.Provider value={{isOpen,openModal,closeModal,content,setContent}}>
          <BrowserRouter>
            <Routes>
              <Route exact path="/" element={<HomePageSelect/>}></Route> 
            </Routes>
          </BrowserRouter>
        </ModalContext.Provider>
        </HomeSelectContext.Provider>
      </div>
      }
    </div>


  );
}



export default App;
