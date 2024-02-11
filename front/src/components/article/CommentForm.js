import React, { useState } from 'react';

function CommentForm() {
  const [comment, setComment] = useState('');

  const handleChange = (event) => {
    setComment(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    alert('댓글이 작성되었습니다: ' + comment);
    setComment('');
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '10px' }}>
      <input 
        type="text"
        value={comment} 
        onChange={handleChange} 
        placeholder="댓글을 입력하세요." 
        style={{ flex: 1 }}
      />
      <button type="submit">댓글 작성</button>
    </form>
  );
}

export default CommentForm;
