import React from 'react';
import { Download } from 'lucide-react';
import CustomDropdown from '../components/CustomDropdown';
import { branches, semesters, subjects, attendanceData } from '../constants/dashboardConstants';

const AttendanceReports = ({ formData, handleInputChange }) => {
  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-semibold text-gray-900">Attendance Reports</h1>
      
      <div className="bg-white border border-gray-300 p-6">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
          <CustomDropdown label='Branch' value={formData.branch} options={branches} onChange={(value) => handleInputChange('branch', value)} />
          <CustomDropdown label='Semester' value={formData.semester} options={semesters} onChange={(value) => handleInputChange('semester', value)} />
          <CustomDropdown label='Subject' value={formData.subject} options={subjects} onChange={(value) => handleInputChange('subject', value)} />
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
};

export default AttendanceReports;