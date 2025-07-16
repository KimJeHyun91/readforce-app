import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';

const AdminPage = () => {
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);
    const [showAddForm, setShowAddForm] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [nickname, setNickname] = useState('');
    const [birthday, setBirthday] = useState('');
    const [role, setRole] = useState('USER');

    useEffect(() => {
        const nickname = localStorage.getItem("nickname");
        if (nickname !== "관리자") {
            alert("접근 권한이 없습니다.");
            navigate("/");
        }
    }, []);

    const fetchUsers = async () => {
        try {
            const res = await axiosInstance.get("/administrator/member/get-all-member-list");
            setUsers(res.data);
        } catch (error) {
            console.error("회원 목록 불러오기 실패", error);
            if (error.response?.status === 403) {
                alert("관리자 권한이 없습니다.");
            } else if (error.response?.status === 401) {
                alert("로그인 정보가 만료되었습니다. 다시 로그인 해주세요.");
            }
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleChangeStatus = async (email, newStatus) => {
        try {
            await axiosInstance.patch("/administrator/member/modify", { email, status: newStatus });
            updateUserStatus(email, newStatus);
        } catch (err) {
            console.error(err);
            alert("계정 상태 변경 실패");
        }
    };

    const handleDelete = async (email) => {
        if (!window.confirm("정말로 이 회원을 삭제하시겠습니까?")) return;
        try {
            await axiosInstance.delete(`/administrator/member/delete`, {
                params: { email },
            });

            alert("회원이 삭제되었습니다.");
            setUsers((prev) => prev.filter((user) => user.email !== email));
        } catch (err) {
            console.error(err);
            alert("계정 삭제 실패");
        }
    };

    const updateUserStatus = (email, newStatus) => {
        setUsers((prev) =>
            prev.map((user) =>
                user.email === email ? { ...user, status: newStatus } : user
            )
        );
    };

    const handleAddMember = async (e) => {
        e.preventDefault();
        try {
            await axiosInstance.post("/administrator/member/add-member", {
                email,
                password,
                nickname,
                birthday,
                role
            });
            alert("멤버가 추가되었습니다.");
            setShowAddForm(false);
            setEmail('');
            setPassword('');
            setNickname('');
            setBirthday('');
            setRole('USER');
            fetchUsers();
        } catch (error) {
            console.error("멤버 추가 실패:", error);
            alert("멤버 추가 실패");
        }
    };

    return (
        <div style={{ padding: "24px" }}>
            <span style={ADMIN_BUTTONS_LIST}>
                <button style={ADMIN_BUTTONS} onClick={() => navigate('/adminpage/adminpassage')}>문제 관리</button>
                <button style={ADMIN_BUTTONS} onClick={() => navigate("/adminpage/average-stat")}>평균 통계</button>
                <button style={ADMIN_BUTTONS} onClick={() => navigate("/adminpage/category-edit")}>카테고리 편집</button>
                <button style={ADMIN_BUTTONS} onClick={() => navigate("/adminpage/classification-edit")}>분류 편집</button>
                <button style={ADMIN_BUTTONS} onClick={() => navigate("/adminpage/language")}>언어 편집</button>
                <button style={ADMIN_BUTTONS} onClick={() => navigate("/adminpage/level")}>난이도 편집</button>
                <button style={ADMIN_BUTTONS} onClick={() => navigate("/adminpage/age-group")}>연령대 관리</button>
            </span>

            <div style={ADMIN_TITLE}>
                <h2>회원 관리</h2>
                {/* <button style={ADMIN_AI_BUTTONS} onClick={() => setShowAddForm(true)}>멤버 추가</button> */}
            </div>

           {/* {showAddForm && (
                    <div style={overlay}>
                        <form onSubmit={handleAddMember} style={centeredForm}>
                            <h3 style={formTitle}>멤버 추가</h3>
                            <div style={formGroup}>
                                <label style={formLabel}>이메일</label>
                                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required style={formInput} />
                            </div>
                            <div style={formGroup}>
                                <label style={formLabel}>비밀번호</label>
                                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required style={formInput} />
                            </div>
                            <div style={formGroup}>
                                <label style={formLabel}>닉네임</label>
                                <input type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} required style={formInput} />
                            </div>
                            <div style={formGroup}>
                                <label style={formLabel}>생일</label>
                                <input type="date" value={birthday} onChange={(e) => setBirthday(e.target.value)} style={formInput} />
                            </div>
                            <div style={formGroup}>
                                <label style={formLabel}>권한</label>
                                <select value={role} onChange={(e) => setRole(e.target.value)} style={formInput}>
                                    <option value="USER">USER</option>
                                    <option value="ADMIN">ADMIN</option>
                                </select>
                            </div>
                            <div style={formButtonGroup}>
                                <button type="submit" style={submitButton}>저장</button>
                                <button type="button" onClick={() => setShowAddForm(false)} style={cancelButton}>취소</button>
                            </div>
                        </form>
                    </div>
                )} */}

            <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "16px" }}>
                <thead>
                    <tr>
                        <th style={thStyle}>닉네임</th>
                        <th style={thStyle}>이메일</th>
                        <th style={thStyle}>생일</th>
                        <th style={thStyle}>가입일</th>
                        <th style={thStyle}>수정일</th>
                        <th style={thStyle}>탈퇴일</th>
                        <th style={thStyle}>소셜</th>
                        <th style={thStyle}>권한</th>
                        <th style={thStyle}>상태</th>
                        <th style={thStyle}>관리</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.email}>
                            <td style={tdStyle}>{user.nickname}</td>
                            <td
                                style={{
                                    cursor: "pointer",
                                    color: "blue",
                                    border: "1px solid #ddd",
                                    padding: "8px",
                                }}
                                onClick={() => navigate(`/adminpage/adminuserinfo/${user.email}`)}
                            >
                                {user.email}
                            </td>
                            <td style={tdStyle}>{user.birthday || "-"}</td>
                            <td style={tdStyle}>{new Date(user.createdAt).toLocaleDateString()}</td>
                            <td style={tdStyle}>{user.lastModifiedAt ? new Date(user.lastModifiedAt).toLocaleDateString() : "-"}</td>
                            <td style={tdStyle}>{user.withdrawAt ? new Date(user.withdrawAt).toLocaleDateString() : "-"}</td>
                            <td style={tdStyle}>{user.socialProvider || "-"}</td>
                            <td style={tdStyle}>{user.role}</td>
                            <td style={{
                                color: user.status === "ACTIVE" ? "green" : "red",
                                border: "1px solid #ddd",
                                padding: "8px",
                            }}>
                                {user.status === "ACTIVE" ? "활성화" : "비활성화"}
                            </td>
                            <td style={tdStyle}>
                                {user.nickname !== "관리자" && (
                                    <>
                                        {user.status === "PENDING_DELETION" ? (
                                            <>
                                                <button onClick={() => handleChangeStatus(user.email, "ACTIVE")} style={{ color: "green" }}>
                                                    계정 활성화
                                                </button>
                                                <button onClick={() => handleDelete(user.email)} style={{ color: "gray", marginLeft: "8px" }}>
                                                    계정 삭제
                                                </button>
                                            </>
                                        ) : (
                                            <button onClick={() => handleChangeStatus(user.email, "PENDING_DELETION")} style={{ color: "red" }}>
                                                계정 비활성화
                                            </button>
                                        )}
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

const ADMIN_BUTTONS_LIST = {
    display: "flex",
    gap: "8px"
};

const ADMIN_BUTTONS = {
    marginBottom: "16px",
    padding: "8px 16px",
    backgroundColor: "#007BFF",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer"
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

const ADMIN_TITLE = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "16px"
};

const ADMIN_AI_BUTTONS = {
    marginBottom: "16px",
    padding: "8px 16px",
    backgroundColor: "#007BFF",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer"
};
const overlay = {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100vw',
    height: '100vh',
    backgroundColor: 'rgba(0,0,0,0.3)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1000,
};

const centeredForm = {
    backgroundColor: '#fafafa',
    padding: '24px',
    borderRadius: '8px',
    boxShadow: '0 2px 8px rgba(0,0,0,0.15)',
    width: '90%',
    maxWidth: '400px',
};

const formTitle = {
    marginBottom: '20px',
    fontSize: '18px',
    fontWeight: 'bold',
};

const formGroup = {
    display: 'flex',
    flexDirection: 'column',
    marginBottom: '16px',
};

const formLabel = {
    marginBottom: '6px',
    fontSize: '14px',
    fontWeight: '500',
};

const formInput = {
    padding: '8px 12px',
    fontSize: '14px',
    borderRadius: '4px',
    border: '1px solid #ccc',
};

const formButtonGroup = {
    display: 'flex',
    justifyContent: 'flex-end',
    gap: '8px',
};

const submitButton = {
    backgroundColor: '#007BFF',
    color: '#fff',
    border: 'none',
    padding: '8px 16px',
    borderRadius: '4px',
    cursor: 'pointer',
};

const cancelButton = {
    backgroundColor: '#6c757d',
    color: '#fff',
    border: 'none',
    padding: '8px 16px',
    borderRadius: '4px',
    cursor: 'pointer',
};


export default AdminPage;
