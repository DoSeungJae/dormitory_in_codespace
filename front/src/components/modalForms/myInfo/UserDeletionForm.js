import React, { useContext, useState } from "react";
import { toast } from "react-toastify";
import ModalContext from "../../common/ModalContext";
import axios from "axios";

function UserDeletionForm ({userId}) {
    const [password, setPassword]=useState("");
    const {isOpen, openModal, closeModal}=useContext(ModalContext);

    const deleteAccount = async () => {
        if(password==""){
            toast.warn("비밀번호를 입력해주세요.");
            return ;
        }
        const path=`${process.env.REACT_APP_HTTP_API_URL}/user/${userId}`;
        const body={
            confirmPassword : password
        };
        try{
            const response = await axios.request({ method: 'DELETE', url: path, data: body});
            localStorage.removeItem("token");
            window.location.reload();
        }catch(error){
            if(error.response.data==="ConfirmPasswordNotCorrect"){
                toast.error("확인 비밀번호가 올바르지 않아요.");
                return ;
            }
            else if(error.response.data==="CannotDeleteUserWhileGrouping"){
                toast.error("탈퇴할 수 없어요! 그룹에 속해있어요.");
                return ;
            }
        }
    }

    return (
        <div>
            <div className="modal-title">계정 탈퇴하기</div>
            <div className="modal-container">
            <div className="modal-container-title">계정 비밀번호</div>
                <input
                    class="form-control form-control-sm"
                    type="password"
                    placeholder="계정 비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <div className="modal-container-title" style={{paddingTop:"1vh", paddingBottom:"1vh"}}>
                    계정을 탈퇴하면 기존의 정보는 다시 되돌릴 수 없어요! 그래도 계속 진행하시겠어요?</div>
                <div className="modal-alternative-buttons">
                    <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={() => (deleteAccount())}>탈퇴하기</button>
                    <button className="modal-button" type="button" class="btn btn-secondary btn-sm" onClick={() => (closeModal())}>취소하기</button>
                </div>
            </div>
        </div>
    )

}

export default UserDeletionForm;