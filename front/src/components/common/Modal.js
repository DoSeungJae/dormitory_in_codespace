import React, { useState } from "react";

function Modal({isOpen, openModal, closeModal}){

    return (
        <div>
            {isOpen && (
                <div className="modal" >
                    <div className="modal-content" onClick={e => e.stopPropagation()}>
                        <span className="closeBtn" onClick={closeModal}>&times;</span> {/* &times -> "x" */}
                        <p>모달 창 내용</p>
                    </div>
                </div>)}
        </div>
    );
}

export default Modal;
