import React, { useState } from 'react';
import ChallengeStartModal from './ChallengeStartModal';
import './challengepage.css';
import trophyAnimation from '../../assets/trophy.json';
import Lottie from 'lottie-react';

const ChallengePage = () => {
  const [showModal, setShowModal] = useState(false);

  return (
    <div className="challenge-wrapper">

     <p className="challenge-info-text">
      카테고리별 오늘의 도전은 <strong>단 하루 한 번!</strong><br />
      <span className="highlight">20문제</span>, <span className="highlight">30분</span>
      당신의 실력을 <strong>랭킹</strong>으로 증명하세요.
    </p>

      <Lottie animationData={trophyAnimation} loop autoplay className="trophy-animation" />

      <button
        className="challenge-btn"
        onClick={() => setShowModal(true)}
      >
        문해력 도전
      </button>

      {showModal && <ChallengeStartModal onClose={() => setShowModal(false)} />}
    </div>
  );
};

export default ChallengePage;
