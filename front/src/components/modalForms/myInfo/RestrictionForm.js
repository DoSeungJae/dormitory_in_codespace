import React, { useContext, useEffect, useState } from "react";
import ModalContext from "../../common/ModalContext";
import { getRelativeTime } from "../../../modules/common/timeModule";
import axios from "axios";

    
function RestrictionForm () {

    const {closeModal}=useContext(ModalContext);
    const [restriction,setRestriction]=useState("");
    const token=localStorage.getItem("token");

    const restrictionData = (data, index) => (
        <div className="modal-restriction-entry" key={index}>
            제재 사유: {data.reason}<br></br>
            만료 시각: {getRelativeTime(data.expireTime)}<br></br>
            만료 여부: {data.isExpired?"O":"X"}
            <hr></hr>
        </div>
    );


    const getRestriction = async () => {
        try {
            const headers = {'Authorization':`${token}`};
            const res = await axios.get(
                `${process.env.REACT_APP_HTTP_API_URL}/restriction/my`,
                {headers}
            );
            setRestriction(res.data.dtoList.map(restrictionData));
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(()=>{
        getRestriction();
    });

    return (
        <div>
            <div className="modal-title">커뮤니티 이용 제한 내역</div>
            <div className="modal-container scrollable">
                {restriction}
            </div>
            <div className="modal-alternative-buttons">
                <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={() => (closeModal())}>닫기</button>
            </div>
        </div>
    )

       
}

export default RestrictionForm;