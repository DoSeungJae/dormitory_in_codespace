import 'react-toastify/dist/ReactToastify.css';
import HomePageSelect from './routeComponents/home/HomePageSelect';
import HomeSelectContext from './components/home/HomeSelectContext';
import {React,useState} from 'react';
import './App.css';
import {Routes,Route,BrowserRouter} from "react-router-dom";
import ModalContext from './components/common/ModalContext';
import { StyledToastContainer } from './modules/common/Toast';
import ProfileImageContext from './components/common/ProfileImageContext';

function App() {
  const [selectComponentIndex, setSelectComponentIndex] = useState(-1);
  const [isOpen, setIsOpen] = useState(false);
  const [content, setContent] = useState({});
  const [profileImages, setProfileImages] = useState({"USERID":{}, "NICKNAME":{}});
  
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
        <StyledToastContainer
          position='top-center'
          closeOnClick={true}
          limit={5}
        />

        <HomeSelectContext.Provider value={{selectComponentIndex,setSelectComponentIndex}}>
        <ModalContext.Provider value={{isOpen,openModal,closeModal,content,setContent}}>
        <ProfileImageContext.Provider value={{profileImages, setProfileImages}}>
          <BrowserRouter>
            <Routes>
              <Route exact path="/" element={<HomePageSelect/>}></Route> 
            </Routes>
          </BrowserRouter>
        </ProfileImageContext.Provider>
        </ModalContext.Provider>
        </HomeSelectContext.Provider>
      </div>
      }
    </div>


  );
}



export default App;
