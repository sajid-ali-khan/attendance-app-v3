// Description: A component for handling file input and upload actions.
// ====================================================================================
export const FileUploader = () => (
    <div className="flex flex-col items-start">
        <label htmlFor="file-upload" className="cursor-pointer bg-white text-slate-700 font-semibold py-2 px-4 border border-slate-300 hover:bg-slate-50 transition-colors duration-200">
            Choose CSV File
        </label>
        <input id="file-upload" type="file" className="hidden" accept=".csv" />
        <p className="text-xs text-slate-500 mt-2">Only .csv files are accepted.</p>
        <button className="mt-4 bg-slate-800 text-white font-semibold py-2 px-6 hover:bg-slate-700 transition-colors duration-200">
          Upload
        </button>
    </div>
);