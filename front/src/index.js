import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { GoogleOAuthProvider } from '@react-oauth/google';
import GoogleLoginButton from './components/logIn/google/GoogleLoginButton';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  /*
  <React.StrictMode>
    <App />
  </React.StrictMode>
   strictMode는 기본적으로 (모든) 컴포넌트들이 중복(2번)으로 렌더링되므로 사용하지 않음 
  */ 
  <GoogleOAuthProvider clientId='272109241394-9qcdekfl9dhmh8ke5dtjod0hmlaf5t73.apps.googleusercontent.com' >
    
    <GoogleLoginButton/>
    {/*<App/>*/}
  </GoogleOAuthProvider>


);


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
