import { useState, useEffect } from "react";
import axiosClient from "../api/axiosClient";

export const useAttendanceFilters = () => {
    const [selectedBranchId, setSelectedBranchId] = useState(0);
    const [selectedSemester, setSelectedSemester] = useState(0);
    const [selectedSection, setSelectedSection] = useState(0);
    const [selectedSubjects, setSelectedSubjects] = useState([]);
    
    const [branches, setBranches] = useState([]);
    const [semesters, setSemesters] = useState([]);
    const [sections, setSections] = useState([]);
    const [subjects, setSubjects] = useState([]);

    const resetSelections = (levels = []) => {
        if (levels.includes("branch")) {
            setSelectedBranchId("");
            setBranches([]);
        }
        if (levels.includes("semester")) {
            setSelectedSemester("");
            setSemesters([]);
        }
        if (levels.includes("section")) {
            setSelectedSection("");
            setSections([]);
        }
        if (levels.includes("subjects")) {
            setSelectedSubjects([]);
            setSubjects([]);
        }
    };

    // Load branches once
    useEffect(() => {
        resetSelections(["branch", "semester", "section", "subjects"]);
        axiosClient
            .get("/student-batches/branches")
            .then((res) => setBranches(res.data))
            .catch((err) => console.error("Error fetching branches:", err));
    }, []);

    // Load semesters when branch changes
    useEffect(() => {
        if (!selectedBranchId) return;
        resetSelections(["semester", "section", "subjects"]);
        axiosClient
            .get(`/student-batches/semesters`, { params: { branchId: selectedBranchId } })
            .then((res) => setSemesters(res.data))
            .catch((err) => console.error("Error fetching semesters:", err));
    }, [selectedBranchId]);

    // Load sections when semester changes
    useEffect(() => {
        if (!selectedSemester || !selectedBranchId) return;
        resetSelections(["section", "subjects"]);
        axiosClient
            .get(`/student-batches/sections`, {
                params: { branchId: selectedBranchId, semester: selectedSemester },
            })
            .then((res) => setSections(res.data))
            .catch((err) => console.error("Error fetching sections:", err));
    }, [selectedBranchId, selectedSemester]);

    // Load subjects when section changes
    useEffect(() => {
        if (!selectedSection || !selectedSemester || !selectedBranchId) return;
        resetSelections(["subjects"]);
        axiosClient
            .get("/student-batches/subjects", {
                params: {
                    branchId: selectedBranchId,
                    semester: selectedSemester,
                    section: selectedSection,
                },
            })
            .then((res) => setSubjects(res.data))
            .catch((err) => console.error("Error fetching subjects:", err));
    }, [selectedBranchId, selectedSemester, selectedSection]);

    return {
        selectedBranchId,
        setSelectedBranchId,
        selectedSemester,
        setSelectedSemester,
        selectedSection,
        setSelectedSection,
        selectedSubjects,
        setSelectedSubjects,
        branches,
        semesters,
        sections,
        subjects,
    };
};
