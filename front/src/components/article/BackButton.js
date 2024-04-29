import {React,useContext} from 'react';
import {useNavigate} from 'react-router-dom';
import HomeSelectContext from '../home/HomeSelectContext';

function BackButton({pageInit}) {
    const navigate=useNavigate();
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);

  return (
    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" 
        className="bi bi-chevron-left" viewBox="0 0 16 16" onClick={() => pageInit()}>
    <path fillRule="evenodd" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0"/>
    </svg>
  );
}

export default BackButton;