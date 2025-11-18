import { useState, useMemo } from "react";

export const ATTENDANCE_FILTERS = [
    { label: "All", value: "all" },
    { label: "< 40%", value: "lt40" },
    { label: "< 65%", value: "lt65" },
    { label: "< 75%", value: "lt75" },
    { label: "> 40%", value: "gt40" },
    { label: "> 65%", value: "gt65" },
    { label: "> 75%", value: "gt75" },
];

export const useTableFilters = (reportData) => {
    const [filters, setFilters] = useState({});

    const handleFilterChange = (column, value) =>
        setFilters((prev) => ({ ...prev, [column]: value }));

    const filteredData = useMemo(() => {
        return reportData.filter((row) =>
            Object.entries(filters).every(([key, value]) => {
                if (!value || value === "all") return true;

                // Numeric filter for attendance columns
                if (!isNaN(key)) {
                    const num = parseFloat(row[key]) || 0;
                    switch (value) {
                        case "lt40": return num < 40;
                        case "lt65": return num < 65;
                        case "lt75": return num < 75;
                        case "gt40": return num > 40;
                        case "gt65": return num > 65;
                        case "gt75": return num > 75;
                        default: return true;
                    }
                }

                // Text filter for roll/name
                return String(row[key] || "")
                    .toLowerCase()
                    .includes(String(value).toLowerCase());
            })
        );
    }, [reportData, filters]);

    return {
        filters,
        handleFilterChange,
        filteredData,
    };
};
