import React, { useEffect, useState, useRef } from 'react';
import './testquestionpage.css';
import { useLocation, useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';

const TestQuestionPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const language = location.state?.language || 'KOREAN';
  const [question, setQuestion] = useState(null);
  const [selected, setSelected] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [timeLeft, setTimeLeft] = useState(180);
  const startTimeRef = useRef(Date.now());
  const timerRef = useRef(null);

  useEffect(() => {
    if (question) {
      startTimeRef.current = Date.now();
      setTimeLeft(180);
      timerRef.current = setInterval(() => {
        setTimeLeft((prev) => {
          if (prev <= 1) {
            clearInterval(timerRef.current);
            handleSubmit(true);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => clearInterval(timerRef.current);
  }, [question]);

  useEffect(() => {
    const raw = location.state?.question;
    if (!raw) {
      alert('초기 문제 정보가 없습니다.');
      navigate('/');
      return;
    }
    setQuestion(raw);
  }, [location.state, navigate]);

  const handleSelect = (idx) => {
    if (isSubmitting) return;
    setSelected(idx);
  };

  const handleSubmit = async (autoSubmit = false) => {
    if (selected === null && !autoSubmit) {
      alert('보기를 선택하세요.');
      return;
    }

    setIsSubmitting(true);
    clearInterval(timerRef.current);

    const timeTaken = Math.floor((Date.now() - startTimeRef.current) / 1000);

    const payload = {
      testerId: question.testerId,
      questionNo: question.quiz.questionNo,
      selectedIndex: selected ?? -1,
      questionSolvingTime: timeTaken,
      language,
    };

    console.log('📤 제출 요청:', payload);

    const endpointMap = {
      VOCABULARY: '/test/submit-vocabulary-result',
      FACTUAL: '/test/submit-factual-result',
      INFERENTIAL: '/test/submit-inferential-result',
    };

    const endpoint = endpointMap[question.category];

    try {
      const res = await api.post(endpoint, payload);

      // ✅ 정답 여부 콘솔 출력
      const submittedChoice = res.data?.choiceList?.find(c => c.choiceIndex === selected);
      if (submittedChoice?.isCorrect === true) {
        console.log('✅ 정답입니다!');
      } else {
        console.log('❌ 오답입니다.');
      }

      if (res.data?.choiceList) {
        const raw = res.data;
        const formatted = {
          article: {
            title: raw.title,
            content: raw.content,
          },
          quiz: {
            questionNo: raw.questionNo,
            questionText: raw.question,
            choices: raw.choiceList.map((c) => c.content),
          },
          testerId: raw.testerId,
          category: raw.category,
        };
        setQuestion(formatted);
        setSelected(null);
      } else {
        navigate('/test-result', { state: { result: res.data } });
      }
    } catch (err) {
      console.error('❌ 제출 오류', err);
      alert('제출 중 오류가 발생했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!question) return <div>문제를 불러오는 중...</div>;

  return (
      <div className="TestQuestion-layout">
    <div className="TestQuestion-article-box">
      <h3 className="TestQuestion-article-title">{question.article.title}</h3>
      <p className="TestQuestion-article-content">{question.article.content}</p>
    </div>

    <div className="TestQuestion-right-container">
      <div className="TestQuestion-quiz-box">
        
        {/* 타이머도 이 박스 안에 넣기 */}
        <div className="TestQuestion-timer">⏱️ 남은 시간: {timeLeft}초</div>

        <h4 className="TestQuestion-quiz-title">문제</h4>
        <p className="TestQuestion-quiz-question">{question.quiz.questionText}</p>

        {question.quiz.choices.map((opt, idx) => (
          <button
            key={idx}
            className={`TestQuestion-quiz-option ${selected === idx ? 'selected' : ''}`}
            onClick={() => handleSelect(idx)}
            disabled={isSubmitting}
          >
            {String.fromCharCode(65 + idx)}. {opt}
          </button>
        ))}

        {/* 제출 버튼도 quiz-box 안에 함께 넣기 */}
        <div className="TestQuestion-controls">
          <button
            className="TestQuestion-submit"
            onClick={() => handleSubmit(false)}
            disabled={selected === null || isSubmitting}
          >
            제출
          </button>
        </div>
      </div>
    </div>
  </div>

  );
};

export default TestQuestionPage;
