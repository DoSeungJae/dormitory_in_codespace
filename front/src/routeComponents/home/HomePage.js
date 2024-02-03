import {React,useEffect,useState,useContext} from 'react';
import axios from 'axios';
import {useNavigate,useLocation} from 'react-router-dom';
import {toast} from 'react-toastify';
import AlertContext from '../../components/common/AlertContext.js';

function HomePage() {

  const[articleList,setArticleList]=useState([]);
  const location=useLocation();
  const setAlert=useContext(AlertContext);

  const getAllArticles = async () => {
    try{
      const response = await axios.get('http://localhost:8080/api/v1/article', {
    });
    const data=response.data.map(item => JSON.parse(item));
    setArticleList(data);    
    }
    catch(error){
      console.log('an error occurred:',error);
    }
  }
  
  useEffect(()=>{
    async function fetchData(){
      await getAllArticles();
    }
    fetchData();
    setAlert(location);
  },[])
  
  const token=localStorage.getItem('token');
  const navigate = useNavigate();

  const checkToken = async (item) => {
    try {
        const response = await axios.get('http://localhost:8080/api/v1/article/validate', {
            headers: {
                'Authorization': `${token}`
            }
        });

        if (response.data === true) {
          const path=buttonToPath[item]
          navigate(`/${path}`);
        } else {
            navigate('/logIn',{state:
              {from:'/',type:"error",
              message:'회원 정보가 유효하지 않아요! 로그인해주세요.'}
                              });

        }
    } catch (error) {
        console.error('An error occurred:', error);
    }
  };

  const goArticlePage = async (article) => {
    try {
      const response = await axios.get('http://localhost:8080/api/v1/article/validate', {
          headers: {
              'Authorization': `${token}`
          }
      });

      if (response.data === true) {
        navigate('/article',{state:article})
      } else {
          navigate('/logIn',{state:
            {from:'/',type:"error",
            message:'글을 보기 위해선 로그인이 필요해요!'}
                            });
      }
  } catch (error) {
      console.error('An error occurred:', error);
  }
    
  };
  
  const buttonToPath = {
    "홈": "",
    "내 글": "myWriting",
    "글쓰기": "newWriting", 
    "알림": "alarm",
    "오름1": 1,
    "오름2": 2,
    "오름3": 3, 
    "푸름1": 4,
    "푸름2": 5,
    "푸름3": 6,
    "푸름4": 7
  };

  const svgMap = {
    '홈': (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 26" 
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M2 15L12 3l10 12v15a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1v-15z" />
          <path d="M9 30V15h6v15" />
        </svg>
    ),
    '내 글': (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <line x1="3" y1="6" x2="17" y2="6" />
          <line x1="3" y1="12" x2="21" y2="12" />
          <line x1="3" y1="18" x2="15" y2="18" />
        </svg>
    ),
    '글쓰기': (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M18.5 3a2.5 2.5 0 0 1 3.5 3.5L7.5 21H3v-4.5L18.5 3z" />
          <path d="M15 5L21 11" />
        </svg>
    ),
    '알림': (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="26"
          height="26"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M12 22c1.1 0 2-.9 2-2H10c0 1.1.9 2 2 2z" />
          <path d="M18 16v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.93 6 11v5l-2 2v1h16v-1l-2-2z" />
        </svg>
    ),
  };

  const handleButtonClick = async (buttonName) => {
    const path = buttonToPath[buttonName];
    if (!path) return; // 만약 해당 버튼 이름이 buttonToPath 객체에 없다면 함수 종료
    const fullPath=`http://localhost:8080/api/v1/article/${path}`

    if(path=='myWriting' || path=='newWriting' || path=='alarm'){ //필요없는 코드블록일지도?
      try {
        const response = await axios.get(fullPath, {
          headers:{
            'Authorization' : 'Bearer'+token
          }
        });
      } catch (error) {
        console.error(error);
      }   
    }
    else if(path>=1 || path<=7){
      try{
        const fullPath=`http://localhost:8080/api/v1/article/dor/${path}`
        const response=await axios.get(fullPath)
        .then(response => {
          const data=response.data.map(item => JSON.parse(item));
          setArticleList(data);
        })
      }
      catch(error){
        console.error(error);
        setArticleList(null);

      }
    }

    else{
      try {
        const response = await axios.get(fullPath);
    } catch (error) {
        console.error(error);
    }
    }

};

return (
  <div className="App">
    <header className="App-home-header">
      <h1></h1>
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
            <div className="preview">
              {articleList===null && <h3>아직 글이 없어요!</h3>}
              
              {articleList && articleList.map((article, index) => (
              <div key={index} className="article-item" onClick={() => goArticlePage(article)}>
                <h2 className="article-item-children">{article.title}</h2>
                <p className="article-item-children">{article.content}</p>
              </div>
            ))}
            </div>
    </main>

    <footer className="App-footer">
      <div className="bottom-menu">
        {['홈','내 글', '글쓰기', '알림'].map((item, i) => (
          <div 
            key={i}
            className="menu-item"
            style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}
            
            onClick={() => {
              if(item!='홈'){
                checkToken(item)
                item=null;                
              }
              else{
                handleButtonClick(item.replace(' ','').toLowerCase());
                getAllArticles();
              }
            }}
          >
            {svgMap[item]}
            {<div style={{fontSize: '14px',paddingTop:'6px'}}>{item}</div>}
          </div>
        ))}
       </div>
     </footer>
   </div>
 );
}



export default HomePage;