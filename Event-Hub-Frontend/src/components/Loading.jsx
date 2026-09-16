// Loading spinner component - shown while waiting for API responses
const Loading = ({ message = "Loading..." }) => {
  return (
    <div className="loading-container">
      <div className="spinner"></div>
      <p className="loading-text">{message}</p>
    </div>
  );
};

export default Loading;
