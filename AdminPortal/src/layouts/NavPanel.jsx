import React from 'react'
import Button from '../components/Button'

const NavPanel = () => {
    return (
        <div className='shadow-lg p-4 flex flex-col gap-2'>
            <Button btnText={'New Semester Setup'} navLink={"/update-data"}/>
            <Button btnText={'Assign Class'} navLink={"/assign-class"}/>
            <Button btnText={'View Assignments'} navLink={"/view-assignments"}/>
            <Button btnText={'Attendance Reports'} navLink={"/attendance-reports"}/>
        </div>
    )
}

export default NavPanel
