// Description: View for generating and filtering attendance reports.
// ====================================================================================
import { useState } from "react";
import { SelectInput } from "../components/ui/SelectInput";
import { Card } from "../components/ui/Card";
import { SectionTitle } from "../components/ui/SectionTitle";
import { MOCK_DATA } from "../constants/data";
import { MultiSelectDropdown } from "../components/shared/MultiSelectDropdown";

export const AttendanceReportView = () => {
    const [selectedSubjects, setSelectedSubjects] = useState([]);
    const [reportData, setReportData] = useState([]);
    const [filters, setFilters] = useState({});

    const handleGenerateReport = () => {
        if (selectedSubjects.length === 0) { setReportData([]); return; }
        const newReportData = MOCK_DATA.attendanceReport.map(student => {
            let totalPercentage = 0;
            const studentSubjectData = {};
            selectedSubjects.forEach(subject => {
                const percentage = student.attendance[subject] || 0;
                studentSubjectData[subject] = percentage;
                totalPercentage += percentage;
            });
            const average = totalPercentage / selectedSubjects.length;
            return { roll: student.roll, name: student.name, ...studentSubjectData, total: average.toFixed(2) };
        });
        setReportData(newReportData);
    };
    
    const handleFilterChange = (column, value) => setFilters(prev => ({...prev, [column]: value}));

    const filteredData = reportData.filter(row => 
        Object.entries(filters).every(([key, value]) => {
            if (!value) return true;
            if(!isNaN(value)) return parseFloat(row[key]) >= parseFloat(value);
            return String(row[key] || '').toLowerCase().includes(String(value).toLowerCase());
        })
    );
    const tableHeaders = ['roll', 'name', ...selectedSubjects, 'total'];

    return (
    <div>
        <SectionTitle>Attendance Reports</SectionTitle>
        <Card>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 items-start mb-6 pb-6 border-b border-slate-200">
                <SelectInput label="Branch" options={MOCK_DATA.branches} />
                <SelectInput label="Semester" options={MOCK_DATA.semesters} />
                <SelectInput label="Section" options={MOCK_DATA.sections} />
                 <MultiSelectDropdown label="Subject(s)" options={MOCK_DATA.subjects['Computer Science']} selectedOptions={selectedSubjects} onChange={setSelectedSubjects} />
            </div>
            <div className="flex items-center gap-4 mb-6">
                 <button onClick={handleGenerateReport} className="bg-slate-800 text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 h-10">Generate Report</button>
                 {reportData.length > 0 && (<button className="bg-white text-slate-700 font-semibold py-2 px-4 border border-slate-300 hover:bg-slate-50 transition-colors duration-200 h-10 flex items-center gap-2"><DownloadIcon /> Download Report</button>)}
            </div>
           {reportData.length > 0 && (
            <div>
                <h3 className="font-semibold text-slate-700 mb-4">Report for Computer Science - Semester 4</h3>
                <div className="overflow-x-auto border border-slate-200">
                    <table className="min-w-full text-sm text-left">
                        <thead className="bg-slate-50 text-slate-600">
                            <tr>{tableHeaders.map(header => (<th key={header} className="p-3 font-medium capitalize">{header.replace('_', ' ')}</th>))}</tr>
                            <tr>{tableHeaders.map(header => (<th key={`${header}-filter`} className="p-2 font-normal"><input type="text" placeholder={`Filter...`} className="w-full p-1 border border-slate-300 text-sm font-normal focus:ring-1 focus:ring-slate-500 focus:outline-none" onChange={(e) => handleFilterChange(header, e.target.value)} /></th>))}</tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-slate-200">
                            {filteredData.map(item => (<tr key={item.roll}>{tableHeaders.map(header => (<td key={`${item.roll}-${header}`} className={`p-3 ${header === 'roll' ? 'font-mono' : ''} ${!isNaN(item[header]) ? 'font-semibold text-center' : ''}`}>{!isNaN(item[header]) ? `${item[header]}%` : item[header]}</td>))}</tr>))}
                        </tbody>
                    </table>
                </div>
            </div>
           )}
        </Card>
    </div>
    )
};