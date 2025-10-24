// src/layouts/DashboardLayout.jsx
import { NavLink, Outlet } from "react-router-dom";
import {
  DashboardIcon,
  UploadIcon,
  AssignIcon,
  StudentIcon,
  CourseIcon,
  ReportIcon
} from "../components/icons";

export const DashboardLayout = () => {
  const navItems = [
    { id: 'dashboard', path: '/', label: 'Dashboard', icon: <DashboardIcon /> },
    { id: 'bulkUpload', path: '/bulk-upload', label: 'Bulk Data Upload', icon: <UploadIcon /> },
    { id: 'assignClass', path: '/assign-class', label: 'Assign Class', icon: <AssignIcon /> },
    { id: 'studentManagement', path: '/student-management', label: 'Student Management', icon: <StudentIcon /> },
    { id: 'courseManagement', path: '/course-management', label: 'Course Management', icon: <CourseIcon /> },
    { id: 'attendanceReports', path: '/attendance-reports', label: 'Attendance Reports', icon: <ReportIcon /> },
  ];

  return (
    <div className="min-h-screen bg-slate-100 font-sans text-slate-900 flex">
      <aside className="w-64 bg-slate-900 text-slate-300 flex flex-col fixed h-full">
        <div className="p-6 text-center border-b border-slate-700">
          <h1 className="text-xl font-bold text-white tracking-wider">
            ATTENDANCE SYS.
          </h1>
        </div>
        <nav className="flex-1 p-4 space-y-2">
          {navItems.map(item => (
            <NavLink
              key={item.id}
              to={item.path}
              className={({ isActive }) =>
                `w-full flex items-center gap-3 px-4 py-2.5 text-sm font-medium text-left transition-colors duration-200 ${isActive
                  ? 'bg-slate-700 text-white'
                  : 'hover:bg-slate-800'}`
              }
            >
              {item.icon}
              <span>{item.label}</span>
            </NavLink>
          ))}
        </nav>
      </aside>
      <main className="flex-1 ml-64 p-8">
        <Outlet /> {/* This is where nested pages render */}
      </main>
    </div>
  );
};
