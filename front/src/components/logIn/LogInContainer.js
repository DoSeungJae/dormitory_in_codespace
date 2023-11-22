import React,{useState} from 'react'
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import UseLogInButton from '../../hooks/logIn/useLogInButton';
import Title from '../common/Title';

const LogInContainer = () => {

    return (
        <div class="LogInPage">
            <div className="container pb-4  ">
                <Title title="Title"/>
            </div>

            <UseLogInButton/>
        </div>

    );
};

export default LogInContainer;

