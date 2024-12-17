import axios from "axios";
import React, { useContext, useState } from "react";
import { toast } from "react-toastify";
import ModalContext from "../../ModalContext";

function InquireForm({userId}){
    const [content, setContent]=useState("");
    const {isOpen, openModal, closeModal}=useContext(ModalContext);
    const token = localStorage.getItem("token");

    const reportInquiry = async () => {
        if(content==""){
            toast.warn("내용을 입력해주세요.");
            return ;
        }
        const path='https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/report/newInquire';
        const body={
            inquireContent:content
        }
        const headers={
            "Authorization":`${token}`
        }
        try{
            const response=await axios.post(path,body,{headers});
            toast.success("문의가 접수되었어요.");
            closeModal();
        }catch(error){
            console.error(error);
        }
    }
    return (
    <div>
        <div className="modal-title">문의하기</div>
        <form >
            <div className="modal-container">
                <div className="modal-container-title">문의할 내용</div>
                <textarea
                    class="form-control form-control-sm "       
                    type="text"
                    placeholder="어떤 점이 불편하셨나요?"
                    value={content}
                    style={{height:"30vh", paddingBottom:"27vh"}}
                    onChange={(e) => setContent(e.target.value)}
                />
            </div>
            <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={reportInquiry}>문의하기</button>
        </form>
    </div>
    )
}

export default InquireForm;