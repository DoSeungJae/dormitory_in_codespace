import React from 'react'
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 
import {} from 'react-router-dom';
import useSignInButton from '../../hooks/signIn/useSignInButton';

const Options = () => {
    return (
        
            <div className="continer border-top">
                <div className="row mt-1">
                    <div className="col">회원가입</div>
                    <div className="col">아이디 찾기</div>
                    <div className="col">비밀번호 찾기</div>
                </div>
            </div> 
    );
};

export default Options;
