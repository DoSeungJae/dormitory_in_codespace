import {React,useEffect,useState,useContext,useRef} from 'react';
import axios from 'axios';
import {useNavigate,useLocation} from 'react-router-dom';
import AlertContext from '../../components/common/AlertContext.js';

function HomePage() {
  const[articleList,setArticleList]=useState([]);
  const[page,setPage]=useState(0);
  const location=useLocation();
  const setAlert=useContext(AlertContext);
  const articleListRef=useRef(null);
  const [doLoadPage,setDoLoadPage]=useState(0);
  const [dorId,setDorId]=useState(0);

  const getArticlesPerPage = async (page) => {
    if(!(dorId>=1 || dorId<=7)){
      return;
    }
    let path;
    if(dorId!=0){
     path=`http://localhost:8080/api/v1/article/dor/${dorId}?page=${page}`;
    } 
    else{
      path=`http://localhost:8080/api/v1/article?page=${page}`;
    }
    
    try{
      const response = await axios.get(path, {
    });
      const data=response.data.map(item => JSON.parse(item));
      setArticleList((prevItems)=>[...prevItems,...data]);
      setDoLoadPage(0);
    }
    catch(error){
      const errMsg=error.response.data;
      if(errMsg==='NoMoreArticlePage' ){
        setDoLoadPage(1);
      }
      if(errMsg==="ArticleNotFound"){
        return ;
      }
    }
  };

  useEffect(()=>{
    setAlert(location);
    if(page==0){
      return ;
    }
    async function fetchData(){
      await getArticlesPerPage(page);
    }
    fetchData(); 
  },[page])

  useEffect(()=>{
    const handleScroll = () => {
      if (articleListRef.current) {
        const { scrollTop, scrollHeight, clientHeight } = articleListRef.current;
        if (scrollTop + clientHeight >= scrollHeight * 0.8 && !doLoadPage) {
          //스크롤 감지가 중복으로 되는 경우가 있기 때문에 여기서 데이터를 가져오지 않음.
          //대신 doLoadPage를 의존성 배열의 인자로 가지는 useEffect를 선언해서 데이터를 가져오도록 설계함
          setDoLoadPage(1);
        }
      }
    };
    articleListRef.current.addEventListener('scroll',handleScroll);
    if(doLoadPage){
      //이 조건문은 스크롤 감지(handleScroll)에서와 다르게 한 번만 실행됨
      setPage(prevPage => prevPage+1);
    }
    return () => {
      if(articleListRef.current){
        articleListRef.current.removeEventListener('scroll',handleScroll);
      }
    }

  },[doLoadPage])

  useEffect(()=>{
    async function fetchData(){
      await getArticlesPerPage(page);
    }
    fetchData();

  },[dorId])


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
        navigate('/article',{state:{info:article}})
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

  function calculateDorItemStyle(selectedIdx, idx) {
    const isSelected = idx === selectedIdx;
    let color;
    const item = ['오름1', '오름2', '오름3', '푸름1', '푸름2', '푸름3', '푸름4'][idx];
    if (item.startsWith('오름')) {
      color = `hsl(120, 39%, ${55 - (idx * 5)}%)`;
    } else {
      color = `hsl(197, 71%, ${65 - (idx - 3) * 10}%)`;
    }
    const border = `2px solid ${color}`;
    return {
      background: isSelected ? color : 'none',
      color: isSelected ? '#fff' : 'black',
      border: border,
    };
  }
  

  
  
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

  const dorIdToDorName={
    1:"오름1",
    2:"오름2",
    3:"오름3",
    4:"푸름1",
    5:"푸름2",
    6:"푸름3",
    7:"푸름4"
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

return (
  <div className="App">
    <header className="App-home-header">
    </header>
    
    <main className="App-main">
      <div className="slide-menu">
        {['오름1', '오름2', '오름3', '푸름1', '푸름2', '푸름3', '푸름4'].map((item, i) => {
          const dorItemStyle=calculateDorItemStyle(dorId-1,i);
          return (
            <div 
              key={i} 
              className="slide-item" 
              style={dorItemStyle}
              onClick={() =>{
                const dorNum=buttonToPath[item.toLowerCase()];
                setDorId(dorNum);
                if(dorNum==dorId){//현재 기숙사 번호와 선택한 기숙사 버튼이 같은 경우 => dorMode를 종료함 => dorId를 0으로 셋
                  setDorId(0);
                }
                setArticleList([]);
                setPage(0);
              }}
            >
                {item}
            </div>
             );
            })}
          </div>
            <div className="preview" ref={articleListRef}>
              
              {articleList===null && <h3>아직 글이 없어요!</h3>}
              
              {articleList && articleList.map((article, index) => (
              <div key={index} className="article-item" onClick={() => goArticlePage(article)}>
                <div className='article-item-summary'>
                  <h2 className="article-item-text">{article.title}</h2>
                  <p className="article-item-text">{article.content}</p>
                </div>
                <div className='article-item-icons'>
                  <div className='dor-icon'
                    style={calculateDorItemStyle(dorId,article.dorId)}>
                    {dorIdToDorName[article.dorId]}
                  </div>
          
                  
                </div>
                
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
                checkToken(item);
                item=null;                
              }
              else{
                window.location.reload();
              }
            }}
          >
            {svgMap[item]}
            {<div style={{fontSize: '14px',paddingTop:'4px'}}>{item}</div>}
          </div>
        ))}
       </div>
     </footer>
   </div>
 );
}



export default HomePage;