import axiosClient from '../../api/axiosClient';
import React, { useState } from 'react';

// ====================================================================================
// File: src/components/ui/FileUploader.jsx
// Description: A component for handling file input and upload actions.
// ====================================================================================
export const FileUploader = ({ api }) => {
    const [file, setFile] = useState(null);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            setFile(selectedFile);
            setError('');
            setSuccess('');
        }
    };

    const handleUpload = () => {
        if (!file) {
            setError('Please select a file before uploading.');
            return;
        }

        setError('');
        setSuccess('');
        setIsLoading(true);

        const formData = new FormData();
        formData.append('file', file);

        axiosClient.post(api, formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }).then(response => {
            setSuccess(`File '${file.name}' uploaded successfully!`);
            setFile(null); // Reset file input after successful upload
            console.log('File uploaded successfully:', response.data);
        }).catch(error => {
            console.error('Error uploading file:', error);
            const errorMessage = error.response?.data?.message || 'Failed to upload file. Please check the console for details.';
            setError(errorMessage);
        }).finally(() => {
            setIsLoading(false);
        });
    };

    return (
        <div className="flex flex-col items-start w-full">
            {/* Notification Messages */}
            {error && (
                <div className="mb-4 text-sm text-red-700 bg-red-100 border border-red-300 px-4 py-3 w-full" role="alert">
                    <p className="font-bold">Error:</p>
                    <p>{error}</p>
                </div>
            )}
            {success && (
                <div className="mb-4 text-sm text-green-700 bg-green-100 border border-green-300 px-4 py-3 w-full" role="alert">
                    <p className="font-bold">Success:</p>
                    <p>{success}</p>
                </div>
            )}

            {/* File Input */}
            <label htmlFor="file-upload" className="cursor-pointer bg-white text-slate-700 font-semibold py-2 px-4 border border-slate-300 hover:bg-slate-50 transition-colors duration-200">
                Choose CSV File
            </label>
            <input
                id="file-upload"
                type="file"
                className="hidden"
                accept=".csv"
                onChange={handleFileChange}
                // Add a key to reset the input field when the file state is cleared
                key={file ? file.name : 'empty'}
            />

            {/* File Info and Upload Button */}
            <div className="mt-2 w-full">
                 {file && (
                    <p className="text-sm text-slate-600 mb-2">
                        Selected file: <strong>{file.name}</strong>
                    </p>
                )}
                <p className="text-xs text-slate-500">Only .csv files are accepted.</p>
                <button
                    onClick={handleUpload}
                    className="mt-4 bg-slate-800 text-white cursor-pointer font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 disabled:bg-slate-400 disabled:cursor-not-allowed flex items-center justify-center min-w-[100px]"
                    disabled={!file || isLoading}
                >
                    {isLoading ? (
                        <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                    ) : 'Upload'}
                </button>
            </div>
        </div>
    );
};

