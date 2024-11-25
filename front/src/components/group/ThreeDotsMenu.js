import React,{ useContext, useEffect, useState } from 'react';
import {Dropdown} from 'react-bootstrap';
import ThreeDots from '../common/ThreeDots';
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios';
import HomeSelectContext from '../home/HomeSelectContext';
import { toast } from 'react-toastify';
import Swal from 'sweetalert2';
import { closeGroup, handleSWalGroupClose } from '../../modules/group/groupModule';
import ArticleContext from '../article/ArticleContext';


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

const ThreeDotsMenu = ({isHostParam,groupParam,hostNickNameParam,myNickName,groupState,setGroupState,socketResponse}) => {
  const [isHost,setIsHost]=useState(0);
  const [group,setGroup]=useState({});
  const [memberList,setMemberList]=useState([]);
  const [expelledMemberIdList,setExpelledMemberIdList]=useState([]);
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  const [initial,setInitial]=useState(0);
  const token=localStorage.getItem("token");
  const {article, setArticle}=useContext(ArticleContext);
  const dormIdToDormName = {
    1:"오름1",
    2:"오름2",
    3:"오름3",
    4:"푸름1",
    5:"푸름2",
    6:"푸름3",
    7:"푸름4",
  }

  const DropDownMenuItems = ({}) => {
    const [dynamicMenuItems, setDynamicMenuItems] = useState([]);
  
    useEffect(() => {
      const updatedMenuItems = {
        0: [
          { type: 'item', eventKey: "1", text: `참여자 [${memberList.length}/${group.maxCapacity}]`, action: () => {} },
          { type: 'divider' },
          { type: 'item', eventKey: "2", text: "그룹 정보", action: () => {} },
          { type: 'divider' },
          { type: 'item', eventKey: "4", text: "그룹 나가기", action: () => quitGroup() },
          { type: 'divider' },
          { type: 'item', eventKey: "5", text: "그룹 신고하기", action: () => handleSwalReportGroup() },
        ],
        1: [
          { type: 'item', eventKey: "1", text: `참여자 [${memberList.length}/${group.maxCapacity}]`, action: () => {} },
          { type: 'divider' },
          { type: 'item', eventKey: "2", text: "그룹 정보", action: () => {} },
          { type: 'divider' },
          { type: 'item', eventKey: "3", text: "그룹 마감하기", action: () => handleSWalGroupClose(group.id, setGroupState) },
        ],
      };
      setDynamicMenuItems(updatedMenuItems[isHost]); 
    }, [memberList,isHost]); 

    useEffect(()=>{
      if(Object.keys(article).length !==0){
          setSelectComponentIndex(5);
      }
    },[article])
  
    return (
      <>
        {dynamicMenuItems.map((item, index) => {
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
                                    onClick={() => { handleSwalMember(memberList[memberIndex]) }}
                                    style={{ fontSize: '0.85rem' }}>
                      <div className='nested-dropdown-item-container'>
                        <div>{handleNickName(member.nickName)}</div>
                        {(myNickName === member.nickName) && <div>{"me!"}</div>}
                      </div>
                      {(memberIndex !== memberList.length - 1) && <Dropdown.Divider />}
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
      </>
    );
  };

  useEffect(()=>{

    if(socketResponse.messageType=="SERVER"){
      handleServerMessage(socketResponse);
    }
  },[socketResponse])

  const handleServerMessage =  async (response) => {
    const type=response.message.split(":")[0];
    const target=response.message.split(":")[1];
    switch(type){
      case 'participatedInGroup':
        //user 정보 받아오기
        if(target!=myNickName){
          toast.info(`${target}님이 그룹에 참가했어요.`)
          applyMemberChange(target,1);
        }
        break;
      case 'expelledFromGroup':
        //user 정보 받아오기
        if(target==myNickName){
          toast.info(`호스트에 의해 그룹에서 추방됐어요.`);
          applyMemberChange(target,-1);
        }
        else{
          if(isHost!=0){
            toast.info(`${target}님이 추방됐어요.`);
            applyMemberChange(target,-1);
          }
        }
        break;
      case 'leftGroup':
        //user 정보 받아오기
        if(target!=myNickName){
          applyMemberChange(target,-1);
          toast.info(`${target}님이 그룹을 떠났어요.`)
        }
        break;
      case 'groupClosed':
        if(isHost==0){
          toast.info("그룹이 마감되었어요, 더이상 새로운 참여자를 받지 않아요.");
        }

        break;
      case 'groupFinished':
        if(isHost==0){
          toast.info("호스트가 그룹을 종료했어요.");
        }
        break;
    }
  }

  const handleSwalReportGroup=async () => {
    Swal.fire({
      confirmButtonColor:"#FF8C00",
      title: "신고",
      confirmButtonText:"신고",
      cancelButtonText:"취소",
      input: "select",
      inputOptions: {
        카테고리벗어남:"카테고리벗어남",
        기타:"기타"
      },
      inputPlaceholder: "신고 사유를 선택하세요.",
      showCancelButton: true,
      inputValidator: (value) => {
        return new Promise((resolve) => {
          if (value) {
            resolve();
            report("GROUP",group.id,value);
          } else {
            resolve("신고 사유를 선택해주세요!");
          }
        });
      }
    });
  }

  const handleSwalReportMember=async (memberId) => {
    Swal.fire({
      confirmButtonColor:"#FF8C00",
      title: "신고",
      confirmButtonText:"신고",
      cancelButtonText:"취소",
      input: "select",
      inputOptions: {
        도배:"도배",
        노쇼:"노쇼",
        욕설:"욕설",
        기타:"기타"
      },
      inputPlaceholder: "신고 사유를 선택하세요.",
      showCancelButton: true,
      inputValidator: (value) => {
        return new Promise((resolve) => {
          if (value) {
            resolve();
            report("USER",memberId,value);
          } else {
            resolve("신고 사유를 선택해주세요!");
          }
        });
      }
    });
  }


  const report = async (targetType,id,reportReason) => {
    const path = `https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/report/new`;
    const body={
      reportType:targetType,
      targetId:id,
      reason:reportReason
    };
    const headers = {
      'Authorization':`${token}`
  };
  try{
    const response=await axios.post(path,body,{headers});
    toast.success("신고가 접수되었어요.");
  }
  catch(error){
    const errMsg=error.response.data;
    if(errMsg=="InvalidToken"){
        toast.error("로그인 정보가 만료되었어요, 다시 로그인해주세요.");
    }
    else{
      console.log(errMsg);
    }
  }
    
  }
  
  const quitGroup = async () => {
    const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/group/quit?groupId=${group.id}`; 
    const headers = {
        'Authorization':`${token}`
    };
    try{
        const response=await axios.patch(path,{},{headers});
        toast.info("그룹을 나왔어요. 이제 다른 그룹에 참여할 수 있어요.");
        setGroupState(0);
    }catch(error){
        const errMsg=error.response.data;
        if(errMsg=="InvalidToken"){
            toast.error("로그인 정보가 만료되었어요, 다시 로그인해주세요.");
        }
    }
  }

  const goToArticlePage = async () => { //수정 중
    const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/article/${group.id}`;
    try{
      const response=await axios.get(path);
      const article1=(response.data);
      setArticle(article1);
      localStorage.setItem("nextIndex",selectComponentIndex);
    }catch(error){ //아래 코드 블록도 수정해야함
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
    if(initial==1){
      return ;
    }
    setMemberList(groupParam.members
      .map(jsonString => JSON.parse(jsonString))
      .filter(member=>!expelledMemberIdList.includes(member.id))
    )
    setInitial(1);
   
  }

  const handleSwalMember = (member) => {
    Swal.fire({
      text:`${member.nickName}`,
      showCancelButton:(member.nickName!=myNickName)?true:false,
      showDenyButton:(isHost&&member.nickName!=myNickName)?true:false,
      confirmButtonText: "확인",
      cancelButtonText:"신고하기",
      denyButtonText:"내보내기"
    }).then((result)=>{
      if(result.isDismissed){
        handleSwalReportMember(member.id);
      }
      else if(result.isDenied){
        handleSwalExpelMember(member);
      }
    })
  } 

  const handleSwalExpelMember =  (member) => {
    Swal.fire({
      text:`${member.nickName}님을 내보낼까요? 추방된 사용자는 다시 이 그룹에 참여할 수 없어요!`,
      showCancelButton:true,
      confirmButtonText:"내보내기",
      cancelButtonText:"취소"
    }).then((result)=>{
      if(result.isConfirmed){
        expelMember(member);
      }
    })
  }

  const expelMember = async (memberToBeExpelled) => {
    const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/group/expel?groupId=${group.id}&targetId=${memberToBeExpelled.id}`;
    const headers = {
      'Authorization':`${token}`
    };
    try{
      const response=await axios.patch(path,{},{headers});
      if(response.data.mode=="expel"){
        toast.success(`${memberToBeExpelled.nickName}님이 추방되었어요.`); //서버 전체 메시지로 전송하기
        setExpelledMemberIdList(prev=>[...prev,memberToBeExpelled.id])
      }
    }catch(error){
      console.error(error);
      toast.error("예상하지 못한 문제가 발생했어요!");
    }
  }

  const applyMemberChange = async (nickName,mode) => { //-1 : 멤버 제거, 1 : 멤버 추가
    const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/user/findByNick/${nickName}`;
    try{
      const response=await axios.get(path);
      const user=(response.data);
      if(mode==1){
        setMemberList([...memberList,user]);
      }else{
        setMemberList(memberList.filter(item=>item.id!=user.id));
      }
    }catch(error){
      console.error(error);
    }
  }

  return (
    <Dropdown onToggle={handleToggle}>
      <Dropdown.Toggle style={{ color: 'black' }} as={CustomToggle} id="dropdown-autoclose-true">
        <ThreeDots />
      </Dropdown.Toggle>

      <Dropdown.Menu >
        <DropDownMenuItems/>
      </Dropdown.Menu>
    </Dropdown>
  );
    
};

export default ThreeDotsMenu;

