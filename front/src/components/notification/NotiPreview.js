import GroupsIcon from '@mui/icons-material/Groups';
import ArticleOutlinedIcon from '@mui/icons-material/ArticleOutlined';
import PersonIcon from '@mui/icons-material/Person';
import ModeCommentOutlinedIcon from '@mui/icons-material/ModeCommentOutlined';
import axios from 'axios';
import { throttle } from '../../modules/common/clickHandleModule';

const NotiPreview = ({notiList}) => {

    const triggerIcon={
        "ARTICLE":<ArticleOutlinedIcon fontSize='large'/>,
        "COMMENT":<ModeCommentOutlinedIcon fontSize='large'/>,
        "GROUP":<GroupsIcon fontSize='large'/>,
        "USER":<PersonIcon fontSize='large'/>
    }

    const getFormattedDate = (timeArr) => {
        const curYear=new Date().getFullYear();
        const year=timeArr[0]
        const month=timeArr[1].toString().padStart(2,'0');
        const day=timeArr[2].toString().padStart(2,'0');
        const hour=timeArr[3].toString().padStart(2,'0');
        const minute=timeArr[4].toString().padStart(2,'0');
        let formattedDate;
        if(year==curYear){
            formattedDate=`${month}/${day} ${hour}:${minute}`;
        }
        else{
            formattedDate = `${year}/${month}/${day} ${hour}:${minute}`;
        }

        return formattedDate;
    }

    const confirmNotification = async (notiId) => {
        const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/notification/confirm/${notiId}`; 
        try{
            const response=await axios.patch(path,{},{});
            console.log(response.data);

        }catch(error){
            const errMsg=error.response.data;
            console.error(errMsg);
        }
      }

    const handleClick = (noti) => {
        //confirmNotification(noti.id);
        console.log(noti);
    }

    const throttledHandleClick=throttle(handleClick,500);

    return (
        <div className="preview" style={{maxHeight:"80vh"}}>
            {notiList && notiList.map((noti,index)=>(
                <div key={index} className="noti-item" 
                    onClick={() => throttledHandleClick(noti)}
                    style={noti.isConfirmed === false ? { backgroundColor: "rgba(255, 209, 184, 0.5)" } : {}}>
                    <div className='noti-item-icon'>{triggerIcon[noti.trigger.entityType]}</div>
                    <div className='noti-item-content-wrapper'>
                        <div className='noti-item-content'>{noti.content}</div>
                        <div className='noti-item-time'>{getFormattedDate(JSON.parse(noti.trigger.stringifiedEntity).createdTime)}</div>
                    </div>
                    
                </div>

            ))} 
        </div>
    )
}

export default NotiPreview;