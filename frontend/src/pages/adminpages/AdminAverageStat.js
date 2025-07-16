import { useEffect, useState } from "react";
import axiosInstance from "../../api/axiosInstance";
import { useNavigate } from "react-router-dom";

// ENUM 데이터 (한글화)
const LANGUAGE_ENUM = [
  { label: "한국어", value: "KOREAN" },
  { label: "일본어", value: "JAPANESE" },
  { label: "영어", value: "ENGLISH" },
];

const CATEGORY_ENUM = [
  { label: "뉴스", value: "NEWS" },
  { label: "소설", value: "NOVEL" },
  { label: "동화", value: "FAIRY_TALE" },
  { label: "어휘", value: "VOCABULARY" },
  { label: "사실적", value: "FACTUAL" },
  { label: "추론적", value: "INFERENTIAL" },
];

const TYPE_ENUM = [
  { label: "정치", value: "POLITICS" },
  { label: "경제", value: "ECONOMY" },
  { label: "사회", value: "SOCIETY" },
  { label: "생활/문화", value: "LIFE_AND_CULTURE" },
  { label: "IT/과학", value: "IT_AND_SCIENCE" },
  { label: "세계", value: "WORLD" },
  { label: "스포츠", value: "SPORTS" },
  { label: "연예", value: "ENTERTAINMENT" },
  { label: "미스터리", value: "MYSTERY" },
  { label: "공상 과학", value: "SCIENCE_FICTION" },
  { label: "판타지", value: "FANTASY" },
  { label: "로맨스", value: "ROMANCE" },
  { label: "역사", value: "HISTORICAL" },
  { label: "모험", value: "ADVENTURE" },
  { label: "스릴러", value: "THRILLER" },
  { label: "일상", value: "SLICE_OF_LIFE" },
  { label: "전통", value: "TRADITIONAL" },
  { label: "정보성", value: "INFORMATIONAL" },
];

const LEVELS = Array.from({ length: 10 }, (_, i) => i + 1);
const AGE_GROUPS = [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100];

const AdminAverageStat = () => {
  const [averageStats, setAverageStats] = useState([]);
  const [form, setForm] = useState({
    language: "",
    category: "",
    type: "",
    level: "",
    ageGroup: "",
    averageQuestionSolvingTime: "",
  });

  const navigate = useNavigate();

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const res = await axiosInstance.get("/administrator/average-question-solving-time/get-all-list");
      setAverageStats(res.data);
    } catch (err) {
      console.error("불러오기 실패:", err);
      alert("통계를 불러오는 데 실패했습니다.");
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleCreate = async () => {
  try {
    await axiosInstance.post("/administrator/average-question-solving-time/create", {
      ...form,
      level: parseInt(form.level, 10),
      ageGroup: parseInt(form.ageGroup, 10),
      averageQuestionSolvingTime: parseInt(form.averageQuestionSolvingTime, 10) * 1000,
    });
    alert("통계가 성공적으로 등록되었습니다.");
    fetchStats();
  } catch (err) {
    console.error("등록 실패:", err);
    alert("통계를 등록하는 데 실패했습니다.");
  }
};


  const handleDelete = async (no) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    try {
      await axiosInstance.delete(`/administrator/average-question-solving-time/delete`, {
        params: { averageQuestionSolvingTimeNo: no },
      });
      alert("삭제되었습니다.");
      fetchStats();
    } catch (err) {
      console.error("삭제 실패:", err);
      alert("삭제에 실패했습니다.");
    }
  };

  return (
    <div style={{ padding: "24px" }}>
      <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>
      <h2>연령별 평균 문제 풀이 시간 통계</h2>

      {/* 생성 폼 */}
      <div style={{ marginBottom: "24px" }}>
        <h3>새 통계 등록</h3>
        <div style={{ display: "flex", flexWrap: "wrap", gap: "8px" }}>
          <select name="language" value={form.language} onChange={handleInputChange} style={inputStyle}>
            <option value="">언어 선택</option>
            {LANGUAGE_ENUM.map((item) => <option key={item.value} value={item.value}>{item.label}</option>)}
          </select>
          <select name="category" value={form.category} onChange={handleInputChange} style={inputStyle}>
            <option value="">카테고리 선택</option>
            {CATEGORY_ENUM.map((item) => <option key={item.value} value={item.value}>{item.label}</option>)}
          </select>
          <select name="type" value={form.type} onChange={handleInputChange} style={inputStyle}>
            <option value="">유형 선택</option>
            {TYPE_ENUM.map((item) => <option key={item.value} value={item.value}>{item.label}</option>)}
          </select>
          <select name="level" value={form.level} onChange={handleInputChange} style={inputStyle}>
            <option value="">난이도 선택</option>
            {LEVELS.map((lvl) => <option key={lvl} value={lvl}>{lvl} 레벨</option>)}
          </select>
          <select name="ageGroup" value={form.ageGroup} onChange={handleInputChange} style={inputStyle}>
            <option value="">연령대 선택</option>
            {AGE_GROUPS.map((age) => <option key={age} value={age}>{age}대</option>)}
          </select>
          <input
            type="number"
            name="averageQuestionSolvingTime"
            placeholder="평균 시간 (초)"
            value={form.averageQuestionSolvingTime}
            onChange={handleInputChange}
            style={inputStyle}
          />
          <button onClick={handleCreate} style={actionBtn}>등록</button>
        </div>
      </div>

      {/* 테이블 */}
      {averageStats.length > 0 ? (
        <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "16px" }}>
          <thead>
            <tr>
              <th style={thStyle}>번호</th>
              <th style={thStyle}>언어</th>
              <th style={thStyle}>카테고리</th>
              <th style={thStyle}>유형</th>
              <th style={thStyle}>난이도</th>
              <th style={thStyle}>연령대</th>
              <th style={thStyle}>평균 시간(ms)</th>
              <th style={thStyle}>등록일</th>
              <th style={thStyle}>수정일</th>
              <th style={thStyle}>삭제</th>
            </tr>
          </thead>
          <tbody>
            {averageStats.map((item) => (
              <tr key={item.averageQuestionSolvingTimeNo}>
                <td style={tdStyle}>{item.averageQuestionSolvingTimeNo}</td>
                <td style={tdStyle}>{LANGUAGE_ENUM.find(lang => lang.value === item.language)?.label}</td>
                <td style={tdStyle}>{CATEGORY_ENUM.find(cat => cat.value === item.category)?.label}</td>
                <td style={tdStyle}>{TYPE_ENUM.find(type => type.value === item.type)?.label}</td>
                <td style={tdStyle}>{item.level} 레벨</td>
                <td style={tdStyle}>{item.ageGroup}대</td>
                <td style={tdStyle}>{(item.averageQuestionSolvingTime / 1000).toLocaleString()}초</td>
                <td style={tdStyle}>{new Date(item.createdAt).toLocaleDateString()}</td>
                <td style={tdStyle}>{new Date(item.lastModifiedAt).toLocaleDateString()}</td>
                <td style={tdStyle}>
                  <button onClick={() => handleDelete(item.averageQuestionSolvingTimeNo)} style={deleteBtn}>삭제</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>등록된 통계가 없습니다.</p>
      )}
    </div>
  );
};

// 공통 스타일
const inputStyle = {
  padding: "8px",
  flex: "1 1 200px"
};

const backbtn = {
  marginBottom: "16px",
  padding: "8px 16px",
  backgroundColor: "#6c757d",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer"
};

const thStyle = {
  border: "1px solid #ccc",
  padding: "8px",
  backgroundColor: "#f2f2f2",
  textAlign: "left"
};

const tdStyle = {
  border: "1px solid #ddd",
  padding: "8px"
};

const actionBtn = {
  padding: "8px 16px",
  backgroundColor: "#007bff",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer"
};

const deleteBtn = {
  padding: "6px 12px",
  backgroundColor: "#dc3545",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer"
};

export default AdminAverageStat;
