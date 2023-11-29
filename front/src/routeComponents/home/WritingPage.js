import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.css'; 
import { Dropdown, DropdownButton } from 'react-bootstrap';

function WritingPage() {
    const [step,setStep]=useState(1);
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");

    const [dorSelect, setDorSelect] = useState("");

    const DorHandleChange = (event) => {
      setDorSelect(event.target.value);
    };

    const [cateSelect, setCateSelect]=useState("");

    const CateHandleChange=(event)=>{
        setCateSelect(event.target.value);
    };

    const buttonPressed = () => {
        //서버로 전송 ,토큰을 담아서 보내야함 -> 유저 정보 파싱
    }
    
    return (
        <div className="App">

            <header className="App-writingPage-header">
                    <h6>글 쓰기</h6> 
                    <button type="button" className='btn btn-outline-dark'onClick={buttonPressed}>작성</button>
            </header>
                                
            <main className="App-main">
        
                <input type="text" value={title} placeholder='제목'  style={{border:'none',outline:'none',width:'90%'}} onChange={e => setTitle(e.target.value)}  />
        
                <br/>
                <br/>
        
                <textarea value={content} placeholder='내용을 입력하세요.' style={{border:'none',outline:'none',width:'90%',height:'50%'}} onChange={e => setContent(e.target.value)}  />
                <br />
            </main>


            <div className="selects">

                <div>
                    <DropdownButton id="dropdown-item-button" title="기숙사" drop="up">
                        <Dropdown.Item as="button">오름1</Dropdown.Item>
                        <Dropdown.Item as="button">오름2</Dropdown.Item>
                        <Dropdown.Item as="button">오름3</Dropdown.Item>
                        <Dropdown.Item as="button">푸름1</Dropdown.Item>
                        <Dropdown.Item as="button">푸름2</Dropdown.Item>
                        <Dropdown.Item as="button">푸름3</Dropdown.Item>
                        <Dropdown.Item as="button">푸름4</Dropdown.Item>
                    </DropdownButton>
                </div>

                <div>
                    <DropdownButton id="dropdown-item-button" title="카테고리" drop="up">
                        <Dropdown.Item as="button"></Dropdown.Item>
                        <Dropdown.Item as="button">족발•보쌈</Dropdown.Item>
                        <Dropdown.Item as="button">찜•탕•찌개</Dropdown.Item>
                        <Dropdown.Item as="button">돈까스•일식</Dropdown.Item>
                        <Dropdown.Item as="button">피자</Dropdown.Item>
                        <Dropdown.Item as="button">고기•구이</Dropdown.Item>
                        <Dropdown.Item as="button">백반•죽•국수</Dropdown.Item>
                        <Dropdown.Item as="button">양식</Dropdown.Item>
                        <Dropdown.Item as="button">치킨</Dropdown.Item>
                        <Dropdown.Item as="button">중식</Dropdown.Item>
                        <Dropdown.Item as="button">아시안</Dropdown.Item>
                        <Dropdown.Item as="button">도시락</Dropdown.Item>
                        <Dropdown.Item as="button">분식</Dropdown.Item>
                        <Dropdown.Item as="button">카페•디저트</Dropdown.Item>
                        <Dropdown.Item as="button">패스트푸드</Dropdown.Item>    
                    </DropdownButton>
                </div>
            </div>


        </div>
        

    );
}

export default WritingPage;
