// import React, { useState } from "react";
// import "./readtest.css";
// import { useNavigate } from "react-router-dom";
// import api from "../../api/axiosInstance";

// const ReadTest = () => {
//   const navigate = useNavigate();
//   const [language, setLanguage] = useState("KOREAN");

//   const handleStart = async () => {
//     try {
//       const res = await api.get(`/test/start?language=${language}`);
//       const raw = res.data;

//       if (!raw || !raw.questionNo) {
//         alert("문제가 없습니다.");
//         return;
//       }

//       const formattedQuestion = {
//         article: {
//           title: raw.title,
//           content: raw.content,
//         },
//         quiz: {
//           questionNo: raw.questionNo,
//           questionText: raw.question,
//           choices: raw.choiceList.map((c) => c.content),
//         },
//         testerId: raw.testerId,
//         language: language,
//       };

//       navigate("/test-question", { state: { question: formattedQuestion } });
//     } catch (err) {
//       console.error("문제 불러오기 실패", err);
//       alert("문제를 불러오지 못했습니다.");
//     }
//   };

//   return (
//     <div className="ReadTest-wrapper">
//       <h2 className="ReadTest-title">당신의 문해력은 어느 정도일까요?</h2>

//       <div className="ReadTest-card">
//         <h3>
//           <strong>
//             <span style={{ textDecoration: "none", color: "inherit" }}>
//               리드 <span style={{ color: "#439395" }}>포스</span>
//             </span>
//           </strong>
//           는 뉴스 기반 문해력 테스트 플랫폼입니다.
//         </h3>
//         <p>
//           <strong>
//             <i>AI</i>
//           </strong>
//           가 뉴스를 요약하고, 우리는 문제를 풀며 <strong>문해력</strong>을 기릅니다.
//         </p>
//         <p>세상을 <strong>읽는 힘</strong>, 지금부터 시작하세요.</p>
//       </div>

//       <div className="ReadTest-language-buttons">
//         {["KOREAN", "ENGLISH", "JAPANESE"].map((lang, i) => (
//           <button
//             key={lang}
//             className={language === lang ? "ReadTest-lang-btn active" : "ReadTest-lang-btn"}
//             onClick={() => setLanguage(lang)}
//           >
//             {["한국어", "English", "日本語"][i]}
//           </button>
//         ))}
//       </div>

//       <button className="ReadTest-btn" onClick={handleStart}>
//         문해력 테스트 시작하기
//       </button>
//     </div>
//   );
// };

// export default ReadTest;
import React, { useState } from "react";
import "./readtest.css";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosInstance";

const ReadTest = () => {
  const navigate = useNavigate();
  const [language, setLanguage] = useState("KOREAN");

  const handleStart = async () => {
    try {
      const res = await api.get(`/test/start?language=${language}`);
      const raw = res.data;

      if (!raw || !raw.questionNo) {
        alert("문제가 없습니다.");
        return;
      }

      const formattedQuestion = {
        article: {
          title: raw.title,
          content: raw.content,
        },
        quiz: {
          questionNo: raw.questionNo,
          questionText: raw.question,
          choices: raw.choiceList.map((c) => c.content),
        },
        testerId: raw.testerId,
        language: language,
        category: raw.category, // ✅ 반드시 포함되어야 함!
      };

      navigate("/test-question", { state: { question: formattedQuestion } });
    } catch (err) {
      console.error("문제 불러오기 실패", err);
      alert("문제를 불러오지 못했습니다.");
    }
  };

  return (
    <div className="ReadTest-wrapper">
      <h2 className="ReadTest-title">당신의 문해력은 어느 정도일까요?</h2>

      <div className="ReadTest-card">
        <h3>
          <strong>
            <span style={{ textDecoration: "none", color: "inherit" }}>
              리드 <span style={{ color: "#439395" }}>포스</span>
            </span>
          </strong>
          는 뉴스 기반 문해력 테스트 플랫폼입니다.
        </h3>
        <p>
          <strong>
            <i>AI</i>
          </strong>
          가 뉴스를 요약하고, 우리는 문제를 풀며 <strong>문해력</strong>을 기릅니다.
        </p>
        <p>세상을 <strong>읽는 힘</strong>, 지금부터 시작하세요.</p>
      </div>

      <div className="ReadTest-language-buttons">
        {["KOREAN", "ENGLISH", "JAPANESE"].map((lang, i) => (
          <button
            key={lang}
            className={language === lang ? "ReadTest-lang-btn active" : "ReadTest-lang-btn"}
            onClick={() => setLanguage(lang)}
          >
            {["한국어", "English", "日本語"][i]}
          </button>
        ))}
      </div>

      <button className="ReadTest-btn" onClick={handleStart}>
        문해력 테스트 시작하기
      </button>
    </div>
  );
};

export default ReadTest;
