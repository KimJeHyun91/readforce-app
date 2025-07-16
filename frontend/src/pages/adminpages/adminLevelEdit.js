import { useEffect, useState } from "react";
import axiosInstance from "../../api/axiosInstance";
import { useNavigate } from "react-router-dom";

const AdminLevelEdit = () => {
  const [levelList, setLevelList] = useState([]);
  const [newLevel, setNewLevel] = useState({
    level: "",
    paragraphCount: "",
    vocabularyLevel: "",
    sentenceStructure: "",
    questionType: ""
  });
  const [editModeId, setEditModeId] = useState(null);
  const [editValue, setEditValue] = useState({});
  const navigate = useNavigate();

  const fetchLevels = async () => {
    try {
      const res = await axiosInstance.get("/administrator/level/get-all-list");
      setLevelList(res.data);
    } catch (err) {
      console.error("불러오기 실패", err);
    }
  };

  useEffect(() => {
    fetchLevels();
  }, []);

  const handleCreate = async () => {
    const { level, paragraphCount, vocabularyLevel, sentenceStructure, questionType } = newLevel;
    if (!level || !paragraphCount || !vocabularyLevel || !sentenceStructure || !questionType) return alert("모든 값을 입력하세요.");
    try {
      await axiosInstance.post("/administrator/level/create", {
        ...newLevel,
        level: parseInt(level),
        paragraphCount: parseInt(paragraphCount)
      });
      alert("레벨 생성 완료!");
      setNewLevel({ level: "", paragraphCount: "", vocabularyLevel: "", sentenceStructure: "", questionType: "" });
      fetchLevels();
    } catch (err) {
      console.error(err);
      alert("생성 실패");
    }
  };

  const handleModify = async (levelNo) => {
    try {
      await axiosInstance.patch("/administrator/level/modify", {
        levelNo,
        ...editValue,
        level: parseInt(editValue.level),
        paragraphCount: parseInt(editValue.paragraphCount)
      });
      alert("수정 완료!");
      setEditModeId(null);
      fetchLevels();
    } catch (err) {
      console.error(err);
      alert("수정 실패");
    }
  };

  const handleDelete = async (levelNo) => {
    const ok = window.confirm("정말 삭제하시겠습니까?");
    if (!ok) return;
    try {
      await axiosInstance.delete("/administrator/level/delete", {
        params: { levelNo }
      });
      alert("삭제 완료!");
      fetchLevels();
    } catch (err) {
      console.error(err);
      alert("삭제 실패");
    }
  };

  return (
    <div style={{ padding: "24px" }}>
      <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>
      <h2>레벨 관리</h2>

      <div style={{ margin: "16px 0" }}>
        <input placeholder="레벨" value={newLevel.level} onChange={(e) => setNewLevel({ ...newLevel, level: e.target.value })} style={{ marginRight: 8 }} />
        <input placeholder="문단 수" value={newLevel.paragraphCount} onChange={(e) => setNewLevel({ ...newLevel, paragraphCount: e.target.value })} style={{ marginRight: 8 }} />
        <input placeholder="어휘 수준" value={newLevel.vocabularyLevel} onChange={(e) => setNewLevel({ ...newLevel, vocabularyLevel: e.target.value })} style={{ marginRight: 8 }} />
        <input placeholder="문장 구조" value={newLevel.sentenceStructure} onChange={(e) => setNewLevel({ ...newLevel, sentenceStructure: e.target.value })} style={{ marginRight: 8 }} />
        <input placeholder="문제 유형" value={newLevel.questionType} onChange={(e) => setNewLevel({ ...newLevel, questionType: e.target.value })} style={{ marginRight: 8 }} />
        <button onClick={handleCreate}>생성</button>
      </div>

      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th style={thStyle}>번호</th>
            <th style={thStyle}>레벨</th>
            <th style={thStyle}>문단 수</th>
            <th style={thStyle}>어휘 수준</th>
            <th style={thStyle}>문장 구조</th>
            <th style={thStyle}>문제 유형</th>
            <th style={thStyle}>생성일</th>
            <th style={thStyle}>관리</th>
          </tr>
        </thead>
        <tbody>
          {levelList.map((item) => (
            <tr key={item.levelNo}>
              <td style={tdStyle}>{item.levelNo}</td>
              <td style={tdStyle}>{editModeId === item.levelNo ? <input value={editValue.level} onChange={(e) => setEditValue({ ...editValue, level: e.target.value })} /> : item.level}</td>
              <td style={tdStyle}>{editModeId === item.levelNo ? <input value={editValue.paragraphCount} onChange={(e) => setEditValue({ ...editValue, paragraphCount: e.target.value })} /> : item.paragraphCount}</td>
              <td style={tdStyle}>{editModeId === item.levelNo ? <input value={editValue.vocabularyLevel} onChange={(e) => setEditValue({ ...editValue, vocabularyLevel: e.target.value })} /> : item.vocabularyLevel}</td>
              <td style={tdStyle}>{editModeId === item.levelNo ? <input value={editValue.sentenceStructure} onChange={(e) => setEditValue({ ...editValue, sentenceStructure: e.target.value })} /> : item.sentenceStructure}</td>
              <td style={tdStyle}>{editModeId === item.levelNo ? <input value={editValue.questionType} onChange={(e) => setEditValue({ ...editValue, questionType: e.target.value })} /> : item.questionType}</td>
              <td style={tdStyle}>{new Date(item.createdAt).toLocaleDateString()}</td>
              <td style={tdStyle}>
                {editModeId === item.levelNo ? (
                  <>
                    <button onClick={() => handleModify(item.levelNo)}>저장</button>
                    <button onClick={() => setEditModeId(null)}>취소</button>
                  </>
                ) : (
                  <>
                    <button onClick={() => {
                      setEditModeId(item.levelNo);
                      setEditValue({
                        level: item.level,
                        paragraphCount: item.paragraphCount,
                        vocabularyLevel: item.vocabularyLevel,
                        sentenceStructure: item.sentenceStructure,
                        questionType: item.questionType
                      });
                    }}>수정</button>
                    <button onClick={() => handleDelete(item.levelNo)} style={{ color: "red", marginLeft: "8px" }}>삭제</button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
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

export default AdminLevelEdit;