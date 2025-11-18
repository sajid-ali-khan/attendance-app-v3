import { useState } from "react";
import axiosClient from "../api/axiosClient";

export const useAssignClass = (resetSelections) => {
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleAssignClass = async (payload, resetForm) => {
        setSuccessMessage('');
        setErrorMessage('');

        // Frontend validation
        const { branchId, semester, section, subjectId, facultyId } = payload;
        if (!branchId || !semester || !section || !subjectId || !facultyId) {
            setErrorMessage('Please ensure all fields are selected before assigning.');
            return;
        }

        try {
            await axiosClient.post('/course-assignments', payload);
            setSuccessMessage("Class has been assigned successfully!");
            resetForm();
        } catch (error) {
            console.error("Error assigning class:", error);
            const message = error.response?.data?.message || "Failed to assign class. Please try again.";
            setErrorMessage(message);
        }
    };

    return {
        errorMessage,
        successMessage,
        handleAssignClass,
    };
};
