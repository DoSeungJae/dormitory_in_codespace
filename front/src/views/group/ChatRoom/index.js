import { Box, Button, Container, Grid, TextField, Typography } from '@mui/material';
import React, { useEffect, useState } from 'react';

import { connect } from 'formik';
import ChatBubble from '../../../components/group/ChatBubble';
import { useSocket } from '../../../hooks/group/useSocket';
import { getSocketResponse } from '../../../service/group/socket';

function ChatRoom({ username, room }) {

  const { isConnected, socketResponse, sendData } = useSocket(room, username);
  const [messageInput, setMessageInput] = useState("");
  const [messageList, setMessageList] = useState([]);

  const addMessageToList = (val) => {
    if (val.room === "") return;
    setMessageList([...messageList]);
    fetchMessage();
  }

  const sendMessage = (e) => {
    e.preventDefault();
    if (messageInput !== "") {
      sendData({
        message: messageInput
      });
      addMessageToList({
        message: messageInput,
        username: username,
        createdAt: new Date(),
        messageType: "CLIENT"
      });
      setMessageInput("");
    }
  }

  const fetchMessage = () => {
    getSocketResponse(room)
            .then((res) => {
                setMessageList([...res]);
            }).catch((err) => {
                console.log(err);
            });
  }

  useEffect(() => {
    fetchMessage();
  }, []);
  //일정 간격으로 fetchMessage 함수를 반복적으로 실행해야함

  useEffect(() => {
    addMessageToList(socketResponse);
  }, [socketResponse]);

  return (
    
    <Container>
      <Grid
        container 
        gap={3}
        flexDirection={'column'} 
        alignItems={"center"} 
        justifyContent={"center"} 
        height={'97vh'}
      >
        <Grid item sx={{ width: '60%' }}>
          <Typography variant='h5'>
            Welcome to room <b>{room}</b>, {username}.
          </Typography>
        </Grid>
        <Grid item sx={{ width: '60%', bgcolor: '#ccd8e2', paddingX: '2rem', borderRadius: 6 }}>
          <Box 
            className="chat-box"
            sx={{ 
              width: '100%',
              paddingY: '2rem',
              borderRadius: 4,
              height: '60vh',
              overflow: 'auto'
            }}
          >
            {
              messageList.map((message) => {
                if (message.messageType === 'CLIENT') {
                  return (
                    <ChatBubble
                      key={message.id} 
                      isSender={message.username === username}
                      username={message.username}
                      message={message.message}
                      time={"12:12"}
                    />
                  )
                } 
              })
            }
          </Box>
          <Grid 
            container 
            alignItems={"center"}
            width={"100%"} 
            sx={{
              paddingY: '0.5rem',
              borderTop: '2px solid #99b1c5',
            }}
          >
            <Grid item xs={11}>
              <TextField 
                variant="standard"
                placeholder='Type your message'
                value={messageInput}
                onChange={(e) => setMessageInput(e.target.value)}
                fullWidth
                InputProps={{
                  disableUnderline: true,
                  sx: {
                    paddingX: '0.5rem'
                  }
                }}
              />
            </Grid>
            <Grid item xs={1}>
              <Button
                onClick={(e) => sendMessage(e)}
              >
                Send
              </Button>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </Container>
  )
}

export default ChatRoom;