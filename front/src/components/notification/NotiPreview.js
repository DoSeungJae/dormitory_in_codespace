import GroupsIcon from '@mui/icons-material/Groups';
import ArticleOutlinedIcon from '@mui/icons-material/ArticleOutlined';
import PersonIcon from '@mui/icons-material/Person';
import ModeCommentOutlinedIcon from '@mui/icons-material/ModeCommentOutlined';
import axios from 'axios';
import { throttle } from '../../modules/common/clickHandleModule';
import { useContext, useEffect } from 'react';
import HomeSelectContext from '../home/HomeSelectContext';
import { toast } from 'react-toastify';

const NotiPreview = ({notiList}) => {
    
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);

    const triggerIcon={
        "ARTICLE":<ArticleOutlinedIcon fontSize='large'/>,
        "COMMENT":<ModeCommentOutlinedIcon fontSize='large'/>,
        "GROUP":<GroupsIcon fontSize='large'/>,
        "USER":<PersonIcon fontSize='large'/>
    }

    const getFormattedDate = (dateString) => {
        const date = new Date(dateString);
        return [
            date.getFullYear(),    
            date.getMonth() + 1,   
            date.getDate(),        
            date.getHours(),      
            date.getMinutes(),     
            date.getSeconds(),     
        ];
    }

    const confirmNotification = async (noti) => {
        const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/notification/confirm/${noti.id}`; 
        try{
            const response=await axios.patch(path,{},{});

            const subjectType=noti.subject.entityType;
            if(subjectType==="ARTICLE"){
                goToArticlePage(JSON.parse(noti.subject.stringifiedEntity));
            }
            else if(subjectType==="COMMENT"){
                const articleId=JSON.parse(noti.subject.stringifiedEntity).articleId;
                getArticle(articleId).then(result=>goToArticlePage(result));
            }
            else{
                localStorage.setItem("nextIndex",selectComponentIndex);
                setSelectComponentIndex(4);
            }
        }catch(error){
            const errMsg=error.response.data;
            console.error(errMsg);
        }
      }

    const handleClick = (noti) => {
        confirmNotification(noti);
    }

    const throttledHandleClick=throttle(handleClick,500);

    const goToArticlePage = async (article) => {

        localStorage.setItem("article",JSON.stringify(article));
        localStorage.setItem("nextIndex",selectComponentIndex);
        setSelectComponentIndex(5);
    }

    const getArticle = async (articleId) => {
        const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/article/${articleId}`;
        try{
            const response=await axios.get(path);
            return response.data;
        }
        catch(error){
            console.error(error);
            return null;
        }
    }

    return (
        <div className="preview" style={{maxHeight:"80vh"}}>
            {notiList && notiList.map((noti,index)=>(
                <div key={index} className="noti-item" 
                    onClick={() => throttledHandleClick(noti)}
                    style={noti.isConfirmed === false ? { backgroundColor: "rgba(255, 209, 184, 0.5)" } : {}}>
                    <div className='noti-item-icon'>{triggerIcon[noti.trigger.entityType]}</div>
                    <div className='noti-item-content-wrapper'>
                        <div className='noti-item-content'>{noti.content}</div>
                        {<div className='noti-item-time'>{getFormattedDate(noti.triggeredDate)}</div>}
                    </div>
                    
                </div>

            ))} 
        </div>
    )
}

export default NotiPreview;