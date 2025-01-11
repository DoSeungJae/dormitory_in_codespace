import React,{useState} from 'react'
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import UseLogInButton from '../../hooks/logIn/useLogInButton';
import Title from '../common/Title';

const LogInContainer = () => {

    return (
        <div className="LogInPage">
            <div className="container text-center">
                <Title title="로그인"/>
            </div>

            <UseLogInButton/>
        </div>

    );
};

export default LogInContainer;

