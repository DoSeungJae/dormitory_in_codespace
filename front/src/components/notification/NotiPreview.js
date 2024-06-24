import GroupsIcon from '@mui/icons-material/Groups';
import ArticleIcon from '@mui/icons-material/Article';
import CommentIcon from '@mui/icons-material/Comment';
import PersonIcon from '@mui/icons-material/Person';

const NotiPreview = ({notiList}) => {

    const triggerIcon={
        "ARTICLE":<ArticleIcon/>,
        "COMMENT":<CommentIcon/>,
        "GRUOP":<GroupsIcon/>,
        "USER":<PersonIcon/>
    }
    
    return (
        <div className="preview">
            {notiList && notiList.map((noti,index)=>(
                <div key={index} className="noti-item" 
                    onClick={console.log()}>
                    <div className='noti-item-icon'></div>
                    <div className='noti-item-main'></div>
                </div>

            ))} 
        </div>
    )
}

export default NotiPreview;