import React from 'react';

/**
 * A component to display a CSV schema definition in a structured table.
 * @param {{
 * schema: {
 * fieldName: string;
 * unique: boolean;
 * notNull: boolean;
 * }[];
 * }} props
 */
function SchemaTable({ schema }) {
  // Helper component for the boolean badges to keep the main table clean
  const BooleanBadge = ({ value }) => {
    const baseClasses = 'px-2.5 py-1 text-xs font-semibold rounded-full';
    const trueClasses = 'bg-green-100 text-green-800';
    const falseClasses = 'bg-gray-100 text-gray-700';

    return (
      <span className={`${baseClasses} ${value ? trueClasses : falseClasses}`}>
        {value ? 'True' : 'False'}
      </span>
    );
  };

  return (
    <div className="w-full overflow-hidden rounded-lg border border-gray-200 bg-white shadow-sm">
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-left">
            <tr>
              <th className="w-16 p-3 font-semibold tracking-wide text-gray-600">S.No</th>
              <th className="p-3 font-semibold tracking-wide text-gray-600">Field Name</th>
              <th className="p-3 font-semibold tracking-wide text-gray-600">Unique?</th>
              <th className="p-3 font-semibold tracking-wide text-gray-600">Not Null?</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {schema.map((field, index) => (
              <tr key={field.fieldName} className="bg-white even:bg-gray-50">
                <td className="p-3 text-gray-500">{index + 1}</td>
                <td className="p-3 text-gray-800">
                  <code className="rounded bg-gray-100 px-2 py-1 font-mono text-sm">
                    {field.fieldName}
                  </code>
                </td>
                <td className="p-3">
                  <BooleanBadge value={field.unique} />
                </td>
                <td className="p-3">
                  <BooleanBadge value={field.notNull} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default SchemaTable;