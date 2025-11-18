import { ATTENDANCE_FILTERS } from "../../hooks/useTableFilters";

export const ReportTable = ({
    className,
    tableHeaders,
    subjects,
    filteredData,
    handleFilterChange,
}) => (
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
                            if (header === -1) {
                                label = "Total %";
                            } else if (!isNaN(header)) {
                                const sub = subjects.find(s => s.id === header);
                                label = sub ? sub.shortForm : header;
                            }
                            return (
                                <th key={header} className="p-3 font-medium capitalize">
                                    {label}
                                </th>
                            );
                        })}
                    </tr>
                    <tr>
                        {tableHeaders.map((header) => {
                            const isNumericColumn = !isNaN(header);
                            return (
                                <th key={`${header}-filter`} className="p-2 font-normal">
                                    {isNumericColumn ? (
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
                                    className={`p-3 ${
                                        header === "roll"
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
);
