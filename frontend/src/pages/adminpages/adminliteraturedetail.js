import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axiosInstance from '../../api/axiosInstance';

const AdminLiteratureDetail = () => {
    const { literatureNo } = useParams();
    const [literature, setLiterature] = useState(null);
    const [paragraphs, setParagraphs] = useState([]);
    const [quizzes, setQuizzes] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // 문학 불러오기
        const fetchLiteratureDetail = async () => {
            try {
                const res = await axiosInstance.post(`/admin/get-all-literature-list`);
                const data = res.data;
                const target = data.find(lit => lit.literature_no === Number(literatureNo));
                setLiterature(target);
            } catch (err) {
                console.error(err);
                alert("문학 정보를 불러오지 못했습니다.");
            }
        };

        // 문단 불러오기
        const fetchParagraphs = async () => {
            try {
                const res = await axiosInstance.get(`/admin/get-all-literature-paragraph-list`);
                const data = res.data;
                const filtered = data.filter(p => String(p.literature_no) === literatureNo);
                setParagraphs(filtered);
            } catch (err) {
                console.error(err);
                alert("문단 정보를 불러오지 못했습니다.");
            }
        };

        // 퀴즈 불러오기
        const fetchQuizzes = async () => {
            try {
                const res = await axiosInstance.get(`/admin/get-all-literature-quiz-list`);
                const data = res.data;
                const filtered = data.filter(q => q.literature_no === Number(literatureNo));
                setQuizzes(filtered);
            } catch (err) {
                console.error(err);
                alert("퀴즈 정보를 불러오지 못했습니다.");
            }
        };

        fetchLiteratureDetail();
        fetchParagraphs();
        fetchQuizzes();
    }, [literatureNo]);

    if (!literature) return <div>불러오는 중...</div>;

    // 문단 삭제 ( 문제도 함께 )
    const handleDeleteParagraph = async (paragraphNo) => {
        if (!window.confirm("정말 이 문단과 관련된 문제도 함께 삭제하시겠습니까?")) return;

        try {
            await axiosInstance.delete(
                `/admin/delete-literature-paragraph-and-literature-by-literature-paragraph-no`, {
                params: {
                    literature_paragraph_no: paragraphNo,
                    literature_no: literatureNo
                }
            }
            );

            alert("문단이 삭제되었습니다.");
            setParagraphs(prev => prev.filter(p => p.literature_paragraph_no !== paragraphNo));
            setQuizzes(prev => prev.filter(q => q.literature_paragraph_no !== paragraphNo));
        } catch (err) {
            console.error(err);
            alert("문단 삭제 중 오류 발생");
        }
    };

    // 퀴즈 삭제
    const handleDeleteQuiz = async (quizNo) => {
        if (!window.confirm("정말 이 퀴즈를 삭제하시겠습니까?")) return;

        try {
            await axiosInstance.delete(`/admin/delete-literature-quiz`, {
                params: {
                    literature_quiz_no: quizNo
                }
            });

            alert("퀴즈가 삭제되었습니다.");
            setQuizzes(prev => prev.filter(q => q.literature_quiz_no !== quizNo));
        } catch (err) {
            console.error(err);
            alert("퀴즈 삭제 중 오류 발생");
        }
    };

    return (
        <div style={{ padding: "24px" }}>
            <button onClick={() => navigate("/adminpage/adminliterature")} style={backbtn}>뒤로가기</button>
            <div style={TITLE_STYLE}>
                <div>
                    <h2>{literature.title}</h2>
                </div>
                <div style={BUTTON_LIST}>
                    <button
                        style={BUTTON_STYLE}
                        onClick={() => navigate(`/adminpage/adminliterature/${literatureNo}/add-paragraph`)}>
                        문단 추가
                    </button>
                </div>
            </div>
            <p><strong>유형:</strong> {literature.type}</p>
            <p><strong>생성일:</strong> {new Date(literature.created_date).toLocaleDateString()}</p>

            <hr style={{ margin: "24px 0" }} />
            {paragraphs.length === 0 ? (
                <p>문단이 없습니다.</p>
            ) : (
                <ul style={{ listStyleType: "none", paddingLeft: 0 }}>
                    {paragraphs.map(p => (
                        <li
                            key={p.literature_paragraph_no}
                            style={{
                                marginBottom: "16px",
                                padding: "12px",
                                position: "relative"
                            }}
                        >
                            <div style={{
                                display: "flex",
                                justifyContent: "space-between",
                            }}>
                                <div style={{ marginBottom: "8px", fontWeight: "bold" }}>
                                    문단 번호 : {p.literature_paragraph_no}
                                </div>
                                <div>
                                    <button
                                        onClick={() => handleDeleteParagraph(p.literature_paragraph_no)}
                                        style={{
                                            backgroundColor: "white",
                                            color: "red",
                                            border: "none",
                                            fontSize: "12px",
                                            cursor: "pointer"
                                        }}
                                    >
                                        삭제
                                    </button>
                                </div>
                            </div>
                            <div>{p.content}</div>
                        </li>
                    ))}
                </ul>
            )}
            <hr style={{ margin: "24px 0" }} />
            <h3 style={{ marginTop: "24px" }}>퀴즈</h3>
            <ul style={{ listStyleType: "none", paddingLeft: 0 }}>
                {quizzes.map(q => (
                    <li key={q.literature_quiz_no} style={{ marginBottom: "20px", padding: "12px", border: "1px solid #ccc", borderRadius: "8px", position: "relative" }}>
                        <button
                            onClick={() => handleDeleteQuiz(q.literature_quiz_no)}
                            style={{
                                position: "absolute",
                                top: "8px",
                                right: "8px",
                                backgroundColor: "white",
                                color: "red",
                                border: "none",
                                fontSize: "12px",
                                cursor: "pointer"
                            }}
                        >
                            삭제
                        </button>
                        <p><strong>문제:</strong> {q.question_text}</p>
                        <ol type="A">
                            <li>{q.choice1}</li>
                            <li>{q.choice2}</li>
                            <li>{q.choice3}</li>
                            <li>{q.choice4}</li>
                        </ol>
                        <p>
                            <strong>정답:</strong>{" "}
                            {String.fromCharCode(64 + q.correct_answer_index)}. {q[`choice${q.correct_answer_index}`]}
                        </p>
                        <p><strong>해설:</strong> {q.explanation}</p>
                        <p><strong>배점:</strong> {q.score}</p>
                    </li>
                ))}
            </ul>
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
}

const TITLE_STYLE = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "16px"
}

const BUTTON_LIST = {
    display: "flex",
    gap: "8px"
}

const BUTTON_STYLE = {
    marginBottom: "16px",
    padding: "8px 16px",
    backgroundColor: "#007BFF",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer"
}

export default AdminLiteratureDetail;