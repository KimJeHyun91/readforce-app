import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from '../../api/axiosInstance';

const AdminLiteratureAdd = () => {
  const [title, setTitle] = useState("");
  const [type, setType] = useState("MYSTERY");
  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      const res = await axiosInstance.post("/admin/add-literature", {
        title,
        type
      });

      alert("문학이 추가되었습니다!");
      navigate("/adminpage/adminliterature");
    } catch (err) {
      console.error(err);
      alert("저장 중 오류 발생");
    }
  };

  return (
    <div style={{ padding: "24px" }}>
      <h2>문학 추가</h2>
      <div style={{ marginBottom: "12px" }}>
        <label style={{ marginRight: "8px" }}>제목</label>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          style={{ padding: "8px", width: "300px" }}
        />
      </div>
      <div style={{ marginBottom: "12px" }}>
        <label style={{ marginRight: "8px" }}>유형</label>
        <select
          value={type}
          onChange={(e) => setType(e.target.value)}
          style={{ padding: "8px" }}
        >
          <option value="NOVEL">소설</option>
          <option value="FAIRYTALE">동화</option>
        </select>
      </div>
      <div style={{ marginTop: "20px" }}>
        <button onClick={handleSubmit} style={saveBtn}>저장</button>
        <button onClick={() => navigate("/adminpage/adminliterature")} style={cancelBtn}>취소</button>
      </div>
    </div>
  );
};

const saveBtn = {
  padding: "8px 16px",
  backgroundColor: "#007bff",
  color: "white",
  border: "none",
  borderRadius: "4px",
  marginRight: "10px",
  cursor: "pointer"
};

const cancelBtn = {
  padding: "8px 16px",
  backgroundColor: "#6c757d",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer"
};

export default AdminLiteratureAdd;