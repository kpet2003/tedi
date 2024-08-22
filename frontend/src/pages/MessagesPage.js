import React from "react";
import NavigationBar from './NavigationBar.js';
import avatar from '../icons/avatar.png'
import '../styling/Messages.css';
import {Client} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useState,useEffect,useRef,useCallback } from "react";
import { useNavigate } from "react-router-dom";
import userService from "../service/userService";

function Chat(){
    const [privateChats, setPrivateChats] = useState(new Map());
    const [tab, setTab] = useState("");
    const [userData, setUserData] = useState({
        id: '',
        receivername: '',
        connected: false,
        message: '',
        first_name: '',
        last_name: '',
        email: '',
        image: '',
        connections: []
    });

    const stompClientRef = useRef(null); // UseRef to hold stompClient
    const navigate = useNavigate(); // Hook to navigate programmatically


    const onConnected = useCallback(() => {
        setUserData(prevState => ({ ...prevState, connected: true }));
        alert(userData.id);
        stompClientRef.current.subscribe(`/user/${userData.id}/chat`, onPrivateMessage,
            {
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}` // Include the token in the headers
        });
    },[userData.id]);

    const connect = useCallback(() => {
        const token = localStorage.getItem('jwt_token'); // Retrieve the JWT token from localStorage

        if (!token) {
            // navigate('/'); // Redirect to login if no token is found
            return;
        }

        // Create WebSocket connection
        const Sock = new SockJS('http://localhost:8080/ws');
        stompClientRef.current = new Client({
            webSocketFactory: () => Sock,
            reconnectDelay: 5000,
            connectHeaders: {
                Authorization: `Bearer ${localStorage.getItem('jwt_token')}` // Include the token in the headers
            },
            onConnect: onConnected,
            onStompError: onError
        });

        stompClientRef.current.activate();
    },[onConnected]);

    useEffect(() => {
        // Fetch user data when the component mounts
        const fetchUserData = async () => {
            try {
                const user = await userService.getUserChatData(localStorage.getItem('jwt_token'));
                setUserData(userData => ({
                    ...userData,
                    id: user.id,
                    firstName: user.first_name,
                    lastName: user.last_name,
                    email: user.email,
                    image: user.image,
                    connections: user.connections
                }));
                connect();
                console.log(user);
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };

        fetchUserData();
    }, [navigate, connect]);


    const onPrivateMessage = (payload) => {
        const payloadData = JSON.parse(payload.body);
        setPrivateChats(prev => {
            const newChats = new Map(prev);
            if (!newChats.has(payloadData.senderName)) {
                newChats.set(payloadData.senderName, []);
            }
            newChats.get(payloadData.senderName).push(payloadData);
            return newChats;
        });
    };

    const onError = (err) => {
        console.error("Connection error:", err);
        if (err.headers && err.headers['error'] === 'InvalidToken') {
            localStorage.removeItem('jwt_token'); // Remove invalid token
            // navigate('/'); // Redirect to login page
        }
    };

    const handleMessage = (event) => {
        const { value } = event.target;
        setUserData(prevState => ({ ...prevState, message: value }));
    };

    const sendPrivateValue = () => {
        if (stompClientRef.current && tab) {
            const chatMessage = {
                senderName: userData.id,
                receiverName: tab,
                message: userData.message,
                status: "MESSAGE"
            };
            setPrivateChats(prev => {
                const newChats = new Map(prev);
                newChats.get(tab).push(chatMessage);
                return newChats;
            });
            stompClientRef.current.publish({
                destination: "/app/Messages",
                body: JSON.stringify(chatMessage)
            });
            setUserData(prevState => ({ ...prevState, message: "" }));
        }
    };

    const handleUsername = (event) => {
        const { value } = event.target;
        setUserData(prevState => ({ ...prevState, username: value }));
    };


    const base64Image = userData.image? `data:image/jpeg;base64,${userData.image}`: `${avatar}`;

    return(
        <div>
            <NavigationBar></NavigationBar>
            <br></br>
            <div className="chatbox">
                <div className="users-chats">
                <ul className="unordered-list">
                    {userData.connections.map((connection) => (
                        <li key={connection.id} className="button-list">
                            <button className="user-button"><img src={connection.image? `data:image/jpeg;base64,${connection.image}`: `${avatar}`} alt="profile" className="button-image"></img> {connection.first_name} {connection.last_name}</button>
                        </li>
                    ))}
                </ul>
                    
                </div>
                <div className="right-side-box">
                <div className="texts-container">
                    {privateChats.get(tab)?.map((chatMessage, index) => (
                        <div key={index} className={chatMessage.senderName === userData.username ? "text-left" : "text-right"}>
                            {chatMessage.senderName !== userData.username && (
                                <img src={chatMessage.senderImage || avatar} alt="profile" className="chat-pfp-other" />
                            )}
                            <div className={chatMessage.senderName === userData.username ? "text-bubble-me" : "text-bubble-other"}>
                                <p>{chatMessage.message}</p>
                                <span className={chatMessage.senderName === userData.username ? "time-and-date-me" : "time-and-date-other"}>
                                    {new Date(chatMessage.date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                </span>
                            </div>
                            {chatMessage.senderName === userData.username && (
                                <img src={chatMessage.senderImage || avatar} alt="profile" className="chat-pfp-me" />
                            )}
                        </div>
                    ))}
                </div>

                    <br></br>
                    <div className="write-bar">
                        <textarea id="text" className="enter-text" value={userData.message} onChange={handleMessage}></textarea>
                        <input type="Submit" id="send-text" value={'Send'} className="send-button" onClick={sendPrivateValue}></input>
                    </div>
                </div> 
            </div>
        </div>
    );
}

export default Chat;