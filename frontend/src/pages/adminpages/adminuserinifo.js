import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../api/axiosInstance";

const AdminUserInfo = () => {
  const navigate = useNavigate();
  const { email } = useParams();
  const [user, setUser] = useState(null);

  // 스코어
  const [scoreList, setScoreList] = useState([]);
  const [newScore, setNewScore] = useState(0);
  const [modifiedScore, setModifiedScore] = useState(0);
  const [modifyTargetScoreNo, setModifyTargetScoreNo] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState("NEWS");
  const [selectedLanguage, setSelectedLanguage] = useState("KOREAN");

  const [showAddScoreModal, setShowAddScoreModal] = useState(false);
  const [showModifyScoreModal, setShowModifyScoreModal] = useState(false);

  // 문제 풀이 기록 가져오기
  const [totalLearning, setTotalLearning] = useState([]);

  // 회원 정보 가져오기
  const fetchUserInfo = async () => {
    try {
      const res = await axiosInstance.get(`/administrator/member/get-member`, {
        params: { email },
      });
      setUser(res.data);
    } catch (err) {
      console.error(err);
      alert("회원 정보를 불러오는 데 실패했습니다.");
    }
  };

  // 문제 풀이 기록 가져오기
  const fetchTotalLearning = async () => {
    try {
      const res = await axiosInstance.get(
        "/administrator/member/get-total-learning",
        {
          params: { email },
        }
      );
      setTotalLearning(res.data);
    } catch (err) {
      console.error("문제 풀이 기록 불러오기 실패:", err);
    }
  };

  // 스코어 가져오기
  const fetchScoreList = async () => {
    try {
      const res = await axiosInstance.get(
        "/administrator/member/get-score-list-by-email",
        {
          params: { email },
        }
      );
      setScoreList(res.data);
    } catch (err) {
      console.error("스코어 목록 불러오기 실패:", err);
      alert("스코어 목록을 불러오는 데 실패했습니다.");
    }
  };

  // 스코어 생성 함수
  const handleAddScore = async () => {
    try {
      await axiosInstance.post("/administrator/member/create-score-by-email", {
        email,
        score: Number(newScore),
        category: selectedCategory,
        language: selectedLanguage,
      });
      alert("스코어가 추가되었습니다.");
      setShowAddScoreModal(false);
      fetchScoreList(); // 스코어 목록 갱신
    } catch (err) {
      console.error(err);
      alert("스코어 추가 중 오류 발생");
    }
  };

  // 스코어 수정 함수
  const handleModifyScore = async () => {
    try {
      await axiosInstance.patch("/administrator/member/modify-score-by-email", {
        email,
        scoreNo: Number(modifyTargetScoreNo),
        score: Number(modifiedScore),
        category: selectedCategory,
        language: selectedLanguage,
      });
      alert("스코어가 수정되었습니다.");
      setShowModifyScoreModal(false);
      fetchScoreList();
    } catch (err) {
      console.error(err);
      alert("스코어 수정 중 오류 발생");
    }
  };

  useEffect(() => {
    fetchUserInfo();
    fetchTotalLearning();
    fetchScoreList();
  }, [email]);

  return (
    <>
      <div style={{ padding: "24px" }}>
        <button onClick={() => navigate("/adminpage")} style={backbtn}>
          뒤로가기
        </button>
        {user ? (
          <div>
            <h2>회원 상세 정보</h2>
            <p>
              <strong>이메일:</strong> {user.email}
            </p>
            <p>
              <strong>닉네임:</strong> {user.nickname}
            </p>
            <p>
              <strong>생일:</strong> {user.birthday}
            </p>
            <p>
              <strong>상태:</strong> {user.status}
            </p>
            <p>
              <strong>역할:</strong> {user.role}
            </p>
            <p>
              <strong>소셜 로그인:</strong> {user.socialProvider || "없음"}
            </p>
            <p>
              <strong>가입일:</strong>{" "}
              {new Date(user.createdAt).toLocaleDateString()}
            </p>
            <p>
              <strong>최근 수정일:</strong>{" "}
              {user.lastModifiedAt
                ? new Date(user.lastModifiedAt).toLocaleDateString()
                : "-"}
            </p>
            <p>
              <strong>탈퇴일:</strong>{" "}
              {user.withdrawAt
                ? new Date(user.withdrawAt).toLocaleDateString()
                : "-"}
            </p>
            {/* {user.profileImagePath && (
              <div>
                <strong>프로필 이미지:</strong>
                <br />
                <img
                  src={user.profileImagePath}
                  alt="프로필"
                  style={{ width: "100px", borderRadius: "50%" }}
                />
              </div>
            )} */}
          </div>
        ) : (
          <p>회원 정보를 불러오는 중입니다...</p>
        )}
        <button
          style={ADMIN_BUTTONS}
          onClick={() =>
            navigate(`/adminpage/adminuserinfo/${email}/attendance`)
          }
        >
          출석 기록 관리
        </button>

        <hr style={{ margin: "24px 0" }} />

        <div style={{ marginTop: "24px" }}>
          <div style={TITLE_STYLE}>
            <h3>스코어 목록</h3>
            <div style={BUTTON_LIST}>
              <button
                style={BUTTON_STYLE}
                onClick={() => setShowAddScoreModal(true)}
              >
                스코어 추가
              </button>
              <button
                style={BUTTON_STYLE}
                onClick={() => setShowModifyScoreModal(true)}
              >
                스코어 수정
              </button>
            </div>
          </div>
          {scoreList.length === 0 ? (
            <p>스코어 기록이 없습니다.</p>
          ) : (
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr>
                  <th style={thStyle}>점수 번호</th>
                  <th style={thStyle}>점수</th>
                  <th style={thStyle}>카테고리</th>
                  <th style={thStyle}>언어</th>
                  <th style={thStyle}>등록일</th>
                  <th style={thStyle}>수정일</th>
                </tr>
              </thead>
              <tbody>
                {scoreList.map((item) => (
                  <tr key={item.scoreNo}>
                    <td style={tdStyle}>{item.scoreNo}</td>
                    <td style={tdStyle}>{item.score}</td>
                    <td style={tdStyle}>{item.category}</td>
                    <td style={tdStyle}>{item.language}</td>
                    <td style={tdStyle}>
                      {new Date(item.createdAt).toLocaleDateString()}
                    </td>
                    <td style={tdStyle}>
                      {item.lastModifiedAt
                        ? new Date(item.lastModifiedAt).toLocaleDateString()
                        : "-"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        <hr style={{ margin: "24px 0" }} />

        <div style={TITLE_STYLE}>
          <h3>문제 풀이 기록</h3>
        </div>

        {Array.isArray(totalLearning) && totalLearning.length > 0 ? (
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr>
                <th style={thStyle}>문제 번호</th>
                <th style={thStyle}>지문 제목</th>
                <th style={thStyle}>정답 여부</th>
                <th style={thStyle}>응시일</th>
              </tr>
            </thead>
            <tbody>
              {totalLearning.map((item, idx) => (
                <tr key={idx}>
                  <td style={tdStyle}>{item.questionNo}</td>
                  <td style={tdStyle}>{item.title}</td>
                  <td style={tdStyle}>{item.isCorrect ? "정답" : "오답"}</td>
                  <td style={tdStyle}>
                    {new Date(item.createdAt).toLocaleString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>기록이 없습니다.</p>
        )}
      </div>
      {/* 스코어 추가 모달 */}
      {showAddScoreModal && (
        <div style={modalOverlayStyle}>
          <div style={modalContentStyle}>
            <h4>스코어 추가</h4>
            <label>스코어: </label>
            <input
              type="number"
              value={newScore}
              onChange={(e) => setNewScore(e.target.value)}
              style={inputStyle}
            />
            <br />
            <label>카테고리: </label>
            <select
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              style={inputStyle}
            >
              <option value="NEWS">뉴스</option>
              <option value="NOVEL">소설</option>
              <option value="FAIRY_TALE">동화</option>
              <option value="VOCABULARY">사전</option>
              <option value="FACTUAL">사실</option>
              <option value="INFERENTIAL">추론</option>
            </select>
            <br />
            <label>언어: </label>
            <select
              value={selectedLanguage}
              onChange={(e) => setSelectedLanguage(e.target.value)}
              style={inputStyle}
            >
              <option value="KOREAN">한국어</option>
              <option value="ENGLISH">영어</option>
              <option value="JAPANESE">일본어</option>
            </select>
            <div style={{ marginTop: "16px" }}>
              <button onClick={handleAddScore}>추가</button>
              <button onClick={() => setShowAddScoreModal(false)}>취소</button>
            </div>
          </div>
        </div>
      )}

      {/* 스코어 수정 모달 */}
      {showModifyScoreModal && (
        <div style={modalOverlayStyle}>
          <div style={modalContentStyle}>
            <h4>스코어 수정</h4>
            <label>수정할 스코어 No: </label>
            <input
              type="number"
              value={modifyTargetScoreNo}
              onChange={(e) => setModifyTargetScoreNo(e.target.value)}
              style={inputStyle}
            />
            <br />
            <label>수정할 스코어 값: </label>
            <input
              type="number"
              value={modifiedScore}
              onChange={(e) => setModifiedScore(e.target.value)}
              style={inputStyle}
            />
            <br />
            <label>카테고리: </label>
            <select
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              style={inputStyle}
            >
              <option value="NEWS">뉴스</option>
              <option value="NOVEL">소설</option>
              <option value="FAIRY_TALE">동화</option>
              <option value="VOCABULARY">사전</option>
              <option value="FACTUAL">사실</option>
              <option value="INFERENTIAL">추론</option>
            </select>
            <br />
            <label>언어: </label>
            <select
              value={selectedLanguage}
              onChange={(e) => setSelectedLanguage(e.target.value)}
              style={inputStyle}
            >
              <option value="KOREAN">한국어</option>
              <option value="ENGLISH">영어</option>
              <option value="JAPANESE">일본어</option>
            </select>
            <div style={{ marginTop: "16px" }}>
              <button onClick={handleModifyScore}>수정</button>
              <button onClick={() => setShowModifyScoreModal(false)}>
                취소
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

const backbtn = {
  marginBottom: "16px",
  padding: "8px 16px",
  backgroundColor: "#6c757d",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

const thStyle = {
  border: "1px solid #ccc",
  padding: "8px",
  backgroundColor: "#f2f2f2",
  textAlign: "left",
};

const tdStyle = {
  border: "1px solid #ddd",
  padding: "8px",
};

const TITLE_STYLE = {
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  marginBottom: "16px",
};

const BUTTON_LIST = {
  display: "flex",
  gap: "8px",
};

const BUTTON_STYLE = {
  marginBottom: "16px",
  padding: "8px 16px",
  backgroundColor: "#007BFF",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

const modalOverlayStyle = {
  position: "fixed",
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  backgroundColor: "rgba(0,0,0,0.5)",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  zIndex: 1000,
};

const modalContentStyle = {
  backgroundColor: "#fff",
  padding: "24px",
  borderRadius: "8px",
  minWidth: "300px",
  boxShadow: "0 4px 12px rgba(0,0,0,0.2)",
};

const ADMIN_BUTTONS = {
  marginBottom: "16px",
  padding: "8px 16px",
  backgroundColor: "#007BFF",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

const inputStyle = {
  width: "100%",
  padding: "6px 8px",
  marginBottom: "10px",
  borderRadius: "4px",
  border: "1px solid #ccc",
};

export default AdminUserInfo;
