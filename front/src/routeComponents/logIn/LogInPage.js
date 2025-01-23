import React from 'react';
import LogInContainer from '../../components/logIn/LogInContainer'
import Options from '../../components/logIn/Options'
import GoogleLoginButton from '../../components/logIn/google/GoogleLoginButton'

const LogInPage = () => {
    return (
        <div className="LogInPage">
            <LogInContainer/>
            <Options/>
            <GoogleLoginButton/>
        </div>

    );
};

export default LogInPage;