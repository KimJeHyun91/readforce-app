import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../api/axiosInstance";

export default function Socialsignup() {
  const navigate = useNavigate();
  const [tempToken, setTempToken] = useState("");

  const [nickname, setNickname] = useState("");
  const [nicknameMessage, setNicknameMessage] = useState("");
  const [isNicknameValid, setIsNicknameValid] = useState(null);

  const [birthday, setBirthday] = useState("");
  const [birthdayMessage, setBirthdayMessage] = useState("");
  const [isBirthdayValid, setIsBirthdayValid] = useState(null);

  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const checkNicknameDuplicate = async (nickname) => {
    try {
      await axiosInstance.get(`/member/nickname-check?nickname=${nickname}`);
      return true;
    } catch (err) {
      return false;
    }
  };

  // 닉네임 형식 + 중복 검사
  const validateNickname = async (value) => {
    const nicknameRegex = /^[a-zA-Z가-힣0-9]{2,12}$/;

    if (!nicknameRegex.test(value)) {
      setNicknameMessage("한글/영문/숫자 조합 2~12자만 사용 가능합니다.");
      setIsNicknameValid(false);
      return;
    }

    const isAvailable = await checkNicknameDuplicate(value);
    if (isAvailable) {
      setNicknameMessage("사용 가능한 닉네임입니다.");
      setIsNicknameValid(true);
    } else {
      setNicknameMessage("이미 존재하는 닉네임입니다.");
      setIsNicknameValid(false);
    }
  };

  const validateBirthday = (value) => {
    const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!birthdayRegex.test(value)) {
      setBirthdayMessage("생년월일 8자리를 입력해주세요 (예: 19971104)");
      setIsBirthdayValid(false);
      return;
    }

    const [year, month, day] = value.split("-").map(Number);
    const date = new Date(year, month - 1, day);

    if (
      date.getFullYear() === year &&
      date.getMonth() === month - 1 &&
      date.getDate() === day
    ) {
      setBirthdayMessage("생년월일 입력 완료");
      setIsBirthdayValid(true);
    } else {
      setBirthdayMessage("존재하지 않는 날짜입니다. 다시 확인해주세요.");
      setIsBirthdayValid(false);
    }
  };

  const handleBirthdayChange = (value) => {
    const numeric = value.replace(/\D/g, "").slice(0, 8);
    let formatted = numeric;
    if (numeric.length >= 5) {
      formatted = `${numeric.slice(0, 4)}-${numeric.slice(4, 6)}`;
      if (numeric.length >= 7) {
        formatted += `-${numeric.slice(6, 8)}`;
      }
    }
    setBirthday(formatted);
    validateBirthday(formatted);
  };

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("TEMPORAL_TOKEN");
    if (!token) {
      alert("잘못된 접근입니다.");
      navigate("/login");
    }
    setTempToken(token);
  }, [navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

    const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!birthdayRegex.test(birthday)) {
      setError("생년월일은 YYYY-MM-DD 형식이어야 합니다.");
      return;
    }

    try {
      await axiosInstance.post("/member/social-sign-up", {
        temporalToken: tempToken,
        nickname,
        birthday,
      });

      setMessage(
        "🎉 소셜 회원가입이 완료되었습니다! 로그인 페이지로 이동합니다."
      );
      setTimeout(() => navigate("/login"), 1500);
    } catch (err) {
      const msg = err.response?.data?.message || "회원가입 실패";
      setError(msg);
    }
  };

  return (
    <div className="page-container">
      <div className="signup-wrapper">
        <h2 className="signup-title">추가 정보 입력</h2>
        <form className="signup-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>닉네임</label>
            <div className="input-with-message">
              <input
                type="text"
                placeholder="한글, 영문, 숫자 조합 (2~12자, 특수문자 제외)"
                value={nickname}
                onChange={async (e) => {
                  const value = e.target.value;
                  setNickname(value);
                  if (value.length >= 2) {
                    await validateNickname(value);
                  } else {
                    setNicknameMessage("2자 이상 입력해주세요");
                    setIsNicknameValid(false);
                  }
                }}
                required
              />
              <span
                className="validation-message"
                style={{
                  color:
                    isNicknameValid === null
                      ? "inherit"
                      : isNicknameValid
                      ? "green"
                      : "red",
                  fontSize: "0.85rem",
                }}
              >
                {nicknameMessage}
              </span>
            </div>
          </div>
          <div className="form-group">
            <label>생년월일</label>
            <div className="input-with-message">
              <input
                type="text"
                placeholder="예: 19971104"
                value={birthday}
                onChange={(e) => handleBirthdayChange(e.target.value)}
                required
              />
              <span
                className="validation-message"
                style={{
                  color:
                    isBirthdayValid === null
                      ? "inherit"
                      : isBirthdayValid
                      ? "green"
                      : "red",
                  fontSize: "0.85rem",
                }}
              >
                {birthdayMessage}
              </span>
            </div>
          </div>

          <button className="submit-btn">회원가입 완료</button>
          {error && <p className="error-message">{error}</p>}
          {message && <p className="success-message">{message}</p>}
        </form>
      </div>
    </div>
  );
}
