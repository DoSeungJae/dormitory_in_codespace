import {React,useContext, useState} from 'react';
import { goArticlePage,calculateDorItemStyle,dorIdToDorName } from '../home/HomeUtils';
import HomeSelectContext from '../home/HomeSelectContext';
import commentImage from '../../images/comment.png';
import groupImage from '../../images/group.png';
import { getRelativeTime } from '../../modules/common/timeModule';

function ArticlePreview({articleList,articleListRef,page,isEndPage,dorId,heightStyle}){
  //detailList 파라미터로 받기 
  
  const token=localStorage.getItem('token');
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  
  //위 결과 : 빈 array

  //commentNum, memberNum, maxCapacity, time, nickName
    return (
        <div className="preview" style={heightStyle} ref={articleListRef}>
              
        {articleList===null && <h3>아직 글이 없어요!</h3>}
        
        {articleList && articleList.map((article, index) => (
        <div key={index} className="article-item" 
        onClick={() => goArticlePage(article,dorId,isEndPage,page,token,setSelectComponentIndex,selectComponentIndex)}>
          <div className='article-item-summary'>
            <div className="article-item-text" 
                  id='article-item-title'>
                    {article.title}
            </div>
            <div className="article-item-text"
                  id='article-item-content'>
                    {article.contentText}
            </div>
          </div>
          <div className='article-item-icons'>
            <div className='dor-icon'
              style={calculateDorItemStyle(dorId-1,article.dormId-1)}>
              {dorIdToDorName[article.dormId]}
            </div>
          </div>

          <div className='article-item-details'>
            <div className='article-item-details-comment'>
              <img src={commentImage} className="image-comment"alt="Comment"/>
              <div className='comment-num'>{article.numComments}</div>
            </div>
            {(article.groupNumMembers !== 0 && article.groupMaxCapacity !== 0) && (
            <div className='article-item-details-group'>
              <img src={groupImage} className="image-group" alt="Group" />
              <div className='group-num'>
                {article.groupNumMembers}/{article.groupMaxCapacity}
              </div>
            </div>
            )}

            <div className='article-item-details-time '>{getRelativeTime(article.createdTime)}</div>
            <div className='article-item-details-nickName'>{article.userNickName}</div>
          </div>
        </div>
      ))}

      </div>

    )
    

}

export default ArticlePreview