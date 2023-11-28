import React, { useState } from 'react';
import Title from '../../components/common/Title';

function WritingPage() {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");

    const handleSubmit = (event) => {
        event.preventDefault();
        const post = { title, content};
        // 여기서 post 객체를 처리하거나 서버로 전송합니다.
    };

    return (
        <div className="App">
            <header className="App-writingPage-header">
                <Title title="글 쓰기"/>
            </header>
                        
            <main /*className="App-writingPage-main"*/>

                <form className="form-writingPage"onSubmit={handleSubmit} style={{border: 'none',width:"100%"}}>
                <label style={{width:"100%"}}>
                    <input type="text" placeholder='제목'  style={{border:'none',outline:'none'}} onChange={e => setTitle(e.target.value)}  />
                </label>
                <br />
                <br />
                
                <label>
                    <textarea value={content} placeholder='내용을 입력하세요.' style={{border:'none',outline:'none'}} onChange={e => setContent(e.target.value)}  />
                </label>
                <br />
                <input type="submit" value="글 작성" />
                </form>

            </main>


        </div>
        

    );
}

export default WritingPage;
