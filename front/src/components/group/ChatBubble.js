import React from 'react'
import './ChatBubble.css'

function ChatBubble({ isSender, messageType, username, message="", createdTime, showDate, showName, showTime}) {
  const date = new Date(createdTime);
  const yearMonthDate = date.getFullYear() + "." + (date.getMonth()+1) + "." + date.getDate();
  const padZero = function (n) {
    const s = String(n);
    return (s.length === 1) ? ("0"+s) : s;
  }
  const time = date.getHours() + ':' + padZero(date.getMinutes());
  const myChatClassName=(messageType==="SERVER" ? "server-chat" : (isSender ? "my-chat" : "other-chat"));

  return (
    <div className="chat-bubble-wrap">
      {showDate && <div className={["chat-new-day-line"].join(" ")}> {yearMonthDate} </div>}
      {showName && <div className={["chat-bubble-username", myChatClassName].join(" ")}> {username} </div>}
      <div className={["chat-bubble-message", myChatClassName].join(" ")}> {message} </div>
      {showTime && <div className={["chat-bubble-time", myChatClassName].join(" ")}> {time} </div>}
    </div>
  )
}

export default ChatBubble;