import { useState, useEffect } from "react";
import axiosClient from "../api/axiosClient";
import { useAuth } from "../provider/AuthProvider";

export const useAssignClassFilters = () => {
    const [selectedScheme, setSelectedScheme] = useState('');
    const [selectedBranchId, setSelectedBranchId] = useState('');
    const [selectedSemester, setSelectedSemester] = useState('');
    const [selectedSection, setSelectedSection] = useState('');
    const [selectedSubjectId, setSelectedSubjectId] = useState('');
    const [selectedFacultyId, setSelectedFacultyId] = useState('');
    
    const [schemes, setSchemes] = useState([]);
    const [branches, setBranches] = useState([]);
    const [semesters, setSemesters] = useState([]);
    const [sections, setSections] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [faculties, setFaculties] = useState([]);
    
    const { token } = useAuth();

    const resetSelections = (levels = []) => {
        if (!token) return;
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

    // Load initial data (schemes, faculties)
    useEffect(() => {
        axiosClient.get('/schemes')
            .then(response => setSchemes(response.data))
            .catch(error => console.error("Error fetching schemes:", error));

        axiosClient.get('/faculties')
            .then(response => setFaculties(response.data))
            .catch(error => console.error("Error fetching faculties:", error));
    }, []);

    // Load branches when scheme changes
    useEffect(() => {
        if (!selectedScheme) return;
        resetSelections(["branch", "semester", "section", "subject"]);

        axiosClient.get('/branches', { params: { scheme: selectedScheme } })
            .then(response => setBranches(response.data))
            .catch(error => console.error("Error fetching branches:", error));
    }, [selectedScheme]);

    // Load semesters when branch changes
    useEffect(() => {
        if (!selectedBranchId) return;
        resetSelections(["semester", "section", "subject"]);
        
        axiosClient.get(`/student-batches/semesters`, { params: { branchId: selectedBranchId } })
            .then(response => setSemesters(response.data))
            .catch(error => console.error("Error fetching semesters:", error));
    }, [selectedScheme, selectedBranchId]);

    // Load sections when semester changes
    useEffect(() => {
        if (!selectedSemester || !selectedBranchId) return;
        resetSelections(["section", "subject"]);
        
        axiosClient.get(`/student-batches/sections`, {
            params: { branchId: selectedBranchId, semester: selectedSemester }
        })
            .then(response => setSections(response.data))
            .catch(error => console.error("Error fetching sections:", error));
    }, [selectedScheme, selectedBranchId, selectedSemester]);

    // Load subjects when section changes
    useEffect(() => {
        if (!selectedSection || !selectedSemester || !selectedBranchId) return;
        resetSelections(["subject"]);

        axiosClient.get('/branch-subjects/subjects', {
            params: { branchId: selectedBranchId, semester: selectedSemester }
        })
            .then(response => setSubjects(response.data))
            .catch(error => console.error("Error fetching subjects:", error));
    }, [selectedScheme, selectedBranchId, selectedSemester, selectedSection]);

    return {
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
    };
};
