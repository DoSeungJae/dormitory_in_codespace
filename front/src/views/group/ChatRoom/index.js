import React, { useEffect, useState } from 'react';

import ChatBubble from '../../../components/group/ChatBubble';
import { getSocketResponse } from '../../../service/group/socket';
import InputForm from '../../../components/common/InputForm';
import Button from '../../../components/common/Button';

function ChatRoom({ username, room, socketResponse, sendData }) {


  const [messageInput, setMessageInput] = useState("");
  const [messageList, setMessageList] = useState([]);

  const sendMessage = (e) => {
    e.preventDefault();
    if (messageInput !== "") {
      sendData({
        message: messageInput,
        createdAt: new Date()
      });
      fetchMessage();
      setMessageInput("");
    }
  }

  const fetchMessage = () => {
    getSocketResponse(room)
      .then((res) => {
          setMessageList(res.filter((message) => {return message.messageType === 'CLIENT'}));
      }).catch((err) => {
          console.error(err);
      });
  }

  useEffect(() => {
    fetchMessage();
  });



  return (
    <div className='App'>
      <div className='group-messages'>
        {
          messageList
            .map((message, index) => {
            return {
              previousChatTime : (messageList[index-1]===undefined)?undefined:(messageList[index-1].createdTime),
              nextChatTime : (messageList[index+1]===undefined)?undefined:(messageList[index+1].createdTime),
              nextUsername : (messageList[index+1]===undefined)?undefined:(messageList[index+1].username),
              ...message
            }
            }).map((message) => {
              return (
                <ChatBubble
                  key={message.id} 
                  isSender={message.username === username}
                  username={message.username}
                  message={message.message}
                  createdTime={message.createdTime}
                  previousChatTime={message.previousChatTime}
                  nextChatTime={message.nextChatTime}
                  nextUsername={message.nextUsername}
                />
              )
            })
        }
      </div>
      <div className='group-form'>
        <InputForm
          placeholder='메세지를 입력하세요.'
          type="standard"
          value={messageInput}
          onChange={(e) => setMessageInput(e.target.value)}
        />
        <Button onClick={(e) => sendMessage(e)}>전송</Button>
      </div>
    </div>
      

  )
}

export default ChatRoom;