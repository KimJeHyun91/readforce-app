import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import './login.css';
import kakaoIcon from '../../assets/image/kakao.png';
// import naverIcon from '../../assets/image/naver.png';
import googleIcon from '../../assets/image/google.png';

export default function Login() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('/authentication/sign-in', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: id, password }),
      });

      const data = await response.json();
      console.log("로그인 응답 데이터", data);

if (response.ok) {
  // ② 필드명 양쪽 다 시도
  const token        = data.ACCESS_TOKEN  || data.accessToken;
  const refreshToken = data.REFRESH_TOKEN || data.refreshToken;
  const nickname     = data.NICKNAME      || data.nickname;

  if (!token) {
    setError('서버에서 토큰을 받지 못했습니다.');
    return;
  }

  const { sub: email, exp } = jwtDecode(token);

  localStorage.setItem('token', token);
  localStorage.setItem('refresh_token', refreshToken);
  localStorage.setItem('nickname', nickname);
  localStorage.setItem('email', email);

  window.dispatchEvent(new Event("nicknameUpdated"));

  navigate('/'); 
      } else {
        setError(data.message || '로그인에 실패했습니다.');
      }
    } catch (err) {
      console.error('로그인 요청 실패:', err);
      setError('서버 오류가 발생했습니다.');
    }
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
              <span onClick={() => navigate('/findpassword')}>비밀번호 재설정</span>
            </div>

            <div className="social-login">
              <button
                type="button"
                className="social-btn"
                onClick={() => window.location.href = process.env.REACT_APP_API_URL+"/oauth2/authorization/kakao"}
              >
                <img src={kakaoIcon} alt="카카오" />
              </button>
              {/* <button
                type="button"
                className="social-btn"
                onClick={() => window.location.href = process.env.REACT_APP_API_URL+"/oauth2/authorization/naver"}
              >
                <img src={naverIcon} alt="네이버" />
              </button> */}
              <button
                type="button"
                className="social-btn"
                onClick={() => window.location.href = process.env.REACT_APP_API_URL+"/oauth2/authorization/google"}
              >
                <img src={googleIcon} alt="구글" />
              </button>
            </div>

            {error && <p className="error-message">{error}</p>}

            <button type="submit" className="login-btn">로그인</button>
          </form>
        </div>
      </div>
    </div>
  );
}