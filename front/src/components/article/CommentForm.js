import React, { useState,useRef} from 'react';
import { IconButton, TextField } from '@mui/material';
import SendIcon from '@mui/icons-material/Send';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import {toast} from 'react-toastify';
import Swal from 'sweetalert2';

function CommentForm({y,rootCommentId,setRootCommentId,placeHolder,setPlaceHolder,inputRef,article_Id,isReply,setIsReply}) {
  const [comment, setComment] = useState('');
  const navigate=useNavigate();
  const token=localStorage.getItem('token');
  const formRef=useRef(null);

  const sendReply = async () => {
    console.log(rootCommentId);
    console.log(isReply);
    if(comment===""){
      toast.warn("내용을 입력해주세요!");
      inputRef.current.focus();
    }
    else{
      const path = `http://localhost:8080/api/v1/comment/newReply`;
      const data={
        rootCommentId:102,
        content:comment,
      };
      try{
        const response=await axios.post(path,data,{
          headers:{
            'Authorization':`${token}`,
          }
        });
        console.log(response);
      }catch(error){
        console.error(error);
        //토큰 에러 처리 필요 
      }
      setIsReply(0);
      setPlaceHolder("댓글을 입력하세요");
      setComment("");
    }

  }

  const sendComment = async () => {

    if(comment==''){
      toast.warn("내용을 입력해주세요!");
      return 
    }
    const fullPath = `http://localhost:8080/api/v1/comment/new`;
    const data = {
      articleId:article_Id,
      content:comment,
    };
  
    try {
    const response = await axios.post(fullPath, data, {
        headers: {
        'Authorization':`${token}`,
        }
    });
    window.location.reload();
    } catch (error) {
        if(error.response.data==="유효하지 않은 토큰입니다."){
            alert("회원 정보가 유요하지 않아요! 로그인해주세요.");
            navigate('/logIn',{state:{from:"/article"}});
        }
    }
}

const handleBlur = () => {
  const rect=formRef.current.getBoundingClientRect();
  if(!(y+10<rect.top)){
    return 
  }

  if(placeHolder==="대댓글을 입력하세요."){
    if(comment!=""){
      Swal.fire({
        confirmButtonColor:"#FF8C00",
        icon:"question",
        text: "대댓글을 작성을 종료하시겠어요?",
        confirmButtonText:"예",
        cancelButtonText:"아니오",
        showCancelButton: true
      }).then((result)=>{
          if(result.isConfirmed){
              setPlaceHolder("댓글을 입력하세요.");
              setComment("");
              setIsReply(0);
              return 
          }
          else{
            inputRef.current.focus();
          }
      });
    }
    else{
      setIsReply(0);
      setPlaceHolder("댓글을 입력하세요.");
    }
  }
}

  return (
    <div className='App'>
      <div className="comment-submit" ref={formRef} >
        <input
          ref={inputRef}
          type='text'
          className="form-control"
          placeholder={placeHolder}
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          onBlur={handleBlur}
        />
        <IconButton
          onClick={() => {
            if(!isReply){
              sendComment();
            }
            else{
              sendReply();
            }
            }}>
          <SendIcon/>
        </IconButton>
      </div>
    </div>

  );
}

export default CommentForm;
