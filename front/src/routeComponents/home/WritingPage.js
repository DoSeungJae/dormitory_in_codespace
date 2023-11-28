import React, { useState } from 'react';
import Title from '../../components/common/Title';

function WritingPage() {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");

    const handleSubmit = (event) => {
        event.preventDefault();
        const post = { title, content};
        // 여기서 post 객체를 처리하거나 서버로 전송
    };

    const [dorSelect, setDorSelect] = useState("");

    const DorHandleChange = (event) => {
      setDorSelect(event.target.value);
    };

    const [cateSelect, setCateSelect]=useState("");

    const CateHandleChange=(event)=>{
        setCateSelect(event.target.value);
    };


    return (
        <div className="App">
            <header className="App-writingPage-header">
                <Title title="글 쓰기"/>
            </header>
                        
            <main /*className="App-writingPage-main"*/>

                <form className="form-writingPage" onSubmit={handleSubmit} style={{border: 'none'}}>
                    <input type="text" placeholder='제목'  style={{border:'none',outline:'none',width:'90%'}} onChange={e => setTitle(e.target.value)}  />

                <br/>

                <div className="containerSelect">
                    <select value={dorSelect} onChange={DorHandleChange} className="dorSelect">
                        <option value="">기숙사</option>
                        <option value="0">오름 1</option>
                        <option value="1">오름 2</option>
                        <option value="2">오름 3</option>
                        <option value="3">푸름 1</option>
                        <option value="4">푸름 2</option>
                        <option value="5">푸름 3</option>
                        <option value="6">푸름 4</option>

                    </select>
                </div>

                <div className="containerSelect">
                    <select value={cateSelect} onChange={CateHandleChange} className="CateSelect">
                        <option value="">카테고리</option>
                        <option value="0">족발•보쌈</option>
                        <option value="1">찜•탕•찌개</option>
                        <option value="2">돈까스•일식</option>
                        <option value="3">피자</option>
                        <option value="4">고기•구이</option>
                        <option value="5">백반•죽•국수</option>
                        <option value="6">양식</option>
                        <option value="7">치킨</option>
                        <option value="8">중식</option>
                        <option value="9">아시안</option>
                        <option value="10">도시락</option>
                        <option value="11">분식</option>
                        <option value="12">카페•디저트</option>
                        <option value="13">패스트푸드</option>
                        

                    </select>
                </div>
                
                
                <textarea value={content} placeholder='내용을 입력하세요.' style={{border:'none',outline:'none',width:'90%'}} onChange={e => setContent(e.target.value)}  />
                <br />
                <input type="submit" value="글 작성" />
                </form>

            </main>


        </div>
        

    );
}

export default WritingPage;
