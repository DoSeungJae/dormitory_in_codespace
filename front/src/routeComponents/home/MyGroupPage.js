import {React, useContext, useEffect, useState} from 'react'
import HomeSelectContext from '../../components/home/HomeSelectContext';
import axios from 'axios';
import BackButton from '../../components/home/BackButton';
import { ThemeProvider } from '@mui/material';
import theme from '../group/theme';
import ChatRoom from '../../views/group/ChatRoom';
import ThreeDotsMenu from '../../components/group/ThreeDotsMenu';


function MyGroupPage(){
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const [nickName,setNickName]=useState("");
    const [groupId,setGroupId]=useState(0);
    const [hostNickName,setHostNickName]=useState("");
    const [isHost,setIsHost]=useState(0);
    const [group,setGroup]=useState({});
    const token=localStorage.getItem("token");
    const title=hostNickName+"의 그룹";

    useEffect(()=>{
        if(selectComponentIndex!=4){
          //pageInit();
          return ;   
        }
        checkIsHost();
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


  const pageInit = () => {
    setNickName("");
    setHostNickName("");
    setGroupId("");
    setGroup({});
    setIsHost(0);
  }

  const getUserNickNameFromToken = async () => {
    if(token==null){
      return ;
    }
    const path=`http://localhost:8080/api/v1/user/NickName`;
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
    const path=`http://localhost:8080/api/v1/group/userBelongsTo`;
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
    const path=`http://localhost:8080/api/v1/group/host/${groupId}`;
    try{
      const response=await axios.get(path);
      setHostNickName(response.data.nickName);
    }catch(error){
      console.error(error);
    }
  }

  const checkIsHost = () => {
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
                {title}
                <ThreeDotsMenu
                  isHostParam={isHost}
                  groupParam={group}
                />

            </header>
            <div className='App-myGroupPage-main'>  
                {(groupId!=0 && nickName!="") ?
                    (
                    <ThemeProvider theme={theme}>
                        <ChatRoom
                            username={nickName}
                            room={groupId}
                        />
                    </ThemeProvider>
                    ) :(
                        <><h6>그룹에 참여하세요!</h6></>
                    )
                }
            </div>
        </div>
    );

}

export default MyGroupPage;
