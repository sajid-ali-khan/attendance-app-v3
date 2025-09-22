// Description: View for adding, removing, and viewing student data.
// ====================================================================================
import { useState } from "react";
import { SectionTitle } from "../components/ui/SectionTitle";
import { Card } from "../components/ui/Card";
import { SelectInput } from "../components/ui/SelectInput";
import { MOCK_DATA } from "../constants/data";
export const StudentManagementView = () => (
    <div>
        <SectionTitle>Student Management</SectionTitle>
        <Card>
            <h3 className="font-semibold text-slate-700 mb-4">Add New Student</h3>
            <div className="flex items-end gap-4 mb-6 pb-6 border-b border-slate-200">
                <div className="flex-grow"><label className="block text-sm font-medium text-slate-600 mb-1">Student Name</label><input type="text" placeholder="e.g., John Doe" className="w-full p-2 border border-slate-300 bg-white focus:ring-2 focus:ring-slate-500 focus:outline-none" /></div>
                <div className="flex-grow"><label className="block text-sm font-medium text-slate-600 mb-1">Roll Number</label><input type="text" placeholder="e.g., 21CS004" className="w-full p-2 border border-slate-300 bg-white focus:ring-2 focus:ring-slate-500 focus:outline-none" /></div>
                <div className="flex-grow"><SelectInput label="Branch" options={MOCK_DATA.branches} /></div>
                <button className="bg-slate-800 text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 h-10">Add</button>
            </div>
            <h3 className="font-semibold text-slate-700 mb-4">Existing Students</h3>
            <div className="overflow-x-auto border border-slate-200">
                <table className="min-w-full text-sm text-left">
                    <thead className="bg-slate-50 text-slate-600"><tr><th className="p-3 font-medium">Roll Number</th><th className="p-3 font-medium">Name</th><th className="p-3 font-medium">Branch</th><th className="p-3 font-medium">Actions</th></tr></thead>
                    <tbody className="bg-white divide-y divide-slate-200">
                        {MOCK_DATA.students.map(student => (<tr key={student.id}><td className="p-3 font-mono">{student.roll}</td><td className="p-3">{student.name}</td><td className="p-3">{student.branch}</td><td className="p-3"><button className="text-red-600 hover:text-red-800 text-xs font-semibold">REMOVE</button></td></tr>))}
                    </tbody>
                </table>
            </div>
        </Card>
    </div>
);