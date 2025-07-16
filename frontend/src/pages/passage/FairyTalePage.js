import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import UniversalList from '../../components/universal/UniversalList';
import { debouncedFetchPassageList } from '../../api/passageApi';
import { fairytaleTypeOptions } from '../../components/TypeOptions';

const FairyTalePage = () => {
  const navigate = useNavigate();
  const [fairyTaleItems, setFairyTaleItems] = useState([]);
  const [language] = useState('KOREAN');
  const [level, setLevel] = useState('');
  const [type, setType] = useState('');
  const [orderBy, setOrderBy] = useState('latest');

  const category = 'FAIRY_TALE';
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
      setFairyTaleItems(data);
    });
  }, [language, classification, category, type, level, orderBy]);

  useEffect(() => {
    fetchData();
    return () => {
      debouncedFetchPassageList.cancel();
    };
  }, [fetchData]);

  const handleSolve = (item) => {
    navigate(`/fairytale/quiz/${item.passageNo}`, {
      state: { passage: item }
    });
  };

  return (
    <div className="page-container">
      <UniversalList
        items={fairyTaleItems}
        level={level}
        setLevel={setLevel}
        type={type}
        setType={setType}
        orderBy={orderBy}
        setOrderBy={setOrderBy}
        typeOptions={fairytaleTypeOptions}
        onSolve={handleSolve}
      />
    </div>
  );
};

export default FairyTalePage;
