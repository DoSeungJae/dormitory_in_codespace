import React,{useState}from 'react';
import {Dropdown} from 'react-bootstrap';
import ThreeDots from '../common/ThreeDots';

const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
  <h1
    href=""
    ref={ref}
    onClick={(e) => {
      e.preventDefault();
      onClick(e);
    }}
  >
    {children}
  </h1>
));

const ThreeDotsMenu = ({isWriterParam}) => {
  const [isWriter,setIsWriter]=useState(0); //초기값을 isWriterParam으로 설정할 시 에러 발생 -> 렌더링 안됨.

  const menuItems = {
    0: [
      { eventKey: "1", text: "신고", action: () => alert('Action 1 executed') },
      { eventKey: "2", text: "URL 공유", action: () => alert('Action 2 executed') },
    ],
    1: [
      { eventKey: "1", text: "수정", action: () => alert('Action 1-2 executed') },
      { eventKey: "2", text: "삭제", action: () => alert('Action 2-2 executed') },
      { eventKey: "3", text: "URL 공유", action: () => alert('Action 3-2 executed') },
    ],

  };

  const handleToggle = () => {
    setIsWriter(isWriterParam === 1 ? 1 : 0); // 상태 토글
  };

  return (
    <Dropdown onToggle={handleToggle}>
      <Dropdown.Toggle style={{ color: 'black' }} as={CustomToggle} id="dropdown-autoclose-true">
        <ThreeDots/>
      </Dropdown.Toggle>

      <Dropdown.Menu>
        {menuItems[isWriter].map((item, index) => (
        <Dropdown.Item 
          eventKey={item.eventKey} 
          key={index} 
          onClick={item.action} 
        >
          {item.text}
        </Dropdown.Item>
      ))}
      </Dropdown.Menu>
    </Dropdown>
  );
};

export default ThreeDotsMenu;
