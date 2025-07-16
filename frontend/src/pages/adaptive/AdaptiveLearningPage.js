import React from 'react';
import Lottie from 'lottie-react';
import robotAnimation from '../../assets/robot.json';
import './AdaptiveLearningPage.css';

const AdaptiveLearningPage = () => {
  const nickname = localStorage.getItem('nickname') || '사용자';

return (
  <main className="adaptive-narrow">
    <div className="adaptive-learning-container">
      <div className="adaptive-content-wrapper">
        <div className="robot-section">
          <Lottie animationData={robotAnimation} loop autoplay className="robot-animation" />
        </div>

        <div className="adaptive-intro">
          <p>안녕하세요!</p>
          <p>여러분들의 <strong style={{ color: '#353535' }}>문해력 학습</strong>을<span className="mobile-only-br"><br/></span> 도와줄 <strong style={{ color: '#353535' }}>리드</strong> <strong style={{ color: '#439395' }}>포스</strong>입니다.</p><br />
          <p><span className="nickname">{nickname}</span>님이</p>
          <p>기사, 소설, 동화에서<span className="mobile-only-br"><br/></span> 푼 문제들을 바탕으로<br />저 리드포스가 분석한 <strong style={{ color: '#353535' }}><span className="mobile-only-br"><br/></span>개인 맞춤형 문제</strong>들을<span className="mobile-only-br"><br/></span> 풀어보세요.</p>
          <div className="start-button">
            <a href="/adaptive-learning/start">적응력 학습 시작하기</a>
          </div>
        </div>
      </div>
    </div>
    </main>
  );
};

export default AdaptiveLearningPage;
