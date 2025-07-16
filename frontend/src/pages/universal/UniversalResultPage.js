import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './css/UniversalResultPage.css';
import clockImg from '../../assets/image/clock.png';
import correctAnim from '../../assets/correct.json';
import incorrectAnim from '../../assets/incorrect.json';
import Lottie from 'lottie-react';

const UniversalResultPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [showExplanationIndex, setShowExplanationIndex] = useState(null);

  const answers = location.state?.answers || [];
  const passage = location.state?.passage || {};
  const quizList = location.state?.quizList || [];
  const category = location.state?.category || 'NEWS';

  const getBackPath = () => {
    switch (category.trim()) {
      case 'NEWS':
        return '/article';
      case 'NOVEL':
        return '/novel';
      case 'FAIRY_TALE':
        return '/fairytale';
      default:
        return '/article';
    }
  };

  const correctCount = answers.reduce((acc, ans) => {
    const question = quizList.find(q => q.questionNo === ans.questionNo);
    const selected = question?.choiceList[ans.selectedIndex];
    return acc + (selected?.isCorrect ? 1 : 0);
  }, 0);

  const formatSeconds = (seconds) => {
    const mins = String(Math.floor(seconds / 60)).padStart(2, '0');
    const secs = String(seconds % 60).padStart(2, '0');
    return `${mins}:${secs}`;
  };

  return (
    <div className="ArticleResult-wrapper">
      <div className="ArticleResult-card">
        <Lottie
          animationData={correctCount === answers.length ? correctAnim : incorrectAnim}
          loop={false}
          style={{ width: 90, height: 90, margin: '0 auto' }}
        />
        <h2>{correctCount === answers.length ? '모두 정답입니다!' : `${correctCount}개 맞혔어요!`}</h2>
        <p className="ArticleResult-subtext">
          총 {answers.length}문제 중 {correctCount}개 정답, {answers.length - correctCount}개 오답
        </p>

        <div className="ArticleResult-buttons">
          <button onClick={() => navigate(-1)}>다시 도전하기</button>
          <button onClick={() => navigate(getBackPath())}>그만하기</button>
        </div>

        <div className="ArticleResult-explanation">
          <h3>문제별 해설</h3>
          {answers.map((ans, idx) => {
            const question = quizList.find(q => q.questionNo === ans.questionNo);
            const selected = question?.choiceList[ans.selectedIndex];
            const correctChoice = question?.choiceList.find(choice => choice.isCorrect);
            const isCorrect = selected?.isCorrect;

            return (
              <div key={idx} className="explanation-item">
                <div className="explanation-header">
                  <p><strong>문제 {idx + 1}:</strong> {question?.question || '문제를 찾을 수 없습니다.'}</p>
                  <div className="solving-time-right">
                    <img src={clockImg} alt="clock" className="clock-icon" />
                    <span>{formatSeconds(ans.questionSolvingTime ?? 0)}</span>
                  </div>
                </div>
                <p className={isCorrect ? 'correct' : 'incorrect'}>
                  {isCorrect ? '정답' : '오답'} | 선택한 보기 : {selected?.content || '없음'}
                </p>
                <button
                  className="explanation-toggle-btn"
                  onClick={() => setShowExplanationIndex(showExplanationIndex === idx ? null : idx)}
                >
                  {showExplanationIndex === idx ? '해설 닫기' : '해설 보기'}
                </button>
                {showExplanationIndex === idx && (
                  <p className="explanation-text">
                    {correctChoice?.explanation || '해설이 없습니다.'}
                  </p>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default UniversalResultPage;
