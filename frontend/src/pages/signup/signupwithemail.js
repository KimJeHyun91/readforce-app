import './signupwithemail.css';
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import Modal from '../../components/modal';

const SignupWithEmail = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [emailMessage, setEmailMessage] = useState('');
  const [isEmailValid, setIsEmailValid] = useState(null);

  const [modal, setModal] = useState({ open: false, title: '', message: '' });

  const [message, setMessage] = useState('');

  const checkEmailDuplicate = async (value) => {
    try {
      const response = await fetch(`/member/email-check?email=${value}`);
      if (response.ok) {
        setEmailMessage('사용 가능한 이메일입니다.');
        setIsEmailValid(true);
      } else {
        const data = await response.json();
        setEmailMessage(data.message || '이미 사용 중인 계정입니다.');
        setIsEmailValid(false);
      }
    } catch (err) {
      setEmailMessage('이메일 확인 중 오류 발생');
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
      const response = await fetch('/email/send-verification-code-for-sign-up', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || '인증 이메일 전송 실패');
      }

      alert("입력한 E-Mail 주소로 인증번호가 전송되었습니다.");
      navigate(`/signup/emailverifypage?email=${encodeURIComponent(email)}`);
    } catch (error) {
      setMessage(`오류: ${error.message}`);
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
      {/* {message && <p className="error-message">{message}</p>} */}
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