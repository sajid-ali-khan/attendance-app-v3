import React, { useState } from 'react'
import { SectionTitle } from '../components/ui/SectionTitle'
import { SelectInput } from '../components/ui/SelectInput'
import { Card } from '../components/ui/Card'
import { MOCK_DATA } from '../constants/data'
import { DownloadIcon } from '../components/icons'

const ViewAssignments = () => {

    const [viewedAssignments, setViewedAssignments] = useState([]);

    const handleViewAssignments = () => setViewedAssignments(MOCK_DATA.classAssignments);
    return (
        <div className="mt-8">
            <SectionTitle>View Class Assignments</SectionTitle>
            <Card>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 items-end mb-6 pb-6 border-b border-slate-200">
                    <SelectInput label="Scheme" options={MOCK_DATA.schemes} />
                    <SelectInput label="Branch" options={MOCK_DATA.branches} />
                    <SelectInput label="Semester" options={MOCK_DATA.semesters} />
                    <SelectInput label="Section" options={MOCK_DATA.sections} />
                </div>
                <div className="flex items-center gap-4 mb-6">
                    <button onClick={handleViewAssignments} className="bg-slate-800 text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 h-10">View Assignments</button>
                    {viewedAssignments.length > 0 && (
                        <button className="bg-white text-slate-700 font-semibold py-2 px-4 border border-slate-300 hover:bg-slate-50 transition-colors duration-200 h-10 flex items-center gap-2">
                            <DownloadIcon /> Download Results
                        </button>
                    )}
                </div>
                {viewedAssignments.length > 0 && (
                    <div className="overflow-x-auto border border-slate-200">
                        <table className="min-w-full text-sm text-left">
                            <thead className="bg-slate-50 text-slate-600">
                                <tr>
                                    <th className="p-3 font-medium">Subject</th><th className="p-3 font-medium">Faculty</th><th className="p-3 font-medium">Scheme</th><th className="p-3 font-medium">Branch</th><th className="p-3 font-medium">Semester</th><th className="p-3 font-medium">Section</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-slate-200">
                                {viewedAssignments.map(item => (
                                    <tr key={item.id}>
                                        <td className="p-3 font-semibold text-slate-800">{item.subject}</td><td className="p-3">{item.faculty}</td><td className="p-3">{item.scheme}</td><td className="p-3">{item.branch}</td><td className="p-3 text-center">{item.semester}</td><td className="p-3 text-center">{item.section}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </Card>
        </div>
    )
}

export default ViewAssignments
