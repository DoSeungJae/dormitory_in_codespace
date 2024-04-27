import {React, useRef} from 'react';
import BackButton from '../../components/home/BackButton';

function MyWritingPage(){
    const [myArticleList,setMyArticleList]=useState([]);
    const [commentedArticleList,setCommentedArticleList]=useState([]);
    const myArticleListRef=useRef(null);
    const commentedArticleListRef=useRef(null);
    

    return (
        <div className='App'>
            <header className='App-myWritingPage-header'>
                <BackButton></BackButton>
            </header>
            <div className='App-myWritingPage-main'>
                <div className='myWritings'>
                    <div className='myWritingPage-main-text'>내가 쓴 글</div>
                </div>
                <div className='commentedOn'>
                    <div className='myWritingPage-main-text'>내가 댓글을 단 글</div>
                </div>
            </div>
        </div>
    );

}

export default MyWritingPage;
