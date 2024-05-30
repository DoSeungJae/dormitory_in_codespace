import React,{ useContext, useState } from 'react';
import {Dropdown} from 'react-bootstrap';
import ThreeDots from '../common/ThreeDots';
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios';
import HomeSelectContext from '../home/HomeSelectContext';
import { toast } from 'react-toastify';

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

const ThreeDotsMenu = ({isHostParam,groupParam,hostNickNameParam}) => {
  const [isHost,setIsHost]=useState(0);
  const [group,setGroup]=useState({});
  const [memberList,setMemberList]=useState([]);
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  
  const dormIdToDormName = {
    1:"오름1",
    2:"오름2",
    3:"오름3",
    4:"푸름1",
    5:"푸름2",
    6:"푸름3",
    7:"푸름4",
  }
  
  const goToArticlePage = async () => {
    const path=`http://localhost:8080/api/v1/article/${group.id}`;
    try{
      const response=await axios.get(path);
      const article=(response.data);
      localStorage.setItem("article",JSON.stringify(article));
      localStorage.setItem("nextIndex",selectComponentIndex);
      setSelectComponentIndex(5);
    }catch(error){
      console.error(error);
      setSelectComponentIndex(8);
      localStorage.setItem("nextIndex",5);
      toast.error('회원 정보가 유효하지 않아요! 다시 로그인해주세요.');
      
    }


  }
  
  const handleNickName = (nick) => {
    if(nick==hostNickNameParam){
      return (<div style={{ fontWeight: 'bold' }}>{nick}</div>);
    }
    else{
      return (<>{nick}</>);
    }
  }

  const handleToggle = () => {
    setIsHost(isHostParam);
    setGroup(groupParam);
    setMemberList(groupParam.members.map(jsonString => JSON.parse(jsonString)))
  }


  const menuItems = {
    //isHost : 0 or 1
    0: [
      { type: 'item', eventKey: "1", text: "참여자"+` [${memberList.length}/${group.maxCapacity}]`, action: () => {} },
      { type: 'divider' },
      { type: 'item', eventKey: "2", text: "그룹 정보", action: () => {} },
      { type: 'divider' },
      { type: 'item', eventKey: "3", text: "준비/해제", action: () => console.log(group) },
      { type: 'divider' },
      { type: 'item', eventKey: "4", text: "그룹 나가기", action: () => console.log(2) },
      { type: 'divider' },
      { type: 'item', eventKey: "5", text: "그룹 신고하기", action: () => console.log(3) },
    ],
    1: [
      { type: 'item', eventKey: "1", text: "참여자"+` [${memberList.length}/${group.maxCapacity}]`, action: () => {} },
      { type: 'divider' },
      { type: 'item', eventKey: "2", text: "그룹 정보", action: () => {} },
      { type: 'divider' },
      { type: 'item', eventKey: "3", text: "그룹 마감하기", action: () => console.log(group) },
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
          if (item.text.startsWith("참여자")) {
            return (
              <Dropdown key={index} drop="start" autoClose="outside">
                <Dropdown.Toggle className="dropdown-item nested-dropdown-toggle" as="div">{item.text}</Dropdown.Toggle>
                <Dropdown.Menu>
                  {memberList.map((member, memberIndex) => (
                    <Dropdown.Item className='nested-dropdown-item' 
                                    key={memberIndex} 
                                    eventKey={`member-${memberIndex}`}
                                    onClick={()=>{console.log(memberList[memberIndex])}} 
                                    style={{ fontSize: '0.85rem'}}>
                      <div>{handleNickName(member.nickName)}</div>
                      {(memberIndex!=memberList.length-1) && <Dropdown.Divider/>}
                    </Dropdown.Item>
                  ))}
                </Dropdown.Menu>
              </Dropdown>
            );
          }
          if(item.text=="그룹 정보"){
            return (
              <Dropdown key={index} drop='start'  autoClose="outside">
                <Dropdown.Toggle className="dropdown-item nested-dropdown-toggle" as="div">{item.text}</Dropdown.Toggle>
                <Dropdown.Menu>
                  <Dropdown.Item className='nested-dropdown-item' style={{ fontSize: '0.85rem'}}>
                    <div className='nested-dropdown-item-container'>
                      <div>{"기숙사"}</div>
                      <div>{dormIdToDormName[group.dormId]}</div>
                    </div>
                  </Dropdown.Item>
                  <Dropdown.Divider />
                  <Dropdown.Item className='nested-dropdown-item' style={{ fontSize: '0.85rem'}}>
                    <div className='nested-dropdown-item-container'>
                      <div>{"카테고리"}</div>
                      <div>{group.category}</div>
                    </div>
                  </Dropdown.Item>
                  <Dropdown.Divider />
                  <Dropdown.Item className='nested-dropdown-item' style={{ fontSize: '0.85rem'}} onClick={() =>{goToArticlePage()}}>
                    {"게시글로 가기"}
                  </Dropdown.Item>
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

