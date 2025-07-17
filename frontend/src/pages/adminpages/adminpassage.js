import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';

const AdminPassage = () => {
    const navigate = useNavigate();

    const [loadingTestPassage, setLoadingTestPassage] = useState(false);
    const [loadingTestQuestion, setLoadingTestQuestion] = useState(false);

    const [loadingPassage, setLoadingPassage] = useState(false);
    const [loadingQuestion, setLoadingQuestion] = useState(false);

    // 지문 생성 모달
    const [showPassageModal, setShowPassageModal] = useState(false);
    const [language, setLanguage] = useState("KOREAN");
    const [level, setLevel] = useState(1);
    const [category, setCategory] = useState("NEWS");
    const [type, setType] = useState("ECONOMY");
    const [classification, setClassification] = useState("NORMAL");

    // 지문 불러오기
    const [passageList, setPassageList] = useState([]);
    const [count, setCount] = useState(1);

    // 챌린지 문제 전환
    const [loadingChallenge, setLoadingChallenge] = useState(false);

    // 지문 직접 추가 모달
    const [showUploadModal, setShowUploadModal] = useState(false);
    const [newPassage, setNewPassage] = useState({
        title: "",
        content: "",
        author: "",
        language: "KOREAN",
        classification: "NORMAL",
        category: "NEWS",
        type: "ECONOMY",
        level: 1
    });
    const TYPE_OPTIONS = {
    NEWS: [
        { value: "0", label: "정치" },
        { value: "1", label: "경제" },
        { value: "2", label: "사회" },
        { value: "3", label: "문화생활" },
        { value: "4", label: "IT과학" },
        { value: "5", label: "세계" },
        { value: "6", label: "스포츠" },
        { value: "7", label: "연예" }
    ],
    NOVEL: [
        { value: "8", label: "추리" },
        { value: "9", label: "공상과학" },
        { value: "10", label: "판타지" },
        { value: "11", label: "로맨스" },
        { value: "12", label: "역사" },
        { value: "13", label: "모험" },
        { value: "14", label: "스릴러" }
    
    ],
    FAIRY_TALE: [
        { value: "15", label: "생활" },
        { value: "16", label: "전통" },
        { value: "17", label: "정보" }
    ]
    
};
    const [filterLanguage, setFilterLanguage] = useState("ALL");
    const [filterCategory, setFilterCategory] = useState("ALL");
    const [filterLevel, setFilterLevel] = useState("ALL");
    const [filterClassification, setFilterClassification] = useState("ALL");

    useEffect(() => {
    const defaultType = TYPE_OPTIONS[category]?.[0]?.value || "";
    setType(defaultType);
}, [category]);

    // 지문 불러오기
    useEffect(() => {
        const fetchPassages = async () => {
            try {
                const res = await axiosInstance.get("/passage/get-all-passages");
                setPassageList(res.data);
            } catch (err) {
                console.error("전체 지문 불러오기 실패", err);
            }
        };

        fetchPassages();
    }, []);

    // 테스트 지문 생성
        const handleGenerateTestPassage = async () => {
            setLoadingTestPassage(true);
            try {
                const res = await axiosInstance.post("/ai/generate-test-passage?language=KOREAN");
                const message = res.data.message || "테스트 지문 생성 완료!";
                alert("성공 : " + message);
            } catch (err) {
                console.error(err);
                alert("실패 : 지문 생성 중 오류가 발생했습니다.");
            } finally {
                setLoadingTestPassage(false);
            }
        };
        // 테스트 문제 생성 
        const handleGenerateTestQuestion = async () => {
            setLoadingTestQuestion(true);
            try {
                const res = await axiosInstance.post("/ai/generate-test-question?language=KOREAN");
                const message = res.data.message || "테스트 문제 생성 완료!";
                alert("성공 : " + message);
            } catch (err) {
                console.error(err);
                alert("실패 : 문제 생성 중 오류가 발생했습니다.");
            } finally {
                setLoadingTestQuestion(false);
            }
        };

    // 일반 지문 생성
    const handleGeneratePassageWithParams = async () => {
        setLoadingPassage(true);
        try {
            const res = await axiosInstance.post("/ai/generate-passage", {
                language,
                level,
                category,
                type,
                classification,
                count
            });
            const data = await res.data;
            alert("일반 지문 생성 성공!");
            setShowPassageModal(false);
        } catch (err) {
            console.error(err);
            alert("실패 : 지문 생성 중 오류가 발생했습니다.");
        } finally {
            setLoadingPassage(false);
        }
    };

    // 일반 문제 생성
    const handleGenerateQuestion = async () => {
        setLoadingQuestion(true);
        try {
            const res = await axiosInstance.post("/ai/generate-question");
            const data = await res.data;
            alert("일반 문제 생성 성공!");
        } catch (err) {
            console.error(err);
            alert("실패 : 문제 생성 중 오류가 발생했습니다.");
        } finally {
            setLoadingQuestion(false);
        }
    };

    const handleUpdateToChallenge = async () => {
    setLoadingChallenge(true);
    try {
        const res = await axiosInstance.post("/challenge/update-to-challenges");
        alert("성공: 챌린지 문제로 업데이트 완료!");
    } catch (err) {
        console.error("챌린지 문제 변환 실패:", err);
        alert("실패: 챌린지 문제 변환 중 오류가 발생했습니다.");
    } finally {
        setLoadingChallenge(false);
    }
};
    // 지문 삭제
    const handleDeletePassage = async (passageNo) => {
        const confirmDelete = window.confirm("정말 이 지문을 삭제하시겠습니까?");
        if (!confirmDelete) return;

        try {
            const res = await axiosInstance.delete(`/administrator/passage/delete`, {
                params: { passageNo },
            });

            alert("성공 : 지문이 삭제되었습니다.");
            // 삭제 후 리스트 갱신
            setPassageList(prev => prev.filter(p => p.passageNo !== passageNo));
        } catch (err) {
            console.error("실패 : 지문 삭제 중 오류 발생:", err);
            alert("실패 : 지문 삭제 실패");
        }
    };

    // 지문 직접 등록
    const handleUploadPassage = async () => {
        try {
            const res = await axiosInstance.post("/administrator/passage/upload-passage", newPassage);
            alert("성공 : 지문 등록 완료!");
            setShowUploadModal(false);
            // 다시 목록 불러오기
            const refreshed = await axiosInstance.get("/passage/get-all-passages");
            setPassageList(refreshed.data);
        } catch (err) {
            console.error("지문 등록 실패:", err);
            alert("실패 : 등록 실패");
        }
    };
    return (
        <>
            {showPassageModal && (
                <div style={{
                    position: "fixed", top: 0, left: 0, width: "100%", height: "100%",
                    backgroundColor: "rgba(0,0,0,0.5)", display: "flex", justifyContent: "center", alignItems: "center"
                }}>
                    <div style={{
                        backgroundColor: "white", padding: "24px", borderRadius: "8px", width: "400px"
                    }}>
                        <h3>AI 지문 생성 옵션</h3>

                        <label>언어:</label>
                        <select value={language} onChange={(e) => setLanguage(e.target.value)}>
                            <option value="KOREAN">한국어</option>
                            <option value="ENGLISH">영어</option>
                            <option value="JAPANESE">일본어</option>

                        </select>

                        <br /><label>난이도:</label>
                        <select value={level} onChange={(e) => setLevel(parseInt(e.target.value))}>
                            {[...Array(10)].map((_, idx) => (
                                <option key={idx + 1} value={idx + 1}>{idx + 1}</option>
                            ))}
                        </select>

                        <br /><label>카테고리:</label>
                        <select value={category} onChange={(e) => setCategory(e.target.value)}>
                            <option value="NEWS">뉴스</option>
                        </select>

                        <br /><label>유형:</label>
                        <select value={type} onChange={(e) => setType(e.target.value)}>
                            {(TYPE_OPTIONS[category] || []).map(option => (
                                <option key={option.value} value={option.value}>
                                    {option.label}
                                </option>
                            ))}
                        </select>


                        <br /><label>분류:</label>
                        <select value={classification} onChange={(e) => setClassification(e.target.value)}>
                            <option value="NORMAL">일반</option>
                            <option value="CHALLANGE">챌린지</option>
                            <option value="TEST">테스트</option>
                        </select>

                        <br /><label>생성 개수:</label> {/* ✅ 추가됨 */}
                        <input type="number" min="1" max="20" value={count} onChange={(e) => setCount(parseInt(e.target.value) || 1)} />

                        <div style={{ marginTop: "16px", display: "flex", justifyContent: "space-between" }}>
                            <button onClick={handleGeneratePassageWithParams} disabled={loadingPassage}>
                                {loadingPassage ? "생성 중..." : "생성"}
                            </button>
                            <button onClick={() => setShowPassageModal(false)}>닫기</button>
                        </div>
                    </div>
                </div>
            )}
            {showUploadModal && (
                <div style={modalBackdrop}>
                    <div style={modalBox}>
                        <h3>지문 직접 등록</h3>
                        제목
                        <input type="text" placeholder="제목" value={newPassage.title} onChange={(e) => setNewPassage({ ...newPassage, title: e.target.value })} />
                        내용
                        <textarea placeholder="내용" value={newPassage.content} onChange={(e) => setNewPassage({ ...newPassage, content: e.target.value })} />
                        작성자
                        <input type="text" placeholder="작성자" value={newPassage.author} onChange={(e) => setNewPassage({ ...newPassage, author: e.target.value })} />
                        언어
                        <select value={newPassage.language} onChange={(e) => setNewPassage({ ...newPassage, language: e.target.value })}>
                            <option value="KOREAN">한국어</option>
                            <option value="JAPANESE">일본어</option>
                            <option value="ENGLISH">영어</option>
                        </select>
                        카테고리
                        <select value={newPassage.category} onChange={(e) => setNewPassage({ ...newPassage, category: e.target.value })}>
                            <option value="NEWS">뉴스</option>
                            <option value="NOVEL">소설</option>
                            <option value="FAIRY_TALE">동화</option>
                            <option value="VOCABULARY">어휘</option>
                            <option value="FACTUAL">사실</option>
                            <option value="INFERENTIAL">추론</option>
                        </select>
                        타입
                        <select value={newPassage.type} onChange={(e) => setNewPassage({ ...newPassage, type: e.target.value })}>
                            <option value="0">정치</option>
                            <option value="1">경제</option>
                            <option value="2">사회</option>
                            <option value="3">문화생활</option>
                            <option value="4">IT과학</option>
                            <option value="5">세계</option>
                            <option value="6">스포츠</option>
                            <option value="7">연예</option>
                            <option value="8">추리</option>
                            <option value="9">공상과학</option>
                            <option value="10">판타지</option>
                            <option value="11">로맨스</option>
                            <option value="12">역사</option>
                            <option value="13">모험</option>
                            <option value="14">스릴러</option>
                            <option value="15">삶의 조각</option>
                            <option value="16">전통</option>
                            <option value="17">정보</option>
                        </select>
                        문제 종류
                        <select value={newPassage.classification} onChange={(e) => setNewPassage({ ...newPassage, classification: e.target.value })}>
                            <option value="NORMAL">일반</option>
                            <option value="CHALLENGE">도전</option>
                            <option value="TEST">테스트</option>
                        </select>
                        난이도
                        <select value={newPassage.level} onChange={(e) => setNewPassage({ ...newPassage, level: parseInt(e.target.value) })}>
                            <option value={1}>1</option>
                            <option value={2}>2</option>
                            <option value={3}>3</option>
                            <option value={4}>4</option>
                            <option value={5}>5</option>
                            <option value={6}>6</option>
                            <option value={7}>7</option>
                            <option value={8}>8</option>
                            <option value={9}>9</option>
                            <option value={10}>10</option>
                        </select>

                        <div style={{ marginTop: "12px", display: "flex", gap: "12px" }}>
                            <button onClick={handleUploadPassage}>등록</button>
                            <button onClick={() => setShowUploadModal(false)}>닫기</button>
                        </div>
                    </div>
                </div>
            )}
            <div style={{ padding: "24px" }}>
                <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>
                <div style={ADMIN_PASSAGE_TITLE}>
                    <div>
                        <h2>전체 지문 목록</h2>
                    </div>
                    <div style={ADMIN_BUTTONS_LIST}>
                        <button style={ADMIN_BUTTONS} onClick={handleGenerateTestPassage} disabled={loadingTestPassage}>
                            {loadingTestPassage ? '생성 중...' : '테스트 지문 생성'}
                        </button>
                        <button style={ADMIN_BUTTONS} onClick={handleGenerateTestQuestion} disabled={loadingTestQuestion}>
                            {loadingTestQuestion ? '생성 중...' : '테스트 문제 생성'}
                        </button>
                        <button style={ADMIN_BUTTONS} onClick={() => setShowPassageModal(true)} disabled={loadingPassage}>
                            {loadingPassage ? '생성 중...' : '일반 지문 생성'}
                        </button>
                        {/* <button style={ADMIN_BUTTONS} onClick={handleGenerateQuestion} disabled={loadingQuestion}>
                            {loadingQuestion ? '생성 중...' : '일반 문제 생성'}
                        </button> */}
                         <button style={ADMIN_BUTTONS} onClick={handleUpdateToChallenge} disabled={loadingChallenge}>
                            {loadingChallenge ? '변환 중...' : '챌린지 문제 변환'}
                        </button>
                        <button style={ADMIN_BUTTONS_2} onClick={() => setShowUploadModal(true)}>
                            지문 직접 등록
                        </button>                    </div>
                </div>
                
                <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "16px" }}>
                       <thead>
                        <tr>
                            <th style={{ ...thStyle, width: "60px" }}>번호</th>
                            <th style={{ ...thStyle, minWidth: "200px" }}>제목</th>
                            <th style={{ ...thStyle, width: "60px", textAlign: "center" }}>언어</th>
                            <th style={{ ...thStyle, width: "80px", textAlign: "center" }}>카테고리</th>
                            <th style={{ ...thStyle, width: "60px", textAlign: "center" }}>난이도</th>
                            <th style={{ ...thStyle, width: "100px", textAlign: "center" }}>생성일</th>
                            <th style={{ ...thStyle, width: "100px", textAlign: "center" }}>문제유형</th>
                            <th style={{ ...thStyle, width: "60px", textAlign: "center" }}>작성자</th>
                            <th style={{ ...thStyle, width: "50px", textAlign: "center" }}>삭제</th>
                        </tr>
                        </thead>
                    <tbody>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>
                        <select
                            value={filterLanguage}
                            onChange={(e) => setFilterLanguage(e.target.value)}
                            style={{ width: "100%" }}
                        >
                            <option value="ALL">전체</option>
                            <option value="KOREAN">한국어</option>
                            <option value="ENGLISH">영어</option>
                            <option value="JAPANESE">일본어</option>
                        </select>
                        </td>
                        <td>
                        <select
                            value={filterCategory}
                            onChange={(e) => setFilterCategory(e.target.value)}
                            style={{ width: "100%" }}
                        >
                            <option value="ALL">전체</option>
                            <option value="NEWS">뉴스</option>
                            <option value="NOVEL">소설</option>
                            <option value="FAIRY_TALE">동화</option>
                             <option value="VOCABULARY">어휘</option> 
                            <option value="FACTUAL">사실</option>
                            <option value="INFERENTIAL">추론</option>
                        </select>
                        </td>
                        <td>
                        <select
                            value={filterLevel}
                            onChange={(e) => setFilterLevel(e.target.value)}
                            style={{ width: "100%" }}
                        >
                            <option value="ALL">전체</option>
                            {[...Array(10)].map((_, i) => (
                            <option key={i + 1} value={i + 1}>
                                {i + 1}
                            </option>
                            ))}
                        </select>
                        </td>
                        <td></td>
                        <td>
                        <select
                            value={filterClassification}
                            onChange={(e) => setFilterClassification(e.target.value)}
                            style={{ width: "100%" }}
                        >
                            <option value="ALL">전체</option>
                            <option value="NORMAL">일반</option>
                            <option value="CHALLENGE">도전</option>
                            <option value="TEST">테스트</option>
                        </select>
                        </td>
                        <td></td>
                        <td></td>
                    </tr>

                    {passageList
                        .filter(p => filterLanguage === "ALL" || p.language === filterLanguage)
                        .filter(p => filterCategory === "ALL" || p.category === filterCategory)
                        .filter(p => filterLevel === "ALL" || p.level === parseInt(filterLevel))
                        .filter(p => filterClassification === "ALL" || p.classification === filterClassification)
                        .map((passage) => (
                        <tr key={passage.passageNo}>
                            <td style={tdStyle}>{passage.passageNo}</td>
                            <td style={{ ...tdStyle, color: "blue", cursor: "pointer" }} onClick={() => navigate(`/adminpage/passage/${passage.passageNo}`, { state: { passage } })}>{passage.title}</td>
                            <td style={tdStyle}>{LANGUAGE_LABELS[passage.language]}</td>
                            <td style={tdStyle}>{CATEGORY_LABELS[passage.category]}</td>
                            <td style={tdStyle}>{passage.level}</td>
                            <td style={tdStyle}>{new Date(passage.createdAt).toLocaleDateString()}</td>
                            <td style={tdStyle}>{CLASSIFICATION_LABELS[passage.classification]}</td>
                            <td style={tdStyle}>{AUTHOR_LABELS[passage.author] || passage.author}</td>
                            <td style={tdStyle}>
                            <button
                                onClick={() => handleDeletePassage(passage.passageNo)}
                                style={{ color: "red", border: "none", background: "none", cursor: "pointer" }}
                            >
                                삭제
                            </button>
                            </td>
                        </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </>
    );
};

const thStyle = {
  border: "1px solid #ccc",
  padding: "8px",
  backgroundColor: "#f2f2f2",
  whiteSpace: "nowrap",
  overflow: "hidden",
  textOverflow: "ellipsis"
};


const tdStyle = {
  border: "1px solid #ddd",
  padding: "8px",
  wordBreak: "break-word",     
  whiteSpace: "normal",      
  maxWidth: "200px",           
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

const LANGUAGE_LABELS = {
    KOREAN: "ko",
    JAPANESE: "jp",
    ENGLISH: "en"
};

const AUTHOR_LABELS = {
    GEMINI: "ai"
}

const CATEGORY_LABELS = {
    NEWS: "뉴스",
    NOVEL: "소설",
    FAIRY_TALE: "동화",
    VOCABULARY: "어휘",
    FACTUAL: "사실",
    INFERENTIAL: "추론"
};

const CLASSIFICATION_LABELS = {
    NORMAL: "일반문제",
    CHALLENGE: "도전문제",
    TEST: "테스트문제"
}

const ADMIN_PASSAGE_TITLE = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "16px"
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

const ADMIN_BUTTONS_2 = {
    marginBottom: "16px",
    padding: "8px 16px",
    backgroundColor: "red",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer"
}

const modalBackdrop = {
    position: "fixed", top: 0, left: 0, width: "100%", height: "100%",
    backgroundColor: "rgba(0,0,0,0.4)", display: "flex", justifyContent: "center", alignItems: "center"
};

const modalBox = {
    backgroundColor: "white", padding: "20px", borderRadius: "8px",
    width: "500px", display: "flex", flexDirection: "column", gap: "10px"
};

export default AdminPassage;