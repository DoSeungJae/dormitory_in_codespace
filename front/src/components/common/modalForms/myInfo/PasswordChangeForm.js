import axios from "axios";
import React, { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import ModalContext from "../../ModalContext";

function PasswordChangeForm({userId}) {

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [newPasswordAgain, setNewPasswordAgain] = useState("");
    const {isOpen, openModal, closeModal}=useContext(ModalContext);
    
    const handleButton = () => {
        if(currentPassword=="" || newPassword=="" || newPasswordAgain==""){
            toast.warn("비워진 부분이 있어요, 모두 입력해주세요.");
            return ;
        }
        if(newPassword!==newPasswordAgain){
            toast.warn("확인 비밀번호가 올바르지 않아요!");
            return ;
        }
        changePassword();
    }

    const changePassword = async () => {
        const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/user/${userId}`;
        const body={
            passWord:newPassword,
            confirmPassword:currentPassword
          };
        try{
            const response=await axios.patch(path,body,{});
            toast.success("변경 사항을 반영했어요!");
            closeModal();
        }catch(error){
            if(error.response.data==="ConfirmPasswordNotCorrect"){
                toast.error("확인 비밀번호가 올바르지 않아요.");
            }
        }
    }

    return (
        <div>
            <div className="modal-title">비밀번호 바꾸기</div>
            <form>
                <div className="modal-container">
                    <div className="modal-container-title">현재 비밀번호</div>
                    <input
                        class="form-control form-control-sm"
                        type="password"
                        placeholder="현재 비밀번호"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                    />
                </div>
                <div className="modal-container">
                    <div className="modal-container-title">변경할 비밀번호</div>
                    <input
                        class="form-control form-control-sm"
                        type="password"
                        placeholder="변경할 비밀번호"
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
                <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={handleButton}>변경하기</button>
            </form>
        </div>
    );
}

export default PasswordChangeForm;
