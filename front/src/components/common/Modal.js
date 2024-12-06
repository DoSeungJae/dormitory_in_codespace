import React, { useContext, useEffect, useState } from "react";
import ModalContext from "./ModalContext";

function Modal(){
    const {isOpen, openModal, closeModal,content,setContent}=useContext(ModalContext);

    return (
        <div>
            {isOpen && (
                <div className="modal" >
                    <div className="modal-content" onClick={e => e.stopPropagation()}>
                        <span className="closeBtn" onClick={closeModal}>&times;</span> {/* &times -> "x" */}
                        <p>{content}</p>
                    </div>
                </div>)}
        </div>
    );
}

export default Modal;
