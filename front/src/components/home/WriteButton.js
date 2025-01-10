import PostArticle from '@mui/icons-material/CreateRounded';
import { Button } from "@mui/material";
import { goToPostingPage } from './HomeUtils';
import { useContext } from 'react';
import HomeSelectContext from './HomeSelectContext';
import WriteIcon from '@mui/icons-material/Create';;
const WriteButton = () => {
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    return (
    <div>        
        <button  className='write-button' onClick={() => goToPostingPage(selectComponentIndex,setSelectComponentIndex)}>
          <div className='write-button-icon'><WriteIcon fontSize='medium'/></div>
          <div className='write-button-text'>글 쓰기</div>
        </button>
    </div>

    );
}

export default WriteButton;
