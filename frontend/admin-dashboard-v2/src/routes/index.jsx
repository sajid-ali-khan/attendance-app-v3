import { RouterProvider, createBrowserRouter } from "react-router-dom";
import { useAuth } from "../provider/AuthProvider";
import { ProtectedRoute } from "./ProtectedRoute";
import LoginPage from "../views/LoginPage";
import { DashboardView } from "../views/DashboardView";
import { BulkUploadView } from "../views/BulkUploadView";
import { AssignClassView } from "../views/AssignClassView";
import { StudentManagementView } from "../views/StudentManagementView";
import { CourseManagementView } from "../views/CourseManagementView";
import { AttendanceReportView } from "../views/AttendanceReportView";
import { DashboardLayout } from "../layouts/Layout";

export const AppRoutes = () => {
  const router = createBrowserRouter([
    {
      path: "/login",
      element: <LoginPage />,
    },
    {
      element: <ProtectedRoute />, // Wraps the entire dashboard layout
      children: [
        {
          element: <DashboardLayout />, // ✅ Layout lives inside router
          children: [
            { path: "/", element: <DashboardView /> },
            { path: "/bulk-upload", element: <BulkUploadView /> },
            { path: "/assign-class", element: <AssignClassView /> },
            { path: "/student-management", element: <StudentManagementView /> },
            { path: "/course-management", element: <CourseManagementView /> },
            { path: "/attendance-reports", element: <AttendanceReportView /> },
          ],
        },
      ],
    },
  ]);

  return <RouterProvider router={router} />;
};
