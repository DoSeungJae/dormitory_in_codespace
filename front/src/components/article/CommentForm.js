import React, { useState} from 'react';
import { IconButton, TextField } from '@mui/material';
import SendIcon from '@mui/icons-material/Send';

function CommentForm({articleId}) {
  const [comment, setComment] = useState('');


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
          console.log(articleId);
            }}>
          <SendIcon/>
        </IconButton>
      </div>
    </div>

  );
}

export default CommentForm;
