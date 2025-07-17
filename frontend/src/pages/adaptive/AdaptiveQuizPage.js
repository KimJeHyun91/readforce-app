import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';
import './AdaptiveQuizPage.css';
import clockImg from '../../assets/image/clock.png';

const AdaptiveQuizPage = () => {
  const navigate = useNavigate();
  const [quiz, setQuiz] = useState(null);
  const [selectedIndex, setSelectedIndex] = useState(null);
  const [notFound, setNotFound] = useState(false);

  const [startTime, setStartTime] = useState(Date.now());
  const [elapsedSeconds, setElapsedSeconds] = useState(0);
  const [isWaiting, setIsWaiting] = useState(true);

  const formatTime = (seconds) => {
    const m = String(Math.floor(seconds / 60)).padStart(2, "0");
    const s = String(seconds % 60).padStart(2, "0");
    return `${m}:${s}`;
  };

  useEffect(() => {
    const fetchQuiz = async () => {
      try {
        const res = await api.get('/recommend/get-recommend?language=KOREAN');
        const data = res.data;

        if (!data || !data.question || !data.choiceList) {
          setNotFound(true);
          return;
        }

        setQuiz(data);
        setStartTime(Date.now());
        setSelectedIndex(null);
      } catch (err) {
        console.error("API 통신 오류:", err);
        setNotFound(true);
      }
    };

    fetchQuiz();
  }, []);

  useEffect(() => {
    if (!quiz?.questionNo) return;

    setIsWaiting(true);
    const newStart = Date.now();
    setStartTime(newStart);
    setElapsedSeconds(0);

    const timer = setInterval(() => {
      const secondsPassed = Math.floor((Date.now() - newStart) / 1000);
      setElapsedSeconds(secondsPassed);
    }, 1000);

    const waitTimer = setTimeout(() => {
      setIsWaiting(false);
    }, 10000);

    return () => {
      clearInterval(timer);
      clearTimeout(waitTimer);
    };
  }, [quiz?.questionNo]);

  const handleSubmit = async () => {
    if (selectedIndex === null) return;

    const solvingTime = Math.max(
      10,
      Math.floor((Date.now() - startTime) / 1000)
    );

    const payload = {
      selectedIndex,
      questionSolvingTime: solvingTime,
      questionNo: quiz.questionNo,
      isFavorit: false,
    };

    try {
      const res = await api.post('/learning/save-multiple-choice', payload);

      const isCorrect = quiz.choiceList[selectedIndex]?.isCorrect;
      const correctChoice = quiz.choiceList.find(choice => choice.isCorrect);
      const correctChoiceIndex = correctChoice?.choiceIndex ?? -1;
      const correctContent = correctChoice?.content ?? "";
      const explanation =
        correctChoice?.explanation ?? "해설이 제공되지 않았습니다.";

      navigate("/adaptive-learning/result", {
        state: {
          isCorrect,
          explanation,
          correctChoiceIndex,
          correctContent,
          next: "/adaptive-learning/start",
        },
      });
    } catch (err) {
      console.error("제출 실패:", err);
    }
  };

  if (notFound) {
    return (
      <div className="page-container quiz-notfound-container">
        <div className="warning">제공된 문제가 없습니다.</div>
        <div className="description">다른 문제를 시도해 주세요.</div>
        <button className="go-back-button" onClick={() => navigate(-1)}>
          🔙 돌아가기
        </button>
      </div>
    );
  }

  if (!quiz) return <div className="page-container">로딩 중...</div>;

  return (
    <div className="quiz-layout">
      <div className="quiz-passage">
        <h3 className="passage-title">🤖 적응력 문제</h3>
        <p className="passage-text">{quiz.content || "※ 추가 지문 없음"}</p>
      </div>

      <div className="quiz-box">
        <div className="quiz-header">
          <h4 className="question-heading">💡 문제</h4>
          <div className="quiz-timer">
            <img src={clockImg} alt="clock" className="clock-icon" />
            {formatTime(elapsedSeconds)}
          </div>
        </div>

        <p className="question-text">{quiz.question}</p>

        {isWaiting && (
          <div className="wait-message">
            ⏳ {Math.max(0, 10 - elapsedSeconds)}초 후에 선택할 수 있어요...
          </div>
        )}

        <div className="quiz-options">
          {quiz.choiceList.map((choice, idx) => (
            <button
              key={idx}
              className={`quiz-option ${
                selectedIndex === idx ? "selected" : ""
              }`}
              disabled={isWaiting}
              onClick={() => setSelectedIndex(idx)}
            >
              {String.fromCharCode(65 + idx)}. {choice.content}
            </button>
          ))}
        </div>

        <div className="quiz-button-container">
          <button
            className="submit-button"
            disabled={selectedIndex === null}
            onClick={handleSubmit}
          >
            정답 제출
          </button>
        </div>
      </div>
    </div>
  );
};

export default AdaptiveQuizPage;
