import {React,useState,useEffect} from 'react';
import axios from 'axios';
import {useLocation} from 'react-router-dom';
import BackButton from '../../components/common/BackButton';
import ThreeDotsMenu from '../../components/article/ThreeDotsMenu';
import userDefault from '../../images/userDefault.png';

function ArticlePage(){
    const[writerNickName,setWriterNickName]=useState("");
    const[isWriter,setIsWriter]=useState(0);

    const location=useLocation();
    const article=location.state;
    const token=localStorage.getItem('token');
    
    const isSame = async (token) => {
      try {
        const response = await axios.get('http://localhost:8080/api/v1/token/userId', {
            headers: {
                'Authorization': `${token}`
            }
        });
        if(response.data===article.userId){
          return 1;
        }
        else{
          return 0;
        }
    
    } catch (error) {
        console.error('An error occurred isSame in ArticlePage.js:', error);
        return 0;
    }

    }
    const convertDorIdToString = (num) => {
      const mappingDict = {
        1: '오름1',
        2: '오름2',
        3: '오름3',
        4: '푸름1',
        5: '푸름2',
        6: '푸름3',
        7: '푸름4',
      };
      return mappingDict[num] || "Invalid input";
    }

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
        isSame(token).then(result=>setIsWriter(result));
    },[]);

    function formatCreateTime(createTime) {
      const currentYear = new Date().getFullYear();

      const year = createTime[0];
      const month = createTime[1];
      const day = createTime[2];
      const hours = createTime[3];
      const minutes = createTime[4];

      return currentYear === year 
        ? `${month}/${day} ${hours}:${minutes}` 
        : `${year}/${month}/${day} ${hours}:${minutes}`;
    }
    
    return (
        <div className="App">
            <div className="app-article-header">
                <BackButton></BackButton>
                <ThreeDotsMenu isWriterParam={isWriter} articleParam={article}></ThreeDotsMenu>
                
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
              <p className='article-title'>{article.title}</p>
                <div className="article-meta">
                  <p className="article-category">{article.category}</p>
                  <p className="article-appointedTime">{article.appointedTime}</p>
                  <p className="article-dormitory">{convertDorIdToString(article.dorId)}</p>
                </div>
                
                <p className='article-content'>{article.content}</p>

              </div>
            </div>
            <div className="app-article-footer">

            </div>
        </div>
    );

}

export default ArticlePage;