// ====================================================================================
// File: src/views/AssignClassForm.jsx

import { SearchableDropdown } from "../components/shared/SearchableDropdown";
import { SectionTitle } from "../components/ui/SectionTitle";
import { SelectInput } from "../components/ui/SelectInput";
import { Card } from "../components/ui/Card";
import { useAssignClassFilters } from "../hooks/useAssignClassFilters";
import { useAssignClass } from "../hooks/useAssignClass";

// ====================================================================================
export const AssignClassForm = () => {
    const {
        selectedScheme,
        setSelectedScheme,
        selectedBranchId,
        setSelectedBranchId,
        selectedSemester,
        setSelectedSemester,
        selectedSection,
        setSelectedSection,
        selectedSubjectId,
        setSelectedSubjectId,
        selectedFacultyId,
        setSelectedFacultyId,
        schemes,
        branches,
        semesters,
        sections,
        subjects,
        faculties,
        resetSelections,
    } = useAssignClassFilters();

    const { errorMessage, successMessage, handleAssignClass } = useAssignClass(resetSelections);

    const resetForm = () => {
        setSelectedScheme('');
        resetSelections(["branch", "semester", "section", "subject"]);
        setSelectedFacultyId('');
    };

    const onSubmit = () => {
        const payload = {
            branchId: selectedBranchId,
            semester: selectedSemester,
            section: selectedSection,
            subjectId: selectedSubjectId,
            facultyId: selectedFacultyId
        };
        handleAssignClass(payload, resetForm);
    };

    return (
        <div>
            <SectionTitle>Assign Class to Faculty</SectionTitle>
            <Card>
                {/* Notification Area */}
                {errorMessage && (
                    <div className="mb-4 text-sm text-red-700 bg-red-100 border border-red-300 px-4 py-3 w-full" role="alert">
                        <p className="font-bold">Error:</p>
                        <p>{errorMessage}</p>
                    </div>
                )}
                {successMessage && (
                    <div className="mb-4 text-sm text-green-700 bg-green-100 border border-green-300 px-4 py-3 w-full" role="alert">
                        <p className="font-bold">Success:</p>
                        <p>{successMessage}</p>
                    </div>
                )}
                
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <SelectInput 
                        label="Scheme" 
                        options={schemes} 
                        value={selectedScheme} 
                        onChange={setSelectedScheme} 
                    />
                    <SelectInput 
                        label="Branch" 
                        options={branches} 
                        value={selectedBranchId} 
                        onChange={setSelectedBranchId} 
                        getOptionLabel={(opt) => `${opt.shortForm} - ${opt.fullForm}`} 
                        getOptionValue={(opt) => opt.id} 
                    />
                    <SelectInput 
                        label="Semester" 
                        options={semesters} 
                        value={selectedSemester} 
                        onChange={setSelectedSemester} 
                    />
                    <SelectInput 
                        label="Section" 
                        options={sections} 
                        value={selectedSection} 
                        onChange={setSelectedSection} 
                    />
                    <SelectInput 
                        label="Subject" 
                        options={subjects} 
                        value={selectedSubjectId} 
                        onChange={setSelectedSubjectId} 
                        getOptionLabel={(opt) => `${opt.shortForm} - ${opt.fullForm}`} 
                        getOptionValue={(opt) => opt.id} 
                    />
                    <SearchableDropdown 
                        label="Faculty" 
                        options={faculties} 
                        selectedFacultyId={selectedFacultyId} 
                        setSelectedFacultyId={setSelectedFacultyId} 
                    />
                </div>
                
                <div className="mt-6">
                    <button 
                        className="bg-slate-800 cursor-pointer text-white font-semibold py-2 px-6 hover:bg-slate-700" 
                        onClick={onSubmit}
                    >
                        Assign
                    </button>
                </div>
            </Card>
        </div>
    );
};