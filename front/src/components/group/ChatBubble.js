import React from 'react'
import './ChatBubble.css'

function ChatBubble({ isSender, username, message="", createdTime, previousChatTime, nextChatTime, nextUsername}) {
  const date = new Date(createdTime);
  const yearMonthDate = date.getFullYear() + "." + (date.getMonth()+1) + "." + date.getDate();
  const padZero = function (n) {
    const s = String(n);
    return (s.length === 1) ? ("0"+s) : s;
  }
  const time = date.getHours() + ':' + padZero(date.getMinutes());
  const myChatClassName=(isSender ? "my-chat" : "other-chat");

  const dateEqual = function (time1, time2) {
    if (time1 === undefined || time2 === undefined) {return false};
    const date1 = new Date(time1); 
    const date2 = new Date(time2);
    return !(date1.getFullYear() === date2.getFullYear() 
      && date1.getMonth() === date2.getMonth() 
      && date.getDate() === date2.getDate());
  };

  const minuteEqual = function (time1, time2) {
    if (time1 === undefined || time2 === undefined) {return false};
    const date1 = new Date(time1);
    const date2 = new Date(time2);
    return (date1.getFullYear() === date2.getFullYear()
      && date1.getMonth() === date2.getMonth()
      && date1.getDate() === date2.getDate()
      && date1.getHours() === date2.getHours()
      && date1.getMinutes() === date2.getMinutes())
  };

  const dayHidden = !dateEqual(createdTime, previousChatTime);
  const usernameHidden = isSender;
  const timeHidden = minuteEqual(createdTime, nextChatTime) && (username === nextUsername);
  const hide = ((hidden) => {return (hidden)?"hidden":""});

  return (
    <div className="chat-bubble-wrap">
      <div className={["chat-new-day-line", hide(dayHidden)].join(" ")}> {yearMonthDate} </div>
      <div className={["chat-bubble-username", myChatClassName, hide(usernameHidden)].join(" ")}> {username} </div>
      <div className={["chat-bubble-message", myChatClassName].join(" ")}> {message} </div>
      <div className={["chat-bubble-time", myChatClassName, hide(timeHidden)].join(" ")}> {time} </div>
    </div>
  )
}

export default ChatBubble;