import axiosClient from "../api/axiosClient";
import {
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
  const [token, setToken_] = useState(localStorage.getItem("token"));

  const setToken = (newToken) => {
    setToken_(newToken);
  };

  // Helper to check token expiration
  const isTokenExpired = (token) => {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 < Date.now();
    } catch {
      return true;
    }
  };

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
      
      // Check if token is already expired
      if (isTokenExpired(token)) {
        setToken(null);
        localStorage.removeItem("token");
      }
    } else {
      localStorage.removeItem("token");
    }
  }, [token]);

  // Periodic check for token expiration (every 60 seconds)
  useEffect(() => {
    if (!token) return;

    const interval = setInterval(() => {
      if (isTokenExpired(token)) {
        console.log('Token expired - logging out');
        setToken(null);
        localStorage.removeItem("token");
      }
    }, 60000); // Check every 60 seconds

    return () => clearInterval(interval);
  }, [token]);

  const contextValue = {
    token,
    setToken,
  };

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
export default AuthProvider;
