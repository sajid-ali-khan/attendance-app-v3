import axiosClient from "../api/axiosClient";
import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
  const [token, setToken_] = useState(localStorage.getItem("token"));

  const setToken = (newToken) => {
    setToken_(newToken);
  };

  useEffect(() => {
    if (token) {
      axiosClient.defaults.headers.common["Authorization"] = "Bearer " + token;
      localStorage.setItem("token", token);
    } else {
      delete axiosClient.defaults.headers.common["Authorization"];
      localStorage.removeItem("token");
    }
  }, [token]);

  const isAuthenticated = !!token; // ✅ derived flag

  const contextValue = useMemo(
    () => ({
      token,
      setToken,
      isAuthenticated,
    }),
    [token, isAuthenticated]
  );

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
export default AuthProvider;
