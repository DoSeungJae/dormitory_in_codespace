import React, { useEffect, useState, useRef, useContext } from 'react';

import ChatBubble from '../../../components/group/ChatBubble';
import { getSocketResponse } from '../../../service/group/socket';
import TextareaAutosize from 'react-textarea-autosize';
import ProfileImageContext from '../../../components/common/ProfileImageContext';
import userDefault from '../../../images/userDefault.png';
import { getProfileImages } from '../../../modules/common/profileImageModule';

function ChatRoom({ username, room, socketResponse, sendData }) {

  const [messageInput, setMessageInput] = useState("");
  const [messageList, setMessageList] = useState([]);
  const [shouldScroll, setShouldScroll] = useState(false);
  const messagesRef = useRef(null);
  const {profileImages, setProfileImages} = useContext(ProfileImageContext);

  const profileImageByNickname = (nickname) => {
    getProfileImages("NICKNAME", nickname, profileImages, setProfileImages);
    return profileImages["NICKNAME"][nickname] || userDefault;
  };

  const dateEqual = function (time1, time2) {
    const date1 = new Date(time1); 
    const date2 = new Date(time2);
    return (date1.getFullYear() === date2.getFullYear() 
      && date1.getMonth() === date2.getMonth() 
      && date1.getDate() === date2.getDate());
  };

  const minuteEqual = function (time1, time2) {
    const date1 = new Date(time1);
    const date2 = new Date(time2);
    return (date1.getFullYear() === date2.getFullYear()
      && date1.getMonth() === date2.getMonth()
      && date1.getDate() === date2.getDate()
      && date1.getHours() === date2.getHours()
      && date1.getMinutes() === date2.getMinutes())
  };

  const sendMessage = (e) => {
    e.preventDefault();
    if (messageInput !== "") {
      sendData({
        message: messageInput,
        createdAt: new Date()
      });
      setMessageInput("");
    }
  }

  const handleMessages = (initialMessages) => {
    const messages = initialMessages.filter((message) => {
      return (message.messageType === "CLIENT") || (message.message.startsWith("participatedInGroup:"))
    });
    return messages.map((currentMessage, index) => {
      const isServerMessage = currentMessage.messageType === "SERVER";
      const isSender = currentMessage.username === username;
      const previousMessage = messages[index-1];
      const nextMessage = messages[index+1];
      const dateEqualWithPrevious = (previousMessage !== undefined) && dateEqual(currentMessage.createdTime, previousMessage.createdTime);
      const usernameEqualWithPrevious = (previousMessage !== undefined) && (currentMessage.username === previousMessage.username);
      const minuteEqualWithPrevious = (previousMessage !== undefined) && minuteEqual(currentMessage.createdTime, previousMessage.createdTime);
      const minuteEqualWithNext = (nextMessage !== undefined) && minuteEqual(currentMessage.createdTime, nextMessage.createdTime);
      const usernameEqualWithNext = (nextMessage !== undefined) && (currentMessage.username === nextMessage.username);
      return {
        ...currentMessage,
        message: (isServerMessage)?(currentMessage.message.slice(20)+" 님이 참여하였습니다."):(currentMessage.message),
        isSender: isSender,
        showDate: !dateEqualWithPrevious,
        showName: !(isServerMessage || isSender || (usernameEqualWithPrevious && minuteEqualWithPrevious)),
        showTime: !(isServerMessage || (usernameEqualWithNext && minuteEqualWithNext)),
        profileImage: profileImageByNickname(currentMessage.username),
      }
    })
  };

  // update messageList
  useEffect(() => {
    getSocketResponse(room)
    .then((res) => {
      const previousMessageCount = messageList.length;
      const { scrollTop, scrollHeight, clientHeight } = messagesRef.current;
      const previouslyScrolled = scrollTop + clientHeight >= scrollHeight * (1-1/previousMessageCount);
      const newMessageList = handleMessages(res);
      setMessageList(newMessageList);
      const existNewMessage = newMessageList.length > previousMessageCount;
      if (previouslyScrolled && existNewMessage) { setShouldScroll(true); };
    }).catch((err) => {
        console.error(err);
    });
  });

  useEffect(() => {
    if (shouldScroll) {
      messagesRef.current.scrollTop = messagesRef.current.scrollHeight;
      setShouldScroll(false);
    }
  }, [shouldScroll])


  return (
    <div className='App'>
      <div className='group-messages' id='group-messages' ref={messagesRef}>
        {
          messageList.map(({id, isSender, username, messageType, message, createdTime, showDate, showName, showTime, profileImage}) => {
            return <ChatBubble
              key={id} 
              isSender={isSender}
              messageType={messageType}
              username={username}
              message={message}
              createdTime={createdTime}
              showDate={showDate}
              showName={showName}
              showTime={showTime}
              profileImage={profileImage}
            />
          })
        }
      </div>
      <div className='group-form' id='group-form'>
        <TextareaAutosize
          type='text'
          minRows={1}
          maxRows={5}
          className="form-control group-form-input"
          id = "group-form-input"
          placeholder='메세지를 입력하세요.'
          value={messageInput}
          onChange={(e) => setMessageInput(e.target.value)}
        />
        <div className="btn btn-primary" onClick={(e) => sendMessage(e)}> 전송 </div>
      </div>
    </div>
      

  )
}

export default ChatRoom;