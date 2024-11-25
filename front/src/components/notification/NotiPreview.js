import GroupsIcon from '@mui/icons-material/Groups';
import ArticleOutlinedIcon from '@mui/icons-material/ArticleOutlined';
import PersonIcon from '@mui/icons-material/Person';
import ModeCommentOutlinedIcon from '@mui/icons-material/ModeCommentOutlined';
import axios from 'axios';
import { throttle } from '../../modules/common/clickHandleModule';
import { useContext, useEffect } from 'react';
import HomeSelectContext from '../home/HomeSelectContext';
import { getRelativeTime } from '../../modules/common/timeModule';
import ArticleContext from '../article/ArticleContext';

const NotiPreview = ({notiList}) => {
    /* noti에 동적 스타일링을 하지 않는 이유는 클릭했을 때 다른 컴포넌트로 이동하고
    backButton을 통해 notiPage로 돌아오면 Noti들을 다시 fetch하기 때문 */
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const {article, setArticle}=useContext(ArticleContext);

    useEffect(()=>{
        if(Object.keys(article).length !==0){
            console.log(article);
            setSelectComponentIndex(5);
        }
    },[article])

    const triggerIcon={
        "ARTICLE":<ArticleOutlinedIcon fontSize='large'/>,
        "COMMENT":<ModeCommentOutlinedIcon fontSize='large'/>,
        "GROUP":<GroupsIcon fontSize='large'/>,
        "USER":<PersonIcon fontSize='large'/>
    }



    const confirmNotification = async (noti) => {
        const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/notification/confirm/${noti.id}`; 
        try{
            const response=await axios.patch(path,{},{});
            
            const subjectType=noti.subject.entityType;
            if(subjectType==="ARTICLE"){
                const articleId=JSON.parse(noti.subject.stringifiedEntity).id;
                getArticle(articleId).then(result=>goToArticlePage(result)); //goToArticle에서 getArticle 호출하도록 변경하기
            }
            else if(subjectType==="COMMENT"){
                const articleId=JSON.parse(noti.subject.stringifiedEntity).articleId;
                getArticle(articleId).then(result=>goToArticlePage(result)); //goToArticle에서 getArticle 호출하도록 변경하기
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

    const goToArticlePage = async (article1) => { //수정 필요 
        localStorage.setItem("nextIndex",selectComponentIndex);
        setArticle(article1);
    }

    const getArticle = async (articleId) => {
        const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/article/${articleId}`;
        try{
            const response=await axios.get(path);
            console.log("article:",response.data);
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
                        {<div className='noti-item-time'>{getRelativeTime(noti.triggeredDate)}</div>}
                    </div>
                    
                </div>

            ))} 
        </div>
    )
}

export default NotiPreview;