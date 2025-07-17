import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './ChallengeStartModal.css';

const ChallengeStartModal = ({ onClose }) => {
  const navigate = useNavigate();

  const [category, setCategory] = useState('NEWS');
  const [language, setLanguage] = useState('KOREAN');

  const handleStart = () => {
    navigate(`/challenge/today?language=${language}&category=${category}`);
    onClose();
  };

  return (
    <div className="ChallengeStartModal-overlay">
      <div className="ChallengeStartModal-content">
        <h2 className="ChallengeStartModal-title">오늘의 도전 시작하기</h2>

        <div className="ChallengeStartModal-option-group">
          <p>카테고리 선택</p>
          <button className={category === 'NEWS' ? 'selected' : ''} onClick={() => setCategory('NEWS')}>뉴스</button>
          <button className={category === 'NOVEL' ? 'selected' : ''} onClick={() => setCategory('NOVEL')}>소설</button>
          <button className={category === 'FAIRY_TALE' ? 'selected' : ''} onClick={() => setCategory('FAIRY_TALE')}>동화</button>
        </div>

        <div className="ChallengeStartModal-option-group">
          <p>언어 선택</p>
          <button className={language === 'KOREAN' ? 'selected' : ''} onClick={() => setLanguage('KOREAN')}>한국어</button>
          <button className={language === 'ENGLISH' ? 'selected' : ''} onClick={() => setLanguage('ENGLISH')}>영어</button>
          <button className={language === 'JAPANESE' ? 'selected' : ''} onClick={() => setLanguage('JAPANESE')}>일본어</button>
        </div>

        <div className="ChallengeStartModal-actions">
          <button onClick={onClose}>취소</button>
          <button onClick={handleStart}>도전 시작</button>
        </div>
      </div>
    </div>
  );
};

export default ChallengeStartModal;
