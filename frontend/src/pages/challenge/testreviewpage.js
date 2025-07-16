import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './testreviewpage.css';

const TestReviewPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { questions, answers } = location.state || {};

  const [visibleArticles, setVisibleArticles] = useState({});

  if (!questions || !answers) return <div>데이터를 불러올 수 없습니다.</div>;

  const toggleArticle = (idx) => {
    setVisibleArticles((prev) => ({ ...prev, [idx]: !prev[idx] }));
  };

  return (
    <div className="TestReview-wrapper">
      <h2>📘 해설 보기</h2>
      {questions.map((q, idx) => {
        const userAnswer = answers[idx];
        const correctAnswer = q.quiz.correct_answer_index;
        const isCorrect = userAnswer === correctAnswer;
        const level = q.article.level || '정보 없음';

        return (
          <div key={idx} className="TestReview-card">
            <h3>문제 {idx + 1} ({level})</h3>
            <p className="TestReview-question">{q.quiz.question_text}</p>

            {[q.quiz.choice1, q.quiz.choice2, q.quiz.choice3, q.quiz.choice4].map((choice, i) => {
              const isCorrectChoice = i === correctAnswer;
              const isUserWrongChoice = i === userAnswer && !isCorrectChoice;

              const className = [
                'TestReview-choice',
                isCorrectChoice ? 'correct' : '',
                isUserWrongChoice ? 'user-wrong' : '',
              ].join(' ').trim();

              return (
                <div key={i} className={className}>
                  <span className="TestReview-label">{String.fromCharCode(65 + i)}.</span>
                  {choice}
                  {isCorrectChoice && <span className="TestReview-tag">정답</span>}
                  {isUserWrongChoice && <span className="TestReview-tag wrong">내 선택</span>}
                </div>
              );
            })}

            <div className="TestReview-explanation">
              <strong>해설:</strong> {q.quiz.explanation || '해설이 없습니다.'}
            </div>

            <button className="TestReview-toggle-btn" onClick={() => toggleArticle(idx)}>
              {visibleArticles[idx] ? '지문 숨기기' : '지문 보기'}
            </button>

            {visibleArticles[idx] && (
              <div className="TestReview-article">
                <h4>{q.article.title}</h4>
                <p>{q.article.content}</p>
              </div>
            )}
          </div>
        );
      })}

      <div className="TestReview-actions">
        <button onClick={() => navigate('/')}>메인으로 돌아가기</button>
      </div>
    </div>
  );
};

export default TestReviewPage;
