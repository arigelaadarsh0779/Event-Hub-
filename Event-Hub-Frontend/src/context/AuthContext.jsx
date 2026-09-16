import { createContext, useContext, useState } from "react";
import { login as loginService, register as registerService } from "../services/authService";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem("token") || null);
  const [user, setUser] = useState(() => {
    const userId = localStorage.getItem("userId");
    const username = localStorage.getItem("username");
    const role = localStorage.getItem("role") || "USER";
    if (userId && username) return { userId: Number(userId), username, role };
    return null;
  });

  const isAuthenticated = !!token;
  const isAdmin = user?.role === "ADMIN";

  const login = async (credentials) => {
    const res = await loginService(credentials);
    const { token, userId, username, role } = res.data;
    const userRole = role || "USER";

    localStorage.setItem("token", token);
    localStorage.setItem("userId", String(userId));
    localStorage.setItem("username", username);
    localStorage.setItem("role", userRole);

    setToken(token);
    setUser({ userId, username, role: userRole });

    return res.data;
  };

  const register = async (data) => {
    return registerService(data);
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ token, user, isAuthenticated, isAdmin, login, logout, register }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
};
