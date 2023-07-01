import React from 'react'
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';

import Title from '../public/Title';
import InputForm from '../public/InputForm';
import Options from "./Options";



const LogInContainer = () => {
    return (
        <div className="LogInPage">

            <div className="container pb-4  ">
                <div className="row mt-4">
                    <Title title="Taxi With"/>
                </div>
            </div>

            <div className="container">
                <div className="row mt-5">
                    <div className="col">
                        <InputForm type="text" placeholder="아이디" />
                    </div>
                </div>
                <div className="row mt-3">
                    <div className="col">
                        <InputForm type="password" placeholder="비밀번호" />
                    </div>
                </div>

                <div class="d-grid mt-4 pb-4">
                    <button class="btn btn-primary" type="button">로그인</button>
                </div>
            </div>

        
        </div>

    );
};

export default LogInContainer;

