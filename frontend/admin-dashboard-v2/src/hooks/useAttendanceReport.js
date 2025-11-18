import { useState, useEffect, useCallback } from "react";
import axiosClient from "../api/axiosClient";
import { convertToCSV, downloadCSV } from "../utils/csvExport";

export const useAttendanceReport = (selectedSubjects) => {
    const [reportData, setReportData] = useState([]);
    const [fullReportData, setFullReportData] = useState(null);
    const [className, setClassName] = useState("");
    const [dateFrom, setDateFrom] = useState("");
    const [dateTo, setDateTo] = useState("");

    // Helper to rebuild table data from cached studentMap
    const rebuildReport = useCallback((studentMap, selectedIds) => {
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
    }, []);

    // Fetch full report
    const handleGenerateReport = async (branchId, semester, section) => {
        console.log('Generating report...');
        if (!branchId || !semester || !section) {
            console.log('Please select branch, semester, and section before generating report.');
            return;
        }

        try {
            const res = await axiosClient.get("/student-batches/report", {
                params: {
                    branchId,
                    semester,
                    section,
                    ...(dateFrom && { dateFrom }),
                    ...(dateTo && { dateTo }),
                },
            });

            const data = res.data;
            setClassName(data.className);
            const _studentMap = data.fullStudentAttendanceMap || {};
            setFullReportData(data);
            rebuildReport(_studentMap, [...selectedSubjects, -1]);
        } catch (err) {
            console.error("Error generating report:", err);
        }
    };

    // Handle date filter application
    const handleApplyDateFilter = async (branchId, semester, section) => {
        if (!fullReportData || !dateFrom || !dateTo) return;
        
        try {
            const res = await axiosClient.get("/student-batches/report", {
                params: {
                    branchId,
                    semester,
                    section,
                    startDate: dateFrom,
                    endDate: dateTo,
                },
            });

            const data = res.data;
            setClassName(data.className);
            const _studentMap = data.fullStudentAttendanceMap || {};
            setFullReportData(data);
            rebuildReport(_studentMap, selectedSubjects);
        } catch (err) {
            console.error("Error applying date filter:", err);
        }
    };

    // Clear date filters
    const handleClearDateFilter = useCallback((branchId, semester, section) => {
        setDateFrom("");
        setDateTo("");
        if (fullReportData) {
            handleGenerateReport(branchId, semester, section);
        }
    }, [fullReportData]);

    // Rebuild locally whenever selected subjects change
    useEffect(() => {
        if (!fullReportData) return;
        const _studentMap = fullReportData.fullStudentAttendanceMap || {};
        rebuildReport(_studentMap, selectedSubjects);
    }, [selectedSubjects, fullReportData, rebuildReport]);

    // Export report as CSV
    const handleDownloadReport = useCallback((filteredData, subjects, className) => {
        if (!filteredData || filteredData.length === 0) {
            console.warn("No data to download");
            return;
        }

        // Build headers dynamically
        const headers = [
            { key: 'roll', label: 'Roll Number' },
            { key: 'name', label: 'Name' },
        ];

        // Add subject headers
        selectedSubjects.forEach(subjectId => {
            const subject = subjects.find(s => s.id === subjectId);
            const label = subject ? `${subject.shortForm} (%)` : `Subject ${subjectId} (%)`;
            headers.push({ key: subjectId, label });
        });

        // Add total percentage header
        headers.push({ key: -1, label: 'Total (%)' });

        // Convert to CSV
        const csvContent = convertToCSV(filteredData, headers);

        // Generate filename with timestamp
        const timestamp = new Date().toISOString().split('T')[0];
        const sanitizedClassName = className.replace(/[^a-z0-9]/gi, '_');
        const filename = `Attendance_Report_${sanitizedClassName}_${timestamp}.csv`;

        // Trigger download
        downloadCSV(csvContent, filename);
    }, [selectedSubjects]);

    return {
        reportData,
        className,
        dateFrom,
        setDateFrom,
        dateTo,
        setDateTo,
        handleGenerateReport,
        handleApplyDateFilter,
        handleClearDateFilter,
        handleDownloadReport,
    };
};
