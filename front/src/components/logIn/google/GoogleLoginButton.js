import React from 'react';
import {GoogleLogin, useGoogleLogin} from '@react-oauth/google';
import axios from 'axios';

const GoogleLoginButton = () => {

    const verifyToken = async (credentialResponse) => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/oauth/google`;
        const data={
            token:credentialResponse.credential
        };
        try{
            const response=await axios.post(path,data,{});
            console.log(response.data);
        }catch(error){
            console.error(error);
        }
    }

    const errorMessage = (error) => {
        console.error(error);
    }

    return (
        <div className='google-login-wrapper' >
            <GoogleLogin
                className="sign"
                onSuccess={credentialResponse => {verifyToken(credentialResponse);}}
                onError={() => {console.log('Login Failed');}}
            />    
        </div>
    );
};

export default GoogleLoginButton;


/*
커스텀 버튼으로 로그인 하기
import React from 'react';
import { GoogleLogin, useGoogleLogin } from '@react-oauth/google';

const GoogleLoginButton = () => {
    const login = useGoogleLogin({
        onSuccess: credentialResponse => {
            console.log(credentialResponse);
        },
        onError: () => {
            console.log('Login Failed');
        }
    });

    return (
        <div className='App'>
            <h2>React Google Sign-In</h2>
            <button onClick={() => login()}>Sign in with Google</button>
        </div>
    );
};

export default GoogleLoginButton;

 */