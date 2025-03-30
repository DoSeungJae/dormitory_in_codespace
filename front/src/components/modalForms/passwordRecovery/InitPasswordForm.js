import axios from 'axios';
import React, { useContext, useState } from 'react';
import { toast } from 'react-toastify';
import ModalContext from '../../common/ModalContext';

function InitPasswordForm({recoveryDetails}){
    const {isOpen, openModal, closeModal}=useContext(ModalContext);
    const[newPassword, setNewPassword]=useState("");
    const[newPasswordAgain, setNewPasswordAgain]=useState("");
    const rex_pw = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9!@#$%^&*()]{8,16}$/;
    const message="현재 창을 끄면 진행 내역이 모두 초기화됩니다."

    const handleButton = () => {
        if(newPassword.length===0 || newPasswordAgain.length===0){
            toast.error("비워진 곳을 채워주세요.");
        }
        if (!rex_pw.test(newPassword)) {
            toast.error("양식에 맞지 않는 비밀번호에요!");
            return;
        }
        if(newPassword!==newPasswordAgain){
            toast.error("확인 비밀번호가 일치하지 않아요.");
        }
        initPassword();
    }
    
    const initPassword = async () => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/user/initPassword`;
        const body={
            "email":recoveryDetails.email,
            "emailToken":recoveryDetails.token,
            "newPassword":`${newPassword}`
        };

        try{
            await axios.patch(path,body,{});
            toast.success("비밀번호를 변경했어요.");
            closeModal();
        }catch(error){
            const errMsg=error.response.data;
            if(errMsg==="InvalidToken" || errMsg==="InvalidEmail" || errMsg==="UserNotFound" || errMsg==="InvalidPassword"){
                toast.error("유효하지 않은 접근입니다.");
            }
            else{
                toast.error("알 수 없는 문제가 발생했어요. 재시도 혹은 문의해주세요.");
            }
        }
    }
    return (
        <div>
            <div className="modal-title">비밀번호 바꾸기</div>
            <form>
                <div className="modal-container">
                    <div className="modal-container-title">변경할 비밀번호</div>
                    <input
                        class="form-control form-control-sm"
                        type="password"
                        placeholder="영문, 숫자를 포함해 8~16자"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                    />
                </div>
                <div className="modal-container" style={{paddingBottom:'2vh'}}>
                    <div className="modal-container-title">변경할 비밀번호 확인</div>
                    <input
                        class="form-control form-control-sm"
                        type="password"
                        placeholder="변경할 비밀번호 확인"
                        value={newPasswordAgain}
                        onChange={(e) => setNewPasswordAgain(e.target.value)}
                    />
                </div>
                <div className="modal-container-title" style={{paddingBottom:'1vh'}}><strong>{message}</strong></div>
                <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={handleButton}>변경하기</button>
            </form>
        </div>
    );
}

export default InitPasswordForm;