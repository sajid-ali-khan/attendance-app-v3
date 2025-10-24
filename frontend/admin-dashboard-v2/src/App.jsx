// src/App.jsx
import AuthProvider from "./provider/AuthProvider";
import { AppRoutes } from "./routes";

export default function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}
