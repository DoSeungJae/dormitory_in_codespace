import { Avatar, Box, Grid, Typography } from '@mui/material'
import React from 'react'

function ChatBubble({ isSender, username, message="" }) {
  const avatar = "https://random.imagecdn.app/500/150";
  const date = new Date();
  const time = date.getHours() + ':' + date.getMinutes();
  return (
    <Box>
      <Grid 
        container 
        gap={2} 
        flexDirection={isSender ? "row-reverse" : "row"}
        sx={{
          width: '100%',
          display: 'flex',
          justifyContent: isSender? 'end' : "start"
        }} 
      >
        <Grid item>
          <Avatar src={avatar} />
        </Grid>
        <Grid item sx={{ textAlign: isSender ? 'right' : 'left' }}>
          <Box>
            <Typography fontSize={14}> {username} </Typography>
            <Box 
              sx={{ 
                marginBottom: '0.5rem',
                paddingRight: isSender ? '0.5rem' : '2rem',
                paddingLeft: isSender ? '2rem' : '0.5rem',
                paddingY: '0.25rem',
                color: isSender ? '#e6ecf0' : '#001e37',
                bgcolor: isSender ? '#001e37' : '#e6ecf0',
                borderRadius: '8px'
              }}>
              <Typography> {message} </Typography>
              <Typography fontSize={10}> {time} </Typography>
            </Box>
          </Box>
        </Grid>
      </Grid>
    </Box>
  )
}

export default ChatBubble;