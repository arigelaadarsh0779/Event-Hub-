import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

// Wrap any route that requires login with this component.
// If not authenticated, redirects to /login.
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;
