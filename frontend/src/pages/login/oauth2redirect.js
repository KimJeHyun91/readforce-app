// import { useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';

// export default function Oauth2RedirectHandler() {
//   const navigate = useNavigate();

//   useEffect(() => {
//     const params = new URLSearchParams(window.location.search);
//     const token = params.get('token');
//     const isNew = params.get('isNew');
//     const nickname = params.get('nickname'); // 닉네임 추가

//     if (!token) {
//       alert('로그인에 실패했습니다.');
//       navigate('/login');
//       return;
//     }

//     localStorage.setItem('token', token);

//     if (isNew === 'true') {
//       navigate(`/social-sign-up?token=${token}`);
//     } else {
//       // ✅ 닉네임도 저장
//       localStorage.setItem('nickname', nickname || '사용자');
//       window.location.href = '/';
//     }
//   }, [navigate]);

//   return (
//     <div className="page-container">
//       <p>로그인 처리 중입니다...</p>
//     </div>
//   );
// }