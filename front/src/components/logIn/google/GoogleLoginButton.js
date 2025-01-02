import React from 'react';
import {GoogleLogin} from '@react-oauth/google';

const GoogleLoginButton = () => {
    const responseMessage = (response) => {
        console.log(response);
    }
    const errorMessage = (error) => {
        console.error(error);
    }

    return (
        <div className='App' >
          <h2>React Google Sign-In</h2>
            <GoogleLogin
                className="sign"
                onSuccess={credentialResponse => {console.log(credentialResponse);}}
                onError={() => {console.log('Login Failed');}}
            />    
        </div>
    );
};

export default GoogleLoginButton;