import React,{useState} from 'react'
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import UseLogInButton from '../../hooks/logIn/useLogInButton';
import Title from '../common/Title';
import InputForm from '../common/InputForm';
import Options from "./Options";


const LogInContainer = () => {

    const [id,setId]=useState(null);
    const [pw,setPw]=useState(null);

    return (
        <div className="LogInPage">
            <div className="container pb-4  ">
                <Title title="Taxi With"/>
            </div>

            <UseLogInButton/>
        </div>

    );
};

export default LogInContainer;

