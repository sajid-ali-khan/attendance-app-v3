// Description: Renders a table to display the required schema for CSV uploads.
// ====================================================================================
export const SchemaTable = ({ schema }) => (
  <div className="mb-6">
    <h3 className="font-semibold text-slate-700 mb-2">{schema.title}</h3>
    <div className="overflow-x-auto border border-slate-200">
      <table className="min-w-full text-sm text-left">
        <thead className="bg-slate-50 text-slate-600">
          <tr>
            <th className="p-3 font-medium">Field Name</th>
            <th className="p-3 font-medium">Data Type</th>
            <th className="p-3 font-medium">Constraints</th>
            <th className="p-3 font-medium">Example</th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-slate-200">
          {schema.fields.map(field => (
            <tr key={field.fieldName}>
              <td className="p-3 font-mono text-slate-800">{field.fieldName}</td>
              <td className="p-3 text-slate-500">{field.dataType}</td>
              <td className="p-3 text-slate-500">
                <div className="flex flex-col space-y-1">
                  {field.required && <span className="inline-block bg-slate-200 text-slate-700 text-xs font-semibold px-2 py-0.5">NOT NULL</span>}
                  {field.unique && <span className="inline-block bg-slate-200 text-slate-700 text-xs font-semibold px-2 py-0.5">UNIQUE</span>}
                </div>
              </td>
              <td className="p-3 text-slate-500 font-mono">{field.example}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);