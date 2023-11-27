import React from 'react'
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 
import { BrowserRouter as Router,Routes,Route,Link, BrowserRouter} from 'react-router-dom';
import useSignInButton from '../../hooks/signIn/UseSignInButton';
import SignInPage from '../../routeComponents/signIn/SignInPage';
 // CSS 파일을 import 합니다.

const Options = () => {
    return (
        <div className="continer options-container">
            <div className="row mt-1">
                <div className="col"><Link to="/signIn">회원가입</Link></div>
                <div className="col">아이디 찾기</div>
                <div className="col">비밀번호 찾기</div>
            </div>
            <div className="separator">혹은</div>
        </div> 
    );
};

export default Options;
