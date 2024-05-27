import {React, useContext, useEffect, useState} from 'react'
import HomeSelectContext from '../../components/home/HomeSelectContext';
import axios from 'axios';
import BackButton from '../../components/home/BackButton';
import { ThemeProvider } from '@mui/material';
import theme from '../group/theme';
import ChatRoom from '../../views/group/ChatRoom';


function MyGroupPage(){
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const [nickName,setNickName]=useState("");
    const [groupId,setGroupId]=useState(0);
    const token=localStorage.getItem("token");
    
    useEffect(()=>{
        if(selectComponentIndex!=4){
            return ;   
        }
        if(nickName==""){
            getUserNickNameFromToken();
        }
        if(groupId==0){
            getGroupIdThatUserBelongsTo();
        }

    },[selectComponentIndex])


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
    }catch(error){
      console.error(error);
    }
  }


    return (
        <div className='App'>
            <header className='App-myGroupPage-header'>
                <BackButton></BackButton>
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
