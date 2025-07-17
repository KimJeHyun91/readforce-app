import './signupchoice.css';
import { useNavigate } from 'react-router-dom';
import kakaoIcon from '../../assets/image/kakao.png';
// import naverIcon from '../../assets/image/naver.png';
import googleIcon from '../../assets/image/google.png';
import emailIcon from '../../assets/image/email.png';

const SignupChoice = () => {
  const navigate = useNavigate();

  return (
    <div className="page-container">
    <div className="signup-page">
      <h2 className="signup-title">Read Force 시작하기</h2>

      <div className="signup-card-list">
        <button className="signup-card kakao" onClick={() => window.location.href = process.env.REACT_APP_API_URL+"/oauth2/authorization/kakao"}>
          <img src={kakaoIcon} alt="카카오" />
          <span>카카오로 가입하기</span>
          <span className="arrow">›</span>
        </button>

        {/* <button className="signup-card naver" onClick={() => window.location.href = "http://localhost:8080/oauth2/authorization/naver"}>
          <img src={naverIcon} alt="네이버" />
          <span>네이버로 가입하기</span>
          <span className="arrow">›</span>
        </button> */}

        <button className="signup-card google" onClick={() => window.location.href = process.env.REACT_APP_API_URL+"/oauth2/authorization/google"}>
          <img src={googleIcon} alt="구글" />
          <span>Google로 가입하기</span>
          <span className="arrow">›</span>
        </button>

        <button className="signup-card email" onClick={() => navigate("/signup")}>
        <img src={emailIcon} alt="구글" />
          <span>이메일로 가입하기</span>
          <span className="arrow">›</span>
        </button>
      </div>
    </div>
    </div>
  );
};

export default SignupChoice;
