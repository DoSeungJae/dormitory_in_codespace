import {React,useEffect,useState,useRef, useContext} from 'react';
import axios from 'axios';
import ArticlePreview from '../../components/article/ArticlePreview.js';
import { calculateDorItemStyle } from '../../components/home/HomeUtils.js';
import WriteButton from '../../components/home/WriteButton.js';
import { toast } from 'react-toastify';
import HomeSelectContext from '../../components/home/HomeSelectContext.js';

function HomePage() {
  const [articleList,setArticleList]=useState([]);
  const[page,setPage]=useState(0);
  const articleListRef=useRef(null);
  const [doLoadPage,setDoLoadPage]=useState(0);
  const [dorId,setDorId]=useState(0);
  const [isEndPage,setIsEndPage]=useState(false);
  const token=localStorage.getItem("token");
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  const previewStyle = {
    maxHeight:'83vh'
  }
  
  const getArticlesPerPage = async (page) => {
    if(!(dorId>=1 || dorId<=8)){
      return;
    }
    let path;
    if(dorId!=0){
     path=`${process.env.REACT_APP_HTTP_API_URL}/article/dorm/${dorId}?page=${page}`;
    } 
    else{
      path=`${process.env.REACT_APP_HTTP_API_URL}/article?page=${page}`;
    } 
    try{
      const headers={"Authorization":`${token}`};
      const response = await axios.get(path, {headers});
      const result=response.data;
      setArticleList((prevItems)=>[...prevItems,...result]);
      setDoLoadPage(0);
    }
    catch(error){
      const err=error.response.data;
      if(err==='NoMoreArticlePage' || err==="ExceededPage"){
        setDoLoadPage(1);
        setIsEndPage(1);
      }
      if(err==="ArticleNotFound"){
        return ;
      }
      if(err.errorType==="InvalidToken"){
        localStorage.setItem("nextIndex",selectComponentIndex);
        setSelectComponentIndex(8);
      }
    }
  };


  useEffect(()=>{
    if(page===0){
      return ;
    }
    async function fetchData(){
      await getArticlesPerPage(page);
    }
    fetchData(); 
  },[page])

  useEffect(()=>{
    const handleScroll = () => {
      const { scrollTop, scrollHeight, clientHeight } = articleListRef.current;

      if (articleListRef.current) {
        //const { scrollTop, scrollHeight, clientHeight } = articleListRef.current;
        
        if (scrollTop + clientHeight >= scrollHeight * 0.8 && !doLoadPage) {
          //스크롤 감지가 중복으로 되는 경우가 있기 때문에 여기서 데이터를 가져오지 않음.
          //대신 doLoadPage를 의존성 배열의 인자로 가지는 useEffect를 선언해서 데이터를 가져오도록 설계함
          setDoLoadPage(1);
        }
      }
    };
    articleListRef.current.addEventListener('scroll',handleScroll);
    if(doLoadPage && !isEndPage){
      //이 조건문은 스크롤 감지(handleScroll)에서와 다르게 한 번만 실행됨
      setPage(prevPage => prevPage+1);
    }
    return () => {
      if(articleListRef.current){
        articleListRef.current.removeEventListener('scroll',handleScroll);
      }
    }

  },[doLoadPage]);

  useEffect(()=>{
    setArticleList([]);
    setIsEndPage(false);
    async function fetchData(){
      await getArticlesPerPage(page);
    }
    fetchData();

  },[dorId]);

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
    "푸름4": 7,
    "상관없음":8
  };

return (
  <div className="App">
    <header className="App-home-header">
    </header>
    
    <main className="App-main">
      <div className="slide-menu no_scroll">
        {['오름1', '오름2', '오름3', '푸름1', '푸름2', '푸름3', '푸름4', '상관없음'].map((item, i) => {
          const dorItemStyle=calculateDorItemStyle(dorId-1,i);
          return (
            <div 
              key={i} 
              className="slide-item" 
              style={dorItemStyle}
              onClick={() =>{
                const dorNum=buttonToPath[item.toLowerCase()];
                setDorId(dorNum);
                if(dorNum==dorId){ //현재 기숙사 번호와 선택한 기숙사 버튼이 같은 경우 => dorMode를 종료함 => dorId를 0으로 셋
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
              
              {articleList===null && <h3>아직 글이 없어요!</h3>}
              
              <ArticlePreview
                articleList={articleList}
                articleListRef={articleListRef}
                dorId={dorId}
                page={page}
                isEndPage={isEndPage}
                heightStyle={previewStyle}
              />
               <WriteButton/>
    </main>

   </div>
 );
}



export default HomePage;