import './layout.css';
import Header from "./header";
import Footer from "./footer";
import { Outlet } from "react-router-dom";

const LayOut = () => {
  return (
    <div className="app-wrapper">
      <Header />
      <main>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default LayOut;