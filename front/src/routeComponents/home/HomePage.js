import React from 'react';


function HomePage() {
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
                <div key={i} className="slide-item" style={{backgroundColor: color, color: '#fff'}}>
                    {item}
                </div>
            );
        })}
    </div>

        <h1></h1>
        <div style={{flex:100}}></div>
        <div className="bottom-menu">
            {['홈','내 글', '글쓰기', '알림'].map((item, i) => (
            <div key={i} className="menu-item">
                {item}
            </div>
        ))}
        </div>
      </main>
      <footer className="App-footer">
        <h1>footer</h1>
      </footer>
    </div>
  );
}

export default HomePage;