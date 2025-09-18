import React from 'react'
import SchemaTable from '../../components/SchemaTable'
import FileUploader from '../../components/FileUploader'

const CoursesUploadTab = () => {
    const coursesSchema = [
        {fieldName: "degr", unique: false, notNull: true},
        {fieldName: "scheme", unique: false, notNull: true},
        {fieldName: "branch", unique: false, notNull: true},
        {fieldName: "sem", unique: false, notNull: true},
        {fieldName: "scode", unique: false, notNull: true},
        {fieldName: "subname", unique: false, notNull: true}
    ]
    return (
        <div className="p-8">
            <h2 className="mb-4 text-xl font-bold">Required CSV Format for Courses</h2>

            {/* Here we use the component */}
            <SchemaTable schema={coursesSchema} />
            <FileUploader />
        </div>
    )
}

export default CoursesUploadTab
