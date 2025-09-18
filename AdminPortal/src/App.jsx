import { useState } from 'react'
import MainLayout from './layouts/MainLayout'
import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import UpdateData from './pages/UpdateData'
import AssignClass from './pages/AssignClass'
import ViewAssignments from './pages/ViewAssignments'
import AttendanceReports from './pages/AttendanceReports'
import ContentLayout from './layouts/ContentLayout'
import StudentsForm from './pages/StudentsForm'
import CoursesForm from './pages/CoursesForm'
import FacultiesForm from './pages/FacultiesForm'

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
								{path: "students", element: <StudentsForm />},
								{path: "courses", element: <CoursesForm />},
								{path: "faculties", element: <FacultiesForm />},
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
