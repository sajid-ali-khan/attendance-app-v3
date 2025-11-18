import { SelectInput } from "../ui/SelectInput";
import { MultiSelectDropdown } from "../shared/MultiSelectDropdown";

export const FilterSection = ({
    branches,
    semesters,
    sections,
    subjects,
    selectedBranchId,
    setSelectedBranchId,
    selectedSemester,
    setSelectedSemester,
    selectedSection,
    setSelectedSection,
    selectedSubjects,
    setSelectedSubjects,
}) => (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 items-start mb-6 pb-6 border-b border-slate-200">
        <div>
            <label className="block text-sm font-medium text-slate-600 mb-1">
                Branch
            </label>
            <SelectInput
                label=""
                options={branches}
                onChange={setSelectedBranchId}
                getOptionLabel={(b) => `${b.shortForm} - ${b.fullForm}`}
                getOptionValue={(b) => b.branchCode}
            />
        </div>
        <div>
            <label className="block text-sm font-medium text-slate-600 mb-1">
                Semester
            </label>
            <SelectInput 
                label="" 
                options={semesters} 
                onChange={setSelectedSemester} 
            />
        </div>
        <div>
            <label className="block text-sm font-medium text-slate-600 mb-1">
                Section
            </label>
            <SelectInput 
                label="" 
                options={sections} 
                onChange={setSelectedSection} 
            />
        </div>
        <div>
            <label className="block text-sm font-medium text-slate-600 mb-1">
                Subject(s) <span className="text-slate-400 text-xs">(Optional)</span>
            </label>
            <MultiSelectDropdown
                label=""
                options={subjects}
                selectedOptions={selectedSubjects}
                onChange={setSelectedSubjects}
            />
        </div>
    </div>
);
