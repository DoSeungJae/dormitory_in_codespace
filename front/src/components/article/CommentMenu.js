import React from 'react';
import ForumOutlinedIcon from '@mui/icons-material/ForumOutlined';
import ThumbUpOutlinedIcon from '@mui/icons-material/ThumbUpOutlined';
import MoreVertOutlinedIcon from '@mui/icons-material/MoreVertOutlined';
import {IconButton} from '@mui/material';

function CommentMenu({isReply,setIsReply}){
    const changeToReplyMode = () => {
        setIsReply(1);
        console.log(isReply);
    }
    return (
        <div className="comment-menu">
            <div className="comment-menu-item">
                <IconButton
                    onClick={() => {
                    changeToReplyMode();
                    }}>
                    <ForumOutlinedIcon  fontSize="small"/>
                </IconButton>
                

            </div>
            <div className="comment-menu-item">
                <IconButton
                    onClick={() => {
                    console.log("like");
                    }}>
                    <ThumbUpOutlinedIcon  fontSize="small"/>
                </IconButton>
                
                
            </div>
            <div className="comment-menu-item">
                <IconButton
                    onClick={() => {
                    console.log("threeDots");
                    }}>
                    <MoreVertOutlinedIcon  fontSize="small"/>
                </IconButton>

                
            </div>
        </div>
    )
}

export default CommentMenu;
//forum
//comment

//like

//MoreVert??
//(~=~ threeDotsMenu)