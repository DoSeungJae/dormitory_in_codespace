import {React,useContext, useState} from 'react';
import { goArticlePage,calculateDorItemStyle,dorIdToDorName } from '../home/HomeUtils';
import HomeSelectContext from '../home/HomeSelectContext';
import commentImage from '../../images/comment.png';
import groupImage from '../../images/group.png';

function ArticlePreview({articleList,articleListRef,page,isEndPage,dorId,heightStyle}){
  //detailList 파라미터로 받기 
  
  const token=localStorage.getItem('token');
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  
  //commentNum, memberNum, maxCapacity, time, nickName

    return (
        <div className="preview" style={heightStyle} ref={articleListRef}>
              
        {articleList===null && <h3>아직 글이 없어요!</h3>}
        
        {articleList && articleList.map((article, index) => (
        <div key={index} className="article-item" 
        onClick={() => goArticlePage(article,dorId,isEndPage,
                                    page,token,
                                    setSelectComponentIndex,
                                    selectComponentIndex)}>
          <div className='article-item-summary'>
            <div className="article-item-text" 
                  id='article-item-title'>
                    {article.title}
            </div>
            <div className="article-item-text"
                  id='article-item-content'>
                    {article.content}
            </div>
          </div>
          <div className='article-item-icons'>
            <div className='dor-icon'
              style={calculateDorItemStyle(dorId-1,article.dorId-1)}>
              {dorIdToDorName[article.dorId]}
            </div>
          </div>

          <div className='article-item-details'>
            <div className='article-item-details-comment'>
              <img src={commentImage} className="image-comment"alt="Comment"/>
              <div className='comment-num'>n</div>
            </div>
            <div className='article-item-details-group'>
              <img src={groupImage} className="image-group"alt="Group"/>
              <div className='group-num'>n/n</div>
            </div>
            <div className='article-item-details-time '>n분 전</div>
            <div className='article-item-details-nickName'>닉네임</div>
          </div>
        </div>
      ))}

      </div>

    )
    

}

export default ArticlePreview