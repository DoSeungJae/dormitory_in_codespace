import {React,useContext, useEffect, useState} from 'react'
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 
import HomeSelectContext from '../home/HomeSelectContext';
import ModalContext from '../common/ModalContext';
import Modal from '../common/Modal';
import EmailVerifyForm from '../modalForms/signIn/EmailVerifyForm';
import SignUpForm from '../modalForms/signIn/SignUpForm';
import GoogleLoginButton from './google/GoogleLoginButton';


const Options = ({socialAccountDetails,setSocialAccountDetails}) => {
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const {isOpen, openModal, closeModal}=useContext(ModalContext);
    const[verifyFinished, setVerifyFinished]=useState(false);
    const[signUpEmail,setSignUpEmail]=useState("");
    let emailDetails;
    

    const verifyEmail = (modalContent) => {
        openModal(modalContent);
    }

    const signUp = (modalContent) => {
        openModal(modalContent);
    }
    
    const initPW = (modalContent) => {
        openModal(modalContent);
    }

    const isEmptyObject = (obj) =>{
        return Object.keys(obj).length === 0;
    }

    useEffect(()=>{
        if(signUpEmail.length===0){
            return ;
        }
        emailDetails={email:signUpEmail,provider:"LOCAL"} //local or provider(GOOGLE or NAVER...)
        signUp(<SignUpForm emailDetails={emailDetails}/>)
    },[signUpEmail])

    useEffect(()=>{
        if(isEmptyObject(socialAccountDetails)){
            return ;
        }
        if(socialAccountDetails.state==='LOGINED'){
            return ;
        }
        emailDetails=socialAccountDetails;
        signUp(<SignUpForm emailDetails={emailDetails}/>)
    },[socialAccountDetails])

    
    return (
        <div className="continer options-container">
            <div><Modal/></div>
            <div className="options-row mt-1">
                <div className="" onClick={()=>verifyEmail(<EmailVerifyForm setSingUpEmail={setSignUpEmail}/>)}>회원가입</div>
                <div className="" onClick={()=>initPW(<div>비번 초기화</div>)}>비밀번호 초기화</div>
            </div>
            <div className="separator mt-4">혹은</div>
            <GoogleLoginButton setSocialAccountDetails={setSocialAccountDetails}/>
            
        </div> 
    );
};

export default Options;
