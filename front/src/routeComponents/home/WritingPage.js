import React, { useState } from 'react';

function WritingPage() {
    const [title, setTitle] = useState("");
    const [author, setAuthor] = useState("");
    const [content, setContent] = useState("");

    const handleSubmit = event => {
        event.preventDefault();
        const post = { title, author, content };
        // 여기서 post 객체를 처리하거나 서버로 전송합니다.
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                제목:
                <input type="text" value={title} onChange={e => setTitle(e.target.value)} />
            </label>
            <br />
            <label>
                작성자:
                <input type="text" value={author} onChange={e => setAuthor(e.target.value)} />
            </label>
            <br />
            <label>
                내용:
                <textarea value={content} onChange={e => setContent(e.target.value)} />
            </label>
            <br />
            <input type="submit" value="글 작성" />
        </form>
    );
}

export default WritingPage;
