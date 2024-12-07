import React, { useState } from "react";

function PasswordChangeForm() {
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        // http request 호출하기
    };

    return (
        <div>
            <div className="modal-title">비밀번호 바꾸기</div>
            <form onSubmit={handleSubmit}>
                <div className="modal-container">
                    <div className="modal-container-title">현재 비밀번호</div>
                    <input
                        type="password"
                        placeholder="현재 비밀번호"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                    />
                </div>
                <div className="modal-container">
                    <div className="modal-container-title">변경할 비밀번호</div>
                    <input
                        type="password"
                        placeholder="변경할 비밀번호"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                    />
                </div>
                <div className="modal-container">
                    <div className="modal-container-title">변경할 비밀번호 확인</div>
                    <input
                        type="password"
                        placeholder="변경할 비밀번호 확인"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />
                </div>
                <button type="submit">변경하기</button>
            </form>
        </div>
    );
}

export default PasswordChangeForm;
