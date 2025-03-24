import {React,useContext} from 'react'
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 
import HomeSelectContext from '../home/HomeSelectContext';
import ModalContext from '../common/ModalContext';
import Modal from '../common/Modal';


const Options = () => {
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const {isOpen, openModal, closeModal}=useContext(ModalContext);

    const signIn = (modalContent) => {
        openModal(modalContent);
    }

    const initPW = (modalContent) => {
        openModal(modalContent);
    }
    
    return (
        <div className="continer options-container">
            <div><Modal/></div>
            <div className="options-row mt-1">
                <div className="" onClick={()=>signIn(<div>회원가입</div>)}>회원가입</div>
                <div className="" onClick={()=>initPW(<div>비번 초기화</div>)}>비밀번호 초기화</div>
            </div>
            <div className="separator mt-4">혹은</div>
        </div> 
    );
};

export default Options;
