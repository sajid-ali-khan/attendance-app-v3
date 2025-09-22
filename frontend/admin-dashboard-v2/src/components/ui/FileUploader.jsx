import React, { useState } from 'react';

// ====================================================================================
// File: src/components/ui/FileUploader.jsx
// Description: A component for handling file input and upload actions.
// ====================================================================================
export const FileUploader = ({ api }) => {
    const [file, setFile] = useState(null);
    const [error, setError] = useState('');

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            setFile(selectedFile);
            setError(''); // Clear any previous error on new file selection
        }
    };

    const handleUpload = () => {
        if (!file) {
            setError('Please select a file before uploading.');
            return;
        }
        // The 'file' object in state is ready to be sent to an API.
        console.log('Uploading file:', file);
        console.log(api)
        // Example API call structure:
        // api.upload(file)
        //   .then(response => console.log('Upload successful:', response))
        //   .catch(err => setError('Upload failed: ' + err.message));
        setError(''); // Clear error on successful start
    };

    return (
        <div className="flex flex-col items-start">
            {/* Error Message Display */}
            {error && (
                <div className="mb-4 text-sm text-red-600 bg-red-100 border border-red-200 px-4 py-2 w-full">
                    {error}
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
                    className="mt-4 bg-slate-800 text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200 disabled:bg-slate-400"
                    disabled={!file}
                >
                    Upload
                </button>
            </div>
        </div>
    );
};
