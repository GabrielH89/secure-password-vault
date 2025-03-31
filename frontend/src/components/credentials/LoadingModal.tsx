import '../../styles/credentials/LoadingModal.css';

const LoadingModal = () => {
  return (
    <div className="loading-modal-overlay">
      <div className="loading-modal-content">
        <div className="loading-spinner"></div>
        <p>Carregando...</p>
      </div>
    </div>
  );
}

export default LoadingModal;
