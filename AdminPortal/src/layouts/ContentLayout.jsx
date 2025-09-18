import React from 'react'
import { Outlet } from 'react-router-dom'
const ContentLayout = () => {
    return (
        <div className='flex-1 flex bg-white m-2 shadow-2xl'>
            <Outlet/>
        </div>
    )
}

export default ContentLayout
