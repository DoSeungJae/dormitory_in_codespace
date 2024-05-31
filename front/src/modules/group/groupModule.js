import axios from "axios";
import { toast } from "react-toastify";

export const closeGroup = async (groupId,setGroupState) => {
    const path=`http://localhost:8080/api/v1/group/close/${groupId}`;
    const token=localStorage.getItem("token");
    const headers={
        'Authorization':`${token}`
      };
    try{
        const response=await axios.patch(path,{},{headers});
        if(response.data=="GroupClosed"){
            setGroupState(-1);
            toast.info("그룹이 마감되었어요.");
            //다른 멤버에게도 web socket과 같은 형태로 전달
        }
    }catch(error){
        console.error(error);
    }
}

export const checkGroupState = async (groupId,setGroupState) => {
    const path=`http://localhost:8080/api/v1/group/${groupId}`; //
    try{
      const response=await axios.get(path);
      const isProceeding=response.data.isProceeding;
      const numMembers=response.data.currentNumberOfMembers;
      if(isProceeding){
        setGroupState(1);
      }
      else if(numMembers>=1){
        setGroupState(-1);
      }
      else{
        setGroupState(-2);
      }
    }catch(error){
      const errMsg=error.response.data;
      if(errMsg=="GroupNotFound"){
        setGroupState(0);
      }
    }
  }