import React, { useState} from 'react';
import { IconButton, TextField } from '@mui/material';
import SendIcon from '@mui/icons-material/Send';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import {toast} from 'react-toastify';

function CommentForm({article_Id}) {
  const [comment, setComment] = useState('');
  const navigate=useNavigate();
  const token=localStorage.getItem('token');

  const sendComment = async () => {
    if(comment==''){
      toast.warn("댓글 내용을 입력해주세요!");
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
    console.log(response.data);
    } catch (error) {
        if(error.response.data==="유효하지 않은 토큰입니다."){
            alert("회원 정보가 유요하지 않아요! 로그인해주세요.");
            navigate('/logIn',{state:{from:"/article"}});
        }
    }
}


  return (
    <div className='App'>
      <div className="comment-submit" >
        <input
          type='text'
          className="form-control"
          placeholder="댓글을 입력하세요."
          value={comment}
          onChange={(e) => setComment(e.target.value)}
        />
        <IconButton
          onClick={() => {
          console.log("button clicked");
          sendComment();
            }}>
          <SendIcon/>
        </IconButton>
      </div>
    </div>

  );
}

export default CommentForm;
