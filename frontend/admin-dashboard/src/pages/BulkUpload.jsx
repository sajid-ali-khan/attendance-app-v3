import React, { useState } from 'react';
import { Upload } from 'lucide-react';
import SchemaTable from '../components/SchemaTable';
import { studentSchema, courseSchema, facultySchema } from '../constants/dashboardConstants';

const BulkUpload = () => {
  const [uploadTab, setUploadTab] = useState('students');

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
          {uploadTab === 'students' && <SchemaTable schema={studentSchema} />}
          {uploadTab === 'courses' && <SchemaTable schema={courseSchema} />}
          {uploadTab === 'faculties' && <SchemaTable schema={facultySchema} />}
          
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
};

export default BulkUpload;