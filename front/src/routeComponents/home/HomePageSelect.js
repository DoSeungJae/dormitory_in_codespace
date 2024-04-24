import {React,useState,useContext} from 'react';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import PostingPage from './PostingPage.js';
import MyWritingPage from './MyWritingPage.js';
import AlarmPage from './AlarmPage.js';
import HomePage from './HomePage.js';
import FooterMenu from './FooterMenu.js';
import HomeSelectContext from '../../components/home/HomeSelectContext.js';

function HomePageSelect() {
  const[page,setPage]=useState(0);
  const [dorId,setDorId]=useState(0);
  const [scrollPosition,setScrollPosition]=useState(0);
  const [isEndPage,setIsEndPage]=useState(false);
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);

  const saveScrollState = () => {
    localStorage.setItem('scrollPosition',scrollPosition);
    localStorage.setItem('dor',dorId);
    if(isEndPage){
      localStorage.setItem('page',page-1);
    }
    else{
      localStorage.setItem('page',page);
    }
  }

  const token=localStorage.getItem('token');
  const navigate = useNavigate();

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
            saveScrollState();
            navigate('/logIn',{state:
              {from:'/',type:"error",
              message:'회원 정보가 유효하지 않아요! 로그인해주세요.'}
                              });

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
      </div>
    

    <footer className="App-footer">
            <FooterMenu
              selectMenu={selectMenu}
              saveScrollState={saveScrollState}
              isEndPage={isEndPage}
              dorId={dorId}
              scrollPosition={scrollPosition}
              page={page}
              setSelectComponentIndex={setSelectComponentIndex}
              selectComponentIndex={selectComponentIndex}
            />
     </footer>
   </div>
 );
}

export default HomePageSelect;