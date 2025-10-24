// src/routes/ProtectedRoute.jsx
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../provider/AuthProvider";

export const ProtectedRoute = () => {
  const { token } = useAuth();

  // ✅ Defensive check: handle "not yet initialized" state
  if (token === undefined) return null; // or a loader/spinner

  // ✅ Redirect only if explicitly no token
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

