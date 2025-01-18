import {React, useContext, useEffect, useState} from 'react'
import HomeSelectContext from '../../components/home/HomeSelectContext';
import axios from 'axios';
import { ThemeProvider } from '@mui/material';
import theme from '../group/theme';
import ChatRoom from '../../views/group/ChatRoom';
import ThreeDotsMenu from '../../components/group/ThreeDotsMenu';
import { checkGroupState, mapGroupStateText } from '../../modules/group/groupModule';
import { useSocket } from '../../hooks/group/useSocket';
import { toast } from 'react-toastify';
import BackButton from '../../components/group/BackButton';


function MyGroupPage(){
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const [nickName,setNickName]=useState("");
    const [groupId,setGroupId]=useState(0);
    const [hostNickName,setHostNickName]=useState("");
    const [isHost,setIsHost]=useState(0);
    const [group,setGroup]=useState({});
    const [groupState,setGroupState]=useState(0);
    const [groupStateText,setGroupStateText]=useState("");
    const { isConnected, socketResponse, sendData } = useSocket(groupId, nickName);

    const token=localStorage.getItem("token");
    const title = (
      <span>
        <strong>{hostNickName}</strong>{`의 그룹 • ${groupStateText}`}
      </span>
    );

    useEffect(()=>{
      if(socketResponse.messageType=="SERVER"){
        handleServerMessage(socketResponse)
      }
    },[socketResponse])

    useEffect(()=>{
      if(groupId==0){
        return ;
      }
      checkGroupState(groupId,setGroupState);
    },[selectComponentIndex,groupId])

    useEffect(()=>{
      if(groupState==1){
        setGroupStateText("진행중");
      }
      else if(groupState==-1){
        setGroupStateText("마감됨");
      }
    },[groupState])

  
  
    useEffect(()=>{
        if(nickName==""){
            getUserNickNameFromToken();
        }
        if(groupId==0){
            getGroupIdThatUserBelongsTo();
        }
    },[selectComponentIndex])

    useEffect(()=>{
      if(nickName!="" && groupId==0){
        return ;
      }
      getGroupHostNickName();
      
    },[nickName,groupId]);

    useEffect(()=>{
      if(nickName=="" || hostNickName==""){
        return ;
      }
      checkIsHost();

    },[hostNickName,nickName,selectComponentIndex])


  const handleServerMessage = async (response) => {
    const type=response.message.split(":")[0];
    const target=response.message.split(":")[1];
    switch(type){
      case 'groupClosed':
        setGroupState(-1);
        break;
      case 'groupFinished':
        setGroupState(-2);
        break;
      case 'expelledFromGroup':
        if(isHost==0 && nickName==target){
          toast.info("호스트에 의해 그룹에서 추방됐어요.");
          setGroupState(-2);
        }
        break;
    }
  }



  const pageInit = () => {
    setNickName("");
    setHostNickName("");
    setGroupId("");
    setGroup({});
    setIsHost(0);
    setGroupStateText("");
  }

  const getUserNickNameFromToken = async () => {
    if(token==null){
      return ;
    }
    const path=`${process.env.REACT_APP_HTTP_API_URL}/user/NickName`;
    const headers={
      'Authorization' : `${token}`
    };
    try{
      const response=await axios.get(path,{headers});
      setNickName(response.data);

    }catch(error){
      console.error(error);
    }
  }

  const getGroupIdThatUserBelongsTo = async () => {
    if(token==null){
      return ;
    }
    const path=`${process.env.REACT_APP_HTTP_API_URL}/group/userBelongsTo`;
    const headers={
      'Authorization' : `${token}`
    };
    try{
      const response=await axios.get(path,{headers});
      setGroupId(response.data.id);
      setGroup(response.data);
    }catch(error){
      console.error(error);
    }
  }

  const getGroupHostNickName = async () => {
    if(groupId==0){
      return ;
    }
    const path=`${process.env.REACT_APP_HTTP_API_URL}/group/host/${groupId}`;
    try{
      const response=await axios.get(path);
      setHostNickName(response.data.nickName);
    }catch(error){
      console.error(error);
    }
  }

  const checkIsHost = () => {
    if(hostNickName=="" || nickName==""){
      return ;
    }
    if(hostNickName==nickName){
      setIsHost(1);
    }
    else{
      setIsHost(0);
    }
  }



  return (
    <div className='App-myGroupPage'>
      <header className='App-myGroupPage-header'>
        <BackButton></BackButton>
        {(groupState != -2 && groupState!=0)  ? (
        <>
            {title}
            <ThreeDotsMenu
                isHostParam={isHost}
                groupParam={group}
                hostNickNameParam={hostNickName}
                myNickName={nickName}
                groupState={groupState}
                setGroupState={setGroupState}
                socketResponse={socketResponse}
            />
        </>
      ) : null}
      </header>
      <div className='App-myGroupPage-main'>  
        {
          (groupId!=0 && nickName!="" && groupState!=-2 && groupState!=0) ?
          (
            <ChatRoom
                username={nickName}
                room={groupId}
                socketResponse={socketResponse}
                sendData={sendData}
            />
          ) :
          (
            <><h6>그룹을 만들거나 진행중인 그룹에 참여하세요!</h6></>
          )
        }
      </div>
    </div>
  );

}

export default MyGroupPage;
