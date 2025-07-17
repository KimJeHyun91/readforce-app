import React, { useEffect, useState } from "react";
import "./MyPage.css";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import axiosInstance from "../../api/axiosInstance";
import { useNavigate } from "react-router-dom";
import defaultProfileImage from "../../assets/image/default-profile.png";

const MyPage = () => {
  const [nickname, setNickname] = useState("");
  const [profileImageUrl, setProfileImageUrl] = useState(defaultProfileImage);
  const [attendanceDates, setAttendanceDates] = useState([]);
  const [summary, setSummary] = useState({
    total: 0,
    monthlyRate: 0,
    streak: 0,
  });
  const [correctRate, setCorrectRate] = useState(0);
  const [todaySolvedCount, setTodaySolvedCount] = useState(0);
  const [totalLearning, setTotalLearning] = useState([]);
  const [totalIncorrect, setTotalIncorrect] = useState([]);
  const [todayLearning, setTodayLearning] = useState([]);
  const [todayIncorrect, setTodayIncorrect] = useState([]);
  const [favoritePassages, setFavoritePassages] = useState([]);
  const [selectedNoteType, setSelectedNoteType] = useState(null);
  const navigate = useNavigate();
  const isLoggedIn = !!localStorage.getItem("token");

  const handleOpenModal = (type) => setSelectedNoteType(type);
  const handleCloseModal = () => setSelectedNoteType(null);

  useEffect(() => {
    if (isLoggedIn) setNickname(localStorage.getItem("nickname") || "사용자");
  }, [isLoggedIn]);

  useEffect(() => {
    if (!isLoggedIn) return;

    let imageUrl = null;

    axiosInstance
      .get("/file/get-profile-image", { responseType: "blob" })
      .then((res) => {
        imageUrl = URL.createObjectURL(res.data);
        setProfileImageUrl(imageUrl);
      })
      .catch(() => {

        setProfileImageUrl(defaultProfileImage);
      });

    return () => {
      if (imageUrl) URL.revokeObjectURL(imageUrl);
    };
  }, [isLoggedIn]);

  useEffect(() => {
    axiosInstance
      .get("/attendance/get-attendance-date-list")
      .then((res) => {
        const dates = (res.data || []).map((d) => new Date(d));
        setAttendanceDates(dates);
        const today = new Date();
        const thisMonthDates = dates.filter(
          (d) =>
            d.getFullYear() === today.getFullYear() &&
            d.getMonth() === today.getMonth()
        );
        const monthlyRate = Math.round(
          (thisMonthDates.length / today.getDate()) * 100
        );
        const getStreak = (dates) => {
          const sorted = [...dates]
            .map((d) => new Date(d.getFullYear(), d.getMonth(), d.getDate()))
            .sort((a, b) => b - a);
          let streak = 0;
          let current = new Date();
          for (const date of sorted) {
            if (date.toDateString() === current.toDateString()) {
              streak++;
              current.setDate(current.getDate() - 1);
            } else if (
              date.toDateString() ===
              new Date(
                current.getFullYear(),
                current.getMonth(),
                current.getDate() - 1
              ).toDateString()
            ) {
              streak++;
              current.setDate(current.getDate() - 1);
            } else break;
          }
          return streak;
        };
        setSummary({
          total: dates.length,
          monthlyRate,
          streak: getStreak(dates),
        });
      })

  }, []);

  useEffect(() => {
    axiosInstance
      .get("/result/get-overall-correct-answer-rate")
      .then((res) => {
        const rate = res.data?.OVERALL_CORRECT_ANSWER_RATE;
        if (typeof rate === "number") setCorrectRate(rate);
      })

  }, []);

  useEffect(() => {
    axiosInstance
      .get("/result/get-today-solved-question-count")
      .then((res) => {
        const count = res.data?.TODAY_SOLVED_QUESTION_COUNT;
        if (typeof count === "number") setTodaySolvedCount(count);
      })

  }, []);

  useEffect(() => {
    const fetchLearningData = async () => {
      try {
        const results = await Promise.allSettled([
          axiosInstance.get("/learning/get-total-learning"),
          axiosInstance.get("/learning/get-total-incorrect-learning"),
          axiosInstance.get("/learning/get-today-learning"),
          axiosInstance.get("/learning/get-today-incorrect-learning"),
          axiosInstance.get("/passage/get-favorite-passage-list"),
        ]);

        const [total, incorrect, today, todayWrong, fav] = results;
        setTotalLearning(total.status === "fulfilled" ? total.value.data : []);
        setTotalIncorrect(
          incorrect.status === "fulfilled" ? incorrect.value.data : []
        );
        setTodayLearning(today.status === "fulfilled" ? today.value.data : []);
        setTodayIncorrect(
          todayWrong.status === "fulfilled" ? todayWrong.value.data : []
        );
        setFavoritePassages(
          fav.status === "fulfilled" ? fav.value.data : []
        );

      } catch (e) {

      }
    };

    fetchLearningData();
  }, []);

  const noteTypeMap = {
    total: { label: "전체 푼 문제", list: totalLearning },
    totalIncorrect: { label: "전체 틀린 문제", list: totalIncorrect },
    today: { label: "오늘의 푼 문제", list: todayLearning },
    incorrect: { label: "오늘의 틀린 문제", list: todayIncorrect },
    favorite: { label: "즐겨찾기 지문", list: favoritePassages },
  };

  const handleQuizClick = (item) => {
    if (!item?.passageNo || !item?.title) return;
    navigate(`/questionpage/${item.passageNo}`, {
      state: {
        passage: {
          passageNo: item.passageNo,
          title: item.title ?? "",
          content: item.content ?? "",
          author: item.author ?? "",
          language: item.language ?? "KOREAN",
          category: item.category ?? "NEWS",
        },
      },
    });
  };

  return (
    <div className="mypage-container">
      <div className="top-section">
        <div className="left-top">
          <img
            src={profileImageUrl}
            alt="프로필"
            className="profile-img"
            onError={(e) => {
              e.target.onerror = null;
              e.target.src = defaultProfileImage;
            }}
          />
          <h3 className="nickname">{nickname} 님</h3>
        </div>
        <div className="calendar-section">
          <h4>출석 현황</h4>
          <div className="calendar-summary">
            <div className="summary-row">
              <div className="summary-title">총 출석일</div>
              <div className="summary-title">이번 달 출석률</div>
              <div className="summary-title">연속 출석</div>
            </div>
            <div className="summary-row">
              <div className="summary-value">{summary.total}일</div>
              <div className="summary-value">{summary.monthlyRate}%</div>
              <div className="summary-value">{summary.streak}일</div>
            </div>
          </div>
          <Calendar
            calendarType="gregory"
            next2Label={null}
            prev2Label={null}
            minDetail="month"
            maxDetail="month"
            showNeighboringMonth={false}
            tileClassName={({ date, view }) => {
              if (view === "month") {
                const isAttendance = attendanceDates.some(
                  (att) => att.toDateString() === date.toDateString()
                );
                if (isAttendance) return "attended-day";
                if (date.getDay() === 0) return "sunday";
                if (date.getDay() === 6) return "saturday";
              }
              return null;
            }}
          />
        </div>
      </div>

      <div className="bottom-section">
        <div className="learning-status-box">
          <h4>학습 현황</h4>
          <div className="summary-cards">
            <div className="summary-card">
              <div className="summary-title">전체 정답률</div>
              <div className="summary-value">{correctRate}%</div>
            </div>
            <div className="summary-card">
              <div className="summary-title">오늘 푼 문제</div>
              <div className="summary-value">{todaySolvedCount}문제</div>
            </div>
            <div className="summary-card">
              <div className="summary-title">연속 학습일</div>
              <div className="summary-value">{summary.streak}일</div>
            </div>
          </div>
        </div>

        <div className="learning-note-box">
          <h4>학습 노트</h4>
          <div className="summary-cards">
            {Object.entries(noteTypeMap).map(([key, { label, list }]) => (
              <div
                key={key}
                className="summary-card"
                onClick={() => handleOpenModal(key)}
              >
                <div className="summary-title">{label}</div>
                <div className="summary-value">
                  {list.length}
                  {key === "favorite" ? "개" : "문제"}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {selectedNoteType && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h4>{noteTypeMap[selectedNoteType].label}</h4>
            <ul style={{ maxHeight: "50%", overflowY: "auto", padding: 0 }}>
              {noteTypeMap[selectedNoteType].list.map((item) => (
                <li
                  key={item.passageNo || item.questionNo}
                  onClick={() => handleQuizClick(item)}
                  style={{
                    marginBottom: "12px",
                    paddingBottom: "8px",
                    borderBottom: "1px solid #eee",
                    cursor: "pointer",
                  }}
                >
                  <div>
                    <strong>{item.title}</strong>
                  </div>
                  <div style={{ fontSize: "0.9rem", color: "#666" }}>
                    {new Date(item.createdAt).toLocaleString()}
                  </div>
                  {"isCorrect" in item && (
                    <div
                      style={{
                        marginTop: "4px",
                        fontWeight: "bold",
                        color: item.isCorrect ? "#22c55e" : "#ef4444",
                      }}
                    >
                      {item.isCorrect ? "정답" : "오답"}
                    </div>
                  )}
                </li>
              ))}
            </ul>
            <div style={{ display: "flex", justifyContent: "flex-end" }}>
              <button
                onClick={handleCloseModal}
                style={{
                  padding: "8px 16px",
                  backgroundColor: "#f3f4f6",
                  border: "1px solid #ccc",
                  borderRadius: "6px",
                  cursor: "pointer",
                  fontWeight: "bold",
                }}
              >
                닫기
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyPage;
