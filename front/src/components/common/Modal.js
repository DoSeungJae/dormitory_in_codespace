import React, { useContext, useState } from "react";
import ModalContext from "./ModalContext";

function Modal(){
    const {isOpen, openModal, closeModal}=useContext(ModalContext);

    return (
        <div>
            {isOpen && (
                <div className="modal" >
                    <div className="modal-content" onClick={e => e.stopPropagation()}>
                        <span className="closeBtn" onClick={closeModal}>&times;</span> {/* &times -> "x" */}
                        <p>{"123"}</p>
                    </div>
                </div>)}
        </div>
    );
}

export default Modal;
