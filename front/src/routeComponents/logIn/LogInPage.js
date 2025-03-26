import React, { useEffect, useState } from 'react';
import LogInContainer from '../../components/logIn/LogInContainer'
import Options from '../../components/logIn/Options'
import GoogleLoginButton from '../../components/logIn/google/GoogleLoginButton'

const LogInPage = () => {
    const[socialAccountDetails,setSocialAccountDetails]=useState({});

    useEffect(()=>{
        if(socialAccountDetails.state!=="LOGINED"){
            return ;
        }
        const token=socialAccountDetails.token;
        localStorage.setItem('token',token);
        window.location.reload();
    },[socialAccountDetails])

    return (
        <div className="LogInPage">
            <LogInContainer/>
            <Options socialAccountDetails={socialAccountDetails} setSocialAccountDetails={setSocialAccountDetails} />
        </div>

    );
};

export default LogInPage;