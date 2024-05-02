import {React,useState,useContext,useEffect} from 'react';
import axios from 'axios';
import PostingPage from './PostingPage.js';
import MyWritingPage from './MyWritingPage.js';
import AlarmPage from './AlarmPage.js';
import HomePage from './HomePage.js';
import FooterMenu from './FooterMenu.js';
import HomeSelectContext from '../../components/home/HomeSelectContext.js';
import ArticlePage from '../article/ArticlePage.js';
import SignInPage from '../signIn/SignInPage.js';
import LogInPage from '../logIn/LogInPage.js';

function HomePageSelect() {
  const[page,setPage]=useState(0);
  const [dorId,setDorId]=useState(0);
  const [scrollPosition,setScrollPosition]=useState(0);
  const [isEndPage,setIsEndPage]=useState(false);
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  const [init,setInit]=useState(1);
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
        const response = await axios.get('http://localhost:8080/api/v1/article/validate', {
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
            setSelectComponentIndex(8);
            localStorage.setItem("index",menuToIndex[item]);
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
  };

return (
  <div className="App">
    
      <div className='App-component-switcher'>
          <div style={{display : selectComponentIndex==0 ? 'block' : 'none'}}><HomePage/></div>
          <div style={{display : selectComponentIndex==1 ? 'block' : 'none'}} ><MyWritingPage/></div>
          <div style={{display : selectComponentIndex==2 ? 'block' : 'none'}}><PostingPage/></div>
          <div style={{display : selectComponentIndex==3 ? 'block' : 'none'}}><AlarmPage/></div>
          {selectComponentIndex==5 && <ArticlePage/>}
          <div style={{display : selectComponentIndex==6 ? 'block' : 'none'}}><PostingPage/></div>
          <div style={{display : selectComponentIndex==7 ? 'block' : 'none'}}><SignInPage/></div>
          <div style={{display : selectComponentIndex==8 ? 'block' : 'none'}}><LogInPage/></div>
      </div>
    {
      (selectComponentIndex!=8 && selectComponentIndex!=7 && selectComponentIndex!=5) &&
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