// Description: View for uploading bulk data via CSV files.
// ====================================================================================
import { useState } from 'react';
import { SCHEMA_DATA } from '../constants/data';
import { Card } from "../components/ui/Card";
import { SectionTitle } from "../components/ui/SectionTitle";
import { FileUploader } from "../components/ui/FileUploader";
import { SchemaTable } from "../components/shared/SchemaTable";

export const BulkUploadView = () => {
  const [activeTab, setActiveTab] = useState('students');
  const tabs = [{ id: 'students', label: 'Students' }, { id: 'courses', label: 'Courses' }, { id: 'faculties', label: 'Faculties' }];
  const api = {
    "students": "student api",
    "courses": "courses api",
    "faculties": "faculties api"
  }

  return (
    <div>
      <SectionTitle>Bulk Data Upload</SectionTitle>
      <Card>
        <div className="border-b border-slate-200">
          <nav className="-mb-px flex space-x-6">
            {tabs.map(tab => (
              <button key={tab.id} onClick={() => setActiveTab(tab.id)} className={`whitespace-nowrap pb-3 px-1 border-b-2 font-medium text-sm transition-colors duration-200 ${activeTab === tab.id ? 'border-slate-800 text-slate-800' : 'border-transparent text-slate-500 hover:text-slate-700 hover:border-slate-300'}`}>
                {tab.label}
              </button>
            ))}
          </nav>
        </div>
        <div className="pt-6">
            <SchemaTable schema={SCHEMA_DATA[activeTab]} />
            <FileUploader />
        </div>
      </Card>
    </div>
  );
};