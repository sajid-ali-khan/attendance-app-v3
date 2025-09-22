// Description: A custom dropdown component that allows selecting multiple options.
// ====================================================================================
import { useState } from "react";
import React from "react";
export const MultiSelectDropdown = ({ label, options, selectedOptions, onChange }) => {
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = React.useRef(null);

    React.useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, [dropdownRef]);

    const handleOptionToggle = (option) => {
        const newSelectedOptions = [...selectedOptions];
        const optionIndex = newSelectedOptions.indexOf(option);
        if (optionIndex > -1) {
            newSelectedOptions.splice(optionIndex, 1);
        } else {
            newSelectedOptions.push(option);
        }
        onChange(newSelectedOptions);
    };

    return (
        <div className="relative" ref={dropdownRef}>
            <label className="block text-sm font-medium text-slate-600 mb-1">{label}</label>
            <button type="button" onClick={() => setIsOpen(!isOpen)} className="w-full p-2 border border-slate-300 bg-white text-left flex justify-between items-center focus:ring-2 focus:ring-slate-500 focus:outline-none">
                <span className="text-slate-700">
                    {selectedOptions.length > 0 ? `${selectedOptions.length} selected` : `Select ${label}`}
                </span>
                <svg className={`w-4 h-4 text-slate-500 transform transition-transform ${isOpen ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 9l-7 7-7-7"></path></svg>
            </button>
            {isOpen && (
                <div className="absolute z-10 w-full mt-1 bg-white border border-slate-300 shadow-lg max-h-60 overflow-y-auto">
                    {options.map(option => (
                        <label key={option} className="flex items-center w-full px-3 py-2 text-sm text-slate-700 hover:bg-slate-50 cursor-pointer">
                            <input type="checkbox" className="mr-3 h-4 w-4 rounded text-slate-600 focus:ring-slate-500" checked={selectedOptions.includes(option)} onChange={() => handleOptionToggle(option)} />
                            {option}
                        </label>
                    ))}
                </div>
            )}
        </div>
    );
};