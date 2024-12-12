import React, { useState } from "react";

function InquireForm(){
    const [content, setContent]=useState("");

    const reportInquiry = async () => {
        console.log(content);

    }
    return (
    <div>
        <div className="modal-title">문의하기</div>
        <form >
            <div className="modal-container">
                <div className="modal-container-title">문의할 내용</div>
                <input
                    class="form-control form-control-sm "       
                    type="text"
                    placeholder="어떤 점이 불편하셨나요?"
                    value={content}
                    style={{height:"30vh", paddingBottom:"27vh"}}
                    onChange={(e) => setContent(e.target.value)
                    }
                />
            </div>
            <button className="modal-button" type="button" class="btn btn-primary btn-sm" onClick={reportInquiry}>변경하기</button>
        </form>
    </div>
    )
}

export default InquireForm;