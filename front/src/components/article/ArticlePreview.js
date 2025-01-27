import {React,useContext, useState} from 'react';
import { goArticlePage,calculateDorItemStyle,dorIdToDorName, calculateArticlePreviewDorm } from '../home/HomeUtils';
import HomeSelectContext from '../home/HomeSelectContext';
import commentImage from '../../images/comment.png';
import groupImage from '../../images/group.png';
import { getRelativeTime } from '../../modules/common/timeModule';
import ArticleContext from './ArticleContext';

function ArticlePreview({articleList,articleListRef,page,isEndPage,dorId,heightStyle}){
  //detailList 파라미터로 받기 
  
  const token=localStorage.getItem('token');
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  const {article,setArticle}=useContext(ArticleContext);
  
  //위 결과 : 빈 array

  //commentNum, memberNum, maxCapacity, time, nickName
    return (
        <div className="preview" style={heightStyle} ref={articleListRef}>
              
        {articleList===null && <h3>아직 글이 없어요!</h3>}
        
        {articleList && articleList.map((articlePreview, index) => (
        <div key={index} className="article-item" 
        onClick={() => goArticlePage(articlePreview,dorId,isEndPage,page,token,setSelectComponentIndex,selectComponentIndex,setArticle)}>
          <div className='article-item-summary'>
            <div className="article-item-text" 
                  id='article-item-title'>
                    {articlePreview.title}
            </div>
            <div className="article-item-text"
                  id='article-item-content'>
                    {articlePreview.contentText}
            </div>
          </div>
          <div className='article-item-icons'>
            <div className='dorm-text' style={calculateArticlePreviewDorm(dorId-1,articlePreview.dormId-1)}>
              {dorIdToDorName[articlePreview.dormId]}
            </div>
          </div>

          <div className='article-item-details'>
            <div className='article-item-details-comment'>
              <img src={commentImage} className="image-comment"alt="Comment"/>
              <div className='comment-num'>{articlePreview.numComments}</div>
            </div>
            {(articlePreview.groupNumMembers !== 0 && articlePreview.groupMaxCapacity !== 0) && (
            <div className='article-item-details-group'>
              <img src={groupImage} className="image-group" alt="Group" />
              <div className='group-num'>
                {articlePreview.groupNumMembers}/{articlePreview.groupMaxCapacity}
              </div>
            </div>
            )}

            <div className='article-item-details-time '>{getRelativeTime(articlePreview.createdTime)}</div>
            <div className='article-item-details-nickName'>{articlePreview.userNickName || "알 수 없음"}</div>
          </div>
        </div>
      ))}

      </div>

    )
    

}

export default ArticlePreview