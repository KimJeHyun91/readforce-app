import { useEffect, useState } from "react";
import axiosInstance from "../../api/axiosInstance";
import { useNavigate } from "react-router-dom";

const AdminLanguageEdit = () => {
  const [languageList, setLanguageList] = useState([]);
  const [newLanguage, setNewLanguage] = useState("");
  const [editModeId, setEditModeId] = useState(null);
  const [editValue, setEditValue] = useState("");
  const navigate = useNavigate();

  const fetchLanguages = async () => {
    try {
      const res = await axiosInstance.get("/administrator/language/get-all-list");
      setLanguageList(res.data);
    } catch (err) {
      console.error("불러오기 실패", err);
    }
  };

  useEffect(() => {
    fetchLanguages();
  }, []);

  const handleCreate = async () => {
    if (!newLanguage.trim()) return alert("언어명을 입력하세요.");
    try {
      await axiosInstance.post("/administrator/language/create", {
        language: newLanguage.trim()
      });
      alert("언어 생성 완료!");
      setNewLanguage("");
      fetchLanguages();
    } catch (err) {
      console.error(err);
      alert("생성 실패");
    }
  };

  const handleModify = async (languageNo) => {
    try {
      await axiosInstance.post("/administrator/language/modify", {
        languageNo,
        language: editValue.trim()
      });
      alert("수정 완료!");
      setEditModeId(null);
      fetchLanguages();
    } catch (err) {
      console.error(err);
      alert("수정 실패");
    }
  };

  const handleDelete = async (languageNo) => {
    const ok = window.confirm("정말 삭제하시겠습니까?");
    if (!ok) return;
    try {
      await axiosInstance.post(`/administrator/language/delete?languageNo=${languageNo}`);
      alert("삭제 완료!");
      fetchLanguages();
    } catch (err) {
      console.error(err);
      alert("삭제 실패");
    }
  };

  return (
    <div style={{ padding: "24px" }}>
      <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>
      <h2>언어 관리</h2>

      <div style={{ margin: "16px 0" }}>
        <input
          placeholder="새 언어명 입력"
          value={newLanguage}
          onChange={(e) => setNewLanguage(e.target.value)}
          style={{ marginRight: "8px" }}
        />
        <button onClick={handleCreate}>생성</button>
      </div>

      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th style={thStyle}>번호</th>
            <th style={thStyle}>언어명</th>
            <th style={thStyle}>생성일</th>
            <th style={thStyle}>관리</th>
          </tr>
        </thead>
        <tbody>
          {languageList.map((item) => (
            <tr key={item.languageNo}>
              <td style={tdStyle}>{item.languageNo}</td>
              <td style={tdStyle}>
                {editModeId === item.languageNo ? (
                  <input
                    value={editValue}
                    onChange={(e) => setEditValue(e.target.value)}
                  />
                ) : (
                  item.languageName
                )}
              </td>
              <td style={tdStyle}>{new Date(item.createdAt).toLocaleDateString()}</td>
              <td style={tdStyle}>
                {editModeId === item.languageNo ? (
                  <>
                    <button onClick={() => handleModify(item.languageNo)}>저장</button>
                    <button onClick={() => setEditModeId(null)}>취소</button>
                  </>
                ) : (
                  <>
                    <button onClick={() => {
                      setEditModeId(item.languageNo);
                      setEditValue(item.languageName);
                    }}>수정</button>
                    <button onClick={() => handleDelete(item.languageNo)} style={{ color: "red", marginLeft: "8px" }}>
                      삭제
                    </button>
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

export default AdminLanguageEdit;