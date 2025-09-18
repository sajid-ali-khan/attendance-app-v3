import React from 'react'
import NavButton from '../components/NavButton'

const NavPanel = () => {
    return (
        <div className='shadow-2xl m-2 h-min flex flex-col'>
            <NavButton btnText={'Bulk Data Upload'} navLink={"/update-data"}/>
            <NavButton btnText={'Assign Class'} navLink={"/assign-class"}/>
            <NavButton btnText={'View Assignments'} navLink={"/view-assignments"}/>
            <NavButton   btnText={'Attendance Reports'} navLink={"/attendance-reports"}/>
        </div>
    )
}

export default NavPanel
