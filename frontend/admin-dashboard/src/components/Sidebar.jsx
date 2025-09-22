import React from 'react';

const Sidebar = ({ navItems, activeTab, setActiveTab }) => {
  return (
    <div className="w-64 bg-gray-900 text-white">
      <div className="p-4 border-b border-gray-800">
        <h2 className="text-xl font-semibold">Admin Dashboard</h2>
        <p className="text-sm text-gray-400">Attendance Management</p>
      </div>
      
      <nav className="mt-6">
        {navItems.map((item) => {
          const Icon = item.icon;
          return (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`w-full flex items-center px-4 py-3 text-sm font-medium transition-colors ${
                activeTab === item.id
                  ? 'bg-blue-600 text-white border-r-2 border-blue-400'
                  : 'text-gray-300 hover:bg-gray-800 hover:text-white'
              }`}
            >
              <Icon className="h-5 w-5 mr-3" />
              {item.label}
            </button>
          );
        })}
      </nav>
    </div>
  );
};

export default Sidebar;