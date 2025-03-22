import React, { useContext } from "react";
import ModalContext from "../../common/ModalContext";


function LogoutForm () {

    const {isOpen, openModal, closeModal}=useContext(ModalContext);

    const logout = () => {
        localStorage.clear();
        window.location.reload();
    }
    return (
        <div>
            <div className="modal-title">로그아웃 할까요?</div>
            <div className="modal-alternative-buttons">
                <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={() => (logout())}>네</button>
                <button className="modal-button" type="button" class="btn btn-secondary btn-sm" onClick={() => (closeModal())}>아니요</button>
            </div>
        </div>
    )

       
}

export default LogoutForm;