import { useEffect, useState } from "react";
import { SectionTitle } from '../components/ui/SectionTitle';
import { Card } from '../components/ui/Card';
import { SelectInput } from '../components/ui/SelectInput';
import axios from "axios";
import { DownloadIcon } from '../components/icons';

const ViewAssignments = () => {
    // State for filter selections
    const [selectedScheme, setSelectedScheme] = useState('');
    const [selectedBranchId, setSelectedBranchId] = useState('');
    const [selectedSemester, setSelectedSemester] = useState('');
    const [selectedSection, setSelectedSection] = useState('');

    const [schemes, setSchemes] = useState([]);
    const [branches, setBranches] = useState([]);
    const [semesters, setSemesters] = useState([]);
    const [sections, setSections] = useState([]);

    // State for UI
    const [viewedAssignments, setViewedAssignments] = useState([]);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [hasViewedAssignments, setHasViewedAssignments] = useState(false);

    const resetSelections = (levels = []) => {
        if (levels.includes("branch")) {
            setSelectedBranchId('');
            setBranches([]);
        }
        if (levels.includes("semester")) {
            setSelectedSemester('');
            setSemesters([]);
        }
        if (levels.includes("section")) {
            setSelectedSection('');
            setSections([]);
        }
    };

    useEffect(() => {
        axios.get('http://localhost:8080/api/schemes')
            .then(response => setSchemes(response.data))
            .catch(error => console.log(error));
    }, []);

    useEffect(() => {
        if (!selectedScheme) return;

        resetSelections(["branch", "semester", "section"]);

        axios.get('http://localhost:8080/api/branches', { params: { scheme: selectedScheme } })
            .then(response => setBranches(response.data))
            .catch(error => console.error("Error fetching branches:", error));
    }, [selectedScheme]);

    useEffect(() => {
        if (!selectedBranchId) return;

        resetSelections(["semester", "section"]);

        axios.get('http://localhost:8080/api/student-batches/semesters', { params: { branchId: selectedBranchId } })
            .then(response => setSemesters(response.data))
            .catch(error => console.error("Error fetching semesters:", error));
    }, [selectedScheme, selectedBranchId]);

    useEffect(() => {
        if (!selectedBranchId || !selectedSemester) return;

        resetSelections(["section"]);

        axios.get('http://localhost:8080/api/student-batches/sections', { params: { branchId: selectedBranchId, semester: selectedSemester } })
            .then(response => setSections(response.data))
            .catch(error => console.error("Error fetching sections:", error));
    }, [selectedScheme, selectedBranchId, selectedSemester]);

    const handleViewAssignments = () => {
        setError('');
        setHasViewedAssignments(true);

        if (!selectedScheme || !selectedBranchId || !selectedSemester || !selectedSection) {
            setError('Please select all filters to view assignments.');
            setViewedAssignments([]);
            return;
        }

        axios.get('http://localhost:8080/api/course-assignments/assignments', {
            params: {
                branchId: selectedBranchId,
                semester: selectedSemester,
                section: selectedSection
            }
        })
            .then(response => setViewedAssignments(response.data))
            .catch(error => console.log(error));
    };

    return (
        <div className="mt-8">
            <SectionTitle>View Class Assignments</SectionTitle>
            <Card>
                {/* Filter Controls */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 items-end mb-6 pb-6 border-b border-slate-200">
                    <SelectInput label="Scheme" options={schemes} value={selectedScheme} onChange={setSelectedScheme} />
                    <SelectInput
                        label="Branch"
                        options={branches}
                        value={selectedBranchId}
                        onChange={setSelectedBranchId}
                        getOptionLabel={(opt) => `${opt.shortForm} - ${opt.fullForm}`}
                        getOptionValue={(opt) => opt.id}
                    />
                    <SelectInput label="Semester" options={semesters} value={selectedSemester} onChange={setSelectedSemester} />
                    <SelectInput label="Section" options={sections} value={selectedSection} onChange={setSelectedSection} />
                </div>

                {/* Action Buttons */}
                <div className="flex items-center gap-4 mb-6">
                    <button
                        onClick={handleViewAssignments}
                        className="bg-slate-800 text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 h-10 disabled:bg-slate-400"
                        disabled={isLoading}
                    >
                        {isLoading ? 'Loading...' : 'View Assignments'}
                    </button>
                </div>

                {/* Error Display */}
                {error && <div className="text-sm text-red-600 mb-4">{error}</div>}

                {/* Results Table */}
                {hasViewedAssignments &&
                    <div className="overflow-x-auto border border-slate-200">
                        <table className="min-w-full text-sm text-left">
                            <thead className="bg-slate-50 text-slate-600">
                                <tr>
                                    <th className="p-3 font-medium">Subject Code</th>
                                    <th className="p-3 font-medium">Subject</th>
                                    <th className="p-3 font-medium">Type</th>
                                    <th className="p-3 font-medium">Assigned Faculty</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-slate-200">
                                {viewedAssignments.length > 0 ? (
                                    viewedAssignments.map((item, idx) => (
                                        <tr key={idx}>
                                            <td className="p-3 font-mono text-slate-500">{item.subjectCode}</td>
                                            <td className="p-3 font-semibold text-slate-800">{item.subject}</td>
                                            <td className="p-3">{item.subjectType}</td>
                                            <td className="p-3">
                                                <div className="flex flex-col gap-1">
                                                    {item.faculties.map((facultyMember, index) => (
                                                        <div key={index}>
                                                            {facultyMember.facultyName}
                                                            <span className="ml-2 text-xs font-mono text-slate-500">
                                                                ({facultyMember.facultyCode})
                                                            </span>
                                                        </div>
                                                    ))}
                                                </div>
                                            </td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan="4" className="p-3 text-center text-red-500 font-semibold">
                                            No assignments on selected class.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                }
            </Card>
        </div>
    );
};

export default ViewAssignments;
