import { BrowserRouter, Outlet } from 'react-router-dom'
import NavPanel from './NavPanel'
import { Link } from 'react-router-dom'


const MainLayout = () => {
    return (
        <div className='flex flex-col h-screen'>
            <div className='relative z-0 bg-gray-900 text-white px-6 py-4 shadow-2xl'>
				<Link to="/" className='text-xl font-bold'>Admin Portal</Link>
			</div>
            <div className="flex-1 flex relative z-50">
                <NavPanel />
                <Outlet/>
            </div>
        </div>
    )
}

export default MainLayout
