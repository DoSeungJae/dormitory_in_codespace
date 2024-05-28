import { Avatar, Box, Grid, Typography } from '@mui/material'
import React from 'react'

function ChatBubble({ isSender, username, message="",createdTime}) {
  const avatar = "https://random.imagecdn.app/500/150";
  const date = new Date(createdTime);
  //현재시간이 아니라 실제로 댓글이 작성된 시점을 파라미터로 가져와서 설정해야함

  const time = date.getHours() + ':' + date.getMinutes();
  return (
    <Box>
      <Grid 
        container 
        gap={1} 
        flexDirection={isSender ? "row-reverse" : "row"}
        sx={{
          width: '100%',
          display: 'flex',
          justifyContent: isSender? 'end' : "start"
        }} 
      >
        <Grid item>
          {!isSender && <Avatar src={avatar} />}
        </Grid>
        <Grid item sx={{ textAlign: isSender ? 'right' : 'left' }}>
          <Box>
            {!isSender && <Typography fontSize={14}> {username} </Typography>}
            <Box 
              sx={{ 
                marginBottom: '0.5rem',
                paddingRight: isSender ? '0.5rem' : '1rem',
                paddingLeft: isSender ? '1rem' : '0.5rem',
                paddingY: '0.15rem',
                color: isSender ? '#e6ecf0' : '#001e37',
                bgcolor: isSender ? '#001e37' : '#e6ecf0',
                borderRadius: '8px'
              }}>
              <Typography fontSize={18}> {message} </Typography>
              <Typography fontSize={11}> {time} </Typography>
            </Box>
          </Box>
        </Grid>
      </Grid>
    </Box>
  )
}

export default ChatBubble;