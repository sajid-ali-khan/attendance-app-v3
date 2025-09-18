import React from 'react'
import { Link } from 'react-router-dom'

const Button = ({btnText, navLink}) => {
    return (
        <Link
            to={navLink}
            className='border-2 text-center rounded-md px-3 py-2 shadow cursor-pointer bg-blue-600 text-white hover:bg-blue-700 active:bg-blue-800'>
            {btnText}
        </Link>
    )
}

export default Button
