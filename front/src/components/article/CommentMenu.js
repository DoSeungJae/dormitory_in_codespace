import {React,useEffect,useState} from 'react';  
import ForumOutlinedIcon from '@mui/icons-material/ForumOutlined';
import ThumbUpOutlinedIcon from '@mui/icons-material/ThumbUpOutlined';
import MoreVertOutlinedIcon from '@mui/icons-material/MoreVertOutlined';
import {IconButton} from '@mui/material';
import Swal from 'sweetalert2';
import { RestartAltOutlined } from '@mui/icons-material';
import ThreeDotsMenu from './ThreeDotsMenu';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


function CommentMenu({rootCommentId,setRootCommentId,setPlaceHolder,inputRef,isForReply,setIsReply,writerId,commentParam}){
    const [width,setWidth]=useState(130);
    const [isWriter,setIswriter]=useState(0);
    const navigate=useNavigate();
    const [comment,setComment]=useState("");
    const token=localStorage.getItem('token');

    useEffect(()=>{
        if(isForReply){
            setWidth(88);
        }
        isSame(token).then(result=>setIswriter(result));
        setComment(commentParam);
    })
 
    const isSame = async (token) => {
      try {
        const response = await axios.get(`${process.env.REACT_APP_HTTP_API_URL}/token/userId`, {
            headers: {
                'Authorization': `${token}`
            }
        });
        if(response.data===writerId){
            return 1;
        }
        else{
            return 0;

        }
    
    } catch (error) {
        //navigate('/logIn',{state:{from:"/article"}});
        console.error(error);
        return 0;
    }
    
    
    }

    const changeToReplyMode =  () => {
        setTimeout(()=>{
            setIsReply(1);
            setRootCommentId(rootCommentId);
            setPlaceHolder("대댓글을 입력하세요.");
            inputRef.current.focus();
        },300)

    }
    
    const commentMenuWidth = {
        width: width+'px',
      };

    const handleSwal= () => {
        Swal.fire({
          confirmButtonColor:"#FF8C00",
          icon:"question",
          text: "대댓글을 작성하시겠어요?",
          confirmButtonText:"예",
          cancelButtonText:"아니오",
          showCancelButton: true
        }).then((result)=>{
            if(result.isConfirmed){
                changeToReplyMode();
            }

        });
      }
    return (
        <div className="comment-menu" style={commentMenuWidth} >
            {!isForReply &&
            <div className="comment-menu-item" >
                <IconButton
                    onClick={() => {
                    handleSwal();
                    }}>
                    <ForumOutlinedIcon  fontSize="small"/>
                </IconButton>
            </div>
            }
            
            <div className="comment-menu-item">
                <IconButton
                    onClick={() => {
                    console.log("like");
                    }}>
                    <ThumbUpOutlinedIcon  fontSize="small"/>
                </IconButton>
            </div>

            <div className='comment-menu-item'>
                <ThreeDotsMenu isWriterParam={isWriter} 
                isForReply={1}
                commentParam={comment}
                >
                </ThreeDotsMenu>
            </div>
        </div>
    )
}

export default CommentMenu;
