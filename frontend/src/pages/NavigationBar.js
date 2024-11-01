import home from '../icons/home.png';
import network from '../icons/network.png';
import jobs from '../icons/jobs.png';
import message from '../icons/text-bubble.png';
import bell from '../icons/bell.png';
import settings from '../icons/settings.png'
import profile from '../icons/profile.png';
import {useNavigate} from "react-router-dom";
import "../styling/NavigationBar.css"

function NavigationBar() {
    const navigate = useNavigate();

    const handleClick = async(event) => {
        event.preventDefault();
        

        const icon = event.target.id;
        if(icon === 'home'){
            navigate('/HomePage');
        }
        else if(icon === 'net'){
            navigate('/Network');
        }
        else if(icon === 'job'){
            navigate('/Jobs');
        }
        else if(icon === 'message'){
            navigate('/Messages');
        }
        else if(icon === 'notifs'){
            navigate('/Notifications');
        }
        else if(icon === 'profile'){
            navigate('/Profile');
        }
        else if(icon === 'settings'){
            navigate('/Settings')
        }
    }

    return(
        <div >
            <nav className='navigation'>
                <div className='link-container'>
                    <img src={home} alt='' className='home-icon' id='home' onClick={handleClick}></img>
                    <a href='/HomePage' className='link'>Home</a> 
                </div>
                <div className='link-container'>
                    <img src={network} alt='' className='net-icon' id='net' onClick={handleClick}></img>
                    <a href='/Network' className='link'>Network</a> 
                </div>
                <div className='link-container'>
                    <img src={jobs} alt='' className='icons' id='job' onClick={handleClick}></img>
                    <a href='/Jobs' className='link'>Jobs</a> 
                </div>
                <div className='link-container'>
                    <img src={message} alt='' className='text-icon' id='message' onClick={handleClick}></img>
                    <a href='/Messages' className='link'>Messages</a> 
                </div>
                <div className='link-container'>
                    <img src={bell} alt='' className='icons' id='notifs' onClick={handleClick}></img>
                    <a href='/Notifications' className='link'>Notifications</a> 
                </div>
                <div className='link-container'>
                    <img src={profile} alt='' className='profile-icon' id='profile' onClick={handleClick}></img>
                    <a href='/Profile' className='link'>Profile</a> 
                </div>
                <div className='link-container'>
                    <img src={settings} alt='' className='icons' id='settings' onClick={handleClick}></img>
                    <a href='/Settings' className='link'>Settings</a> 
                </div>
            </nav>
        </div>
        
    );
}

export default NavigationBar;