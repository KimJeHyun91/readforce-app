import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './ChallengeResultPage.css';

const ChallengeResultPage = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const { finalScore, maxScore } = location.state || { finalScore: 0, maxScore: 0 };

  return (
    <div className="ChallengeResultPage-container">
      <h2 className="ChallengeResultPage-title">오늘의 도전 완료!</h2>
      <p className="ChallengeResultPage-score">총 점수: {finalScore} / {maxScore}</p>
      <button className="ChallengeResultPage-button" onClick={() => navigate('/challenge')}>
        문해력 도전으로 돌아가기
      </button>
    </div>
  );
};

export default ChallengeResultPage;
