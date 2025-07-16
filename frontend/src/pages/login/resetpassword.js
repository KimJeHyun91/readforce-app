import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import './resetpassword.css';

export default function ResetPassword() {
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const token = queryParams.get('token');

  const handleResetPassword = async (e) => {
    e.preventDefault();

    if (!token) {
      alert('유효하지 않은 접근입니다.');
      return;
    }

    if (newPassword !== confirmPassword) {
      alert('비밀번호가 서로 다릅니다.');
      return;
    }

    try {
      const response = await fetch('/member/password-reset-from-link', {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          temporalToken: token,
          newPassword: newPassword,
        }),
      });

      if (response.ok) {
        alert('비밀번호가 성공적으로 재설정되었습니다.');
        window.location.href = '/login';
      } else {
        const data = await response.json();
        alert(`재설정 실패: ${data.message || '오류 발생'}`);
      }
    } catch (error) {
      console.error('비밀번호 재설정 에러:', error);
      alert('서버 오류로 비밀번호를 재설정할 수 없습니다.');
    }
  };

  return (
    <div className="page-container">
      <div className="reset-password-wrapper">
        <h2 className="reset-password-title">비밀번호 재설정</h2>
        <form className="reset-password-form" onSubmit={handleResetPassword}>
          <div className="form-group">
            <label htmlFor="newPassword">새 비밀번호</label>
            <input
              id="newPassword"
              type="password"
              value={newPassword}
              placeholder="새 비밀번호"
              onChange={(e) => setNewPassword(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">비밀번호 확인</label>
            <input
              id="confirmPassword"
              type="password"
              value={confirmPassword}
              placeholder="비밀번호 확인"
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="reset-btn">
            비밀번호 재설정
          </button>
        </form>
      </div>
    </div>
  );
}
