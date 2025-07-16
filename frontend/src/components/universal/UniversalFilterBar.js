import React from 'react';
import './css/UniversalFilterBar.css';
import Select from 'react-select';

const UniversalFilterBar = ({ level, setLevel, type, setType, orderBy, setOrderBy, typeOptions }) => {

  const levelOptions = [
    { value: '', label: '모두'},
    ...Array.from({ length: 10 }, (_, i) => ({
      value: (i + 1).toString(),
      label: `${i + 1} 단계`,
    })),
  ];

  return (
    <div className="UniversalFilterBar-layout">
      <div className="UniversalFilterBar-left">
        <button
          className={`UniversalFilterBar-sort-button ${orderBy === 'latest' ? 'active' : ''}`}
          onClick={() => setOrderBy('latest')}
        >
          · 최신순
        </button>
        <button
          className={`UniversalFilterBar-sort-button ${orderBy === 'oldest' ? 'active' : ''}`}
          onClick={() => setOrderBy('oldest')}
        >
          · 오래된순
        </button>
      </div>

      <div className="UniversalFilterBar-center">
        <div className="category-hashtags">
          {typeOptions.slice(1).map((opt) => (
            <span
              key={opt.value}
              className={`category-tag ${type === opt.value ? 'active' : ''}`}
              onClick={() => setType(type === opt.value ? '' : opt.value)}
            >
              #{opt.label}
            </span>
          ))}
        </div>
      </div>

      <div className="UniversalFilterBar-right">
        <div className="level-select-dropdown">
        <Select
          classNamePrefix="react-select"
          options={levelOptions}
          value={levelOptions.find((opt) => opt.value === level)}
          onChange={(selected) => setLevel(selected.value)}
          isSearchable={false}
          placeholder="단계"
            styles={{
                      control: (base) => ({
                        ...base,
                        minWidth: '100px',
                        height: '32px',
                        fontSize: '14px',
                        border: '1px solid #ccc',
                        boxShadow: 'none',
                        borderRadius: '12px',
                        outline: 'none', 
                        '&:hover': {
                          borderColor: '#999',
                        },
                      }),
                      menu: (base) => ({
                        ...base,
                        borderRadius: '12px',
                        marginTop: '4px',
                        boxShadow: '0 4px 10px rgba(0, 0, 0, 0.1)',
                        overflow: 'hidden',
                      }),
                      option: (base, state) => ({
                        ...base,
                        backgroundColor: state.isFocused ? '#f0f0f0' : 'white',
                        color: '#333',
                        cursor: 'pointer',
                        fontSize: '14px',
                      }),
                      singleValue: (base) => ({
                        ...base,
                        color: '#333',
                      }),
                      placeholder: (base) => ({
                        ...base,
                        color: '#999',
                      }),
                    }}
                  />
        </div>
      </div>
    </div>
  );
};

export default UniversalFilterBar;