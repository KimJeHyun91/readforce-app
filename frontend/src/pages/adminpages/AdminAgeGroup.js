import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstance';
import { useNavigate } from 'react-router-dom';

const AdminAgeGroup = () => {
    const [ageGroups, setAgeGroups] = useState([]);
    const [newAgeGroup, setNewAgeGroup] = useState('');
    const navigate = useNavigate(); 

    const fetchAgeGroups = async () => {
        try {
            const res = await axiosInstance.get('/administrator/age-group/get-all-list');
            setAgeGroups(res.data);
        } catch (err) {
            console.error('연령대 목록 불러오기 실패:', err);
            alert('연령대 목록을 불러오는 데 실패했습니다.');
        }
    };

    useEffect(() => {
        fetchAgeGroups();
    }, []);

    const handleAdd = async () => {
        const trimmed = newAgeGroup.trim();
        if (!trimmed) return alert('숫자로 연령대를 입력해주세요.');
        const num = Number(trimmed);
        if (isNaN(num) || num <= 0) return alert('유효한 숫자를 입력하세요.');

        try {
            await axiosInstance.post('/administrator/age-group/create', {
                ageGroup: num
            });
            alert('연령대가 추가되었습니다.');
            setNewAgeGroup('');
            fetchAgeGroups();
        } catch (err) {
            console.error('연령대 추가 실패:', err);
            alert('추가 중 오류가 발생했습니다.');
        }
    };

    const handleDelete = async (ageGroupNo) => {
        if (!window.confirm('정말로 삭제하시겠습니까?')) return;
        try {
            await axiosInstance.delete('/administrator/age-group/delete', {
                params: { ageGroupNo }
            });
            alert('삭제되었습니다.');
            fetchAgeGroups();
        } catch (err) {
            console.error('삭제 실패:', err);
            alert('삭제 중 오류가 발생했습니다.');
        }
    };

    return (
        <div style={{ padding: '24px' }}>
            <h2>연령대 관리</h2>
            <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>

            <div style={{ marginTop: '20px' }}>
                <input
                    type="number"
                    min="0"
                    placeholder="예: 10 (10대)"
                    value={newAgeGroup}
                    onChange={(e) => setNewAgeGroup(e.target.value)}
                    style={{ padding: '8px', marginRight: '8px', width: '160px' }}
                />
                <button
                    onClick={handleAdd}
                    style={{
                        padding: '8px 16px',
                        backgroundColor: '#007BFF',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}
                >
                    추가
                </button>
            </div>

            <table style={{ marginTop: '24px', width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th style={thStyle}>ID</th>
                        <th style={thStyle}>연령대</th>
                        <th style={thStyle}>삭제</th>
                    </tr>
                </thead>
                <tbody>
                    {ageGroups.map((group) => (
                        <tr key={group.ageGroupNo}>
                            <td style={tdStyle}>{group.ageGroupNo}</td>
                            <td style={tdStyle}>{`${group.ageGroup}대`}</td>
                            <td style={tdStyle}>
                                <button
                                    onClick={() => handleDelete(group.ageGroupNo)}
                                    style={{
                                        color: 'white',
                                        backgroundColor: 'red',
                                        border: 'none',
                                        padding: '6px 12px',
                                        borderRadius: '4px',
                                        cursor: 'pointer'
                                    }}
                                >
                                    삭제
                                </button>
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
    border: '1px solid #ccc',
    padding: '8px',
    backgroundColor: '#f8f8f8',
    textAlign: 'left',
};

const tdStyle = {
    border: '1px solid #ddd',
    padding: '8px',
};

export default AdminAgeGroup;
