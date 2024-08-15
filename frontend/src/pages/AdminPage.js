import '../Admin.css';
import React, { useEffect, useState } from 'react';
import AdminService from '../service/adminService.js'; 



function UserList() {
    const [users, setUsers] = useState([]);
    let [selectedUsers,setSelectedUsers] = useState([]);

    // renders the list of existing users, except for the admin 
    useEffect(() => {
        const fetchUsers = async() => {
            try {
                const response = await  AdminService.getUsers();
                const finalUsers = response.filter(user => user.admin !== true);
                setUsers(finalUsers);
            } 
            catch (error) {
                console.error("There was an error getting the user list", error);
            }
        };
        fetchUsers();
    }, []);

     
    const handleSelect = (userId) => {
        setSelectedUsers(prevSelectedUsers => 
            prevSelectedUsers.includes(userId) ? prevSelectedUsers.filter(id => id !== userId) : [...prevSelectedUsers, userId]
        );
    };

    // handle the select all checkbox
    const selectAll = ()=>{
        if(selectedUsers.length===users.length) {
            setSelectedUsers([]);
            return;
        }
        
        setSelectedUsers(users.map(user => user.id));
        
    }

    // export the user data in JSON form
    const exportJSON = () => {

        const excludeAttributes = ['profilePicture'];
            const finalUsers = users.filter(user => selectedUsers.includes(user.id)).map(user => {
            const { [excludeAttributes[0]]: _, ...rest } = user;
            return rest;
        });
            
        
            const jsonData = new Blob([JSON.stringify(finalUsers, null, 2)], { type: 'application/json' });
            const jsonURL = URL.createObjectURL(jsonData);
            const link = document.createElement('a');
            link.href = jsonURL;
            link.download = `users.json`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
    }

    function JSONtoXML(obj) {
        let xml = '';
        for (let prop in obj) {
          xml += obj[prop] instanceof Array ? '' : '<' + prop + '>';
          if (obj[prop] instanceof Array) {
            for (let array in obj[prop]) {
              xml += '\n<' + prop + '>\n';
              xml += JSONtoXML(new Object(obj[prop][array]));
              xml += '</' + prop + '>';
            }
          } else if (typeof obj[prop] == 'object') {
            xml += JSONtoXML(new Object(obj[prop]));
          } else {
            xml += obj[prop];
          }
          xml += obj[prop] instanceof Array ? '' : '</' + prop + '>\n';
        }
        xml = xml.replace(/<\/?[0-9]{1,}>/g, '');
        return xml;
      }

    // export the user data in XML form
    const exportXML = () => {
        const excludeAttributes = ['profilePicture'];
        
        const finalUsers = users.filter(user => selectedUsers.includes(user.id)).map(user => {
            const { [excludeAttributes[0]]: _, ...rest } = user;
            return rest;
        });

        const xml = JSONtoXML(finalUsers);


       
        const xmlString = `<?xml version="1.0" encoding="UTF-8"?>\n<users>\n${xml}\n</users>`;
        const blob = new Blob([xmlString], { type: "application/xml"});
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.download = "users.xml";
        link.href = url;
        link.click();
    }

 
    return (
        <div>
            <table className='userlist'> 
                <thead className='header'>
                    <tr>
                        <th className='select'> <input type='checkbox' name='selectall' onChange={selectAll} checked={selectedUsers.length===users.length}/>   </th>
                        <th > Name </th>
                        <th> E-mail </th>
                        <th> Profile Page</th>
                    </tr>
                </thead>
                
                <tbody>                    
                    {users.map(user => (
                        <tr key={user.id}>
                            <td  className='select' > <input type="checkbox" name={user.id}  checked={selectedUsers.includes(user.id)} onChange={() => {handleSelect(user.id)}}/>  </td>
                            <td>{user.firstName} {user.lastName}</td>
                            <td> {user.email}</td>
                            <td> <a href={`/VisitProfile/${user.id}`}> Visit Profile </a></td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <div className='buttons'>
                <input type='button' value={'Export in JSON '}className='button'onClick={exportJSON}/>
                <input type='button' value={'Export in XML '}className='button'onClick={exportXML}/>
            </div>
            
        </div>
        
    );

}



function AdminPage() {
    return(
        <div>
            <UserList/>
        </div>
    )
    
}

export default AdminPage;
