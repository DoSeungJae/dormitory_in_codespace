import React from 'react';
import ForumOutlinedIcon from '@mui/icons-material/ForumOutlined';
import ThumbUpOutlinedIcon from '@mui/icons-material/ThumbUpOutlined';
import MoreVertOutlinedIcon from '@mui/icons-material/MoreVertOutlined';
import {IconButton} from '@mui/material';
import Swal from 'sweetalert2';
import { RestartAltOutlined } from '@mui/icons-material';

function CommentMenu({setPlaceHolder,inputRef,isReply,setIsReply}){
    const changeToReplyMode = () => {
        setTimeout(()=>{
            setIsReply(1);
            setPlaceHolder("대댓글을 입력하세요.");
            inputRef.current.focus();
        },300)

    }
    //문제 해결 블로그 작성하기!
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
        <div className="comment-menu">
            <div className="comment-menu-item">
                <IconButton
                    onClick={() => {
                    handleSwal();
                    }}>
                    <ForumOutlinedIcon  fontSize="small"/>
                </IconButton>
                
            </div>
            <div className="comment-menu-item">
                <IconButton
                    onClick={() => {
                    console.log("like");
                    }}>
                    <ThumbUpOutlinedIcon  fontSize="small"/>
                </IconButton>
                
            </div>
            <div className="comment-menu-item">
                <IconButton
                    onClick={() => {
                    console.log("threeDots");
                    }}>
                    <MoreVertOutlinedIcon  fontSize="small"/>
                </IconButton>
 
            </div>
        </div>
    )
}

export default CommentMenu;
