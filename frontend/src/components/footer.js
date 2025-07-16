// ✅ 공통 레이아웃 .page-container 반영됨
import './footer.css';

const Footer = () => {
    return (
        <footer className="footer">
            <div className="page-container">
                <h3 className="footer-title">리드 포스</h3>
                <p>서울 강남구 강남대로78길 8 한국빌딩 8층</p>
                <p>상담시간 9:00~18:00 (점심시간 12:50~13:50)</p>
            </div>
        </footer>
    );
};

export default Footer;