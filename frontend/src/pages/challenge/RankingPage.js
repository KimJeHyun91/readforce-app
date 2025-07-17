import React, { useState, useEffect } from 'react';
import api from '../../api/axiosInstance';
import './RankingPage.css';

const categories = [
  { label: '뉴스', category: 'NEWS', language: 'KOREAN', scoreKey: 'korean_news' },
  { label: '소설', category: 'NOVEL', language: 'KOREAN', scoreKey: 'novel' },
  { label: '동화', category: 'FAIRY_TALE', language: 'KOREAN', scoreKey: 'fairytale' },
];

const RankingPage = () => {
  const [selectedCategory, setSelectedCategory] = useState(categories[0]);
  const [rankingData, setRankingData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRanking = async () => {
      setIsLoading(true);
      setError(null);

      try {
        const res = await api.get('/ranking/get-ranking-list', {
          params: {
            category: selectedCategory.category,
            language: selectedCategory.language,
          },
        });
        setRankingData(res.data);
      } catch (err) {
        setError('랭킹 정보를 불러오지 못했습니다.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchRanking();
  }, [selectedCategory]);

  const renderScore = (user) => {
    const score = user[selectedCategory.scoreKey] ?? user.score ?? 0;
      return Number(score).toFixed(1);
    };

  return (
    <div className="page-container ranking-wrapper">
      <h2 className="ranking-title">🏆 문해력 랭킹</h2>

      <div className="ranking-tabs">
        {categories.map((cat) => (
          <button
            key={cat.label}
            className={selectedCategory.label === cat.label ? 'active' : ''}
            onClick={() => setSelectedCategory(cat)}
          >
            {cat.label}
          </button>
        ))}
      </div>

      {isLoading ? (
        <p className="ranking-loading">불러오는 중...</p>
      ) : error ? (
        <p className="ranking-error">{error}</p>
      ) : rankingData.length === 0 ? (
        <p className="ranking-empty">데이터가 없습니다.</p>
      ) : (
        <div className="ranking-list">
          {rankingData.map((user, idx) => (
            <div key={user.email} className="ranking-item">
              <span className={`ranking-rank rank-${idx + 1}`}>
                {idx + 1}위
              </span>
              <span className="ranking-nickname">{user.nickname}</span>
              <span className="ranking-score">{renderScore(user)}점</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default RankingPage;
