import React, { useState } from 'react';
import { FaFileUpload } from "react-icons/fa";

function FileUploader() {
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileChange = (event) => {
    // Get the first file from the selection
    const file = event.target.files[0];
    if (file && file.type === 'text/csv') {
      setSelectedFile(file);
    } else {
      alert('Please select a valid .csv file.');
      setSelectedFile(null);
    }
  };

  const handleSubmit = () => {
    if (!selectedFile) {
      alert('Please select a file first!');
      return;
    }
    // This is where we'll call the API function later
    console.log('Uploading file:', selectedFile.name);
    //
    // Call your API upload function here, e.g., uploadStudentData(selectedFile);
    //
  };

  return (
    <div className="mt-6 rounded-lg border border-gray-200 bg-white p-6 shadow-sm">
      <h3 className="text-lg font-semibold text-gray-800">Upload Your File</h3>
      <p className="mt-1 text-sm text-gray-500">
        The file will be validated before any data is saved.
      </p>

      {/* File Input Area */}
      <div className="mt-4 flex items-center justify-center rounded-md border-2 border-dashed border-gray-300 p-8">
        <div className="text-center">
          <FaFileUpload className='text-gray-400 mx-auto text-2xl m-2'/>
          <label htmlFor="file-upload" className="relative cursor-pointer rounded-md font-medium text-blue-600 hover:text-blue-500">
            <span>Select a file</span>
            <input id="file-upload" name="file-upload" type="file" className="sr-only" accept=".csv" onChange={handleFileChange} />
          </label>
          <p className="pl-1 text-sm text-gray-500">or drag and drop</p>
        </div>
      </div>
      
      {/* Selected File Display */}
      {selectedFile && (
        <div className="mt-4 text-sm font-medium text-gray-600">
          Selected file: <span className="font-semibold text-gray-800">{selectedFile.name}</span>
        </div>
      )}

      {/* Submit Button */}
      <div className="mt-6 text-right">
        <button
          onClick={handleSubmit}
          disabled={!selectedFile}
          className="rounded-md bg-blue-600 px-5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-gray-300"
        >
          Validate and Upload
        </button>
      </div>
    </div>
  );
}

export default FileUploader;