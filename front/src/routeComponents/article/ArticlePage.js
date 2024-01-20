import {React,useState,useEffect} from 'react';
import axios from 'axios';
import {useLocation} from 'react-router-dom';
import BackButton from '../../components/common/BackButton';
import ThreeDotsMenu from '../../components/home/ThreeDotsMenu';
import userDefault from '../../images/userDefault.png'
function ArticlePage(){
    const[writerNickName,setWriterNickName]=useState("");
    const location=useLocation();
    const article=location.state;

    const getWriterNickName = async () => {
        try{
          const response = await axios.get(`http://localhost:8080/api/v1/user/${article.userId}`, {
        });
        const nickName=response.data.nickName;
        setWriterNickName(nickName);
        }
        catch(error){
          console.log('an error occurred:',error);
        }
      }

    useEffect(()=>{
        getWriterNickName();
    },[]);

    function formatCreateTime(createTime) {
      // 현재 년도를 얻어옴
      const currentYear = new Date().getFullYear();
    
      // createTime에서 년도, 월, 일, 시간, 분을 추출
      const year = createTime[0];
      const month = createTime[1];
      const day = createTime[2];
      const hours = createTime[3];
      const minutes = createTime[4];
      
      // 현재 년도와 createTime의 년도가 같다면 년도를 생략하고 "월/일 시간:분" 형태로 반환, 아니라면 "년도/월/일 시간:분" 형태로 반환
      return currentYear === year 
        ? `${month}/${day} ${hours}:${minutes}` 
        : `${year}/${month}/${day} ${hours}:${minutes}`;
    }
    


    return (
        <div className="App">
            <div className="app-article-header">
                <BackButton></BackButton>
                <ThreeDotsMenu></ThreeDotsMenu>
            </div>
            <div className="app-article-main">
              <div className="article-info">
                <img src={userDefault} alt="description" className='rounded-image'/>
                  <div className="article-info-detail">
                    <p>{writerNickName}</p>
                    <p>{formatCreateTime(article.createTime)}</p>
                  </div>
                

              </div>
              <div className='article-body'>
                <div></div>
                <p className='article-title'>{article.title}</p>
                <p className='article-content'>{article.content}</p>

              </div>
            </div>
            <div className="app-article-footer">

            </div>
        </div>
    );

}

export default ArticlePage;