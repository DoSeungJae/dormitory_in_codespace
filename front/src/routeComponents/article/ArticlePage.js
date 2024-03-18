import {React,useState,useEffect,useContext,useRef,forwardRef} from 'react';
import axios from 'axios';
import {useLocation} from 'react-router-dom';
import BackButton from '../../components/article/BackButton';
import ThreeDotsMenu from '../../components/article/ThreeDotsMenu';
import userDefault from '../../images/userDefault.png';
import 'react-toastify/dist/ReactToastify.css';
import {toast} from 'react-toastify';
import CommentForm from '../../components/article/CommentForm';
import CommentMenu from '../../components/article/CommentMenu';

function ArticlePage(){
    const[writerNickName,setWriterNickName]=useState("");
    const[commentList,setCommentList]=useState([]);
    const[isWriter,setIsWriter]=useState(0);
    const[isReply,setIsReply]=useState(0);
    const[formPlaceHolder,setFormPlaceHolder]=useState("댓글을 입력하세요.");
    const[commentId,setCommentId]=useState(-1);
    const[touchY,setTouchY]=useState(-1);
    const[page,setPage]=useState(0);

    const [doLoadPage,setDoLoadPage]=useState(0);

    const location=useLocation();
    const article=location.state.info;
    const token=localStorage.getItem('token');
    const inputRef=useRef();
    const commentListRef=useRef(null);



    const handleTouchStart = (e) => {
      const touch = e.touches[0];
      setTouchY(touch.clientY);
    }


    const getCommentsPerPage = async (page) => {
      const path=`http://localhost:8080/api/v1/comment/article/${article.id}?page=${page}`;
      try{
        const response=await axios.get(path);
        const rootCommentList=response.data.rootComments.map(item => JSON.parse(item));
        const replyCommentList=response.data.replyComments.map(item => JSON.parse(item));
        
        const combinedComments = rootCommentList.map(rootComment => {
          const repliesToRoot = replyCommentList.filter(reply => reply.rootCommentId === rootComment.id);
          const unrelatedReplies = replyCommentList.filter(reply => 
            !rootCommentList.some(rootComment => rootComment.id === reply.rootCommentId));
          if(unrelatedReplies.length>0){
            let updatedComments=[...commentList];
            for(let replyIndex=0;replyIndex<unrelatedReplies.length;replyIndex++){
              const curReply=unrelatedReplies.at(replyIndex);
              for(let rootCommentIndex=0;rootCommentIndex<updatedComments.length;rootCommentIndex++){
                const target=updatedComments[rootCommentIndex]
                if(curReply.rootCommentId===target.id){
                  const isDuplicate = target.replyComments.some(reply => reply.id === curReply.id);
                  if(isDuplicate){
                    return 
                  }
                  target.replyComments=target.replyComments.concat(curReply);
                }
              }
            }
            setCommentList(updatedComments);
            setCommentList((prev)=>[...prev,...rootCommentList]);
            return
          }
            return {...rootComment, replyComments: repliesToRoot};
          });
        const update=combinedComments;
        if(update.at(0)!==undefined){
          setCommentList((prev)=>[...prev,...update]);
        }
        setDoLoadPage(0); 
      } 
      catch(error){
        if(error.response.data==='NoMoreCommentPage'){
          setDoLoadPage(1);
        } 
      }
    }


    const isSame = async (token) => {
      try {
        const response = await axios.get('http://localhost:8080/api/v1/token/userId', {
            headers: {
                'Authorization': `${token}`
            }
        });
        if(response.data===article.userId){
          return 1;
        }
        else{
          return 0;
        }
    
    } catch (error) {
        console.error('An error occurred isSame in ArticlePage.js:', error);
        return 0;
    }
    

    }
    const convertDorIdToString = (num) => {
      const mappingDict = {
        1: '오름1',
        2: '오름2',
        3: '오름3',
        4: '푸름1',
        5: '푸름2',
        6: '푸름3',
        7: '푸름4',
      };
      return mappingDict[num] || "Invalid input";
    }

    const getWriterNickName = async () => {
        try{
          const response = await axios.get(`http://localhost:8080/api/v1/user/${article.userId}`, {
        });
        const nickName=response.data.nickName;
        setWriterNickName(nickName);
        }
        catch(error){
          console.log('an error occurred:',error);
        }
      }

    useEffect(()=>{
        getWriterNickName();
        isSame(token).then(result=>setIsWriter(result));
      

        if(location.state.reload===1){
          toast.success("글을 수정했어요!");
          location.state.reload=0;
        }

    },[]);

    useEffect(()=>{
      async function fetchData(){
        await getCommentsPerPage(page);
      }
      fetchData();

    },[page])

    useEffect(()=>{
      const handleScroll = () => {
        if (commentListRef.current) {
          const { scrollTop, scrollHeight, clientHeight } = commentListRef.current;
          if (scrollTop + clientHeight >= scrollHeight * 0.8 && !doLoadPage) {
            //스크롤감지는 중복으로 감지되는 경우가 있음.
            setDoLoadPage(1);
          }
        }
      };
      commentListRef.current.addEventListener('scroll', handleScroll);
      if(doLoadPage){
        setPage(prevPage=>prevPage+1);
      }
      return () => {
        if(commentListRef.current){
          commentListRef.current.removeEventListener('scroll',handleScroll);
        }
      }

    },[doLoadPage]);


    function formatCreateTime(createTime) {
      const currentYear = new Date().getFullYear();

      const year = createTime[0];
      const month = createTime[1];
      const day = createTime[2];
      const hours = createTime[3];
      const minutes = createTime[4];

      return currentYear === year 
        ? `${month}/${day} ${hours}:${minutes}` 
        : `${year}/${month}/${day} ${hours}:${minutes}`;
    }
    
    return (
        <div className="App"
              onTouchStart={handleTouchStart}
              style={{ height: '100vh', width: '100vw' }}>

            <div className="app-article-header">
                <BackButton></BackButton>
                <ThreeDotsMenu isWriterParam={isWriter} articleParam={article}></ThreeDotsMenu>
                
            </div>
            <div className="app-article-main">
              <div className="article-info">
                <img src={userDefault} alt="description" className='rounded-image'/>
                  <div className="article-info-detail">
                    <p>{writerNickName}</p>
                    <p>{formatCreateTime(article.createTime)}</p>
                  </div>
              </div>
              <div className='article-body'>
              <p className='article-title'>{article.title}</p>
                <div className="article-meta">
                  <p className="article-category">{article.category}</p>
                  <p className="article-appointedTime">{article.appointedTime}</p>
                  <p className="article-dormitory">{convertDorIdToString(article.dorId)}</p>
                </div>

                <div className='article-content' ref={commentListRef} >
                  {article.content}
                  <div className="comment-list" >
                  {commentList && commentList.map((comment, index) => (
                  <div key={index} className="comment-item">
                    <div className="comment-item-header">
                      {comment.user.nickName}
                      <CommentMenu
                        rootCommentId={comment.id}
                        setRootCommentId={setCommentId}
                        setPlaceHolder={setFormPlaceHolder}
                        inputRef={inputRef}
                        isReply={isReply}
                        setIsReply={setIsReply}
                        writerId={comment.user.id}
                        commentParam={comment}
                        >
                        
                      </CommentMenu>
                    </div>
                    <p className="comment-item-content">{comment.content}</p>
                    {comment.replyComments && comment.replyComments.map((reply, replyIndex) => (
                    <div key={replyIndex} className="comment-item reply">
                        <div className="comment-item-header">
                            {reply.user.nickName}
                            <CommentMenu
                              isForReply={1}
                              writerId={reply.user.id}
                              commentParam={reply}

                                >
                            </CommentMenu>
                        </div>
                        <p className="comment-item-content">{reply.content}</p>
                    </div>
                    ))}
                  </div>
                  ))}
                  </div>
                  </div>
              </div>
            </div>
      
            <div className="app-article-footer">
              <CommentForm
                y={touchY}
                rootCommentId={commentId}
                setPlaceHolder={setFormPlaceHolder}
                placeHolder={formPlaceHolder}
                inputRef={inputRef}
                article_Id={article.id}
                isReply={isReply} 
                setIsReply={setIsReply}>
              </CommentForm>
            </div>
        </div>
    );
}



export default ArticlePage;