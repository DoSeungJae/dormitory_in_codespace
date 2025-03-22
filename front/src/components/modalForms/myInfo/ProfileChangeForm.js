import React, { useContext, useEffect, useRef, useState } from "react";
import ModalContext from "../../ModalContext";
import axios from "axios";
import { toast } from "react-toastify";

function ProfileChangeForm({setProfileImage}){

    const {isOpen, openModal, closeModal}=useContext(ModalContext);
    const inputRef=useRef(null);
    const token=localStorage.getItem("token");

    const changeProfile = () => {
        inputRef.current.click(); 
    }

    const deleteProfile = () => {
        setProfileImage(null);
        deleteProfileImage();
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
            closeModal();
            saveImage(file);
            
        }else{
            return ;
        }
    }

    const saveImage = async (image) => {
        const path = `${process.env.REACT_APP_HTTP_API_URL}/file/userImage`;
        const formData = new FormData();
        formData.append("file", image);
    
        try {
          await axios.put(path, formData, {
            headers: {
              'Authorization': token,
              'Content-Type': 'multipart/form-data',
            },
          });
        } catch (error) {
          console.error(error);
          if (error.response.data === "유효하지 않은 토큰입니다.") {
            toast.error("회원 정보가 유효하지 않아요, 다시 로그인해주세요.");
          }
        }
    };    

    const deleteProfileImage = async () => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/file/userImage`;
        try{
            axios.delete(path,{headers:{'Authorization':`${token}`}});
        }catch(error){
            console.error(error);
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