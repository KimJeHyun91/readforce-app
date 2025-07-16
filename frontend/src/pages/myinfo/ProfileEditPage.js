import React, { useEffect, useState, useRef } from 'react';
import axiosInstance from '../../api/axiosInstance';
import kakaoIcon from '../../assets/image/kakao.png';
import googleIcon from '../../assets/image/google.png';
import defaultProfileImage from '../../assets/image/default-profile.png';
import './EditProfilePage.css';

const ProfileEditPage = () => {
  const [nickname, setNickname] = useState('');
  const [nicknameMessage, setNicknameMessage] = useState('');
  const [isNicknameValid, setIsNicknameValid] = useState(null);
  const [birthday, setBirthday] = useState('');
  const [birthdayMessage, setBirthdayMessage] = useState('');
  const [isBirthdayValid, setIsBirthdayValid] = useState(true);
  const [profileImageUrl, setProfileImageUrl] = useState(defaultProfileImage);
  const [selectedFile, setSelectedFile] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const hasFetchedImage = useRef(false);
  const previewUrlRef = useRef(null); // ✅ 미리보기 URL 추적용

  // ✅ 프로필 이미지 가져오기
  useEffect(() => {
    let objectUrl = null;
  
    const fetchProfileImage = async () => {
      try {
        const res = await axiosInstance.get('/file/get-profile-image', {
          responseType: 'blob',
        });
        objectUrl = URL.createObjectURL(res.data);
        setProfileImageUrl(objectUrl);
      } catch {
        // 🔇 어떤 에러든 기본 이미지로 fallback
        setProfileImageUrl(defaultProfileImage);
      }
    };
  
    if (!hasFetchedImage.current) {
      hasFetchedImage.current = true;
      fetchProfileImage();
    }
  
    return () => {
      if (objectUrl) URL.revokeObjectURL(objectUrl);
      if (previewUrlRef.current) URL.revokeObjectURL(previewUrlRef.current);
    };
  }, []);

  // ✅ 닉네임 중복 체크
  const checkNicknameDuplicate = async (nickname) => {
    try {
      const res = await axiosInstance.get(`/member/nickname-check?nickname=${nickname}`);
      return res.status === 200;
    } catch {
      return false;
    }
  };

  const validateNickname = async (value) => {
    setNicknameMessage('');
    const nicknameRegex = /^[a-zA-Z가-힣0-9]{2,12}$/;
    if (!nicknameRegex.test(value)) {
      setNicknameMessage('한글/영문/숫자 조합 2~12자만 사용 가능합니다.');
      setIsNicknameValid(false);
      return;
    }
    const isAvailable = await checkNicknameDuplicate(value);
    setNicknameMessage(
      isAvailable ? '사용 가능한 닉네임입니다.' : '이미 존재하는 닉네임입니다.'
    );
    setIsNicknameValid(isAvailable);
  };

  const validateBirthday = (value) => {
    setBirthdayMessage('');
    const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;
  
    if (!birthdayRegex.test(value)) {
      setBirthdayMessage('생년월일 형식이 올바르지 않습니다. (예: YYYY-MM-DD)');
      setIsBirthdayValid(false);
      return;
    }
  
    const [year, month, day] = value.split('-').map(Number);
    const date = new Date(year, month - 1, day);
  
    if (
      date.getFullYear() === year &&
      date.getMonth() === month - 1 &&
      date.getDate() === day
    ) {
      setBirthdayMessage('생년월일 입력 완료');
      setIsBirthdayValid(true);
    } else {
      setBirthdayMessage('존재하지 않는 날짜입니다.');
      setIsBirthdayValid(false);
    }
  };
  

  const handleBirthdayChange = (value) => {
    const numeric = value.replace(/\D/g, '').slice(0, 8);
    let formatted = numeric;
    if (numeric.length >= 5) {
      formatted = `${numeric.slice(0, 4)}-${numeric.slice(4, 6)}`;
      if (numeric.length >= 7) {
        formatted += `-${numeric.slice(6, 8)}`;
      }
    }
    setBirthday(formatted);
    validateBirthday(formatted);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (nickname && !isNicknameValid) {
      alert('닉네임 형식이 잘못되었거나 중복입니다.');
      return;
    }
    if (birthday && !isBirthdayValid) {
      alert('생년월일 형식이 잘못되었습니다.');
      return;
    }

    const updates = [];
    const infoPayload = {};
    if (nickname && isNicknameValid) infoPayload.nickname = nickname;
    if (birthday && isBirthdayValid) infoPayload.birthday = birthday;

    if (Object.keys(infoPayload).length > 0) {
      updates.push(axiosInstance.patch('/member/modify', infoPayload));
    }

    if (selectedFile) {
      const formData = new FormData();
      formData.append('profileImageFile', selectedFile);
      updates.push(
        axiosInstance.post('/file/upload-profile-image', formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        })
      );
    }

    try {
      const responses = await Promise.all(updates);
      const modifyResponse = responses.find((res) => res?.data?.NICKNAME);
      if (modifyResponse) {
        const newNickname = modifyResponse.data.NICKNAME;
        localStorage.setItem('nickname', newNickname);
        window.dispatchEvent(new Event('nicknameUpdated'));
      }
      alert('회원정보가 수정되었습니다.');
      window.location.href = '/';
    } catch {
      alert('회원정보 수정 실패');
    }
  };

  const handleWithdraw = async () => {
    try {
      await axiosInstance.delete('/member/withdraw');
      localStorage.clear();
      alert('탈퇴 완료되었습니다.');
      window.location.href = '/';
    } catch {
      alert('탈퇴 실패');
    }
  };

  const handleImageDelete = async () => {
    try {
      await axiosInstance.delete('/file/delete-profile-image');
      setProfileImageUrl(defaultProfileImage);
      setSelectedFile(null);
      if (previewUrlRef.current) {
        URL.revokeObjectURL(previewUrlRef.current);
        previewUrlRef.current = null;
      }
      alert('프로필 이미지가 삭제되었습니다.');
    } catch {
      alert('이미지 삭제 실패');
    }
  };

  const openSocialRedirect = async (provider) => {
    try {
      const res = await axiosInstance.post('/auth/get-social-account-link-token');
      const state = res.data.STATE;
      const redirectUri = `http://localhost:8080/oauth2/authorization/${provider}?state=${state}`;
      window.location.href = redirectUri;
    } catch {
      alert('SNS 연동 요청 실패');
    }
  };

  return (
    <div className="profile-edit-page">
      <h2>회원정보 수정</h2>
      <form className="edit-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>회원 이미지</label>
          <div className="profile-image-box">
            <img src={profileImageUrl} alt="프로필 이미지" className="profile-image" />
            <input
              type="file"
              accept="image/jpeg,image/png,image/gif"
              onChange={(e) => {
                const file = e.target.files[0];
                if (file && file.size <= 5 * 1024 * 1024) {
                  const previewUrl = URL.createObjectURL(file);
                  if (previewUrlRef.current) {
                    URL.revokeObjectURL(previewUrlRef.current);
                  }
                  previewUrlRef.current = previewUrl;
                  setProfileImageUrl(previewUrl);
                  setSelectedFile(file);
                } else {
                  alert('이미지 용량은 5MB 이하여야 합니다.');
                }
              }}
            />
            <button type="button" className="remove-image-button" onClick={handleImageDelete}>
              이미지 삭제
            </button>
          </div>
        </div>

        <div className="form-group">
          <label>닉네임</label>
          <div className="input-with-message">
            <input
              type="text"
              value={nickname}
              onChange={async (e) => {
                const value = e.target.value;
                setNickname(value);
                setNicknameMessage('');
                if (value.length >= 2) {
                  await validateNickname(value);
                } else {
                  setNicknameMessage('2자 이상 입력해주세요');
                  setIsNicknameValid(false);
                }
              }}
            />
            {nicknameMessage && (
              <span className={`validation-message ${isNicknameValid ? 'valid' : 'invalid'}`}>
                {nicknameMessage}
              </span>
            )}
          </div>
        </div>

        <div className="form-group">
          <label>생년월일</label>
          <div className="input-with-message">
            <input
              type="text"
              placeholder="예:YYYY-MM-DD"
              value={birthday}
              onChange={(e) => handleBirthdayChange(e.target.value)}
            />
            {birthdayMessage && (
              <span className={`validation-message ${isBirthdayValid ? 'valid' : 'invalid'}`}>
                {birthdayMessage}
              </span>
            )}
          </div>
        </div>

        <div className="form-group">
          <label>SNS 계정 연동</label>
          <div className="social-login">
            <button type="button" className="social-btn" onClick={() => openSocialRedirect('kakao')}>
              <img src={kakaoIcon} alt="카카오" />
            </button>
            <button type="button" className="social-btn" onClick={() => openSocialRedirect('google')}>
              <img src={googleIcon} alt="구글" />
            </button>
          </div>
        </div>

        <div className="button-group">
          <button type="submit">정보 수정</button>
        </div>

        <div className="withdraw-area">
          <button type="button" className="withdraw-button" onClick={() => setShowModal(true)}>
            회원 탈퇴
          </button>
        </div>
      </form>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">
            <p>정말로 탈퇴하시겠습니까?</p>
            <div className="modal-buttons">
              <button className="confirm" onClick={handleWithdraw}>예</button>
              <button className="cancel" onClick={() => setShowModal(false)}>아니요</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProfileEditPage;
