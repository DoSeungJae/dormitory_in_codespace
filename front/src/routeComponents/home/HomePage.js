import {React,useEffect,useState,useContext,useRef} from 'react';
import axios from 'axios';
import {useLocation} from 'react-router-dom';
import AlertContext from '../../components/common/AlertContext.js';
import ArticlePreview from '../../components/article/ArticlePreview.js';
import { calculateDorItemStyle } from '../../components/home/HomeUtils.js';

function HomePage() {
  const[articleList,setArticleList]=useState([]);
  const[page,setPage]=useState(0);
  const location=useLocation();
  const setAlert=useContext(AlertContext);
  const articleListRef=useRef(null);
  const [doLoadPage,setDoLoadPage]=useState(0);
  const [dorId,setDorId]=useState(0);
  const [scrollPosition,setScrollPosition]=useState(0);
  const [isDataLoaded,setIsDataLoaded]=useState(false);
  const [isRangeProcessed, setIsRangeProcessed]=useState(false);
  const [isEndPage,setIsEndPage]=useState(false);
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
      if(errMsg==='NoMoreArticlePage'){
        setDoLoadPage(1);
        setIsEndPage(1);
      }
      if(errMsg==="ArticleNotFound"){
        return ;
      }
    }
  };



  useEffect(()=>{
    const loadRangePage = async (start,end) => {
      if(isRangeProcessed){
        return ;
      }
      setDoLoadPage(0);
      if(end===1){
        setPage(end); //최대 page가 1일 때 테스트 필요
      }
      else if(end>=2){
        const path=`http://localhost:8080/api/v1/article/range?start=${start}&end=${end-1}`;
        try{
          const response=await axios.get(path);
          const data=response.data.map(item => JSON.parse(item));          
          setPage(end);
          setArticleList((prevItems)=>[...prevItems,...data]);     
        }
        catch(error){
          console.error(error);
        }
      }
      setIsRangeProcessed(true);
    }

    const loadRangeDorPage = async (start,end,dor)=>{
      if(isRangeProcessed || dor===dorId){
        return ;
      }
      setDoLoadPage(0);
      if(dor===0 || dor===-1){
        return ;
      }
      setDorId(dor);
      if(end===1){
        const path=`http://localhost:8080/api/v1/article/dor/${dor}?page=0`;
        try{
          const response=await axios.get(path);
          const data=response.data.map(item=>JSON.parse(item));
          const response2=await axios.get(path);

          setPage(end);
          //원래 의도한 코드가 아님
          //setArticleList((prev)=>[...prev,...data]);를 추가하지 않고 실행하였고,
          //공교롭게도 에러가 어느정도 해결되었던 것.
          //단, setArticleList((prev)=>[...prev,...data]);를 추가하면 에러가 나타남..
        }
        catch(error){
          console.error(error);
        }
      }
      else if(end>=2){
        const path=`http://localhost:8080/api/v1/article/dor/${dor}/range?start=${start}&end=${end-1}`;
        try{
          const response=await axios.get(path);
          const data=response.data.map(item => JSON.parse(item));
          setPage(end); 
          setArticleList((prev)=>[...prev,...data]);
        }
        catch(error){
          console.error(error);
        }
      }
      setIsRangeProcessed(true);

    }
    const toSavedScroll = () => {
      const savedPage=parseInt(localStorage.getItem('page') || -1,10);
      const savedScrollPosition=parseInt((localStorage.getItem('scrollPosition') || -1),10);
      const dor=parseInt((localStorage.getItem('dor') || -1),10);
      if(savedScrollPosition===-1 || savedPage===-1 || dor===-1){
        return ; 
      }

      if(dor===0){
        loadRangePage(1,savedPage)
        .then(()=>{
          if(savedPage!==page){
            return ;
          }
          articleListRef.current.scrollTo(0,parseInt(savedScrollPosition,10));
          localStorage.removeItem('scrollPosition');
          localStorage.removeItem('page');
        });
      }
      else{
        loadRangeDorPage(1,savedPage,dor)
        .then(()=>{
          if(savedPage!==page){
            return ;
          }
          articleListRef.current.scrollTo(0,parseInt(savedScrollPosition,10));
          localStorage.removeItem('scrollPosition');
          localStorage.removeItem('page');
          localStorage.removeItem('dor');
        })

      }
    }

    if(isDataLoaded){
      toSavedScroll();
      setIsRangeProcessed(true);
    }
  },[isDataLoaded]);

  useEffect(()=>{
    setAlert(location);
    setIsDataLoaded(false);
    if(page===0){
      return ;
    }
    async function fetchData(){
      await getArticlesPerPage(page);
      setIsDataLoaded(true);
    }
    fetchData(); 
  },[page])

  useEffect(()=>{
    const handleScroll = () => {
      const { scrollTop, scrollHeight, clientHeight } = articleListRef.current;
      const position=scrollTop;
      setScrollPosition(position);

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

  },[doLoadPage])

  useEffect(()=>{
    setArticleList([]);
    setIsEndPage(false);
    async function fetchData(){
      await getArticlesPerPage(page);
      setIsDataLoaded(true);
    }
    fetchData();

  },[dorId])


/*
  function calculateDorItemStyle(selectedIdx, idx) {
    const isSelected = idx === selectedIdx;
    let color;
    const item = ['오름1', '오름2', '오름3', '푸름1', '푸름2', '푸름3', '푸름4'][idx];
    if (item.startsWith('오름')) {
      color = `hsl(120, 39%, ${55 - (idx * 6)}%)`;
    } else {
      color = `hsl(197, 71%, ${70 - (idx - 3) * 10}%)`;
    }
    const border = `2px solid ${color}`;
    return {
      background: isSelected ? color : 'none',
      color: isSelected ? '#fff' : 'black',
      border: border,
    };
  }

*/
  
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
              
              {articleList===null && <h3>아직 글이 없어요!</h3>}
              
              <ArticlePreview
                articleList={articleList}
                articleListRef={articleListRef}
                dorId={dorId}
                page={page}
                isEndPage={isEndPage}
                scrollPosition={scrollPosition}
              />


    </main>

   </div>
 );
}



export default HomePage;