import React, { useEffect, useState } from 'react';
import axiosClient from '../api/axiosClient';
import { useAuth } from '../provider/AuthProvider';
import { useNavigate } from 'react-router-dom';

export const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const { token, setToken } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (token) {
            navigate("/", { replace: true });
        }
    }, [token, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const response = await axiosClient.post('/auth/login', {
                username,
                password
            });
            setToken(response.data.token);
            // Redirect to the dashboard or another page after successful login

            navigate("/");
        } catch (err) {
            console.error('Login failed:', err);
            setError((err.response && (err.response.status === 401 || err.response.status === 403))
                ? 'Invalid username or password.'
                : 'An error occurred. Please try again.'
            );
            setIsLoading(false);
        }
        // No need to setIsLoading(false) on success because the page will redirect
    };

    return (
        <div className="bg-slate-100 flex items-center justify-center min-h-screen">
            <div className="w-full max-w-md p-8 space-y-6 bg-white shadow-md border border-slate-200">
                <h2 className="text-2xl font-bold text-center text-slate-900">
                    Admin Portal Login
                </h2>

                <form onSubmit={handleSubmit} className="space-y-6">
                    {error && (
                        <div className="text-sm text-red-700 bg-red-100 border border-red-300 px-4 py-3" role="alert">
                            {error}
                        </div>
                    )}

                    <div>
                        <label htmlFor="username" className="block text-sm font-medium text-slate-700">
                            Username / Email
                        </label>
                        <div className="mt-1">
                            <input id="username" name="username" type="text" required
                                className="w-full p-3 border border-slate-300 bg-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-slate-500"
                                placeholder="admin@example.com"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                disabled={isLoading} />
                        </div>
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-slate-700">
                            Password
                        </label>
                        <div className="mt-1">
                            <input id="password" name="password" type="password" required
                                className="w-full p-3 border border-slate-300 bg-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-slate-500"
                                placeholder="••••••••"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                disabled={isLoading} />
                        </div>
                    </div>

                    <div>
                        <button type="submit"
                            id="login-button"
                            className="w-full flex justify-center py-3 px-4 border border-transparent font-semibold text-white bg-slate-800 hover:bg-slate-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-500 transition-colors duration-200 disabled:bg-slate-400"
                            disabled={isLoading}>
                            <span id="button-text" className={isLoading ? 'hidden' : ''}>Sign in</span>
                            {isLoading && (
                                <svg id="button-loader" className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

// If this is in its own file (e.g., src/views/LoginPage.jsx), you might want a default export:
export default LoginPage;