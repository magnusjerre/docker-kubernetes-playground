import React, { useState } from 'react';
import './App.css';

function App() {
  const [persons, setPersons] = useState([]);
  
  const fetchPersons = () => {
    fetch(`/persons`)
    .then(response => response.json())
    .then(personsJsonArray => {
      setPersons(personsJsonArray);
    })
    .catch(reason =>  console.log("reason", reason));
  }

  return (
    <div className="App">
      <header className="App-header">
        <div className="usernames">
          <button onClick={fetchPersons}>Fetch persons</button>
          <h1>Persons</h1>
          <ul>
            {
              persons.map((person, index) => <li key={index}>{person}</li>)
            }
          </ul>
        </div>
      </header>
    </div>
  );
}

export default App;
