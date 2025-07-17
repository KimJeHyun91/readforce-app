import './signupwithemail.css';
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import Modal from '../../components/modal';
// 우리가 설정한 axios 인스턴스를 가져옵니다.
import api from '../../api/axiosInstance'; 

const SignupWithEmail = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [emailMessage, setEmailMessage] = useState('');
  const [isEmailValid, setIsEmailValid] = useState(null);

  const [modal, setModal] = useState({ open: false, title: '', message: '' });

  const [message, setMessage] = useState('');

  const checkEmailDuplicate = async (value) => {
    try {
      // fetch 대신 api.get을 사용합니다. baseURL이 자동으로 적용됩니다.
      // 백엔드 API 경로에 맞게 '/api/members/email-check'로 수정했습니다.
      const response = await api.get(`/member/email-check?email=${value}`);
      if (response.status === 200) {
        setEmailMessage('사용 가능한 이메일입니다.');
        setIsEmailValid(true);
      }
    } catch (err) {
      // axios는 오류 응답을 err.response.data로 전달합니다.
      const errorMessage = err.response?.data?.message || '이미 사용 중인 계정입니다.';
      setEmailMessage(errorMessage);
      setIsEmailValid(false);
    }
  };

  useEffect(() => {
    const delay = setTimeout(() => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (emailRegex.test(email)) {
        checkEmailDuplicate(email);
      } else if (email !== '') {
        setEmailMessage('올바른 이메일 형식이 아닙니다.');
        setIsEmailValid(false);
      } else {
        setEmailMessage('');
        setIsEmailValid(null);
      }
    }, 400);

    return () => clearTimeout(delay);
  }, [email]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');

    try {
      // fetch 대신 api.post를 사용합니다.
      // 백엔드 API 경로에 맞게 '/api/email/send-verification-code-for-sign-up'로 수정했습니다.
      const response = await api.post('/email/send-verification-code-for-sign-up', { email });

      if (response.status !== 200) {
        throw new Error(response.data.message || '인증 이메일 전송 실패');
      }

      alert("입력한 E-Mail 주소로 인증번호가 전송되었습니다.");
      navigate(`/signup/emailverifypage?email=${encodeURIComponent(email)}`);
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.message;
      setMessage(`오류: ${errorMessage}`);
      // 사용자에게 오류를 보여주기 위해 모달을 사용할 수 있습니다.
      setModal({ open: true, title: '오류', message: errorMessage });
    }
  };

  return (
    <div className="page-container">
    <div className="signup-wrapper">
      <h2 className="signup-title">E-Mail 입력</h2>
      <form className="signup-form" onSubmit={handleSubmit}>
        <div className="form-group input-with-message">
          <input
            type="email"
            placeholder="example@email.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <span
            className="validation-message"
            style={{
              color:
                isEmailValid === null
                  ? 'inherit'
                  : isEmailValid
                    ? 'green'
                    : 'red',
              fontSize: '0.85rem',
            }}
          >
            {emailMessage}
          </span>
        </div>
        <button className="submit-btn" disabled={!isEmailValid}>
          E-Mail 인증
        </button>
      </form>
      {modal.open && (
        <Modal
          title={modal.title}
          message={modal.message}
          onClose={() => setModal({ open: false, title: '', message: '' })}
        />
      )}
    </div>
    </div>
  );
};

export default SignupWithEmail;
