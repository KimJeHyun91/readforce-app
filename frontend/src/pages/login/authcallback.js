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
    // 백엔드가 보내주는 토큰 이름이 TEMPORAL_TOKEN이 아닐 수 있으므로, 두 가지 모두 확인합니다.
    const temporalToken = urlParams.get("TEMPORAL_TOKEN") || urlParams.get("temporalToken");

    // 신규 회원을 회원가입 페이지로 보내는 함수
    const handleNewUser = () => {
      if (temporalToken) {
        // 회원가입 페이지로 임시 토큰을 전달합니다.
        navigate("/social-sign-up", { state: { temporalToken } });
      } else {
        alert("회원가입에 필요한 정보를 받지 못했습니다.");
        navigate("/login");
      }
    };

    // 기존 회원을 로그인 처리하고 홈으로 보내는 함수
    const handleExistingUser = (data) => {
      const { ACCESS_TOKEN, REFRESH_TOKEN, NICKNAME, SOCIAL_PROVIDER } = data;

      // ★★★ 핵심 수정 부분 ★★★
      // 백엔드 응답에 NICKNAME이 없거나, 비어있거나, null이면 신규 회원으로 간주합니다.
      if (!NICKNAME) {
        handleNewUser();
        return;
      }

      // 모든 정보가 있으면 기존 회원으로 로그인 처리
      localStorage.setItem("token", ACCESS_TOKEN);
      localStorage.setItem("refresh_token", REFRESH_TOKEN);
      localStorage.setItem("nickname", NICKNAME);
      localStorage.setItem("social_provider", SOCIAL_PROVIDER || "");

      window.dispatchEvent(new Event("nicknameUpdated"));
      navigate("/");
    };

    if (!temporalToken) {
      alert("로그인에 필요한 정보를 받지 못했습니다.");
      navigate("/login");
      return;
    }

    // 임시 토큰으로 최종 토큰/사용자 정보 요청
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
        // 성공 시, 응답 내용을 바탕으로 신규/기존 회원인지 판단
        handleExistingUser(res.data);
      })
      .catch(() => {
        // API 요청 자체가 실패하면 신규 회원으로 간주
        handleNewUser();
      });
  }, [navigate]);

  return <div>로그인 처리 중입니다... 잠시만 기다려 주세요.</div>;
}
