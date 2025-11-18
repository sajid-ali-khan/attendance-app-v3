export const DateRangeFilter = ({
    dateFrom,
    setDateFrom,
    dateTo,
    setDateTo,
    onApplyFilter,
    onClearFilter,
}) => (
    <div className="mb-6 pb-6 border-b border-slate-200">
        <h3 className="font-semibold text-slate-700 mb-3">Filter by Date Range</h3>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
            <div>
                <label className="block text-sm font-medium text-slate-600 mb-1">
                    From Date
                </label>
                <input
                    type="date"
                    value={dateFrom}
                    onChange={(e) => setDateFrom(e.target.value)}
                    className="w-full p-2 border border-slate-300 bg-white focus:ring-2 focus:ring-slate-500 focus:outline-none"
                />
            </div>
            <div>
                <label className="block text-sm font-medium text-slate-600 mb-1">
                    To Date
                </label>
                <input
                    type="date"
                    value={dateTo}
                    onChange={(e) => setDateTo(e.target.value)}
                    className="w-full p-2 border border-slate-300 bg-white focus:ring-2 focus:ring-slate-500 focus:outline-none"
                />
            </div>
            <button
                onClick={onApplyFilter}
                disabled={!dateFrom || !dateTo}
                className="bg-slate-800 cursor-pointer text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 h-10 disabled:bg-slate-400 disabled:cursor-not-allowed"
            >
                Apply Filter
            </button>
            <button
                onClick={onClearFilter}
                className="bg-white text-slate-700 font-semibold py-2 px-4 border border-slate-300 hover:bg-slate-50 transition-colors duration-200 h-10"
            >
                Clear Filter
            </button>
        </div>
    </div>
);
