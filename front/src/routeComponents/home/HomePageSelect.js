import {React,useState,useContext,useEffect} from 'react';
import axios from 'axios';
import PostingPage from './PostingPage.js';
import MyWritingPage from './MyWritingPage.js';
import NotificationPage from './NotificationPage.js';
import HomePage from './HomePage.js';
import FooterMenu from '../../components/home/FooterMenu.js';
import HomeSelectContext from '../../components/home/HomeSelectContext.js';
import ArticlePage from '../article/ArticlePage.js';
//import SignInPage from '../signIn/signInPage.js';
import LogInPage from '../logIn/LogInPage.js';
import { toast } from 'react-toastify';
import MyGroupPage from './MyGroupPage.js';
import ArticleContext from '../../components/article/ArticleContext.js';

function HomePageSelect() {
  const [page,setPage]=useState(0);
  const [dorId,setDorId]=useState(0);
  const [scrollPosition,setScrollPosition]=useState(0);
  const [isEndPage,setIsEndPage]=useState(false);
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  const [article,setArticle]=useState({});
  const token=localStorage.getItem('token');

  useEffect(()=>{
    const idx=parseInt(localStorage.getItem("index"));
    if(idx>=0){
      setSelectComponentIndex(idx);
      localStorage.removeItem('index');
    } 
  },)
  
  const selectMenu = async (item) => {
    try {
        const response = await axios.get('https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/article/validate', {
            headers: {
                'Authorization': `${token}`
            }
        });
        if (response.data === true) {
            if(item===null){
              return ;
            }
            setSelectComponentIndex(menuToIndex[item]);
          } else {
            localStorage.setItem("nextIndex",menuToIndex[item]);
            setSelectComponentIndex(8);
            toast.error("로그인이 필요한 서비스에요!");

    
        }
    } catch (error) {
        console.error('An error occurred:', error);
    }
  };

  const menuToIndex={
    '홈':0,
    '내 글':1,
    '글쓰기':2,
    '알림':3,
    '내 그룹':4,
  };

return (
  <div className="App">
    
      <div className='App-component-switcher'>

          <div style={{display : selectComponentIndex==1 ? 'block' : 'none'}} ><MyWritingPage/></div>
          <div style={{display : selectComponentIndex==2 ? 'block' : 'none'}}><PostingPage/></div>
          <div style={{display : selectComponentIndex==3 ? 'block' : 'none'}}><NotificationPage/></div>
          <div style={{display : selectComponentIndex==4 ? 'block' : 'none'}}><MyGroupPage/></div>

          <ArticleContext.Provider value={{article,setArticle}}>
            <div style={{display : selectComponentIndex==0 ? 'block' : 'none'}}><HomePage/></div>
            <div> {selectComponentIndex==5 && <ArticlePage/>} </div>
          </ArticleContext.Provider>

          <div style={{display : selectComponentIndex==6 ? 'block' : 'none'}}><PostingPage/></div>
          {/*<div style={{display : selectComponentIndex==7 ? 'block' : 'none'}}><SignInPage/></div>*/}
          <div style={{display : selectComponentIndex==8 ? 'block' : 'none'}}><LogInPage/></div>
      </div>
    {
      (selectComponentIndex!=8 && selectComponentIndex!=7 && selectComponentIndex!=5 && selectComponentIndex!=4) &&
      <footer className="App-footer">
              <FooterMenu
                selectMenu={selectMenu}
                isEndPage={isEndPage}
                dorId={dorId}
                scrollPosition={scrollPosition}
                page={page}
                setSelectComponentIndex={setSelectComponentIndex}
                selectComponentIndex={selectComponentIndex}
              />
      </footer>
    }
   </div>
 );
}

export default HomePageSelect;