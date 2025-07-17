import './signupchoice.css';
import { useNavigate } from 'react-router-dom';
import kakaoIcon from '../../assets/image/kakao.png';
import googleIcon from '../../assets/image/google.png';
import emailIcon from '../../assets/image/email.png';

const SignupChoice = () => {
  const navigate = useNavigate();

  // 환경 변수에서 백엔드 기본 URL을 가져옵니다.
  // 만약 환경 변수가 없다면, 기본값으로 로컬 주소를 사용합니다. (개발 환경을 위해)
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

  /**
   * 소셜 로그인 URL을 생성하고 해당 주소로 이동하는 함수입니다.
   * @param {string} provider - 소셜 로그인 제공자 (예: 'kakao', 'google')
   */
  const handleSocialLogin = (provider) => {
    // 환경 변수를 사용하여 동적으로 URL을 생성합니다.
    const socialLoginUrl = `${API_BASE_URL}/oauth2/authorization/${provider}`;
    window.location.href = socialLoginUrl;
  };

  return (
    <div className="page-container">
      <div className="signup-page">
        <h2 className="signup-title">Read Force 시작하기</h2>

        <div className="signup-card-list">
          {/* onClick 핸들러를 함수 호출로 변경합니다. */}
          <button className="signup-card kakao" onClick={() => handleSocialLogin('kakao')}>
            <img src={kakaoIcon} alt="카카오" />
            <span>카카오로 가입하기</span>
          </button>

          <button className="signup-card google" onClick={() => handleSocialLogin('google')}>
            <img src={googleIcon} alt="구글" />
            <span>Google로 가입하기</span>
          </button>
          

          <button className="signup-card email" onClick={() => navigate("/signup")}>
            <img src={emailIcon} alt="이메일" />
            <span>이메일로 가입하기</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignupChoice;
