import React, { useState } from 'react';
import axiosInstance from '../../api/axiosInstance';
import './EditProfilePage.css';

const ChangePasswordPage = () => {
  const [currentPassword, setCurrentPassword] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordMessage, setPasswordMessage] = useState('');
  const [isPasswordValid, setIsPasswordValid] = useState(null);
  const [confirmPasswordMessage, setConfirmPasswordMessage] = useState('');
  const [isConfirmPasswordValid, setIsConfirmPasswordValid] = useState(null);

  const validatePassword = (value) => {
    const valid =
      value.length >= 8 &&
      value.length <= 16 &&
      /[a-zA-Z]/.test(value) &&
      /[0-9]/.test(value) &&
      /[!@#$%^&*]/.test(value);
    setPasswordMessage(
      valid
        ? '사용 가능한 비밀번호입니다'
        : '영문/숫자/특수문자를 모두 포함하여 8~16자로 설정해주세요'
    );
    setIsPasswordValid(valid);
  };

  const validateConfirmPassword = (value) => {
    const match = value === password;
    setConfirmPasswordMessage(
      match ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다'
    );
    setIsConfirmPasswordValid(match);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!isPasswordValid || !isConfirmPasswordValid) return;

    try {
      await axiosInstance.patch('/member/password-reset', {
        oldPassword: currentPassword,
        newPassword: password,
      });
      alert('비밀번호가 성공적으로 변경되었습니다.');
      window.location.href = '/';
    } catch (error) {
      if (error.response && error.response.status === 400) {
        alert('현재 비밀번호가 일치하지 않습니다.');
      } else {
        alert('비밀번호 변경 중 오류가 발생했습니다.');
      }
    }
  };

  return (
    <div className="profile-edit-page">
      <h2>비밀번호 변경</h2>
      <form className="edit-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>현재 비밀번호</label>
          <input
            type="password"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label>새 비밀번호</label>
          <div className="input-with-message">
            <input
              type="password"
              value={password}
              onChange={(e) => {
                const value = e.target.value;
                setPassword(value);
                validatePassword(value);
              }}
              required
            />
            <span className="validation-message" style={{ color: isPasswordValid ? 'green' : 'red' }}>
              {passwordMessage}
            </span>
          </div>
        </div>

        <div className="form-group">
          <label>비밀번호 확인</label>
          <div className="input-with-message">
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => {
                const value = e.target.value;
                setConfirmPassword(value);
                validateConfirmPassword(value);
              }}
              required
            />
            <span className="validation-message" style={{ color: isConfirmPasswordValid ? 'green' : 'red' }}>
              {confirmPasswordMessage}
            </span>
          </div>
        </div>

        <div className="button-group">
          <button type="submit">비밀번호 변경</button>
        </div>
      </form>
    </div>
  );
};

export default ChangePasswordPage;
