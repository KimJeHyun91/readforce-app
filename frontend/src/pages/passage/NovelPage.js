import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import UniversalList from '../../components/universal/UniversalList';
import { debouncedFetchPassageList } from '../../api/passageApi';
import { novelTypeOptions } from '../../components/TypeOptions';

const NovelPage = () => {
  const navigate = useNavigate();
  const [novelItems, setNovelItems] = useState([]);
  const [language] = useState('KOREAN');
  const [level, setLevel] = useState('');
  const [type, setType] = useState('');
  const [orderBy, setOrderBy] = useState('latest');

  const category = 'NOVEL';
  const classification = 'NORMAL';

  const fetchData = useCallback(() => {
    const apiLevel = level || '';
    const apiType = type || '';

    debouncedFetchPassageList({
      language,
      classification,
      category,
      type: apiType,
      level: apiLevel,
      orderBy,
      }, (data) => {
        setNovelItems(data);
      }
    );
  }, [language, classification, category, type, level, orderBy]);

  useEffect(() => {
    fetchData();
    return () => {
      debouncedFetchPassageList.cancel();
    };
  }, [fetchData]);

  const handleSolve = (item) => {
    navigate(`/novel/quiz/${item.passageNo}`, {
      state: { passage: item }
    });
  };

  return (
    <div className="page-container">
      <UniversalList
        items={novelItems}
        level={level}
        setLevel={setLevel}
        type={type}
        setType={setType}
        orderBy={orderBy}
        setOrderBy={setOrderBy}
        typeOptions={novelTypeOptions}
        onSolve={handleSolve}
      />
    </div>
  );
};

export default NovelPage;
