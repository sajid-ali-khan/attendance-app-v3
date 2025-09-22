import React from 'react';

const SchemaTable = ({ schema }) => (
  <div className="bg-white border border-gray-300">
    <div className="px-4 py-3 bg-gray-50 border-b border-gray-300">
      <h3 className="text-sm font-medium text-gray-900">Required CSV Schema</h3>
    </div>
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-300">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Field Name</th>
            <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Data Type</th>
            <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Required</th>
            <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Example</th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-300">
          {schema.map((item, index) => (
            <tr key={index} className="hover:bg-gray-50">
              <td className="px-4 py-2 text-sm font-medium text-gray-900">{item.field}</td>
              <td className="px-4 py-2 text-sm text-gray-600">{item.type}</td>
              <td className="px-4 py-2 text-sm">
                <span className={`px-2 py-1 text-xs ${item.required === 'Yes' ? 'bg-red-100 text-red-800' : 'bg-gray-100 text-gray-800'}`}>
                  {item.required}
                </span>
              </td>
              <td className="px-4 py-2 text-sm text-gray-600">{item.example}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);

export default SchemaTable;