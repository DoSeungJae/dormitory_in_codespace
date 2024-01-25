import React from 'react';
import Modal from 'react-modal';

Modal.setAppElement('#root'); // Accessibility purposes

const Alert = ({message, onClose}) => {
  return (
    <Modal 
      isOpen={!!message} 
      onRequestClose={onClose}
      style={{
        overlay: {
          backgroundColor: 'rgba(0, 0, 0, 0.5)'
        },
        content: {
          position: 'absolute',
          top: '40px',
          left: '40px',
          right: '40px',
          bottom: '40px',
          border: '1px solid #ccc',
          background: '#fff',
          overflow: 'auto',
          WebkitOverflowScrolling: 'touch',
          borderRadius: '4px',
          outline: 'none',
          padding: '20px'
        }
      }}
      contentLabel="Alert Modal"
    >
      <p>{message}</p>
      <button onClick={onClose}>닫기</button>
    </Modal>
  );
};

export default Alert;
