import GroupsIcon from '@mui/icons-material/Groups';
import ArticleOutlinedIcon from '@mui/icons-material/ArticleOutlined';
import PersonIcon from '@mui/icons-material/Person';
import ModeCommentOutlinedIcon from '@mui/icons-material/ModeCommentOutlined';

const NotiPreview = ({notiList}) => {

    const triggerIcon={
        "ARTICLE":<ArticleOutlinedIcon fontSize='large'/>,
        "COMMENT":<ModeCommentOutlinedIcon fontSize='large'/>,
        "GRUOP":<GroupsIcon fontSize='large'/>,
        "USER":<PersonIcon fontSize='large'/>
    }

    const handleClick = (subjectType) => {
        console.log(subjectType);
    }
    
    return (
        <div className="preview" style={{maxHeight:"80vh"}}>
            {notiList && notiList.map((noti,index)=>(
                <div key={index} className="noti-item" 
                    onClick={console.log()}>
                    <div className='noti-item-icon'>{triggerIcon[notiList[index].trigger.entityType]}</div>
                    <div className='noti-item-main'>{notiList[index].content}</div>
                </div>

            ))} 
        </div>
    )
}

export default NotiPreview;