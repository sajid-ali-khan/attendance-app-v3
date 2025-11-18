/**
 * Converts an array of objects to CSV format
 * @param {Array} data - Array of objects to convert
 * @param {Array} headers - Array of header objects with { key, label }
 * @returns {string} CSV formatted string
 */
export const convertToCSV = (data, headers) => {
    if (!data || data.length === 0) return '';

    // Create header row
    const headerRow = headers.map(h => h.label).join(',');
    
    // Create data rows
    const dataRows = data.map(row => {
        return headers.map(h => {
            const value = row[h.key];
            // Escape quotes and wrap in quotes if contains comma
            const stringValue = String(value || '');
            if (stringValue.includes(',') || stringValue.includes('"') || stringValue.includes('\n')) {
                return `"${stringValue.replace(/"/g, '""')}"`;
            }
            return stringValue;
        }).join(',');
    });

    return [headerRow, ...dataRows].join('\n');
};

/**
 * Triggers browser download of CSV file
 * @param {string} csvContent - CSV formatted string
 * @param {string} filename - Name of the file to download
 */
export const downloadCSV = (csvContent, filename) => {
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    
    if (link.download !== undefined) {
        // Create a link to the file
        const url = URL.createObjectURL(blob);
        link.setAttribute('href', url);
        link.setAttribute('download', filename);
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    }
};
