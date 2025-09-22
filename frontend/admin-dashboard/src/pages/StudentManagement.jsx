import React from 'react';
import { Plus, Minus } from 'lucide-react';
import CustomDropdown from '../components/CustomDropdown';
import { branches, semesters, attendanceData } from '../constants/dashboardConstants';

const StudentManagement = ({ formData, handleInputChange }) => {
  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-semibold text-gray-900">Student Management</h1>
      
      <div className="bg-white border border-gray-300 p-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          <CustomDropdown label='Branch' value={formData.branch} options={branches} onChange={(value) => handleInputChange('branch', value)} />
          <CustomDropdown label='Semester' value={formData.semester} options={semesters} onChange={(value) => handleInputChange('semester', value)} />
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
};

export default StudentManagement;