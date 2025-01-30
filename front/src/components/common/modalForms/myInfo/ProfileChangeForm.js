import React, { useContext, useEffect, useRef } from "react";
import ModalContext from "../../ModalContext";

function ProfileChangeForm({setProfileImage, userId}){

    const {isOpen, openModal, closeModal}=useContext(ModalContext);
    const inputRef=useRef(null);

    const changeProfile = () => {
        inputRef.current.click();
        //서버에 요청;
        closeModal();
    }

    const deleteProfile = () => {
        setProfileImage(null);
        //서버에 요청
        closeModal();
    }

    const handleImageChange = (e) => {
        const file=e.target.files[0];
        if(file){
            const reader=new FileReader();
            reader.onloadend = () => {
                setProfileImage(reader.result);
            };
            reader.readAsDataURL(file);
        }else{
            return ;
        }
    }


    return (
        <div>
            <div className="modal-title">프로필 사진 변경</div>
            <div className="modal-alternative-buttons">
                <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={() => (changeProfile())}>프로필 변경</button>
                <button className="modal-button" type="button" class="btn btn-secondary btn-sm" onClick={() => (deleteProfile())}>프로필 삭제</button>
                <input
                    type="file"
                    ref={inputRef}
                    style={{display:'none'}}
                    onChange={handleImageChange}
                />
            </div>
        </div>
    )


}

export default ProfileChangeForm;