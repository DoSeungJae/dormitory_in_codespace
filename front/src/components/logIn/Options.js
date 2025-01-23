import {React,useContext} from 'react'
import 'bootstrap/dist/css/bootstrap.css'; 
import 'bootstrap/dist/js/bootstrap.js'; 
import HomeSelectContext from '../home/HomeSelectContext';

const Options = () => {
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const handleTextClick = () => {
        setSelectComponentIndex(7);
    }
    return (
        <div className="continer options-container">
            <div className="options-row mt-1">
                <div className="text-center btn btn-secondary btn-sm">
                    <div onClick={handleTextClick}>회원가입</div>    
                </div>
                <div className="text-center btn btn-secondary btn-sm">아이디 찾기</div>
                <div className="text-center btn btn-secondary btn-sm">비밀번호 변경</div>
            </div>
            <div className="separator mt-4">혹은</div>
        </div> 
    );
};

export default Options;
