import React from 'react'
import SchemaTable from '../../components/SchemaTable';
import FileUploader from '../../components/FileUploader';

const FacultiesUploadTab = () => {
    const facultySchemaData = [
        { fieldName: 'EMPID', unique: true, notNull: true },
        { fieldName: 'PWD', unique: false, notNull: true },
        { fieldName: 'GENDER', unique: false, notNull: false },
        { fieldName: 'SALU', unique: false, notNull: false },
        { fieldName: 'NAME', unique: false, notNull: true },
    ];
    return (
        <div className='p-8'>
            <h2 className="mb-4 text-xl font-bold">Required CSV Format for Faculties</h2>

            <SchemaTable schema={facultySchemaData}/>
            <FileUploader />
        </div>
    )
}

export default FacultiesUploadTab
