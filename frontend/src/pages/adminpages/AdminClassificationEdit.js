import { useEffect, useState } from "react";
import axiosInstance from "../../api/axiosInstance";
import { useNavigate } from "react-router-dom";

const AdminClassificationEdit = () => {
  const [classificationList, setClassificationList] = useState([]);
  const [newClassification, setNewClassification] = useState("");
  const [editModeId, setEditModeId] = useState(null);
  const [editValue, setEditValue] = useState("");
  const navigate = useNavigate();

  const fetchClassifications = async () => {
    try {
      const res = await axiosInstance.get("/administrator/classification/get-all-list");
      setClassificationList(res.data);
    } catch (err) {
      console.error("불러오기 실패", err);
    }
  };

  useEffect(() => {
    fetchClassifications();
  }, []);

  const handleCreate = async () => {
    if (!newClassification.trim()) return alert("분류명을 입력하세요.");
    try {
      await axiosInstance.post("/administrator/classification/create", {
        classification: newClassification.trim()
      });
      alert("생성 완료!");
      setNewClassification("");
      fetchClassifications();
    } catch (err) {
      console.error(err);
      alert("생성 실패");
    }
  };

  const handleModify = async (classificationNo) => {
    try {
      await axiosInstance.patch("/administrator/classification/modify", {
        classificationNo,
        classification: editValue.trim()
      });
      alert("수정 완료!");
      setEditModeId(null);
      fetchClassifications();
    } catch (err) {
      console.error(err);
      alert("수정 실패");
    }
  };

  const handleDelete = async (classificationNo) => {
    const ok = window.confirm("정말 삭제하시겠습니까?");
    if (!ok) return;
    try {
      await axiosInstance.delete("/administrator/classification/delete", {
        params: { classificationNo }
      });
      alert("삭제 완료!");
      fetchClassifications();
    } catch (err) {
      console.error(err);
      alert("삭제 실패");
    }
  };

  return (
    <div style={{ padding: "24px" }}>
      <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>
      <h2>분류 관리</h2>

      <div style={{ margin: "16px 0" }}>
        <input
          placeholder="새 분류명 입력"
          value={newClassification}
          onChange={(e) => setNewClassification(e.target.value)}
          style={{ marginRight: "8px" }}
        />
        <button onClick={handleCreate}>생성</button>
      </div>

      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th style={thStyle}>번호</th>
            <th style={thStyle}>분류명</th>
            <th style={thStyle}>생성일</th>
            <th style={thStyle}>관리</th>
          </tr>
        </thead>
        <tbody>
          {classificationList.map((item) => (
            <tr key={item.classificationNo}>
              <td style={tdStyle}>{item.classificationNo}</td>
              <td style={tdStyle}>
                {editModeId === item.classificationNo ? (
                  <input
                    value={editValue}
                    onChange={(e) => setEditValue(e.target.value)}
                  />
                ) : (
                  item.classification
                )}
              </td>
              <td style={tdStyle}>{new Date(item.createdAt).toLocaleDateString()}</td>
              <td style={tdStyle}>
                {editModeId === item.classificationNo ? (
                  <>
                    <button onClick={() => handleModify(item.classificationNo)}>저장</button>
                    <button onClick={() => setEditModeId(null)}>취소</button>
                  </>
                ) : (
                  <>
                    <button onClick={() => {
                      setEditModeId(item.classificationNo);
                      setEditValue(item.classification);
                    }}>수정</button>
                    <button onClick={() => handleDelete(item.classificationNo)} style={{ color: "red", marginLeft: "8px" }}>
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

export default AdminClassificationEdit;