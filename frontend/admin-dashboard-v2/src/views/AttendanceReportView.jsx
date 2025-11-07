import { useState, useEffect } from "react";
import { SelectInput } from "../components/ui/SelectInput";
import { Card } from "../components/ui/Card";
import { SectionTitle } from "../components/ui/SectionTitle";
import { MultiSelectDropdown } from "../components/shared/MultiSelectDropdown";
import { DownloadIcon } from "../components/icons/";
import axiosClient from "../api/axiosClient";

export const AttendanceReportView = () => {
    const [selectedSubjects, setSelectedSubjects] = useState([]); // stores selected subject IDs
    const [reportData, setReportData] = useState([]);
    const [fullReportData, setFullReportData] = useState(null); // ✅ full API response stored here
    const [filters, setFilters] = useState({});
    const [selectedBranchId, setSelectedBranchId] = useState(0);
    const [selectedSemester, setSelectedSemester] = useState(0);
    const [selectedSection, setSelectedSection] = useState(0);
    const [branches, setBranches] = useState([]);
    const [semesters, setSemesters] = useState([]);
    const [sections, setSections] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [className, setClassName] = useState("");

    const resetSelections = (levels = []) => {
        if (levels.includes("branch")) {
            setSelectedBranchId("");
            setBranches([]);
        }
        if (levels.includes("semester")) {
            setSelectedSemester("");
            setSemesters([]);
        }
        if (levels.includes("section")) {
            setSelectedSection("");
            setSections([]);
        }
        if (levels.includes("subjects")) {
            setSelectedSubjects([]);
            setSubjects([]);
        }
    };

    const ATTENDANCE_FILTERS = [
        { label: "All", value: "all" },
        { label: "< 65%", value: "lt65" },
        { label: "< 75%", value: "lt75" },
        { label: "> 65%", value: "gt65" },
        { label: "> 75%", value: "gt75" },
    ];


    // 🔹 Load branches once
    useEffect(() => {
        resetSelections(["branch", "semester", "section", "subjects"]);
        axiosClient
            .get("/student-batches/branches")
            .then((res) => setBranches(res.data))
            .catch((err) => console.error("Error fetching branches:", err));
    }, []);

    // 🔹 Load semesters when branch changes
    useEffect(() => {
        if (!selectedBranchId) return;
        resetSelections(["semester", "section", "subjects"]);
        axiosClient
            .get(`/student-batches/semesters`, { params: { branchId: selectedBranchId } })
            .then((res) => setSemesters(res.data))
            .catch((err) => console.error("Error fetching semesters:", err));
    }, [selectedBranchId]);

    // 🔹 Load sections when semester changes
    useEffect(() => {
        if (!selectedSemester || !selectedBranchId) return;
        resetSelections(["section", "subjects"]);
        axiosClient
            .get(`/student-batches/sections`, {
                params: { branchId: selectedBranchId, semester: selectedSemester },
            })
            .then((res) => setSections(res.data))
            .catch((err) => console.error("Error fetching sections:", err));
    }, [selectedBranchId, selectedSemester]);

    // 🔹 Load subjects when section changes
    useEffect(() => {
        if (!selectedSection || !selectedSemester || !selectedBranchId) return;
        resetSelections(["subjects"]);
        axiosClient
            .get("/student-batches/subjects", {
                params: {
                    branchId: selectedBranchId,
                    semester: selectedSemester,
                    section: selectedSection,
                },
            })
            .then((res) => setSubjects(res.data))
            .catch((err) => console.error("Error fetching subjects:", err));
    }, [selectedBranchId, selectedSemester, selectedSection]);

    // ✅ Fetch full report only once on button click
    const handleGenerateReport = async () => {
        if (!selectedBranchId || !selectedSemester || !selectedSection) return;

        try {
            const res = await axiosClient.get("/student-batches/report", {
                params: {
                    branchId: selectedBranchId,
                    semester: selectedSemester,
                    section: selectedSection,
                },
            });

            const data = res.data;
            setClassName(data.className);
            const _studentMap = data.fullStudentAttendanceMap || {};
            setFullReportData(data);
            rebuildReport(_studentMap, [...selectedSubjects, -1]); // build initial view (total only)
        } catch (err) {
            console.error("Error generating report:", err);
        }
    };

    // ✅ Rebuild locally whenever selected subjects change
    useEffect(() => {
        if (!fullReportData) return;
        const _studentMap = fullReportData.fullStudentAttendanceMap || {};
        rebuildReport(_studentMap, selectedSubjects);
    }, [selectedSubjects]);

    // ✅ Helper to rebuild table data from cached studentMap
    const rebuildReport = (studentMap, selectedIds) => {
        const processedData = Object.values(studentMap).map((student) => {
            const row = { roll: student.roll, name: student.name };
            selectedIds.forEach((subjectId) => {
                const entry = student.subjectAttendanceMap[String(subjectId)];
                row[subjectId] = entry ? entry.percentage.toFixed(2) : 0;
            });
            const entry = student.subjectAttendanceMap[String(-1)];
            row[-1] = entry ? entry.percentage.toFixed(2) : 0;
            return row;
        });
        setReportData(processedData);
    };

    // ✅ Filter logic
    const handleFilterChange = (column, value) =>
        setFilters((prev) => ({ ...prev, [column]: value }));

    const filteredData = reportData.filter((row) =>
        Object.entries(filters).every(([key, value]) => {
            if (!value || value === "all") return true;

            // 🧮 Numeric filter for attendance columns
            if (!isNaN(key)) {
                const num = parseFloat(row[key]) || 0;
                switch (value) {
                    case "lt65":
                        return num < 65;
                    case "lt75":
                        return num < 75;
                    case "gt65":
                        return num > 65;
                    case "gt75":
                        return num > 75;
                    default:
                        return true;
                }
            }

            // 🔤 Text filter for roll/name
            return String(row[key] || "")
                .toLowerCase()
                .includes(String(value).toLowerCase());
        })
    );


    const tableHeaders = ["roll", "name", ...selectedSubjects, -1];

    return (
        <div>
            <SectionTitle>Attendance Reports</SectionTitle>
            <Card>
                {/* Filters */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 items-start mb-6 pb-6 border-b border-slate-200">
                    <SelectInput
                        label="Branch"
                        options={branches}
                        onChange={setSelectedBranchId}
                        getOptionLabel={(b) => `${b.shortForm} - ${b.fullForm}`}
                        getOptionValue={(b) => b.branchCode}
                    />
                    <SelectInput label="Semester" options={semesters} onChange={setSelectedSemester} />
                    <SelectInput label="Section" options={sections} onChange={setSelectedSection} />
                    <MultiSelectDropdown
                        label="Subject(s)"
                        options={subjects}
                        selectedOptions={selectedSubjects}
                        onChange={setSelectedSubjects}
                    />
                </div>

                {/* Buttons */}
                <div className="flex items-center gap-4 mb-6">
                    <button
                        onClick={handleGenerateReport}
                        className="bg-slate-800 cursor-pointer   text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 h-10"
                    >
                        Generate Report
                    </button>
                    {reportData.length > 0 && (
                        <button className="bg-white text-slate-700 font-semibold py-2 px-4 border border-slate-300 hover:bg-slate-50 transition-colors duration-200 h-10 flex items-center gap-2">
                            <DownloadIcon /> Download Report
                        </button>
                    )}
                </div>

                {/* Table */}
                {reportData.length > 0 && (
                    <div>
                        <h3 className="font-semibold text-slate-700 mb-4">
                            Report for {className}
                        </h3>
                        <div className="overflow-x-auto border border-slate-200">
                            <table className="min-w-full text-sm text-left">
                                <thead className="bg-slate-50 text-slate-600">
                                    <tr>
                                        {tableHeaders.map((header) => {
                                            let label = header;
                                            if (!isNaN(header)) {
                                                label = header === -1 ? "Total %" : subjects.find(s => s.id === header)?.shortForm || header;
                                            }
                                            return (
                                                <th key={header} className="p-3 font-medium capitalize">
                                                    {label}
                                                </th>
                                            );
                                        })}
                                    </tr>
                                    {/* <tr>
                                        {tableHeaders.map((header) => (
                                            <th key={`${header}-filter`} className="p-2 font-normal">
                                                <input
                                                    type="text"
                                                    placeholder="Filter..."
                                                    className="w-full p-1 border border-slate-300 text-sm font-normal focus:ring-1 focus:ring-slate-500 focus:outline-none"
                                                    onChange={(e) => handleFilterChange(header, e.target.value)}
                                                />
                                            </th>
                                        ))}
                                    </tr> */}
                                    <tr>
                                        {tableHeaders.map((header) => {
                                            const isNumericColumn = !isNaN(header); // attendance columns
                                            return (
                                                <th key={`${header}-filter`} className="p-2 font-normal">
                                                    {isNumericColumn ? (
                                                        // ✅ Dropdown for attendance filters
                                                        <select
                                                            className="w-full p-1 border border-slate-300 text-sm font-normal focus:ring-1 focus:ring-slate-500 focus:outline-none"
                                                            onChange={(e) => handleFilterChange(header, e.target.value)}
                                                        >
                                                            {ATTENDANCE_FILTERS.map((opt) => (
                                                                <option key={opt.value} value={opt.value}>
                                                                    {opt.label}
                                                                </option>
                                                            ))}
                                                        </select>
                                                    ) : (
                                                        // ✅ Text input for roll/name
                                                        <input
                                                            type="text"
                                                            placeholder="Filter..."
                                                            className="w-full p-1 border border-slate-300 text-sm font-normal focus:ring-1 focus:ring-slate-500 focus:outline-none"
                                                            onChange={(e) => handleFilterChange(header, e.target.value)}
                                                        />
                                                    )}
                                                </th>
                                            );
                                        })}
                                    </tr>

                                </thead>
                                <tbody className="bg-white divide-y divide-slate-200">
                                    {filteredData.map((item) => (
                                        <tr key={item.roll}>
                                            {tableHeaders.map((header) => (
                                                <td
                                                    key={`${item.roll}-${header}`}
                                                    className={`p-3 ${header === "roll"
                                                        ? "font-mono"
                                                        : !isNaN(item[header])
                                                            ? "font-semibold text-center"
                                                            : ""
                                                        }`}
                                                >
                                                    {!isNaN(item[header]) ? `${item[header]}%` : item[header]}
                                                </td>
                                            ))}
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}
            </Card>
        </div>
    );
};
