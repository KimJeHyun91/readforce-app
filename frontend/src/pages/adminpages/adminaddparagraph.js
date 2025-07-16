import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axiosInstance from '../../api/axiosInstance';

const AdminAddParagraph = () => {
    const { literatureNo } = useParams();
    const navigate = useNavigate();

    const CATEGORY_OPTIONS = ["MYSTERY", "HISTORY", "CLASSIC", "MODERN", "CHILDREN"];
    const LEVEL_OPTIONS = ["BEGINNER", "INTERMEDIATE", "ADVANCED"];

    const [category, setCategory] = useState(CATEGORY_OPTIONS[0]);
    const [level, setLevel] = useState(LEVEL_OPTIONS[0]);
    const [content, setContent] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const body = {
            literature_no: Number(literatureNo),
            category,
            level,
            content,
        };
        console.log("literatureNo:", literatureNo);
        console.log("body:", body);

        try {
            await axiosInstance.post("/admin/add-literature-paragraph", body);

            alert("문단이 성공적으로 추가되었습니다!");
            navigate(`/adminpage/adminliterature/${literatureNo}`);
        } catch (err) {
            console.error(err);
            alert("문단 추가 중 오류 발생: " + err.message);
        }
    };

    return (
        <div style={{ padding: "20px" }}>
            <h2>문단 추가</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>카테고리: </label>
                    <select value={category} onChange={(e) => setCategory(e.target.value)}>
                        {CATEGORY_OPTIONS.map((cat) => (
                            <option key={cat} value={cat}>
                                {cat}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>난이도: </label>
                    <select value={level} onChange={(e) => setLevel(e.target.value)}>
                        {LEVEL_OPTIONS.map((lvl) => (
                            <option key={lvl} value={lvl}>
                                {lvl}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>내용: </label>
                    <textarea
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        rows={6}
                        style={{ width: "100%" }}
                        required
                    />
                </div>
                <button type="submit" style={{ marginTop: "10px" }}>
                    문단 추가하기
                </button>
            </form>
        </div>
    );
};

export default AdminAddParagraph;