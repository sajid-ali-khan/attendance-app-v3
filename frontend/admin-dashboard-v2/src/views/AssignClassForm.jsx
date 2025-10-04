// ====================================================================================
// File: src/views/AssignClassForm.jsx

import { useEffect, useState } from "react";
import { SearchableDropdown } from "../components/shared/SearchableDropdown";
import { SectionTitle } from "../components/ui/SectionTitle";
import { SelectInput } from "../components/ui/SelectInput";
import { MOCK_DATA } from "../constants/data";
import { Card } from "../components/ui/Card";
import axios from "axios";

// ====================================================================================
export const AssignClassForm = () => {
    const [selectedFacultyId, setSelectedFacultyId] = useState('');
    const [selectedScheme, setSelectedScheme] = useState('');
    const [selectedBranchId, setSelectedBranchId] = useState('');
    const [selectedSemester, setSelectedSemester] = useState('');
    const [selectedSection, setSelectedSection] = useState('');
    const [selectedSubjectId, setSelectedSubjectId] = useState('');

    const [schemes, setSchemes] = useState([]);
    const [branches, setBranches] = useState([]);
    const [semesters, setSemesters] = useState([]);
    const [sections, setSections] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [faculties, setFaculties] = useState([]);
    
    // State for UI feedback
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const resetSelections = (levels = []) => {
        if (levels.includes("branch")) {
            setSelectedBranchId('');
            setBranches([]);
        }
        if (levels.includes("semester")) {
            setSelectedSemester('');
            setSemesters([]);
        }
        if (levels.includes("section")) {
            setSelectedSection('');
            setSections([]);
        }
        if (levels.includes("subject")) {
            setSelectedSubjectId('');
            setSubjects([]);
        }
    };


    useEffect(() => {
        // set initial data(schemes, faculties)
        const fetchInitialData = () => {
            axios.get('http://localhost:8080/api/schemes')
                .then(response => {
                    setSchemes(response.data);
                })
                .catch(error => {
                    console.error("Error fetching schemes:", error);
                });

            axios.get('http://localhost:8080/api/faculties')
                .then(response => {
                    setFaculties(response.data);
                })
                .catch(error => {
                    console.error("Error fetching faculties:", error);
                });
        };
        fetchInitialData();
    }, []);

    useEffect(() => {
        // reset branch for selected scheme
        if (!selectedScheme) return;
        resetSelections(["branch", "semester", "section", "subject"]);

        axios.get('http://localhost:8080/api/branches', {
            params: { scheme: selectedScheme }
        })
            .then(response => {
                setBranches(response.data);
                // console.log("Fetched branches for scheme:", selectedScheme, " - ", response.data);
            })
            .catch(error => {
                console.error("Error fetching branches:", error);
            });
    }, [selectedScheme]);

    useEffect(() => {
        // Reset the semesters based on branch
        if (!selectedBranchId) return;
        resetSelections(["semester", "section", "subject"]);
        axios.get(`http://localhost:8080/api/student-batches/semesters`, {
            params: { branchId: selectedBranchId }
        })
            .then(response => {
                setSemesters(response.data);
                // console.log("Fetched semesters for branch:", selectedBranchId, " - ", response.data);
            })
            .catch(error => {
                console.error("Error fetching semesters:", error);
            });
    }, [selectedScheme, selectedBranchId]);

    useEffect(() => {
        // Reset the sections based on semester
        if (!selectedSemester || !selectedBranchId) return;
        resetSelections(["section", "subject"]);
        axios.get(`http://localhost:8080/api/student-batches/sections`, {
            params: { branchId: selectedBranchId, semester: selectedSemester }
        })
            .then(response => {
                setSections(response.data);
                // console.log("Fetched sections for semester:", selectedSemester, " - ", response.data);
            })
            .catch(error => {
                console.error("Error fetching sections:", error);
            });
    }, [selectedScheme, selectedBranchId, selectedSemester]);

    useEffect(() => {
        // Reset subjects based on section
        if (!selectedSection || !selectedSemester || !selectedBranchId) return;
        resetSelections(["subject"]);

        axios.get('http://localhost:8080/api/branch-subjects/subjects', {
            params: { branchId: selectedBranchId, semester: selectedSemester }
        })
            .then(response => {
                setSubjects(response.data);
                // console.log("Fetched subjects for section:", selectedSection, " - ", response.data);
            })
            .catch(error => {
                console.error("Error fetching subjects:", error);
            });
    }, [selectedScheme, selectedBranchId, selectedSemester, selectedSection]);

    const handleAssignClass = () => {
        // Clear previous messages before a new attempt
        setSuccessMessage('');
        setErrorMessage('');

        // Frontend validation
        if (!selectedBranchId || !selectedSemester || !selectedSection || !selectedSubjectId || !selectedFacultyId) {
            setErrorMessage('Please ensure all fields are selected before assigning.');
            return;
        }
        const payload = {
            branchId: selectedBranchId,
            semester: selectedSemester,
            section: selectedSection,
            subjectId: selectedSubjectId,
            facultyId: selectedFacultyId
        }
        console.log("Assigning class:",payload);
        axios.post('http://localhost:8080/api/course-assignments', payload)
            .then(response => {
                setSuccessMessage("Class has been assigned successfully!");
                // Optionally reset form fields after success
                setSelectedScheme('');
                resetSelections(["branch", "semester", "section", "subject"]);
                setSelectedFacultyId('');
            })
            .catch(error => {
                console.error("Error assigning class:", error);
                // Try to get a specific error message from the backend, otherwise show a generic one
                const message = error.response?.data?.message || "Failed to assign class. Please try again.";
                setErrorMessage(message);
            });
    }

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
                    <SelectInput label="Scheme" options={schemes} value={selectedScheme} onChange={setSelectedScheme} />
                    <SelectInput label="Branch" options={branches} value={selectedBranchId} onChange={setSelectedBranchId} getOptionLabel={(opt) => `${opt.shortForm} - ${opt.fullForm}`} getOptionValue={(opt) => opt.id} />
                    <SelectInput label="Semester" options={semesters} value={selectedSemester} onChange={setSelectedSemester} />
                    <SelectInput label="Section" options={sections} value={selectedSection} onChange={setSelectedSection} />
                    <SelectInput label="Subject" options={subjects} value={selectedSubjectId} onChange={setSelectedSubjectId} getOptionLabel={(opt) => `${opt.shortForm} - ${opt.fullForm}`} getOptionValue={(opt) => opt.id} />
                    <SearchableDropdown label="Faculty" options={faculties} selectedFacultyId={selectedFacultyId} setSelectedFacultyId={setSelectedFacultyId} />
                </div>
                <div className="mt-6">
                    <button className="bg-slate-800 text-white font-semibold py-2 px-6 hover:bg-slate-700" onClick={handleAssignClass}>Assign</button>
                </div>
            </Card>
        </div>
    );
}; 