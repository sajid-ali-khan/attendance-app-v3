import { BrowserRouter, Outlet } from 'react-router-dom'
import NavPanel from './NavPanel'
import { Link } from 'react-router-dom'


const MainLayout = () => {
    return (
        <div className='flex flex-col h-screen'>
            <div className='bg-white-600 px-6 py-4 text-blue-600 shadow-sm'>
				<Link to="/" className='text-2xl font-bold'>Admin Portal</Link>
			</div>
            <div className="flex-1 flex">
                <NavPanel />
                <Outlet/>
            </div>
        </div>
    )
}

export default MainLayout
