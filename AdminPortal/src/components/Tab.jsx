import React from 'react'
import { NavLink } from 'react-router-dom'

const Tab = ({ btnText, navLink }) => {
    return (
        <NavLink
            to={navLink}
            className={({ isActive }) =>
                `flex-1 text-center px-6 py-2 cursor-pointer font-bold 
            ${isActive ? "text-blue-600 border-b-4" : "bg-white"}`
            }>
            {btnText}
        </NavLink>
    )
}

export default Tab
