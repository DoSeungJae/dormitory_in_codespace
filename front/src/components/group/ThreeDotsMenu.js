import React,{ useState } from 'react';
import {Dropdown} from 'react-bootstrap';
import ThreeDots from '../common/ThreeDots';
import 'react-toastify/dist/ReactToastify.css';

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

const ThreeDotsMenu = ({isHostParam,groupParam}) => {
  const [isHost,setIsHost]=useState(0);
  const [group,setGroup]=useState({});
  const [memberList,setMemberList]=useState([]);

  const handleToggle = () => {
    setIsHost(isHostParam);
    setGroup(groupParam);
    setMemberList(groupParam.members.map(jsonString => JSON.parse(jsonString)))
  }

  const showMembers = async () => {
   console.log(memberList); 
  }

  const menuItems = {
    //isHost : 0 or 1
    0: [
      { type: 'item', eventKey: "1", text: "참여자 보기 ", action: () => {} },
      { type: 'divider' },
      { type: 'item', eventKey: "2", text: "준비/해제", action: () => console.log(1) },
      { type: 'divider' },
      { type: 'item', eventKey: "2", text: "그룹 나가기", action: () => console.log(2) },
      { type: 'divider' },
      { type: 'item', eventKey: "3", text: "그룹 신고하기", action: () => console.log(3) },
    ],
    1: [
      { type: 'item', eventKey: "1", text: "참여자 보기 ", action: () => {} },
      { type: 'divider' },
      { type: 'item', eventKey: "2", text: "그룹 마감하기", action: () => console.log(5) },
    ],
  };

  return (
    <Dropdown onToggle={handleToggle}>
      <Dropdown.Toggle style={{ color: 'black' }} as={CustomToggle} id="dropdown-autoclose-true">
        <ThreeDots />
      </Dropdown.Toggle>

      <Dropdown.Menu >
        {menuItems[isHost].map((item, index) => {
          if (item.type === 'divider') {
            return <Dropdown.Divider key={index} />;
          }
          if (item.text === "참여자 보기 ") {
            return (
              <Dropdown key={index} drop="start" autoClose="outside">
                <Dropdown.Toggle className="dropdown-item nested-dropdown-toggle" as="div">{item.text}</Dropdown.Toggle>
                <Dropdown.Menu>
                  {memberList.map((member, memberIndex) => (
                    <Dropdown.Item  key={memberIndex} eventKey={`member-${memberIndex}`} style={{ fontSize: '0.85rem'}}>
                      {member.nickName}
                      {(memberIndex!=memberList.length-1) &&<Dropdown.Divider key={`divider-${index}`} />}
                    </Dropdown.Item>
                  ))}
                </Dropdown.Menu>
              </Dropdown>
            );
          }
          return (
            <Dropdown.Item
              eventKey={item.eventKey}
              key={index}
              onClick={item.action}
            >
              {item.text}
            </Dropdown.Item>
          );
        })}
      </Dropdown.Menu>
    </Dropdown>
  );
    

};

export default ThreeDotsMenu;

