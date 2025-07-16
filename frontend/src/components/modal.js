import React from 'react';
import './modal.css';

const Modal = ({ title, message, onClose }) => {
  return (
    <div className="modal-backdrop">
      <div className="modal-box">
        <h3>{title}</h3>
        <p>{message}</p>
        <button onClick={onClose}>확인</button>
      </div>
    </div>
  );
};

export default Modal;