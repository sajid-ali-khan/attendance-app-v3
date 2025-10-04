// ====================================================================================
// File: src/views/AssignClassForm.jsx

import { useState } from "react";
import { SearchableDropdown } from "../components/shared/SearchableDropdown";
import { SectionTitle } from "../components/ui/SectionTitle";
import { SelectInput } from "../components/ui/SelectInput";
import { MOCK_DATA } from "../constants/data";
import { Card } from "../components/ui/Card";

// ====================================================================================
export const AssignClassForm = () => {
    const [selectedFaculty, setSelectedFaculty] = useState('');
    return (
        <div>
            <SectionTitle>Assign Class to Faculty</SectionTitle>
            <Card>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <SelectInput label="Scheme" options={MOCK_DATA.schemes} />
                    <SelectInput label="Branch" options={MOCK_DATA.branches} />
                    <SelectInput label="Semester" options={MOCK_DATA.semesters} />
                    <SelectInput label="Section" options={MOCK_DATA.sections} />
                    <SelectInput label="Subject" options={MOCK_DATA.subjects['Computer Science']} />
                    <SearchableDropdown label="Faculty" options={MOCK_DATA.faculties} selectedOption={selectedFaculty} onChange={setSelectedFaculty} />
                </div>
                <div className="mt-6">
                    <button className="bg-slate-800 text-white font-semibold py-2 px-6 hover:bg-slate-700">Assign</button>
                </div>
            </Card>
        </div>
    );
}; 