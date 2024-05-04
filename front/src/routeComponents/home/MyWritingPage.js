import {React, useEffect, useRef,useState} from 'react';
import BackButton from '../../components/home/BackButton';
import ArticlePreview from '../../components/article/ArticlePreview';
import axios from 'axios';

function MyWritingPage(){
    const [myArticleList,setMyArticleList]=useState([]);
    const [commentedArticleList,setCommentedArticleList]=useState([]);
    const [myPage,setMyPage]=useState(0);
    const [cmtPage,setCmtPage]=useState(0);
    const [doLoadMyPage,setDoLoadMyPage]=useState(0);
    const [doLoadCmtPage,setDoLoadCmtPage]=useState(0);
    const [isEndMyPage,setIsEndMyPage]=useState(0);
    const [isEndCmtPage,setIsEndCmtPage]=useState(0);
    const myArticleListRef=useRef(null);
    const commentedArticleListRef=useRef(null);
    const token=localStorage.getItem('token');
    const [extendedPreview,setExtendedPreview]=useState("");// "" or "my" or "cmt"

    const [myHeight,setMyHeight]=useState("37vh");
    const [cmtHeight, setCmtHeight]=useState("37vh");
    
    const myPreviewStyle={
        maxHeight:myHeight
    }
    const cmtPreviewStyle={
        maxHeight:cmtHeight
    }

    const extendMaxHeightOfPreview = () =>{
        if(extendedPreview===""){
            setMyHeight("37vh");
            setCmtHeight("37vh");
        }
        else if(extendedPreview==="my"){
            setMyHeight("78vh");
            setCmtHeight("0vh");
            console.log("my");
        }
        else{
            setMyHeight("0vh");
            setCmtHeight("78vh");
            console.log("cmt");
        }
    }
    
    const handleExtendedMy = () => {
        if(extendedPreview==="my"){
            setExtendedPreview("");
        }
        else{
            setExtendedPreview("my")
        }
    }
    const handleExtendedCmt = () => {
        if(extendedPreview==="cmt"){
            setExtendedPreview("");
        }
        else{
            setExtendedPreview("cmt");
        }
    }
    
    const getArticleListPerPage = async (page,mode) => {
        let path;
        //mode: my or cmt
        if(mode==='my'){
            path=`http://localhost:8080/api/v1/article/filter/user?page=${page}`;
        }
        else{
            path=`http://localhost:8080/api/v1/article/filter/userComment?page=${page}`;
        }
        try{
            const response=await axios.get(path,{
                headers : {
                    'Authorization' : `${token}`
                }
            });
            const data=response.data.map(item=>JSON.parse(item));
            if(mode==='cmt'){
                setCommentedArticleList((prev)=>[...prev,...data]);
                setDoLoadCmtPage(0);
            }
            else{
                setMyArticleList((prev)=>[...prev,...data]);
                setDoLoadMyPage(0);
            }

        }
        catch(error){
            const errMsg=error.response.data;
            if(errMsg==='NoMoreArticlePage'){
                if(mode==='cmt'){
                    setDoLoadCmtPage(1);
                    setIsEndCmtPage(1);
                }
                else{
                    setDoLoadMyPage(1);
                    setIsEndMyPage(1);
                }
            }
            if(errMsg==='ArticleNotFound'){
                return ;
            }
        }
    };

    useEffect(()=>{
        extendMaxHeightOfPreview();
        console.log(extendedPreview);
    },[extendedPreview])

    useEffect(()=>{
        
        async function fetchData(){
            const mode='my'
            await getArticleListPerPage(myPage,mode);
        }
        fetchData();
    },[myPage])

    useEffect(()=>{
        const handleScroll = () => {
            const {scrollTop, scrollHeight, clientHeight}= myArticleListRef.current;
            if(myArticleListRef.current){
                if(scrollTop + clientHeight >= scrollHeight * 0.8 && !doLoadMyPage){
                    setDoLoadMyPage(1);
                }
            }
        };
        myArticleListRef.current.addEventListener('scroll',handleScroll);
        if(doLoadMyPage && !isEndMyPage){
            setMyPage(prevPage=>prevPage+1);
        }
        return () => {
            if(myArticleListRef.current){
                myArticleListRef.current.removeEventListener('scroll',handleScroll);
            }
        }
    },[doLoadMyPage])

    useEffect(()=>{
        async function fetchData(){
            const mode='cmt';
            await getArticleListPerPage(cmtPage,mode);
        }
        fetchData();
    },[cmtPage])

    useEffect(()=>{
        const handleScroll = () => {
            const {scrollTop, scrollHeight, clientHeight} = commentedArticleListRef.current;
            if(commentedArticleListRef.current){
                if(scrollTop+clientHeight>=scrollHeight*0.8 && !doLoadCmtPage){
                    setDoLoadCmtPage(1);
                }
            }
        };
        commentedArticleListRef.current.addEventListener('scroll',handleScroll);
        if(doLoadCmtPage && !isEndCmtPage){
            setCmtPage(prevPage=>prevPage+1);
        }
        return () => {
            if(commentedArticleListRef.current){
                commentedArticleListRef.current.removeEventListener('scroll',handleScroll);
            }
        }
    },[doLoadCmtPage])

    return (
        <div className='App'>
            <header className='App-myWritingPage-header'>
                <BackButton></BackButton>
            </header>
            <div className='App-myWritingPage-main'>
                {extendedPreview!=="cmt" &&
                    (<div className='myWritings'>
                        <div className='myWritingPage-main-text'
                            onClick={handleExtendedMy}>내가 쓴 글</div>
                            <ArticlePreview
                                articleList={myArticleList}
                                articleListRef={myArticleListRef}
                                dorId={0}
                                page={myPage}
                                isEndPage={isEndMyPage}
                                heightStyle={myPreviewStyle}
                            />
                    </div>)
                }
                {extendedPreview!=="my" &&
                    (<div className='commentedOn'>
                        <div className='myWritingPage-main-text'
                            onClick={handleExtendedCmt}>내가 댓글을 단 글</div>
                            <ArticlePreview
                                articleList={commentedArticleList}
                                articleListRef={commentedArticleListRef}
                                dorId={0}
                                page={cmtPage}
                                isEndPage={isEndCmtPage}
                                heightStyle={cmtPreviewStyle}
                            />
                    </div>)
                }
            </div>
        </div>
    );

}

export default MyWritingPage;
