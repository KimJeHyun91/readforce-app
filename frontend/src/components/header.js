import "./header.css";
import React, { useState, useEffect } from "react";
import { useNavigate, Link, useLocation } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

const Header = () => {
  const [showLangMenu, setShowLangMenu] = useState(false);
  const [selectedLang, setSelectedLang] = useState("한국어");
  const [showUserMenu, setShowUserMenu] = useState(false);
  const [nickname, setNickname] = useState(
    localStorage.getItem("nickname") || "사용자"
  );
  const [showMobileMenu, setShowMobileMenu] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();
  const currentPath = location.pathname;

  const isLoggedIn = !!localStorage.getItem("token");
  const provider = localStorage.getItem("social_provider");

  useEffect(() => {
    if (showMobileMenu) {
      document.body.classList.add("no-scroll");
    } else {
      document.body.classList.remove("no-scroll");
    }
    return () => document.body.classList.remove("no-scroll");
  }, [showMobileMenu]);

  useEffect(() => {
    const updateNickname = () => {
      const storedNickname = localStorage.getItem("nickname");
      setNickname(storedNickname || "사용자");
    };

    window.addEventListener("nicknameUpdated", updateNickname);
    return () => {
      window.removeEventListener("nicknameUpdated", updateNickname);
    };
  }, []);

  const handleLangSelect = (lang) => {
    setSelectedLang(lang);
    setShowLangMenu(false);
  };

  const handleLogout = async () => {
    try {
      const res = await axiosInstance.delete("/authentication/sign-out");
      const result = res.data;
      const kakaoLogoutUrl = result.KAKAO_SIGN_OUT_URL;

      localStorage.clear();
      setShowUserMenu(false);

      if (kakaoLogoutUrl) {
        window.location.href = kakaoLogoutUrl;
      } else {
        navigate("/");
      }
    } catch (error) {
      console.error("로그아웃 중 오류 발생:", error);
      localStorage.clear();
      setShowUserMenu(false);
      navigate("/");
    }
  };

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth >= 768) {
        setShowMobileMenu(false); // 데스크톱으로 전환되면 강제로 닫음
        document.body.classList.remove("no-scroll"); // 스크롤 잠금도 해제
      }
    };

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  useEffect(() => {
    if (showMobileMenu) {
      document.body.classList.add("no-scroll"); // 모바일 메뉴 열릴 때
    } else {
      document.body.classList.remove("no-scroll"); // 닫힐 때 해제
    }
  }, [showMobileMenu]);

  return (
    <header className="header">
      <div className="page-container header-inner">
        <div className="header-left">
          <h1 className="title">
            <a href="/" style={{ textDecoration: "none", color: "inherit" }}>
              리드 <span style={{ color: "#439395" }}>포스</span>
            </a>
          </h1>
        </div>

        <div className="header-center">
          <nav className="nav">
            <Link
              to="/article"
              className={`nav-item ${
                currentPath.startsWith("/article") ? "active" : ""
              }`}
            >
              뉴스
            </Link>
            <Link
              to="/novel"
              className={`nav-item ${
                currentPath.startsWith("/novel") ? "active" : ""
              }`}
            >
              소설
            </Link>
            <Link
              to="/fairytale"
              className={`nav-item ${
                currentPath.startsWith("/fairytale") ? "active" : ""
              }`}
            >
              동화
            </Link>
            <Link
              to="/challenge"
              className={`nav-item ${
                currentPath.startsWith("/challenge") ? "active" : ""
              }`}
            >
              문해력 도전
            </Link>
            <Link
              to="/adaptive-learning"
              className={`nav-item ${
                currentPath.startsWith("/adaptive-learning") ? "active" : ""
              }`}
            >
              적응력 학습
            </Link>
          </nav>
        </div>

        <div
          className="hamburger"
          onClick={() => setShowMobileMenu((prev) => !prev)}
        >
          ☰
        </div>

        <div className="header-right auth-buttons">
          <div className="lang-selector">
            <button
              className="lang-button"
              onClick={() => setShowLangMenu(!showLangMenu)}
            >
              {selectedLang} ▼
            </button>
            {showLangMenu && (
              <div className="lang-menu">
                <div onClick={() => handleLangSelect("한국어")}>🇰🇷 한국어</div>
                <div onClick={() => handleLangSelect("日本語")}>🇯🇵 日本語</div>
                <div onClick={() => handleLangSelect("English")}>
                  🇺🇸 English
                </div>
              </div>
            )}
          </div>

          {isLoggedIn ? (
            <div className="user-menu-wrapper">
              <button
                className="nickname-button"
                onClick={() => setShowUserMenu(!showUserMenu)}
              >
                <span>{nickname}</span>
                <span style={{ color: "#0d9488" }}>님 ▼</span>
              </button>
              {showUserMenu && (
                <div className="user-dropdown">
                  <div
                    onClick={() => {
                      setShowUserMenu(false);
                      navigate("/mypage");
                    }}
                  >
                    마이페이지
                  </div>
                  <div
                    onClick={() => {
                      setShowUserMenu(false);
                      navigate("/profile-edit");
                    }}
                  >
                    회원정보 수정
                  </div>
                  {(!provider || provider === "") && (
                    <div
                      onClick={() => {
                        setShowUserMenu(false);
                        navigate("/change-password");
                      }}
                    >
                      비밀번호 수정
                    </div>
                  )}
                  {nickname === "관리자" && (
                    <div
                      onClick={() => {
                        setShowUserMenu(false);
                        navigate("/adminpage");
                      }}
                    >
                      관리자 페이지
                    </div>
                  )}
                  <div onClick={handleLogout}>로그아웃</div>
                </div>
              )}
            </div>
          ) : (
            <>
              <button onClick={() => navigate("/login")}>로그인</button>
              <button onClick={() => navigate("/signup/signupchoice")}>
                회원가입
              </button>
            </>
          )}
        </div>
      </div>

      {showMobileMenu && (
        <div className="mobile-menu">
          <div className="menu-group">
            <Link to="/article" onClick={() => setShowMobileMenu(false)}>
              뉴스
            </Link>
            <Link to="/novel" onClick={() => setShowMobileMenu(false)}>
              소설
            </Link>
            <Link to="/fairytale" onClick={() => setShowMobileMenu(false)}>
              동화
            </Link>
            <Link to="/challenge" onClick={() => setShowMobileMenu(false)}>
              문해력도전
            </Link>
            <Link
              to="/adaptive-learning"
              onClick={() => setShowMobileMenu(false)}
            >
              적응형학습
            </Link>
          </div>

          <div className="menu-section-title">계정</div>
          <div className="menu-group">
            {isLoggedIn ? (
              <>
                <div
                  onClick={() => {
                    setShowMobileMenu(false);
                    navigate("/mypage");
                  }}
                >
                  마이페이지
                </div>
                <div
                  onClick={() => {
                    setShowMobileMenu(false);
                    navigate("/profile-edit");
                  }}
                >
                  회원정보 수정
                </div>
                {(!provider || provider === "") && (
                  <div
                    onClick={() => {
                      setShowMobileMenu(false);
                      navigate("/change-password");
                    }}
                  >
                    비밀번호 수정
                  </div>
                )}
                {nickname === "관리자" && (
                  <div
                    onClick={() => {
                      setShowMobileMenu(false);
                      navigate("/adminpage");
                    }}
                  >
                    관리자 페이지
                  </div>
                )}
                <div onClick={handleLogout}>로그아웃</div>
              </>
            ) : (
              <>
                <div
                  onClick={() => {
                    setShowMobileMenu(false);
                    navigate("/login");
                  }}
                >
                  로그인
                </div>
                <div
                  onClick={() => {
                    setShowMobileMenu(false);
                    navigate("/signup/signupchoice");
                  }}
                >
                  회원가입
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </header>
  );
};

export default Header;
