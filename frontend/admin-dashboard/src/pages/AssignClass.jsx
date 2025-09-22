import React from 'react';
import CustomDropdown from '../components/CustomDropdown';
import { schemes, branches, semesters, subjects, faculties } from '../constants/dashboardConstants';

const AssignClass = ({ formData, handleInputChange }) => {
  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-semibold text-gray-900">Assign Class</h1>
      
      <div className="bg-white border border-gray-300 p-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <CustomDropdown label='Scheme' value={formData.scheme} options={schemes} onChange={(value) => handleInputChange('scheme', value)} />
          <CustomDropdown label='Branch' value={formData.branch} options={branches} onChange={(value) => handleInputChange('branch', value)} />
          <CustomDropdown label='Semester' value={formData.semester} options={semesters} onChange={(value) => handleInputChange('semester', value)} />
          <CustomDropdown label='Subject' value={formData.subject} options={subjects} onChange={(value) => handleInputChange('subject', value)} />
          <CustomDropdown label='Faculty' value={formData.faculty} options={faculties} onChange={(value) => handleInputChange('faculty', value)} />
        </div>
        
        <div className="mt-6 flex justify-end">
          <button className="px-6 py-2 bg-blue-600 text-white text-sm font-medium hover:bg-blue-700 transition-colors">
            Assign Class
          </button>
        </div>
      </div>
    </div>
  );
};

export default AssignClass;