import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from '../../api/axiosInstance';

const AdminLiterature = () => {
    const [literatureList, setLiteratureList] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // 문학 목록 불러오기
        const fetchLiterature = async () => {
            try {
                const res = await axiosInstance.post("/admin/get-all-literature-list");
                setLiteratureList(res.data);
            } catch (err) {
                console.error(err);
                alert("문학 데이터를 불러오는 데 실패했습니다.");
            }
        };

        fetchLiterature();
    }, []);

    // 문학 문제 생성
    const handleGenerateQuiz = async () => {
        if (!window.confirm("문학 문제를 생성하시겠습니까?\n(문제가 없는 문단에만 생성됩니다)")) return;

        try {
            await axiosInstance.post("/admin/generate-creative-literature-quiz");
            alert("문학 문제가 생성되었습니다!");

            // 문학 목록 갱신 (옵션)
            const refresh = await axiosInstance.post("/admin/get-all-literature-list");
            setLiteratureList(refresh.data);

        } catch (err) {
            console.error(err);
            alert("문학 문제 생성 중 오류 발생");
        }
    };

    // 문학 삭제 ( 문제, 문단 함께 )
    const handleDeleteLiterature = async (literatureNo) => {
        if (!window.confirm("정말 이 문학과 관련된 모든 데이터를 삭제하시겠습니까?")) return;

        try {
            await axiosInstance.delete(`/admin/delete-literature-and-literature-paragraph-and-literature-quiz-by-literature-no`, {
                params: { literature_no: literatureNo }
            });
            alert("삭제되었습니다.");

            // 목록 갱신
            setLiteratureList(prev => prev.filter(lit => lit.literature_no !== literatureNo));
        } catch (err) {
            console.error(err);
            alert("문학 삭제 중 오류가 발생했습니다.");
        }
    };

    return (
        <div style={{ padding: "24px" }}>
            <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>
            <div style={ADMIN_LITERATURE_TITLE}>
                <div>
                    <h2>전체 문학 목록</h2>
                </div>
                <div style={LITERATURE_BUTTONS_LIST}>
                   <button
                        style={LITERATURE_BUTTONS_BUTTONS}
                        onClick={() => navigate("/adminpage/adminliterature/add-paragraph")}
                    >
                        Passage 추가
                    </button>


                    <button
                        style={LITERATURE_BUTTONS_BUTTONS}
                        onClick={handleGenerateQuiz}
                    >
                        문학 문제 생성
                    </button>
                </div>
            </div>
            <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "16px" }}>
                <thead>
                    <tr>
                        <th style={thStyle}>문학 번호</th>
                        <th style={thStyle}>제목</th>
                        <th style={thStyle}>유형</th>
                        <th style={thStyle}>작성일</th>
                        <th style={thStyle}>관리</th>
                    </tr>
                </thead>
                <tbody>
                    {literatureList.map((lit) => (
                        <tr key={lit.literature_no}>
                            <td style={tdStyle}>{lit.literature_no}</td>
                            <td
                                style={{ ...tdStyle, color: "blue", cursor: "pointer" }}
                                onClick={() => navigate(`/adminpage/adminliterature/${lit.literature_no}`)}
                            >
                                {lit.title}
                            </td>
                            <td style={tdStyle}>{CONTENT_TYPE[lit.type] || lit.type}</td>
                            <td style={tdStyle}>{new Date(lit.created_date).toLocaleDateString()}</td>
                            <td style={tdStyle}>
                                <button
                                    style={{ color: "red", border: "none", background: "none", cursor: "pointer" }}
                                    onClick={() => handleDeleteLiterature(lit.literature_no)}
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

const thStyle = {
    border: "1px solid #ccc",
    padding: "8px",
    backgroundColor: "#f8f9fa",
    textAlign: "left"
};

const tdStyle = {
    border: "1px solid #ddd",
    padding: "8px"
};

const backbtn = {
    marginBottom: "16px",
    padding: "8px 16px",
    backgroundColor: "#6c757d",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer"
}

const CONTENT_TYPE = {
    MYSTERY: '미스터리',
    HISTORY: '역사',
    CLASSIC: '고전문학',
    MODERN: '현대문학',
    CHILDREN: '영유아'

};

const ADMIN_LITERATURE_TITLE = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "16px"
}

const LITERATURE_BUTTONS_LIST = {
    display: "flex",
    gap: "8px"
}

const LITERATURE_BUTTONS_BUTTONS = {
    marginBottom: "16px",
    padding: "8px 16px",
    backgroundColor: "#007BFF",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer"
}

export default AdminLiterature;