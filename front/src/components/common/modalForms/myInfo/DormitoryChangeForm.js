import React, { useState } from "react";

function DormitoryChangeForm() {
    const [dormitory, setDormitory] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        // 기숙사 변경 로직을 여기에 추가하세요
        console.log("Dormitory:", dormitory);
        console.log("Password:", password);
    };

    return (
        <div>
            <div className="modal-title">기숙사 바꾸기</div>
            <form onSubmit={handleSubmit}>
                <div className="modal-container">
                    <div className="modal-container-title">바꿀 기숙사</div>
                    <input
                        type="text"
                        placeholder="바꿀 기숙사"
                        value={dormitory}
                        onChange={(e) => setDormitory(e.target.value)}
                    />
                </div>
                <div className="modal-container">
                    <div className="modal-container-title">비밀번호 확인</div>
                    <input
                        type="password"
                        placeholder="비밀번호 확인"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button type="submit">변경하기</button>
            </form>
        </div>
    );
}

export default DormitoryChangeForm;
