import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import "./login.css";
import kakaoIcon from "../../assets/image/kakao.png";
// import naverIcon from '../../assets/image/naver.png';
import googleIcon from "../../assets/image/google.png";
import axiosInstance from "../../api/axiosInstance";

export default function Login() {
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      // =================================================================
      // 이 부분을 수정합니다! (경로에 /api 추가)
      // =================================================================
      const res = await axiosInstance.post("/authentication/sign-in", {
        email: id,
        password,
      });

      const data = res.data;

      const token = data.ACCESS_TOKEN || data.accessToken;
      const refreshToken = data.REFRESH_TOKEN || data.refreshToken;
      const nickname = data.NICKNAME || data.nickname;

      if (!token) {
        setError("서버에서 토큰을 받지 못했습니다.");
        return;
      }

      const { sub: email } = jwtDecode(token);

      localStorage.setItem("token", token);
      localStorage.setItem("refresh_token", refreshToken);
      localStorage.setItem("nickname", nickname);
      localStorage.setItem("email", email);

      window.dispatchEvent(new Event("nicknameUpdated"));
      navigate("/");
    } catch (err) {
      const msg = err.response?.data?.message || "로그인에 실패했습니다.";
      setError(msg);
    }
  };

  const handleSocialLogin = (provider) => {
    // =================================================================
    // 이 부분을 수정합니다! (경로에 /api 추가)
    // =================================================================
    const socialLoginUrl = `https://readforce-app-backend-production.up.railway.app/oauth2/authorization/${provider}`;
    window.location.href = socialLoginUrl;
  };

  return (
    <div>
      <div className="page-container">
        <div className="login-wrapper">
          <h2 className="login-title">로그인</h2>
          <form className="login-form" onSubmit={handleLogin}>
            <div className="form-group">
              <label htmlFor="userId">ID</label>
              <input
                id="userId"
                type="text"
                value={id}
                onChange={(e) => setId(e.target.value)}
                placeholder="이메일"
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="userPw">PW</label>
              <input
                id="userPw"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="비밀번호"
                required
              />
            </div>

            <div className="login-links">
              <span onClick={() => navigate("/findpassword")}>
                비밀번호 재설정
              </span>
            </div>

            <div className="social-login">
              <button
                type="button"
                className="social-btn"
                onClick={() => handleSocialLogin('kakao')}
              >
                <img src={kakaoIcon} alt="카카오" />
              </button>
              {/* <button type="button" ... /> */}
              <button
                type="button"
                className="social-btn"
                onClick={() => handleSocialLogin('google')}
              >
                <img src={googleIcon} alt="구글" />
              </button>
            </div>

            {error && <p className="error-message">{error}</p>}

            <button type="submit" className="login-btn">
              로그인
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
