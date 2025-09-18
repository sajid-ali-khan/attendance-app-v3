import React from 'react'
import { NavLink } from 'react-router-dom'

const NavButton = ({btnText, navLink}) => {
    return (
        <NavLink
            to={navLink}
            className={({ isActive }) =>
                `text-center px-6 py-2 cursor-pointer font-bold
     ${isActive ? "bg-linear-65 from-blue-700 to-blue-400 text-white" : "bg-white hover:bg-gray-200"}`
            }
            >
            {btnText}
        </NavLink>
    )
}

export default NavButton
