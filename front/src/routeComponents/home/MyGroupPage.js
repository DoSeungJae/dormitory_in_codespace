import {React, useContext, useEffect, useState} from 'react'
import HomeSelectContext from '../../components/home/HomeSelectContext';
import axios from 'axios';

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
        console.log(groupId,nickName);

    },[selectComponentIndex])

    useEffect(()=>{
        console.log(groupId,nickName);
    },[nickName,groupId])

    
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
        <h1>MyGroupPage</h1>
        

    );

}

export default MyGroupPage;
