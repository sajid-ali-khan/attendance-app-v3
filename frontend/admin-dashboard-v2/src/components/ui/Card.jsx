// Description: A simple, reusable card component for wrapping content sections.
// ====================================================================================
export const Card = ({ children, className = '' }) => (
  <div className={`bg-white p-6 shadow-sm border border-slate-200 ${className}`}>
    {children}
  </div>
);