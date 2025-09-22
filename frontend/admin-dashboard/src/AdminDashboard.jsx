import React, { useState } from 'react';

// Import Constants
import { navItems } from './constants/dashboardConstants';

// Import Components
import Sidebar from './components/Sidebar';

// Import Pages
import BulkUpload from './pages/BulkUpload';
import AssignClass from './pages/AssignClass';
import StudentManagement from './pages/StudentManagement';
import CourseManagement from './pages/CourseManagement';
import AttendanceReports from './pages/AttendanceReports';
import ComingSoon from './pages/ComingSoon';

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('bulk-upload');
  const [formData, setFormData] = useState({
    scheme: '',
    branch: '',
    semester: '',
    subject: '',
    faculty: ''
  });

  const handleInputChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const renderContent = () => {
    switch (activeTab) {
      case 'bulk-upload':
        return <BulkUpload />;
      case 'assign-class':
        return <AssignClass formData={formData} handleInputChange={handleInputChange} />;
      case 'student-management':
        return <StudentManagement formData={formData} handleInputChange={handleInputChange} />;
      case 'course-management':
        return <CourseManagement formData={formData} handleInputChange={handleInputChange} />;
      case 'attendance-reports':
        return <AttendanceReports formData={formData} handleInputChange={handleInputChange} />;
      default:
        return <ComingSoon />;
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar navItems={navItems} activeTab={activeTab} setActiveTab={setActiveTab} />

      <main className="flex-1 overflow-auto">
        <div className="p-8">
          {renderContent()}
        </div>
      </main>
    </div>
  );
};

export default AdminDashboard;