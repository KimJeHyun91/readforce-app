import { useEffect, useState } from "react";
import axiosInstance from "../../api/axiosInstance";
import { useNavigate } from "react-router-dom";

const AdminCategoryEdit = () => {
  const [categoryList, setCategoryList] = useState([]);
  const [newCategory, setNewCategory] = useState("");
  const [editModeId, setEditModeId] = useState(null);
  const [editValue, setEditValue] = useState("");
  const navigate = useNavigate();

  const fetchCategories = async () => {
    try {
      const res = await axiosInstance.get("/administrator/category/get-all-list");
      setCategoryList(res.data);
    } catch (err) {
      console.error("불러오기 실패", err);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleCreate = async () => {
    if (!newCategory.trim()) return alert("카테고리를 입력하세요.");
    try {
      await axiosInstance.post("/administrator/category/create", {
        category: newCategory.trim()
      });
      alert("카테고리 생성 완료!");
      setNewCategory("");
      fetchCategories();
    } catch (err) {
      console.error(err);
      alert("생성 실패");
    }
  };

  const handleModify = async (categoryNo) => {
    try {
      await axiosInstance.patch("/administrator/category/modify", {
        categoryNo,
        category: editValue.trim()
      });
      alert("수정 완료!");
      setEditModeId(null);
      fetchCategories();
    } catch (err) {
      console.error(err);
      alert("수정 실패");
    }
  };

  const handleDelete = async (categoryNo) => {
    const ok = window.confirm("정말 삭제하시겠습니까?");
    if (!ok) return;
    try {
      await axiosInstance.delete("/administrator/category/delete", {
        params: { categoryNo }
      });
      alert("삭제 완료!");
      fetchCategories();
    } catch (err) {
      console.error(err);
      alert("삭제 실패");
    }
  };

  return (
    <div style={{ padding: "24px" }}>
      <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>
      <h2>카테고리 관리</h2>

      <div style={{ margin: "16px 0" }}>
        <input
          placeholder="새 카테고리명 입력"
          value={newCategory}
          onChange={(e) => setNewCategory(e.target.value)}
          style={{ marginRight: "8px" }}
        />
        <button onClick={handleCreate}>생성</button>
      </div>

      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th style={thStyle}>번호</th>
            <th style={thStyle}>카테고리명</th>
            <th style={thStyle}>생성일</th>
            <th style={thStyle}>관리</th>
          </tr>
        </thead>
        <tbody>
          {categoryList.map((item) => (
            <tr key={item.categoryNo}>
              <td style={tdStyle}>{item.categoryNo}</td>
              <td style={tdStyle}>
                {editModeId === item.categoryNo ? (
                  <input
                    value={editValue}
                    onChange={(e) => setEditValue(e.target.value)}
                  />
                ) : (
                  item.category
                )}
              </td>
              <td style={tdStyle}>{new Date(item.createdAt).toLocaleDateString()}</td>
              <td style={tdStyle}>
                {editModeId === item.categoryNo ? (
                  <>
                    <button onClick={() => handleModify(item.categoryNo)}>저장</button>
                    <button onClick={() => setEditModeId(null)}>취소</button>
                  </>
                ) : (
                  <>
                    <button onClick={() => {
                      setEditModeId(item.categoryNo);
                      setEditValue(item.category);
                    }}>수정</button>
                    <button onClick={() => handleDelete(item.categoryNo)} style={{ color: "red", marginLeft: "8px" }}>
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

export default AdminCategoryEdit;