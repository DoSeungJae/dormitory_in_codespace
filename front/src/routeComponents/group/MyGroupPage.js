import { ThemeProvider } from '@mui/material';
import { useState } from 'react';
import theme from './theme';
import Login from '../../views/group/LogIn';
import ChatRoom from '../../views/group/ChatRoom';


function MyGroupPage() {
  const [username, setUsername] = useState("");
  const [room, setRoom] = useState("");
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  return (
    <ThemeProvider theme={theme}>
      {
        !isLoggedIn ? (
          <Login
            room={room}
            setRoom={setRoom}
            username={username}
            setUsername={setUsername}
            setIsLoggedin={setIsLoggedIn}
          />
        
        ) : (
          <ChatRoom 
            username={username}
            room={room}
          />
        )
      }
    </ThemeProvider>
  )
}

export default MyGroupPage;