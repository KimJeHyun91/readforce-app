import "./signupwithemail.css";
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import Modal from "../../components/modal";
import axiosInstance from "../../api/axiosInstance";

const SignupWithEmail = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [emailMessage, setEmailMessage] = useState("");
  const [isEmailValid, setIsEmailValid] = useState(null);

  const [modal, setModal] = useState({ open: false, title: "", message: "" });
  const [message, setMessage] = useState("");

  const checkEmailDuplicate = async (value) => {
    try {
      await axiosInstance.get(`/member/email-check?email=${value}`);
      setEmailMessage("사용 가능한 이메일입니다.");
      setIsEmailValid(true);
    } catch (err) {
      const msg = err.response?.data?.message || "이미 사용 중인 계정입니다.";
      setEmailMessage(msg);
      setIsEmailValid(false);
    }
  };

  useEffect(() => {
    const delay = setTimeout(() => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (emailRegex.test(email)) {
        checkEmailDuplicate(email);
      } else if (email !== "") {
        setEmailMessage("올바른 이메일 형식이 아닙니다.");
        setIsEmailValid(false);
      } else {
        setEmailMessage("");
        setIsEmailValid(null);
      }
    }, 400);

    return () => clearTimeout(delay);
  }, [email]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      await axiosInstance.post("/email/send-verification-code-for-sign-up", {
        email,
      });

      alert("입력한 E-Mail 주소로 인증번호가 전송되었습니다.");
      navigate(`/signup/emailverifypage?email=${encodeURIComponent(email)}`);
    } catch (error) {
      const msg = error.response?.data?.message || "인증 이메일 전송 실패";
      setMessage(`오류: ${msg}`);
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
                    ? "inherit"
                    : isEmailValid
                    ? "green"
                    : "red",
                fontSize: "0.85rem",
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
            onClose={() => setModal({ open: false, title: "", message: "" })}
          />
        )}
      </div>
    </div>
  );
};

export default SignupWithEmail;
