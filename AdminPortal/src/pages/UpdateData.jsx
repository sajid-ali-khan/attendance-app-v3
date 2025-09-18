import { Outlet } from 'react-router-dom'
import Tab from '../components/Tab'

const UpdateData = () => {
	return (
		<div className='flex-1 flex flex-col'>
			<div className='flex'>
				<Tab btnText='Courses' navLink="courses" />
				<Tab btnText='Students' navLink="students" />
				<Tab btnText='Faculties' navLink="faculties" />
			</div>
			<Outlet/>
		</div>
	)
}

export default UpdateData
