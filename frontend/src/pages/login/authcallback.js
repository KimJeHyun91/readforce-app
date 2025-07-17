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
    const temporalToken = urlParams.get("TEMPORAL_TOKEN") || urlParams.get("temporalToken");

    // 신규 회원을 회원가입 페이지로 보내는 함수
    const handleNewUser = () => {
      if (temporalToken) {
        navigate("/social-sign-up", { state: { temporalToken } });
      } else {
        alert("회원가입에 필요한 정보를 받지 못했습니다.");
        navigate("/login");
      }
    };

    // 응답 데이터를 기반으로 신규/기존 사용자를 판별하는 메인 핸들러
    const processUserAuth = (data) => {
      // ★★★ 핵심 수정 부분 ★★★
      // ACCESS_TOKEN과 NICKNAME이 모두 존재해야만 기존 회원으로 간주합니다.
      if (data && data.ACCESS_TOKEN && data.NICKNAME) {
        // 기존 회원 로그인 처리
        localStorage.setItem("token", data.ACCESS_TOKEN);
        localStorage.setItem("refresh_token", data.REFRESH_TOKEN);
        localStorage.setItem("nickname", data.NICKNAME);
        localStorage.setItem("social_provider", data.SOCIAL_PROVIDER || "");

        window.dispatchEvent(new Event("nicknameUpdated"));
        navigate("/");
      } else {
        // 그 외의 모든 경우는 신규 회원으로 처리
        handleNewUser();
      }
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
        processUserAuth(res.data);
      })
      .catch(() => {
        // API 요청 자체가 실패하면 신규 회원으로 간주
        handleNewUser();
      });
  }, [navigate]);

  return <div>로그인 처리 중입니다... 잠시만 기다려 주세요.</div>;
}
