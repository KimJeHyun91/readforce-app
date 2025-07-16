import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axiosInstance from "../../api/axiosInstance";

const AdminPassageDetail = () => {
    const navigate = useNavigate();
    const { state } = useLocation();
    const passage = state?.passage;

    const [questionList, setQuestionList] = useState([]);
    const [generating, setGenerating] = useState(false);

    useEffect(() => {
        const fetchQuestions = async () => {
            try {
                const res = await axiosInstance.get("/multiple_choice/get-multiple-choice-question-list", {
                    params: { passageNo: passage.passageNo }
                });
                setQuestionList(res.data);
            } catch (err) {
                console.error("실패 : 문제 불러오기 실패:", err);
            }
        };

        if (passage?.passageNo) {
            fetchQuestions();
        }
    }, [passage?.passageNo]);

    const handleGenerateQuestionForPassage = async () => {
        if (!passage?.passageNo) return alert("지문 번호를 찾을 수 없습니다.");

        setGenerating(true);
        try {
            await axiosInstance.post("/ai/generate-question-by-passage-no", {
                passageNo: passage.passageNo
            });
            alert("성공: 문제 생성 완료!");

            // 문제 목록 새로고침
            const refreshed = await axiosInstance.get("/multiple_choice/get-multiple-choice-question-list", {
                params: { passageNo: passage.passageNo }
            });
            setQuestionList(refreshed.data);
        } catch (err) {
            console.error("문제 생성 실패:", err);
            alert("실패: 문제 생성 중 오류가 발생했습니다.");
        } finally {
            setGenerating(false);
        }
    };

    if (!passage) {
        return <div style={containerStyle}>지문 정보를 불러오지 못했습니다.</div>;
    }

    return (
        <div style={containerStyle}>
            <div style={{ display: "flex", gap: "8px", marginBottom: "20px" }}>
                <button onClick={() => navigate(-1)} style={backBtnStyle}>뒤로가기</button>
                <button onClick={handleGenerateQuestionForPassage} style={{ ...backBtnStyle, backgroundColor: "#007bff" }} disabled={generating}>
                    {generating ? "생성 중..." : "문제 생성"}
                </button>
            </div>

            <h2>{passage.title}</h2>

            <div style={infoBox}>
                <div style={metaContainerStyle}>
                    <div style={metaItem}><strong>지문 번호</strong><div>{passage.passageNo}</div></div>
                    <div style={metaItem}><strong>언어</strong><div>{LANGUAGE_LABELS[passage.language]}</div></div>
                    <div style={metaItem}><strong>카테고리</strong><div>{CATEGORY_LABELS[passage.category]}</div></div>
                    <div style={metaItem}><strong>난이도</strong><div>{passage.level}</div></div>
                    <div style={metaItem}><strong>문제유형</strong><div>{CLASSIFICATION_LABELS[passage.classification]}</div></div>
                    <div style={metaItem}><strong>작성자</strong><div>{AUTHOR_LABELS[passage.author] || passage.author}</div></div>
                    <div style={metaItem}><strong>생성일</strong><div>{new Date(passage.createdAt).toLocaleString()}</div></div>
                </div>

                <div style={passageBox}>{passage.content}</div>

                <div style={passageBox}>
                    {questionList.map((q, idx) => {
                        const correctChoice = q.choiceList.find(c => c.isCorrect);
                        return (
                            <div key={q.questionNo} style={{ marginBottom: "32px" }}>
                                <p><strong>Q{idx + 1}. {q.question}</strong></p>
                                <ul style={{ listStyleType: "none", paddingLeft: 0 }}>
                                    {q.choiceList.map((choice) => (
                                        <li key={choice.choiceIndex} style={{ marginBottom: "6px" }}>
                                            {String.fromCharCode(65 + choice.choiceIndex)}. {choice.content}
                                        </li>
                                    ))}
                                </ul>
                                {correctChoice && (
                                    <div style={answerBox}>
                                        <p><strong>정답:</strong> {String.fromCharCode(65 + correctChoice.choiceIndex)}</p>
                                        <p><strong>해설:</strong> {correctChoice.explanation || "없음"}</p>
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </div>
            </div>
        </div>
    );
};

// ------------------- Styles -------------------

const containerStyle = {
    padding: "24px",
    maxWidth: "800px",
    margin: "0 auto"
};

const backBtnStyle = {
    padding: "8px 16px",
    backgroundColor: "#6c757d",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer"
};

const infoBox = {
    backgroundColor: "#f9f9f9",
    padding: "20px",
    borderRadius: "8px",
    boxShadow: "0 0 5px rgba(0,0,0,0.1)"
};

const passageBox = {
    whiteSpace: "pre-wrap",
    backgroundColor: "#fff",
    padding: "16px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    marginTop: "8px"
};

const LANGUAGE_LABELS = {
    KOREAN: "한국어",
    JAPANESE: "일본어",
    ENGLISH: "영어"
};

const AUTHOR_LABELS = {
    GEMINI: "AI"
};

const CATEGORY_LABELS = {
    NEWS: "뉴스",
    NOVEL: "소설",
    FAIRY_TALE: "동화",
    VOCABULARY: "사전",
    FACTUAL: "사실",
    INFERENTIAL: "추론"
};

const CLASSIFICATION_LABELS = {
    NORMAL: "일반문제",
    CHALLENGE: "도전문제",
    TEST: "테스트문제"
};

const metaContainerStyle = {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
    backgroundColor: "#f9f9f9",
    borderRadius: "8px",
    marginBottom: "20px",
};

const metaItem = {
    backgroundColor: "white",
    border: "1px solid #ccc",
    borderRadius: "6px",
    padding: "12px",
    fontSize: "14px",
    lineHeight: "1.4"
};

const answerBox = {
    backgroundColor: "#f1f9f9",
    border: "1px solid #ccc",
    borderRadius: "6px",
    padding: "12px",
    marginTop: "8px",
    fontSize: "14px",
    lineHeight: "1.5"
};

export default AdminPassageDetail;
