import { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../api/axiosInstance";

export default function Authcallback() {
  const navigate = useNavigate();
  const hasRunRef = useRef(false);

  useEffect(() => {
    if (hasRunRef.current) return;
    hasRunRef.current = true;

    const urlParams = new URLSearchParams(window.location.search);
    const temporalToken = urlParams.get("TEMPORAL_TOKEN");

    const handleNewUser = () => {
      if (temporalToken) {
        localStorage.setItem("temporal_token", temporalToken);
        navigate("/social-sign-up", { state: { temporalToken } });
      } else {
        alert("회원가입에 실패했습니다.");
        navigate("/login");
      }
    };

    const handleExistingUser = (data) => {
      const { ACCESS_TOKEN, REFRESH_TOKEN, NICKNAME, SOCIAL_PROVIDER } = data;

      if (!ACCESS_TOKEN || !REFRESH_TOKEN || !NICKNAME) {
        handleNewUser();
        return;
      }

      localStorage.setItem("token", ACCESS_TOKEN);
      localStorage.setItem("refresh_token", REFRESH_TOKEN);
      localStorage.setItem("nickname", NICKNAME);
      localStorage.setItem("social_provider", SOCIAL_PROVIDER || "");

      window.dispatchEvent(new Event("nicknameUpdated"));

      navigate("/");
    };

    if (!temporalToken) {
      alert("로그인에 실패했습니다.");
      navigate("/login");
      return;
    }

    axiosInstance
      .post(
        "/authentication/get-tokens",
        new URLSearchParams({ temporalToken }),
        {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
        }
      )
      .then((res) => {
        handleExistingUser(res.data);
      })
      .catch(() => {
        handleNewUser();
      });
  }, [navigate]);

  return <div>로그인 처리 중입니다... 잠시만 기다려 주세요.</div>;
}
