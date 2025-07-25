import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "./signupwithemail.css";
import axiosInstance from "../../api/axiosInstance";

const EmailVerifyPage = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const [code, setCode] = useState("");
  const [error, setError] = useState("");

  const queryParams = new URLSearchParams(location.search);
  const email = queryParams.get("email");

  const handleVerify = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await axiosInstance.post(
        "/email/verify-verification-code-for-sign-up",
        {
          email,
          verificationCode: code,
        }
      );

      alert("E-mail 인증이 완료되었습니다.");
      navigate(`/signup/signupcompletepage?email=${encodeURIComponent(email)}`);
    } catch (err) {
      const msg = err.response?.data?.message || "인증에 실패했습니다.";
      setError(msg);
    }
  };

  return (
    <div className="signup-wrapper">
      <h2 className="signup-title">E-mail 인증</h2>
      <form className="signup-form" onSubmit={handleVerify}>
        <div className="form-group">
          <label className="labe112">인증번호 입력</label>
          <input
            type="text"
            placeholder="123456"
            value={code}
            onChange={(e) => setCode(e.target.value)}
            required
          />
        </div>
        <button className="submit-btn">확인</button>
        {error && <p className="error-message">{error}</p>}
      </form>
    </div>
  );
};

export default EmailVerifyPage;
