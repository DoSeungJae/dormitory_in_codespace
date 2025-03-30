import axios from "axios";
import React, { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import ModalContext from "../../common/ModalContext";

function EmailVerifyForm({setSingUpEmail}){
    const {isOpen, openModal, closeModal}=useContext(ModalContext);
    
    const[sent,setSent]=useState(false);
    const[email,setEmail]=useState("");
    const[message, setMessage]=useState("회원가입을 하려면 먼저 이메일 인증이 필요해요.");
    const[verifyCode,setVerifyCode]=useState("");
    const[inputDisabled,setInputDisabled]=useState(false);

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    const handleButtonClick = () => {
        if(!sent){
            sendVerifyCode();
        }else{
            authenticateVerifyCode();
        }
    }

    const sendVerifyCode = async () => {
        if(email.length===0){
            toast.error("이메일을 입력해주세요.");
            return ;
        }
        if(!emailRegex.test(email)){
            toast.error("이메일 양식에 맞지 않아요.");
            return ;
        }
        let duplicated;
        try{
            duplicated=await checkEmailDuplicated();
        }catch(error){
            toast.error("Unexpected error occured");
            return ;
        }
        if(duplicated=="DUPLICATED"){
            toast.error("이미 사용중인 메일이에요.");
            return ;
        }

        const path=`${process.env.REACT_APP_HTTP_API_URL}/email/sendVerifyCode`;
        const headers={
            Email:`${email}`
        };
        try{
            await axios.get(path,headers);
        }catch(error){
            console.error(error);
            return ;
        }
        setSent(true);
        setInputDisabled(true);
        setMessage("발급된 인증 코드는 10분 동안 유효합니다. 창을 껐다 키면 재발급 받을 수 있습니다.");
        toast.success("인증 코드가 메일로 전송되었어요.");
    }
    
    const checkEmailDuplicated = async () => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/user/emailDuplicated`;
        const headers={'Email':`${email}`};
        try{
            const response=await axios.get(path,{headers});
            return response.data;
        }catch(error){
            console.error(error);
            throw new Error(error);
        }
    }

    const authenticateVerifyCode = async () => {
        if(verifyCode.length==0){
            toast.error("인증 코드를 입력해주세요.");
            return 
        }
        if(verifyCode.length!==6){
            toast.error("유효하지 않은 인증 코드에요");
            return ;
        }
        const path=`${process.env.REACT_APP_HTTP_API_URL}/email/authenticateCode`;
        const headers={
            Email:`${email}`,
            VerifyCode:`${verifyCode}`
        };
        try{
            const response=await axios.get(path,headers);
            const result=response.data.stateType;
            if(result==='MISMATCH'){
                toast.error("인증 코드가 맞지 않아요.");
                return ;
            }
            toast.success("이메일이 인증되었어요");
            setSingUpEmail(email);
            closeModal();
            
            
            
        }catch(error){
            console.error(error);
        }   
    }

    return (
        <div>
            <div className="modal-title">이메일 인증</div>
            <div className="modal-container">
                    <div className="modal-container-title">{message}</div>
                    <input
                        class="form-control form-control-sm"
                        type="text"
                        placeholder="이메일"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        disabled={inputDisabled}
                    />
                    {sent && (<div className="modal-container-title">인증 코드</div>)}
                    {sent && (
                        <input
                            class="form-control form-control-sm"
                            type="text"
                            placeholder="인증 코드"
                            value={verifyCode}
                            onChange={(e) => setVerifyCode(e.target.value)}
                        />
                    )}

                </div>
            <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={handleButtonClick}>
                {!sent ? "인증 코드 받기" : "확인"}
            </button>
                
        </div>
    )
}

export default EmailVerifyForm;
