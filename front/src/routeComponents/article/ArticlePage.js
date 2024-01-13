import {React,useState,useEffect} from 'react';
import axios from 'axios';
import {useLocation} from 'react-router-dom';
import BackButton from '../../components/common/BackButton';
import ThreeDots from '../../components/common/ThreeDots';

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



    return (
        <div className="App">
            <div className="app-article-header">
                <BackButton></BackButton>
                <ThreeDots></ThreeDots>

            </div>
        </div>
    );

}

export default ArticlePage;