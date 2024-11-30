import {React,useContext} from 'react';
import HomeSelectContext from '../home/HomeSelectContext';

function ForwardButton({destIndex}) {
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);

    return (
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" 
            className="bi bi-chevron-right" viewBox="0 0 16 16"
            onClick={() => setSelectComponentIndex(destIndex)}>
            <path fillRule="evenodd" 
                d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"
            />
        </svg>
    );
}

export default ForwardButton;