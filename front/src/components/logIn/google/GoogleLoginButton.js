import React from 'react';
import {GoogleLogin, useGoogleLogin} from '@react-oauth/google';
import axios from 'axios';

const GoogleLoginButton = ({setSocialAccountDetails}) => {

    const verifyToken = async (credentialResponse) => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/oauth/google`;
        const data={
            token:credentialResponse.credential
        };
        try{
            const response=await axios.post(path,data,{});
            setSocialAccountDetails(response.data);
            
        }catch(error){
            console.error(error);
        }
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

