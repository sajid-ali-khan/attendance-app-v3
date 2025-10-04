// Description: A generic, styled dropdown select input component.
// ====================================================================================
// Description: A generic, styled dropdown select input component.
export const SelectInput = ({
    label,
    options,
    value,
    onChange,
    getOptionLabel = (opt) => opt,   // default for strings
    getOptionValue = (opt) => opt,   // default for strings
    ...props
}) => (
    <div>
        <label className="block text-sm font-medium text-slate-600 mb-1">{label}</label>
        <select
            value={value}
            onChange={(e) => onChange(e.target.value)}
            {...props}
            className="w-full p-2 border border-slate-300 bg-white focus:ring-2 focus:ring-slate-500 focus:outline-none"
        >
            <option value="">Select {label}</option>
            {options.map(opt => (
                <option key={getOptionValue(opt)} value={getOptionValue(opt)}>
                    {getOptionLabel(opt)}
                </option>
            ))}
        </select>
    </div>
);
