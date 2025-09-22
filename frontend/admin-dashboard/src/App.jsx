import React, { useState } from 'react';
import { 
  Upload, 
  Users, 
  BookOpen, 
  UserPlus, 
  GraduationCap, 
  BarChart3, 
  Settings,
  ChevronDown,
  Plus,
  Minus,
  Download
} from 'lucide-react';

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('bulk-upload');
  const [uploadTab, setUploadTab] = useState('students');
  const [formData, setFormData] = useState({
    scheme: '',
    branch: '',
    semester: '',
    subject: '',
    faculty: ''
  });

  // Sample data
  const schemes = ['CBCS', 'Autonomous', 'University'];
  const branches = ['Computer Science', 'Electronics', 'Mechanical', 'Civil'];
  const semesters = ['1st', '2nd', '3rd', '4th', '5th', '6th', '7th', '8th'];
  const subjects = ['Data Structures', 'Operating Systems', 'Database Systems', 'Computer Networks'];
  const faculties = ['Dr. Smith', 'Prof. Johnson', 'Dr. Brown', 'Prof. Davis'];

  const studentSchema = [
    { field: 'roll_number', type: 'String', required: 'Yes', example: 'CS2021001' },
    { field: 'name', type: 'String', required: 'Yes', example: 'John Doe' },
    { field: 'email', type: 'String', required: 'Yes', example: 'john@email.com' },
    { field: 'branch', type: 'String', required: 'Yes', example: 'Computer Science' },
    { field: 'semester', type: 'String', required: 'Yes', example: '5th' },
    { field: 'phone', type: 'String', required: 'No', example: '9876543210' }
  ];

  const courseSchema = [
    { field: 'course_code', type: 'String', required: 'Yes', example: 'CS301' },
    { field: 'course_name', type: 'String', required: 'Yes', example: 'Data Structures' },
    { field: 'credits', type: 'Number', required: 'Yes', example: '4' },
    { field: 'branch', type: 'String', required: 'Yes', example: 'Computer Science' },
    { field: 'semester', type: 'String', required: 'Yes', example: '3rd' },
    { field: 'type', type: 'String', required: 'Yes', example: 'Theory/Lab' }
  ];

  const facultySchema = [
    { field: 'faculty_id', type: 'String', required: 'Yes', example: 'FAC001' },
    { field: 'name', type: 'String', required: 'Yes', example: 'Dr. Smith' },
    { field: 'email', type: 'String', required: 'Yes', example: 'smith@college.edu' },
    { field: 'department', type: 'String', required: 'Yes', example: 'Computer Science' },
    { field: 'phone', type: 'String', required: 'No', example: '9876543210' },
    { field: 'designation', type: 'String', required: 'Yes', example: 'Professor' }
  ];

  const attendanceData = [
    { roll: 'CS2021001', name: 'John Doe', subject: 'Data Structures', percentage: '85%' },
    { roll: 'CS2021002', name: 'Jane Smith', subject: 'Data Structures', percentage: '92%' },
    { roll: 'CS2021003', name: 'Mike Johnson', subject: 'Data Structures', percentage: '78%' },
    { roll: 'CS2021004', name: 'Sarah Wilson', subject: 'Data Structures', percentage: '96%' }
  ];

  const navItems = [
    { id: 'bulk-upload', label: 'Bulk Data Upload', icon: Upload },
    { id: 'assign-class', label: 'Assign Class', icon: UserPlus },
    { id: 'assignments', label: 'Assignments', icon: BookOpen },
    { id: 'student-management', label: 'Student Management', icon: Users },
    { id: 'course-management', label: 'Course Management', icon: GraduationCap },
    { id: 'attendance-reports', label: 'Attendance Reports', icon: BarChart3 },
    { id: 'settings', label: 'Settings', icon: Settings }
  ];

  const handleInputChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const renderSchema = (schema) => (
    <div className="bg-white border border-gray-300">
      <div className="px-4 py-3 bg-gray-50 border-b border-gray-300">
        <h3 className="text-sm font-medium text-gray-900">Required CSV Schema</h3>
      </div>
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-300">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Field Name</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Data Type</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Required</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Example</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-300">
            {schema.map((item, index) => (
              <tr key={index} className="hover:bg-gray-50">
                <td className="px-4 py-2 text-sm font-medium text-gray-900">{item.field}</td>
                <td className="px-4 py-2 text-sm text-gray-600">{item.type}</td>
                <td className="px-4 py-2 text-sm">
                  <span className={`px-2 py-1 text-xs ${item.required === 'Yes' ? 'bg-red-100 text-red-800' : 'bg-gray-100 text-gray-800'}`}>
                    {item.required}
                  </span>
                </td>
                <td className="px-4 py-2 text-sm text-gray-600">{item.example}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );

  const renderDropdown = (label, value, options, onChange) => (
    <div className="mb-4">
      <label className="block text-sm font-medium text-gray-700 mb-2">{label}</label>
      <div className="relative">
        <select
          value={value}
          onChange={(e) => onChange(e.target.value)}
          className="w-full px-3 py-2 border border-gray-300 bg-white text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 appearance-none"
        >
          <option value="">Select {label}</option>
          {options.map((option, index) => (
            <option key={index} value={option}>{option}</option>
          ))}
        </select>
        <ChevronDown className="absolute right-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400 pointer-events-none" />
      </div>
    </div>
  );

  const renderContent = () => {
    switch (activeTab) {
      case 'bulk-upload':
        return (
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <h1 className="text-2xl font-semibold text-gray-900">Bulk Data Upload</h1>
            </div>
            
            <div className="bg-white border border-gray-300">
              <div className="border-b border-gray-300">
                <nav className="flex space-x-0">
                  {[
                    { id: 'students', label: 'Students' },
                    { id: 'courses', label: 'Courses' },
                    { id: 'faculties', label: 'Faculties' }
                  ].map((tab) => (
                    <button
                      key={tab.id}
                      onClick={() => setUploadTab(tab.id)}
                      className={`px-6 py-3 text-sm font-medium border-r border-gray-300 last:border-r-0 ${
                        uploadTab === tab.id
                          ? 'bg-blue-50 text-blue-600 border-b-2 border-blue-600'
                          : 'text-gray-500 hover:text-gray-700 hover:bg-gray-50'
                      }`}
                    >
                      {tab.label}
                    </button>
                  ))}
                </nav>
              </div>
              
              <div className="p-6">
                {uploadTab === 'students' && renderSchema(studentSchema)}
                {uploadTab === 'courses' && renderSchema(courseSchema)}
                {uploadTab === 'faculties' && renderSchema(facultySchema)}
                
                <div className="mt-6 space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Upload CSV File
                    </label>
                    <div className="border-2 border-dashed border-gray-300 p-6 text-center hover:border-gray-400 transition-colors">
                      <Upload className="mx-auto h-8 w-8 text-gray-400 mb-2" />
                      <p className="text-sm text-gray-600 mb-2">Drop your CSV file here or click to browse</p>
                      <input type="file" accept=".csv" className="hidden" />
                      <button className="px-4 py-2 bg-blue-600 text-white text-sm font-medium hover:bg-blue-700 transition-colors">
                        Choose File
                      </button>
                    </div>
                  </div>
                  
                  <div className="flex justify-end">
                    <button className="px-6 py-2 bg-green-600 text-white text-sm font-medium hover:bg-green-700 transition-colors">
                      Upload & Process
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        );

      case 'assign-class':
        return (
          <div className="space-y-6">
            <h1 className="text-2xl font-semibold text-gray-900">Assign Class</h1>
            
            <div className="bg-white border border-gray-300 p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {renderDropdown('Scheme', formData.scheme, schemes, (value) => handleInputChange('scheme', value))}
                {renderDropdown('Branch', formData.branch, branches, (value) => handleInputChange('branch', value))}
                {renderDropdown('Semester', formData.semester, semesters, (value) => handleInputChange('semester', value))}
                {renderDropdown('Subject', formData.subject, subjects, (value) => handleInputChange('subject', value))}
                {renderDropdown('Faculty', formData.faculty, faculties, (value) => handleInputChange('faculty', value))}
              </div>
              
              <div className="mt-6 flex justify-end">
                <button className="px-6 py-2 bg-blue-600 text-white text-sm font-medium hover:bg-blue-700 transition-colors">
                  Assign Class
                </button>
              </div>
            </div>
          </div>
        );

      case 'student-management':
        return (
          <div className="space-y-6">
            <h1 className="text-2xl font-semibold text-gray-900">Student Management</h1>
            
            <div className="bg-white border border-gray-300 p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                {renderDropdown('Branch', formData.branch, branches, (value) => handleInputChange('branch', value))}
                {renderDropdown('Semester', formData.semester, semesters, (value) => handleInputChange('semester', value))}
              </div>
              
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">Students List</h3>
                <div className="flex space-x-2">
                  <button className="px-4 py-2 bg-green-600 text-white text-sm font-medium hover:bg-green-700 transition-colors flex items-center">
                    <Plus className="h-4 w-4 mr-1" />
                    Add Student
                  </button>
                  <button className="px-4 py-2 bg-red-600 text-white text-sm font-medium hover:bg-red-700 transition-colors flex items-center">
                    <Minus className="h-4 w-4 mr-1" />
                    Remove Student
                  </button>
                </div>
              </div>
              
              <div className="border border-gray-300">
                <table className="min-w-full divide-y divide-gray-300">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Roll Number</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Branch</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Semester</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-300">
                    {attendanceData.map((student, index) => (
                      <tr key={index} className="hover:bg-gray-50">
                        <td className="px-4 py-3 text-sm font-medium text-gray-900">{student.roll}</td>
                        <td className="px-4 py-3 text-sm text-gray-600">{student.name}</td>
                        <td className="px-4 py-3 text-sm text-gray-600">Computer Science</td>
                        <td className="px-4 py-3 text-sm text-gray-600">5th</td>
                        <td className="px-4 py-3 text-sm">
                          <button className="text-red-600 hover:text-red-800">Remove</button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );

      case 'course-management':
        return (
          <div className="space-y-6">
            <h1 className="text-2xl font-semibold text-gray-900">Course Management</h1>
            
            <div className="bg-white border border-gray-300 p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                {renderDropdown('Branch', formData.branch, branches, (value) => handleInputChange('branch', value))}
                {renderDropdown('Semester', formData.semester, semesters, (value) => handleInputChange('semester', value))}
              </div>
              
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">Courses List</h3>
                <div className="flex space-x-2">
                  <button className="px-4 py-2 bg-green-600 text-white text-sm font-medium hover:bg-green-700 transition-colors flex items-center">
                    <Plus className="h-4 w-4 mr-1" />
                    Add Course
                  </button>
                  <button className="px-4 py-2 bg-red-600 text-white text-sm font-medium hover:bg-red-700 transition-colors flex items-center">
                    <Minus className="h-4 w-4 mr-1" />
                    Remove Course
                  </button>
                </div>
              </div>
              
              <div className="border border-gray-300">
                <table className="min-w-full divide-y divide-gray-300">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Course Code</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Course Name</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Credits</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-300">
                    {subjects.map((subject, index) => (
                      <tr key={index} className="hover:bg-gray-50">
                        <td className="px-4 py-3 text-sm font-medium text-gray-900">CS{300 + index + 1}</td>
                        <td className="px-4 py-3 text-sm text-gray-600">{subject}</td>
                        <td className="px-4 py-3 text-sm text-gray-600">4</td>
                        <td className="px-4 py-3 text-sm text-gray-600">Theory</td>
                        <td className="px-4 py-3 text-sm">
                          <button className="text-red-600 hover:text-red-800">Remove</button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );

      case 'attendance-reports':
        return (
          <div className="space-y-6">
            <h1 className="text-2xl font-semibold text-gray-900">Attendance Reports</h1>
            
            <div className="bg-white border border-gray-300 p-6">
              <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
                {renderDropdown('Branch', formData.branch, branches, (value) => handleInputChange('branch', value))}
                {renderDropdown('Semester', formData.semester, semesters, (value) => handleInputChange('semester', value))}
                {renderDropdown('Subject', formData.subject, subjects, (value) => handleInputChange('subject', value))}
              </div>
              
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">Attendance Report</h3>
                <button className="px-4 py-2 bg-green-600 text-white text-sm font-medium hover:bg-green-700 transition-colors flex items-center">
                  <Download className="h-4 w-4 mr-1" />
                  Export Report
                </button>
              </div>
              
              <div className="border border-gray-300">
                <table className="min-w-full divide-y divide-gray-300">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Roll Number</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Subject</th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Attendance %</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-300">
                    {attendanceData.map((record, index) => (
                      <tr key={index} className="hover:bg-gray-50">
                        <td className="px-4 py-3 text-sm font-medium text-gray-900">{record.roll}</td>
                        <td className="px-4 py-3 text-sm text-gray-600">{record.name}</td>
                        <td className="px-4 py-3 text-sm text-gray-600">{record.subject}</td>
                        <td className="px-4 py-3 text-sm">
                          <span className={`px-2 py-1 text-xs font-medium ${
                            parseInt(record.percentage) >= 75 
                              ? 'bg-green-100 text-green-800' 
                              : 'bg-red-100 text-red-800'
                          }`}>
                            {record.percentage}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );

      default:
        return (
          <div className="text-center py-12">
            <h2 className="text-xl font-medium text-gray-900 mb-2">Coming Soon</h2>
            <p className="text-gray-600">This feature is under development.</p>
          </div>
        );
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <div className="w-64 bg-gray-900 text-white">
        <div className="p-4 border-b border-gray-800">
          <h2 className="text-xl font-semibold">Admin Dashboard</h2>
          <p className="text-sm text-gray-400">Attendance Management</p>
        </div>
        
        <nav className="mt-6">
          {navItems.map((item) => {
            const Icon = item.icon;
            return (
              <button
                key={item.id}
                onClick={() => setActiveTab(item.id)}
                className={`w-full flex items-center px-4 py-3 text-sm font-medium transition-colors ${
                  activeTab === item.id
                    ? 'bg-blue-600 text-white border-r-2 border-blue-400'
                    : 'text-gray-300 hover:bg-gray-800 hover:text-white'
                }`}
              >
                <Icon className="h-5 w-5 mr-3" />
                {item.label}
              </button>
            );
          })}
        </nav>
      </div>

      {/* Main Content */}
      <div className="flex-1 overflow-auto">
        <div className="p-8">
          {renderContent()}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;