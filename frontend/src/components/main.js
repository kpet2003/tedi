import React from 'react';
import {Routes, Route,useLocation,useNavigate} from 'react-router-dom';
import { useState,useEffect } from 'react';

import ProtectedRoute from './ProtectedRoute';
import WelcomePage from "../pages/WelcomePage"
import SignUpPage from "../pages/SignUpPage"
import HomePage from "../pages/HomePage"
import AdminPage from '../pages/AdminPage';
import ProfilePage from '../pages/ProfilePage';
import SettingsPage from '../pages/SettingsPage';
import NewEmail from '../pages/NewEmail';
import NewPassword from '../pages/NewPassword';
import Notifications from '../pages/NotificationPage';
import Network from '../pages/Network';
import VisitProfile from '../pages/VisitProfilePage';
import ViewNetwork from '../pages/ViewNetwork';
import Chat from '../pages/MessagesPage';
import axios from 'axios';
import JobPage from '../pages/JobsPage';
import userService from '../service/userService';

const SERVER_URL = "https://localhost:8080";

const authenticate = (token) => {
    const API_URL = SERVER_URL + "/auth/";
    return axios.get(API_URL,{
        params: {token : token},
        responseType: 'json'
    }).then(response => response.data);
}

const Main=()=>{
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    const navigate = useNavigate();
    const location = useLocation();

    const checkAuthentication = async () => {
        const token = localStorage.getItem('jwt_token');
        if (token !== null) {

            try {
                const response = await authenticate(token);
                setIsAuthenticated(true);
            }
            catch(error) {
                console.error('Token validation failed:', error.response);
                setIsAuthenticated(false);
                alert('token validation failed')
                navigate('/');
            }
            
        } 
        else {
            console.log('User not authenticated');
            alert('User not authenticated')
            setIsAuthenticated(false);
            navigate('/');
        }
        setIsLoading(false); // Loading complete
    };

    useEffect(() => {
        if (location.pathname !== '/SignUp' && location.pathname !== '/') {
            checkAuthentication();
            const token = userService.decodeToken(localStorage.getItem('jwt_token'));
            if(location.pathname === '/HomePage' && token.sub === 'admin@gmail.com') {
                navigate(-1);
            }
        }
        else {
            setIsLoading(false);
        }

    },  [location.pathname]);

    const handleLoginSuccess = (isAdmin) => {
        setIsAuthenticated(true);
        if(isAdmin) {
            navigate('/AdminPage')
        }
        else{
            navigate('/HomePage');
        }
    };

    if (isLoading) {
        return <div>Loading...</div>; // Show a loading state until auth status is determined
    }

    return(
        <Routes>
            <Route exact path='/' element={<WelcomePage onLoginSuccess={handleLoginSuccess} />} ></Route>
            <Route exact path='/SignUp' Component={SignUpPage}></Route>
            <Route element={<ProtectedRoute isAuthenticated={isAuthenticated} />}>
                <Route exact path='/AdminPage' Component={AdminPage}></Route>
                <Route exact path='/HomePage' Component={HomePage}></Route>
                <Route exact path='/Network' Component={Network}></Route>
                <Route exact path='/Notifications' Component={Notifications}></Route>
                <Route exact path='/Profile' Component={ProfilePage}></Route>
                <Route exact path='/Settings' Component={SettingsPage}></Route>
                <Route exact path='/NewEmail' Component={NewEmail}></Route>
                <Route exact path='/NewPassword' Component={NewPassword}></Route>
                <Route exact path='/VisitProfile/:id' Component={VisitProfile}></Route>
                <Route exact path='/ViewNetwork/:id' Component={ViewNetwork}></Route>
                <Route exact path='/Messages' Component={Chat}></Route>
                <Route exact path='/Jobs' Component={JobPage}></Route>
            </Route>
           
             
        </Routes>
    );
}

export default Main;