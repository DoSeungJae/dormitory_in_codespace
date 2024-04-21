import {React} from 'react';
import { useNavigate } from 'react-router-dom';

function ArticlePreview({articleList,articleListRef,goArticlePage,calculateDorItemStyle,
                        saveScrollState,dorId,dorIdToDorName}){
                          
  const token=localStorage.getItem('token');
  const navigate=useNavigate();

    return (
        <div className="preview" ref={articleListRef}>
              
        {articleList===null && <h3>아직 글이 없어요!</h3>}
        
        {articleList && articleList.map((article, index) => (
        <div key={index} className="article-item" onClick={() => goArticlePage(article)}>
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
        </div>
      ))}

      </div>

    )
    

}

export default ArticlePreview