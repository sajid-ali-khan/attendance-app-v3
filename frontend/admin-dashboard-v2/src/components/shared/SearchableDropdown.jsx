// Description: A dropdown component with a search/filter input field.
// ====================================================================================
import { useState } from "react";
import React from "react";
export const SearchableDropdown = ({ label, options, selectedOption, onChange }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");
    const dropdownRef = React.useRef(null);

    React.useEffect(() => {
        setSearchTerm(selectedOption || "");
    }, [selectedOption]);

    React.useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
                setSearchTerm(selectedOption || "");
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, [dropdownRef, selectedOption]);

    const filteredOptions = options.filter(option =>
        option.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const selectOption = (option) => {
        onChange(option);
        setSearchTerm(option);
        setIsOpen(false);
    };
    
    return (
        <div className="relative" ref={dropdownRef}>
            <label className="block text-sm font-medium text-slate-600 mb-1">{label}</label>
            <input
                type="text"
                className="w-full p-2 border border-slate-300 bg-white focus:ring-2 focus:ring-slate-500 focus:outline-none"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onFocus={() => setIsOpen(true)}
                placeholder={`Search & Select ${label}`}
            />
            {isOpen && (
                <div className="absolute z-10 w-full mt-1 bg-white border border-slate-300 shadow-lg max-h-60 overflow-y-auto">
                    {filteredOptions.length > 0 ? (
                        filteredOptions.map(option => (
                            <div key={option} className="px-3 py-2 text-sm text-slate-700 hover:bg-slate-100 cursor-pointer" onClick={() => selectOption(option)}>
                                {option}
                            </div>
                        ))
                    ) : (
                        <div className="px-3 py-2 text-sm text-slate-500">No results found</div>
                    )}
                </div>
            )}
        </div>
    );
};