import React, { useState, useRef, useEffect } from 'react';
import './EditProfileModal.css';

const EditProfileModal = ({ onClose }) => {
    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const modalRef = useRef(null);
    const [showMenu, setShowMenu] = useState(false);
    const [profileImage, setProfileImage] = useState('https://via.placeholder.com/100'); // 임시 이미지


    useEffect(() => {
        setTimeout(() => {
            modalRef.current?.classList.add('show');
        }, 10);
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        alert(`닉네임: ${nickname}, 비밀번호: ${password}`);
        onClose(); // 닫기
    };

    const handleUploadClick = () => {

        // TODO: 파일 업로드 로직
    };

    const handleDeleteClick = () => {

        // TODO: 삭제 API 호출
    };

    const handleWithdraw = () => {

        // TODO: 탈퇴 API 호출
    };


    return (
        <div className="modal-overlay" onClick={onClose}>
            <div
                className="modal-content"
                ref={modalRef}
                onClick={(e) => e.stopPropagation()}
            >
                <h2>회원정보 수정</h2>
                <div className="profile-section">
                    <div className="profile-wrapper">
                        <img src={profileImage} alt="프로필" className="profile-image" />
                        <button className="profile-settings" onClick={() => setShowMenu(!showMenu)}>⚙️</button>
                        {showMenu && (
                            <div className="profile-menu">
                                <div onClick={handleUploadClick}>프로필 사진 업로드</div>
                                <div onClick={handleDeleteClick}>프로필 사진 삭제</div>
                            </div>
                        )}
                    </div>
                </div>
                <form onSubmit={handleSubmit} className="edit-form">
                    <label>닉네임</label>
                    <input type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} />

                    <label>비밀번호</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

                    <label>비밀번호 확인</label>
                    <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />

                    <div className="button-group">
                        <button type="submit">수정</button>
                        <div className="button-group center">
                            <button className="withdraw-button" onClick={handleWithdraw}>회원 탈퇴</button>
                        </div>
                        <button type="button" onClick={onClose}>취소</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default EditProfileModal;