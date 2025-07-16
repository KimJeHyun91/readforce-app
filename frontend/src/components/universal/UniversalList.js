import React, { useEffect, useState } from 'react';
import UniversalFilterBar from './UniversalFilterBar';
import UniversalCard from './UniversalCard';
import './css/UniversalList.css';
import { toggleFavoritePassage, fetchFavoritePassageList } from '../../utils/fetchWithAuth';

const UniversalList = ({
  items: initialItems = [],
  level, setLevel,
  type, setType,
  orderBy, setOrderBy,
  typeOptions = [],
  onSolve,
}) => {

  const [items, setItems] = useState([]);

  /* 2️⃣ props 변경 시 state 동기화 */
  useEffect(() => {
    const mergeFavorites = async () => {
   try {
     const favList = await fetchFavoritePassageList();
     const favSet  = new Set(favList.map(f => f.passageNo));  // passageNo만 추출
     const merged = initialItems.map((it, idx) => ({
        ...it,
       _uid: it.id ?? it.passageNo ?? idx,
        isFavorite: favSet.has(it.passageNo),
     }));
     setItems(merged);
   } catch (err) {
      console.error('즐겨찾기 목록 불러오기 실패:', err);
      // 실패하면 즐겨찾기 표시 없이
      setItems(
       initialItems.map((it, idx) => ({
         ...it,
         _uid: it.id ?? it.passageNo ?? idx,
          isFavorite: false,
        }))
      );
    }
  };
  mergeFavorites();
}, [initialItems]);

  const toggleFavorite = (uid, passageNo, currentIsFav) => {
    // 1) 화면 먼저 토글
    setItems(prev =>
      prev.map(it =>
        it._uid === uid ? { ...it, isFavorite: !it.isFavorite } : it
      )
    );

    // 2) DB 저장
    toggleFavoritePassage(passageNo, !currentIsFav).catch(() => {
      // 실패 시 롤백
      setItems(prev =>
        prev.map(it =>
          it._uid === uid ? { ...it, isFavorite: currentIsFav } : it
        )
      );
      alert('서버 저장 실패!');
    });
  };

  /* 4️⃣ 필터링 */
  const filteredItems = items.filter((item) => {
    const matchLevel = level ? item.level === Number(level) : true;
    const matchType  = type  ? item.type === type           : true;
    return matchLevel && matchType;
  });

  /* 5️⃣ 정렬 */
  const sorted = [...filteredItems].sort((a, b) =>
    orderBy === 'latest'
      ? new Date(b.publishedAt) - new Date(a.publishedAt)
      : new Date(a.publishedAt) - new Date(b.publishedAt)
  );

  /* 6️⃣ 페이징 계산 */
  const itemsPerPage   = 5;
  const [currentPage, setCurrentPage] = useState(1);
  const totalPages     = Math.ceil(sorted.length / itemsPerPage);
  const pageGroupSize  = 5;
  const currentGroup   = Math.floor((currentPage - 1) / pageGroupSize);
  const startPage      = currentGroup * pageGroupSize + 1;
  const endPage        = Math.min(startPage + pageGroupSize - 1, totalPages);
  const visiblePages   = Array.from(
    { length: endPage - startPage + 1 },
    (_, i) => startPage + i
  );
  const paginated = sorted.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  return (
    <div className="UniversalList-container">
      <UniversalFilterBar
        level={level}  setLevel={setLevel}
        orderBy={orderBy} setOrderBy={setOrderBy}
        type={type}    setType={setType}
        typeOptions={typeOptions}
      />

      {/* 7️⃣ 카드 리스트 */}
      <div className="UniversalList-list">
        {paginated.length ? (
          paginated.map((item, index) => {
            // 고유 id 없을 경우 대비용
            const fallbackId =
              item.id ?? item.new_passageNo ?? item.news_no ?? index;
            return (
              <UniversalCard
                key={fallbackId}
                data={{ ...item, _uid: fallbackId }}   // _uid 추가
                typeOptions={typeOptions}
                onSolve={onSolve}
                onToggleFavorite={() =>
                  toggleFavorite(item._uid, item.passageNo, item.isFavorite)}
              />
            );
          })
        ) : (
          <div className="UniversalList-no-articles">게시물이 없습니다.</div>
        )}
      </div>

      {/* 8️⃣ 페이지네이션 */}
      <div className="UniversalList-pagination">
        <button
          onClick={() => setCurrentPage(startPage - 1)}
          disabled={startPage === 1}
        >
          «
        </button>
        {visiblePages.map((p) => (
          <button
            key={p}
            onClick={() => setCurrentPage(p)}
            className={currentPage === p ? 'active' : ''}
          >
            {p}
          </button>
        ))}
        <button
          onClick={() => setCurrentPage(endPage + 1)}
          disabled={endPage === totalPages}
        >
          »
        </button>
      </div>
    </div>
  );
};

export default UniversalList;
