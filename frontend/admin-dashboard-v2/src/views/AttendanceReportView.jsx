import { useMemo } from "react";
import { Card } from "../components/ui/Card";
import { SectionTitle } from "../components/ui/SectionTitle";
import { DownloadIcon } from "../components/icons/";
import { useAttendanceFilters } from "../hooks/useAttendanceFilters";
import { useAttendanceReport } from "../hooks/useAttendanceReport";
import { useTableFilters } from "../hooks/useTableFilters";
import { FilterSection } from "../components/attendance/FilterSection";
import { DateRangeFilter } from "../components/attendance/DateRangeFilter";
import { ReportTable } from "../components/attendance/ReportTable";

export const AttendanceReportView = () => {
    // Custom hooks for state management
    const {
        selectedBranchId,
        setSelectedBranchId,
        selectedSemester,
        setSelectedSemester,
        selectedSection,
        setSelectedSection,
        selectedSubjects,
        setSelectedSubjects,
        branches,
        semesters,
        sections,
        subjects,
    } = useAttendanceFilters();

    const {
        reportData,
        className,
        dateFrom,
        setDateFrom,
        dateTo,
        setDateTo,
        handleGenerateReport,
        handleApplyDateFilter,
        handleClearDateFilter,
    } = useAttendanceReport(selectedSubjects);

    const { handleFilterChange, filteredData } = useTableFilters(reportData);

    const tableHeaders = useMemo(
        () => ["roll", "name", ...selectedSubjects, -1],
        [selectedSubjects]
    );

    return (
        <div>
            <SectionTitle>Attendance Reports</SectionTitle>
            <Card>
                <FilterSection
                    branches={branches}
                    semesters={semesters}
                    sections={sections}
                    subjects={subjects}
                    selectedBranchId={selectedBranchId}
                    setSelectedBranchId={setSelectedBranchId}
                    selectedSemester={selectedSemester}
                    setSelectedSemester={setSelectedSemester}
                    selectedSection={selectedSection}
                    setSelectedSection={setSelectedSection}
                    selectedSubjects={selectedSubjects}
                    setSelectedSubjects={setSelectedSubjects}
                />

                {/* Buttons */}
                <div className="flex items-center gap-4 mb-6">
                    <button
                        onClick={() => handleGenerateReport(selectedBranchId, selectedSemester, selectedSection)}
                        className="bg-slate-800 cursor-pointer text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 h-10"
                    >
                        Generate Report
                    </button>
                    {reportData.length > 0 && (
                        <button className="bg-white text-slate-700 font-semibold py-2 px-4 border border-slate-300 hover:bg-slate-50 transition-colors duration-200 h-10 flex items-center gap-2">
                            <DownloadIcon /> Download Report
                        </button>
                    )}
                </div>

                {/* Date Range Filters */}
                {reportData.length > 0 && (
                    <DateRangeFilter
                        dateFrom={dateFrom}
                        setDateFrom={setDateFrom}
                        dateTo={dateTo}
                        setDateTo={setDateTo}
                        onApplyFilter={() => handleApplyDateFilter(selectedBranchId, selectedSemester, selectedSection)}
                        onClearFilter={() => handleClearDateFilter(selectedBranchId, selectedSemester, selectedSection)}
                    />
                )}

                {/* Report Table */}
                {reportData.length > 0 && (
                    <ReportTable
                        className={className}
                        tableHeaders={tableHeaders}
                        subjects={subjects}
                        filteredData={filteredData}
                        handleFilterChange={handleFilterChange}
                    />
                )}
            </Card>
        </div>
    );
};
