import { useState } from 'react'
import MainLayout from './layouts/MainLayout'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import UpdateData from './pages/UpdateData'
import AssignClass from './pages/AssignClass'
import ViewAssignments from './pages/ViewAssignments'
import AttendanceReports from './pages/AttendanceReports'
import ContentLayout from './layouts/ContentLayout'
import { Link } from 'react-router-dom'

function App() {

	const router = createBrowserRouter([
		{
			path: "/",
			element: <MainLayout />,
			children: [
				{
					element: <ContentLayout />,
					children: [
						{ index: true, element: <UpdateData /> },
						{ path: "/update-data", element: <UpdateData /> },
						{ path: "/assign-class", element: <AssignClass /> },
						{ path: "/view-assignments", element: <ViewAssignments /> },
						{ path: "/attendance-reports", element: <AttendanceReports /> }
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
