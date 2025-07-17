import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import "./resetpassword.css";
import axiosInstance from "../../api/axiosInstance";

export default function ResetPassword() {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const token = queryParams.get("token");

  const handleResetPassword = async (e) => {
    e.preventDefault();

    if (!token) {
      alert("유효하지 않은 접근입니다.");
      return;
    }

    if (newPassword !== confirmPassword) {
      alert("비밀번호가 서로 다릅니다.");
      return;
    }

    try {
      await axiosInstance.patch("/member/password-reset-from-link", {
        temporalToken: token,
        newPassword: newPassword,
      });

      alert("비밀번호가 성공적으로 재설정되었습니다.");
      window.location.href = "/login";
    } catch (error) {
      const msg = error.response?.data?.message || "오류 발생";
      alert(`재설정 실패: ${msg}`);
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
