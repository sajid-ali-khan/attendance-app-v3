// src/layouts/DashboardLayout.jsx
import React, { useState } from 'react';
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
  const [mobileOpen, setMobileOpen] = useState(false);

  const navItems = [
    { id: 'dashboard', path: '/', label: 'Dashboard', icon: <DashboardIcon /> },
    { id: 'bulkUpload', path: '/bulk-upload', label: 'Bulk Data Upload', icon: <UploadIcon /> },
    { id: 'assignClass', path: '/assign-class', label: 'Assign Class', icon: <AssignIcon /> },
    // { id: 'studentManagement', path: '/student-management', label: 'Student Management', icon: <StudentIcon /> },
    // { id: 'courseManagement', path: '/course-management', label: 'Course Management', icon: <CourseIcon /> },
    { id: 'attendanceReports', path: '/attendance-reports', label: 'Attendance Reports', icon: <ReportIcon /> },
  ];

  return (
    <div className="min-h-screen h-full flex bg-slate-100 font-sans text-slate-900">
      {/* Mobile top bar */}
      <header className="md:hidden fixed top-0 left-0 right-0 z-30 bg-slate-900 text-slate-300 flex items-center justify-between px-4 py-3">
        <div className="text-white font-bold">ATTENDANCE SYS.</div>
        <button
          aria-label="Toggle menu"
          aria-expanded={mobileOpen}
          onClick={() => setMobileOpen(!mobileOpen)}
          className="p-2 rounded-md bg-slate-800 hover:bg-slate-700 focus:outline-none"
        >
          {mobileOpen ? (
            <svg className="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          ) : (
            <svg className="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          )}
        </button>
      </header>

      {/* Mobile overlay when sidebar open */}
      {mobileOpen && (
        <div
          className="fixed inset-0 bg-black/40 z-20 md:hidden"
          onClick={() => setMobileOpen(false)}
          aria-hidden
        />
      )}

      {/* Sidebar */}
      <aside
        id="sidebar"
        className={`fixed inset-y-0 left-0 z-30 w-64 bg-slate-900 text-slate-300 transform transition-transform duration-200 ${mobileOpen ? 'translate-x-0' : '-translate-x-full'} md:translate-x-0 md:relative md:flex md:flex-col md:w-64 min-h-screen h-full`}
      >
        <div className="p-6 text-center border-b border-slate-700">
          <h1 className="text-xl font-bold text-white tracking-wider">ATTENDANCE SYS.</h1>
        </div>

        <nav className="flex-1 p-4 space-y-2 overflow-y-auto">
          {navItems.map(item => (
            <NavLink
              key={item.id}
              to={item.path}
              onClick={() => setMobileOpen(false)}
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

      {/* Main content */}
      <main className="flex-1 min-h-screen h-full p-4 md:p-6 pt-20 md:pt-6 bg-slate-100">
        <div className="w-full h-full">
          <Outlet /> {/* This is where nested pages render */}
        </div>
      </main>
    </div>
  );
};
