import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './ChallengeResultPage.css';
import trophyAnimation from '../../assets/trophy.json';
import Lottie from 'lottie-react';

const ChallengeResultPage = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const { finalScore } = location.state || { finalScore: 0 };

  return (
    
    <div className="ChallengeResultPage-container">
            <Lottie animationData={trophyAnimation} loop autoplay className="trophy-animation" />
      <h2 className="ChallengeResultPage-title">오늘의 도전 완료!</h2>
      <p className="ChallengeResultPage-score-line">총 점수: <span className="ChallengeResultPage-score">{finalScore}</span></p>
      <button
        className="ChallengeResultPage-button"
        onClick={() => navigate('/challenge')}
      >
        문해력 도전으로 돌아가기
      </button>
    </div>
  );
};

export default ChallengeResultPage;
