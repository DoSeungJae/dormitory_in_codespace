import {React} from 'react'
import {useLocation} from 'react-router-dom'

function ArticlePage(){
    const location=useLocation();
    const article=location.state;
    console.log(article);


    return (
        <h1>Article Page</h1>
    );

}

export default ArticlePage;