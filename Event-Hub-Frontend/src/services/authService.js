import api from "./api";

// POST /api/auth/register
// Role is required by backend - we always send "USER" for regular users
export const register = (data) => {
  return api.post("/api/auth/register", {
    username: data.username,
    password: data.password,
    phone: data.phone,
    email: data.email,
    role: data.role || "USER",
  });
};

// POST /api/auth/login
// Returns: { token, userId, username }
export const login = (data) => {
  return api.post("/api/auth/login", {
    username: data.username,
    password: data.password,
  });
};
