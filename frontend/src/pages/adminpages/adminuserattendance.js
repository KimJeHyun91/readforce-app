import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axiosInstance from "../../api/axiosInstance";

const AdminUserAttendance = () => {
  const navigate = useNavigate();
  const { email } = useParams();
  const [attendanceList, setAttendanceList] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().substring(0, 10));

  const fetchAttendanceList = async () => {
    try {
      const res = await axiosInstance.get("/administrator/member/get-attendance-list-by-email", {
        params: { email },
      });
      setAttendanceList(res.data);
    } catch (err) {
      console.error(err);
      alert("출석 정보를 불러오는 데 실패했습니다.");
    }
  };

  useEffect(() => {
    fetchAttendanceList();
  }, [email]);

  const handleAddAttendance = async () => {
    try {
      await axiosInstance.post("/administrator/member/add-attendance-by-email", {
        email,
        attendanceDate: selectedDate,
      });
      alert("출석이 추가되었습니다.");
      setShowModal(false);
      fetchAttendanceList();
    } catch (err) {
      console.error(err);
      alert("출석 추가 중 오류 발생");
    }
  };

  const handleDelete = async (attendanceNo) => {
    if (!window.confirm("정말 이 출석 기록을 삭제하시겠습니까?")) return;
    try {
      await axiosInstance.delete("/administrator/member/delete-attendance-by-email", {
        params: { attendanceNo },
      });
      alert("삭제 완료되었습니다.");
      fetchAttendanceList();
    } catch (err) {
      console.error(err);
      alert("출석 삭제 중 오류가 발생했습니다.");
    }
  };

  return (
    <div>
      <button onClick={() => navigate(`/adminpage/adminuserinfo/${email}`)} style={styles.backBtn}>뒤로가기</button>
      <h3>출석 내역</h3>
      <button onClick={() => setShowModal(true)} style={styles.addBtn}>출석 추가</button>
      {attendanceList.length === 0 ? (
        <p>출석 기록이 없습니다.</p>
      ) : (
        <ul style={{ listStyle: "none", paddingLeft: 0 }}>
          {attendanceList.map((a) => (
            <li key={a.attendanceNo} style={{ marginBottom: "8px" }}>
              출석 날짜: {new Date(a.attendanceDate).toLocaleDateString()}
              <button onClick={() => handleDelete(a.attendanceNo)} style={styles.deleteBtn}>삭제</button>
            </li>
          ))}
        </ul>
      )}
      {showModal && (
        <div style={styles.modalOverlay}>
          <div style={styles.modalContent}>
            <h4>출석 날짜 선택</h4>
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              style={{ marginBottom: "12px" }}
            />
            <div>
              <button onClick={handleAddAttendance} style={styles.confirmBtn}>추가</button>
              <button onClick={() => setShowModal(false)} style={styles.cancelBtn}>취소</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

const styles = {
  backBtn: {
    marginBottom: "16px",
    padding: "8px 16px",
    backgroundColor: "#6c757d",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
  deleteBtn: {
    marginLeft: "12px",
    padding: "4px 8px",
    backgroundColor: "#dc3545",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
  addBtn: {
    marginBottom: "12px",
    padding: "8px 16px",
    backgroundColor: "#28a745",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
  modalOverlay: {
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: "rgba(0,0,0,0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  modalContent: {
    backgroundColor: "white",
    padding: "24px",
    borderRadius: "8px",
    width: "300px",
    textAlign: "center",
  },
  confirmBtn: {
    padding: "8px 16px",
    marginRight: "10px",
    backgroundColor: "#007bff",
    color: "white",
    border: "none",
    borderRadius: "4px",
  },
  cancelBtn: {
    padding: "8px 16px",
    backgroundColor: "#6c757d",
    color: "white",
    border: "none",
    borderRadius: "4px",
  },
};

export default AdminUserAttendance;