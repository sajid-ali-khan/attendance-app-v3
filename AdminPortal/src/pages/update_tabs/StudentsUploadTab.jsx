import React from 'react';
import SchemaTable from '../../components/SchemaTable';
import FileUploader from '../../components/FileUploader'

function StudentsUploadTab() {
  // This is the data for the student schema
  const studentSchemaData = [
    { fieldName: 'ROLLNO', unique: true, notNull: true },
    { fieldName: 'NAME', unique: false, notNull: true },
    { fieldName: 'DEGR', unique: false, notNull: true },
    { fieldName: 'SCHEME', unique: false, notNull: true },
    { fieldName: 'BRANCH', unique: false, notNull: true },
    { fieldName: 'SEC', unique: false, notNull: true },
    { fieldName: 'SEM', unique: false, notNull: true },
  ];

  return (
    <div className="p-8">
      <h2 className="mb-4 text-xl font-bold">Required CSV Format for Students</h2>

      {/* Here we use the component */}
      <SchemaTable schema={studentSchemaData} />
      <FileUploader />
    </div>
  );
}

export default StudentsUploadTab;