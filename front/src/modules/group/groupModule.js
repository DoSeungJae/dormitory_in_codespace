import axios from "axios";
import { toast } from "react-toastify";
import Swal from "sweetalert2";


export const checkGroupState = async (groupId,setGroupState) => {
  const path=`${process.env.REACT_APP_HTTP_API_URL}/group/${groupId}`; //
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

export const closeGroup = async (groupId,setGroupState) => {
    const path=`${process.env.REACT_APP_HTTP_API_URL}/group/close/${groupId}`;
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
        if(error.response.data==="AlreadyClosedGroup"){
          toast.warn("그룹이 이미 마감되었어요.");
        }
    }
}


export const handleSWalGroupClose = async (groupId, setGroupState) => {
  Swal.fire({
    title:"그룹을 마감할까요?",
    text: "마감되면 참여 혹은 퇴장이 불가능해요.",
    confirmButtonColor:"#FF8C00",
    confirmButtonText:"마감하기",
    cancelButtonText:"취소",
    showCancelButton: true
    }).then((result)=>{
      if(result.isConfirmed){
        closeGroup(groupId,setGroupState);
      }
    })
}

export const mapGroupStateText = (groupState,setter) => {
  switch(groupState){
    case 0:
      setter("그룹 시작");
      break;
    case 1:
      setter("마감하기");
      break;
    case -1:
      setter("마감됨");
      break;
    case -2:
      setter("종료됨");
      break;
  }
}