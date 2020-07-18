import React, { useState } from 'react';
import './App.css';

function App() {
  const [usernames, setUsernames] = useState([]);
  
  const fetchUsernames = () => {
    fetch(`/users`)
    .then(response => response.json())
    .then(json => {
      setUsernames(json);
    })
    .catch(reason =>  console.log("reason", reason));
  }

  return (
    <div className="App">
      <header className="App-header">
        <div className="usernames">
          <button onClick={fetchUsernames}>Fetch usernames</button>
          <h1>Usernames</h1>
          <ul>
            {
              usernames.map((username, index) => <li key={index}>{username}</li>)
            }
          </ul>
        </div>
      </header>
    </div>
  );
}

export default App;
