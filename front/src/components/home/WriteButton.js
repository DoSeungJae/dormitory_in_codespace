import PostArticle from '@mui/icons-material/CreateRounded';
import { Button } from "@mui/material";
import { goToPostingPage } from './HomeUtils';
import { useContext } from 'react';
import HomeSelectContext from './HomeSelectContext';

const WriteButton = () => {
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    return (
    <div>        
        <button  className='write-button' onClick={() => goToPostingPage(selectComponentIndex,setSelectComponentIndex)}>
          글쓰기
        </button>
    </div>

    );
}

export default WriteButton;
