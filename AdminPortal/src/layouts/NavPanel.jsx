import React from 'react'
import Button from '../components/Button'

const NavPanel = () => {
    return (
        <div className='shadow-2xl m-2 h-min flex flex-col'>
            <Button btnText={'New Semester Setup'} navLink={"/update-data"}/>
            <Button btnText={'Assign Class'} navLink={"/assign-class"}/>
            <Button btnText={'View Assignments'} navLink={"/view-assignments"}/>
            <Button btnText={'Attendance Reports'} navLink={"/attendance-reports"}/>
        </div>
    )
}

export default NavPanel
