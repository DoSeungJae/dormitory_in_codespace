import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import axios from 'axios';

function HomePage() {

  const buttonToPath = {
    '오름1': 'oreum1',
    '오름2': 'oreum2',
    '오름3': 'oreum3',
    '푸름1': 'preum1',
    '푸름2': 'preum2',
    '푸름3': 'preum3',
    '푸름4': 'preum4',
    "홈": "home",
    "내 글": "myWriting",
    "글쓰기": "newWriting",
    "알림": "alarm"
  };

  const handleButtonClick = async (buttonName) => {
    const path = buttonToPath[buttonName];
    if (!path) return; // 만약 해당 버튼 이름이 buttonToPath 객체에 없다면 함수 종료

    try {
      const response = await axios.post(`http://localhost:8080/api/v1/home/${path}`, {
        token: "your-token-here"
      });
      console.log(response.data); // 서버 응답 데이터 처리
    } catch (error) {
      console.error(error);
    }
};

  return (
    <div className="App">
      <header className="App-header">
        <h1>header</h1>
      </header>
      
      <main className="App-main">
        <div className="slide-menu">
          {['오름1', '오름2', '오름3', '푸름1', '푸름2', '푸름3', '푸름4'].map((item, i) => {
            let color;
            if (item.startsWith('오름')) {
                // forestgreen 계열
                color = `hsl(120, 39%, ${55 - (i * 5)}%)`;
            } else {
                // skyblue 계열
                color = `hsl(197, 71%, ${65 - (i-3) * 10}%)`;
            }
            
            return (
              <div 
                key={i} 
                className="slide-item" 
                style={{backgroundColor: color, color: '#fff'}}
                onClick={() => handleButtonClick(item.toLowerCase())}
              >
                  {item}
              </div>
            );
          })}
        </div>
      </main>

      <footer className="App-footer">
        <div className="bottom-menu">
          {['홈','내 글', '글쓰기', '알림'].map((item, i) => (
            <div 
              key={i} 
              className="menu-item"
              onClick={() => handleButtonClick(item.replace(' ', '').toLowerCase())}
            >
               {item}
             </div>
           ))}
         </div>
       </footer>
     </div>
   );
}

export default HomePage;
