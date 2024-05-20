import PostArticle from '@mui/icons-material/CreateRounded';
import { Button } from "@mui/material";

const WriteButton = () => {
    const goToPostingPage = () => {
        console.log(123);
    }
    return (
    <div>        
        <button  className='write-button' onClick={() => alert('Write button clicked')}>
          글쓰기
        </button>
    </div>

    );
}

export default WriteButton;
