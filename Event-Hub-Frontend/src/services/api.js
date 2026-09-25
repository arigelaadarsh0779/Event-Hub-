import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const getErrorMessage = (err, fallback = "An unexpected error occurred") => {
  if (!err) return fallback;
  if (typeof err === "string") return err;
  const data = err.response?.data;
  if (!data) return err.message || fallback;
  if (typeof data === "string") return data;
  if (data.errorMessage && typeof data.errorMessage === "string") return data.errorMessage;
  if (data.message && typeof data.message === "string") return data.message;
  if (data.error && typeof data.error === "string") return data.error;
  return err.message || fallback;
};

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const url = error.config?.url || "";
    const isAuthEndpoint = url.includes("/api/auth/");
    if (error.response?.status === 401 && !isAuthEndpoint) {
      localStorage.removeItem("token");
      localStorage.removeItem("userId");
      localStorage.removeItem("username");
      localStorage.removeItem("role");
      if (window.location.pathname !== "/login" && window.location.pathname !== "/register") {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export default api;
