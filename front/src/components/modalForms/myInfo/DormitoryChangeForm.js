import React, { useContext, useState } from "react";
import { toast } from "react-toastify";
import { dormNameToDormId } from "../../home/HomeUtils";
import axios from "axios";
import ModalContext from "../../common/ModalContext";

function DormitoryChangeForm({userId}) {
    const [dormitory, setDormitory] = useState("");
    const [password, setPassword] = useState("");
    const {isOpen, openModal, closeModal}=useContext(ModalContext);

    const handleButton = (e) => {
        e.preventDefault();
        if(dormitory=="" || password==""){
            toast.warn("비워진 부분이 있어요, 모두 입력해주세요.");
            return ;
        }
        if(dormNameToDormId[dormitory]==undefined){
            toast.warn("기숙사 형식이 올바르지 않아요!");
            return ;
        }
        changeDormitory();
    };

    const changeDormitory = async () => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/user/${userId}`;
        const body={
            confirmPassword:password,
            dormId: dormNameToDormId[dormitory]
        };
        try{
            const response=await axios.patch(path,body,{});
            toast.success("변경 사항을 반영했어요! 새로고침해 확인하세요.");
            closeModal();
        }catch(error){
            if(error.response.data==="ConfirmPasswordNotCorrect"){
                toast.error("확인 비밀번호가 올바르지 않아요.");
            }       
        }
    }

    return (
        <div>
            <div className="modal-title">기숙사 바꾸기</div>
            <form>
                <div className="modal-container">
                    <div className="modal-container-title">변경할 기숙사</div>
                    <input
                        class="form-control form-control-sm"
                        type="text"
                        placeholder="오름1 ~ 오름3 혹은 푸름1 ~ 푸름4"
                        value={dormitory}
                        onChange={(e) => setDormitory(e.target.value)}
                    />
                </div>
                <div className="modal-container" style={{paddingBottom:'2vh'}}>
                    <div className="modal-container-title">비밀번호</div>
                    <input
                        class="form-control form-control-sm"
                        type="password"
                        placeholder="비밀번호"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={handleButton}>변경하기</button>
            </form>
        </div>
    );
}

export default DormitoryChangeForm;
