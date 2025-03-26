import React, { useContext, useEffect, useState } from "react";
import { dormNameToDormId } from "../../home/HomeUtils";
import { toast } from "react-toastify";
import axios from "axios";
import ModalContext from "../../common/ModalContext";

function SignUpForm({emailDetails}){

    const {isOpen, openModal, closeModal}=useContext(ModalContext);

    const[password,setPassword]=useState("");
    const[passwordConfirm,setPasswordConfirm]=useState("");
    const[nickname,setNickname]=useState("");
    const[dormitory,setDormitory]=useState("");
    const[isSocial,setIsSocial]=useState(false);

    const rex_pw = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9!@#$%^&*()]{8,16}$/;
    const rex_nick = /^[a-zA-Z0-9ㄱ-ㅎ가-힣]{1,8}$/;

    const handleButton = async () => {

        try {
            if (!rex_pw.test(password)) {
                toast.error("양식에 맞지 않는 비밀번호에요!");
                return;
            }
    
            if (password !== passwordConfirm) {
                toast.error("확인 비밀번호가 올바르지 않아요!");
                return;
            }
    
            if (!rex_nick.test(nickname)) {
                toast.error("양식에 맞지 않는 닉네임이에요!");
                return;
            }
    
            try {
                const duplicated = await checkNicknameDuplicated();
                if (duplicated === "DUPLICATED") {
                    toast.error("이미 사용중인 닉네임이에요.");
                    return;
                }
            } catch (error) {
                toast.error("닉네임 중복 확인 중 오류가 발생했어요!");
                return;
            }
    
            if (dormNameToDormId[dormitory] === undefined) {
                toast.error("기숙사 형식이 올바르지 않아요!");
                return;
            }

            try{
                await signUp();
                toast.success("회원가입에 성공했어요!");
                closeModal();
                
            }catch(error){
                toast.error("회원가입 처리 중 알 수 없는 오류가 발생했습니다."); //에러 throw가 잘 안됨...
            }
        } catch (error) {
            console.error(error);
            toast.error("알 수 없는 오류가 발생했습니다.");
        } 
    };

    useEffect(()=>{
        if(emailDetails.provider!=="LOCAL"){
            setPassword("SOCIALACCOUNT1");
            setPasswordConfirm("SOCIALACCOUNT1");
            setIsSocial(true);
        }
        //setIsSocial(true) 넣고 error throw잘 되는지 확인 필요
    },[emailDetails])
    
    const checkNicknameDuplicated = async () => {
        const path = `${process.env.REACT_APP_HTTP_API_URL}/user/findByNick/${nickname}`;
        try {
            const response = await axios.get(path);
            const user = response.data;
    
            if (user !== null) {
                return "DUPLICATED";
            } else {
                throw new Error("UserNotFound"); 
            }
        } catch (error) {
            if(error.response.data === "UserNotFound") {
                return "NONDUPLICATED"; 
            } else {
                console.error(error);
                throw new Error("Unexpected error occured"); 
            }
        }
    };

    const signUp = async () => {
        const dormId=dormNameToDormId[dormitory]
        let body,path;
        if(isSocial){
            console.log("소셜로그인")
            path=`${process.env.REACT_APP_HTTP_API_URL}/user/join/socialAccount`;
            body={
                "mail":`${emailDetails.email}`,
                "passWord":"1",
                "nickName":nickname,
                "dormId":dormId,
                "provider":emailDetails.provider,
                "authToken":`${emailDetails.token}`
            }
        }else{
            path=`${process.env.REACT_APP_HTTP_API_URL}/user/join`;
            body={
                "mail":`${emailDetails.email}`,
                "passWord":password,
                "nickName":nickname,
                "dormId":dormId
            }
        }

        try{
            await axios.post(path,body,{});
        }catch(error){
            console.error(error);
            throw new Error("Unexpected error occured"); 
        }
    }
    
    
    return (
        <div>
            <div className="modal-title">회원가입</div>
                <div className="modal-container-title">창을 닫으면 진행 내역이 초기화되니 주의해주세요.</div>
                <br></br>
                <div className="modal-container">
                    <div className="modal-container-title">이메일</div>
                    <input
                        class="form-control form-control-sm"
                        type="text"
                        value={emailDetails.provider !== "LOCAL"? `${emailDetails.email} (${emailDetails.provider})` : emailDetails.email}
                        disabled={true}
                    />
                </div>

                {emailDetails.provider === "LOCAL" && (
                <>
                    <div className="modal-container">
                    <div className="modal-container-title">비밀번호</div>
                    <input
                        className="form-control form-control-sm"
                        type="password"
                        placeholder="영문, 숫자를 포함한 8~16자"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    </div>
                    <div className="modal-container">
                    <div className="modal-container-title">비밀번호 확인</div>
                    <input
                        className="form-control form-control-sm"
                        type="password"
                        placeholder="비밀번호 확인"
                        value={passwordConfirm}
                        onChange={(e) => setPasswordConfirm(e.target.value)}
                    />
                    </div>
                </>
                )}

                <div className="modal-container">
                    <div className="modal-container-title">닉네임</div>
                    <input
                        class="form-control form-control-sm"
                        type="text"
                        placeholder="특수문자 제외 최대 8자"
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                    />
                </div>
                <div className="modal-container">
                    <div className="modal-container-title">기숙사</div>
                    <input
                        class="form-control form-control-sm"
                        type="text"
                        placeholder="오름1 ~ 오름3, 푸름1 ~ 푸름4"
                        value={dormitory}
                        onChange={(e) => setDormitory(e.target.value)}
                    />
                </div>
            <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={handleButton}>회원가입</button>
        </div>
    )
}

export default SignUpForm;