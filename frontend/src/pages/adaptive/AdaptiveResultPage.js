import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './AdaptiveResultPage.css';
import CorrectAnimation from '../../assets/correct.json';
import IncorrectAnimation from '../../assets/incorrect.json';
import Lottie from 'lottie-react';

const AdaptiveResultPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [showExplanation, setShowExplanation] = useState(false);

  const isCorrect = location.state?.isCorrect;
  const explanation = location.state?.explanation || '해설이 제공되지 않았습니다.';
  const correctChoiceIndex = location.state?.correctChoiceIndex ?? -1;
  const correctContent = location.state?.correctContent || '';
  const next = location.state?.next || '/adaptive-learning/start';

  const resultMessage = isCorrect ? '정답입니다!' : '오답입니다.';
  const resultSubText = isCorrect
    ? '👏 정확히 파악했어요! 멋져요.'
    : '😢 괜찮아요! 다음에는 더 잘할 수 있어요.';

  const getChoiceLetter = (index) => {
    if (typeof index !== 'number' || index < 0) return '?';
    return String.fromCharCode(65 + index); 
  };

  return (
    <div className="adaptive-result-wrapper">
      <div className="adaptive-result-card">
        <div className="lottie-animation-center">
          <Lottie
            animationData={isCorrect ? CorrectAnimation : IncorrectAnimation}
            loop={false}
            style={{ width: 120, height: 120 }}
          />
        </div>

        <p className="adaptive-result-title">{resultMessage}</p>
        <p className="adaptive-result-subtext">{resultSubText}</p>

        <div className="adaptive-result-buttons">
          <button
            className="adaptive-result-button green"
            onClick={() => setShowExplanation(!showExplanation)}
          >
            {showExplanation ? '해설 닫기' : '해설 보기'}
          </button>
          <button
            className="adaptive-result-button yellow"
            onClick={() => navigate(next)}
          >
            다음 문제
          </button>
          <button
            className="adaptive-result-button gray"
            onClick={() => navigate('/adaptive-learning')}
          >
            닫기
          </button>
        </div>

        {showExplanation && (
          <div className="adaptive-result-explanation">
            <h3>📝 해설</h3>
            <p><strong>정답: {getChoiceLetter(correctChoiceIndex)}. {correctContent}</strong></p>
            <p>{explanation}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdaptiveResultPage;
