import { useState } from 'react'
import MainLayout from './layouts/MainLayout'
import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import UpdateData from './pages/UpdateData'
import AssignClass from './pages/AssignClass'
import ViewAssignments from './pages/ViewAssignments'
import AttendanceReports from './pages/AttendanceReports'
import ContentLayout from './layouts/ContentLayout'
import StudentsUploadTab from './pages/update_tabs/StudentsUploadTab'
import CoursesUploadTab from './pages/update_tabs/CoursesUploadTab'
import FacultiesUploadTab from './pages/update_tabs/FacultiesUploadTab'

function App() {

	const router = createBrowserRouter([
		{
			path: "",
			element: <MainLayout />,
			children: [
				{
					element: <ContentLayout />,
					children: [
						{index: true, element: <Navigate to="update-data" />},
						{
							path: "update-data",
							element: <UpdateData />,
							children: [
								{index: true, element: <Navigate to="students" />},
								{path: "students", element: <StudentsUploadTab />},
								{path: "courses", element: <CoursesUploadTab />},
								{path: "faculties", element: <FacultiesUploadTab />},
							]
						},
						{ path: "assign-class", element: <AssignClass /> },
						{ path: "view-assignments", element: <ViewAssignments /> },
						{ path: "attendance-reports", element: <AttendanceReports /> }
					]
				},
			]
		}
	])

	return (
		<div className='flex flex-col'>
			<RouterProvider router={router} />
		</div>
	)
}

export default App
