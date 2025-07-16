// import React from 'react';
// import './testresultpage.css';
// import { useLocation, useNavigate } from 'react-router-dom';

// const TestResultPage = () => {
//   const location = useLocation();
//   const navigate = useNavigate();
//   const { score, total, questions, answers } = location.state || {};
//   const percent = Math.round((score / total) * 100);

//   const getLiteracyLevel = (questions, answers) => {
//     if (!questions || !answers || questions.length !== answers.length) return '초급';

//     const correct = { beginner: 0, intermediate: 0, advanced: 0 };

//     questions.forEach((q, idx) => {
//       const isCorrect = answers[idx] === q.quiz.correct_answer_index;
//       const level = (q.article.level || '').toLowerCase();

//       if (isCorrect) {
//         if (level === 'beginner') correct.beginner++;
//         else if (level === 'intermediate') correct.intermediate++;
//         else if (level === 'advanced') correct.advanced++;
//       }
//     });

//     const { beginner, intermediate, advanced } = correct;

//     if (beginner === 0) return '초급';
//     if (beginner === 1 && intermediate === 2 && advanced >= 1) return '고급';
//     if (
//       (beginner === 1 && intermediate === 2) ||
//       (beginner === 1 && intermediate === 1 && advanced === 1) ||
//       (beginner === 1 && advanced === 2)
//     )
//       return '중급';

//     return '초급';
//   };

//   const level = getLiteracyLevel(questions, answers);

//   const goToReview = () => {
//     navigate('/test-review', { state: { questions, answers } });
//   };

//   return (
//     <div className="TestResult-wrapper">
//       <div className="TestResult-card">
//         <h2>
//           🎉 당신의 문해력은 <span className="TestResult-highlight">{level}</span>입니다!
//         </h2>
//         <p>{total}문제 중 {score}문제를 맞았습니다.</p>
//         <p>상위 {100 - percent}%에 해당합니다.</p>
//         <div className="TestResult-actions">
//           <button onClick={goToReview}>해설 보기</button>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default TestResultPage;
import React from 'react';
import './testresultpage.css';
import { useLocation, useNavigate } from 'react-router-dom';

const TestResultPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const result = location.state?.result;

  if (!result || !result.testResultComment) {
    return <div>결과를 불러올 수 없습니다.</div>;
  }

  return (
    <div className="TestResult-wrapper">
      <div className="TestResult-card">
        <h2>🎉 문해력 테스트 결과</h2>
        <p className="TestResult-comment">{result.testResultComment}</p>

        <div className="TestResult-actions">
          <button onClick={() => navigate('/')}>메인으로 돌아가기</button>
        </div>
      </div>
    </div>
  );
};

export default TestResultPage;
