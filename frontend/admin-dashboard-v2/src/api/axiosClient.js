import axios from "axios";

const token = localStorage.getItem("token");

const axiosClient = axios.create({
  baseURL: "/api", // ✅ relative URL — Vite proxy will handle the rest
  headers: token ? { Authorization: `Bearer ${token}` } : {},
});

axiosClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default axiosClient;
