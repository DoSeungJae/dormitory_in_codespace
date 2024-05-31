import { Button } from "@mui/material";
import axios from "axios";
import { useEffect, useState,useContext } from "react";
import { toast } from "react-toastify";
import Swal from "sweetalert2";
import HomeSelectContext from "../home/HomeSelectContext";
import { checkGroupState, closeGroup } from "../../modules/group/groupModule";

const GroupStartButton = ({articleId}) => {
  const [buttontext,setButtonText]=useState("그룹 시작");
  const [groupState, setGroupState]=useState(0);
  const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
  const token = localStorage.getItem("token");
  //0: 시작대기, 1: 진행중, -1:마감됨, -2:종료됨
  const defaultColor = '#FF8C00';
  const [isBlur, setIsBlur]=useState(0);
  const bgColor = isBlur ? 'rgba(255, 140, 0, 0.6)' : defaultColor;
  const handleBlurStyle = () => {
    if(groupState==-1 || groupState==-2){
      setIsBlur(1);
    }
    else{
      setIsBlur(0);
    }
  }
  const mapFunctions = () => {
    switch(groupState){
      case 0:
        handleSwalMaxCapacity();
        break;
      case 1:
        handleSWalGroupClose();
        break;
      case -1:
        handleSWalGroupFinish();
        break;
      case -2:
        toast.info("종료된 그룹이에요.");
        break;
    } 
  }
  const mapButtonText = () => {
    switch(groupState){
      case 0:
        setButtonText("그룹 시작");
        break;
      case 1:
        setButtonText("마감하기");
        break;
      case -1:
        setButtonText("마감됨");
        break;
      case -2:
        setButtonText("종료됨");
        break;
    }
  }

  const handleSWalGroupClose = async () => {
    Swal.fire({
      title:"그룹을 마감할까요?",
      text: "마감되면 다시 되돌릴 수 없어요!",
      confirmButtonColor:"#FF8C00",
      confirmButtonText:"마감하기",
      cancelButtonText:"취소",
      showCancelButton: true
      }).then((result)=>{
        if(result.isConfirmed){
          closeGroup(articleId,setGroupState);
        }
      })
  }

  const handleSWalGroupFinish = async () => {
    Swal.fire({
      title:"그룹을 종료할까요?",
      text: "마감이후 일정 시간이 지나면 자동으로 종료돼요.",
      confirmButtonColor:"#FF8C00",
      confirmButtonText:"종료하기",
      cancelButtonText:"취소",
      showCancelButton: true
      }).then((result)=>{
        if(result.isConfirmed){
          finishGroup();
        }
      })
  }

  const handleSwalGroupFinishForce = async () => {
    Swal.fire({
      title:"그룹 강제 종료",
      text: "아직 참여 중인 인원이 있어요, 종료하기 위해서는 모든 사람들을 내보내야해요. ",
      confirmButtonColor:"#FF8C00",
      confirmButtonText:"그래도 종료하기",
      cancelButtonText:"취소",
      showCancelButton: true
      }).then((result)=>{
        if(result.isConfirmed){
          const forcePath=`http://localhost:8080/api/v1/group/finish/${articleId}?force=1`;
          finishGroup(forcePath);
        }
      })
  }

  const finishGroup = async (forcePath) => {
    let path;
    if(forcePath){
      path=forcePath;
    }
    else{
      path=`http://localhost:8080/api/v1/group/finish/${articleId}`;
    }
    console.log(path);
    const headers={
      'Authorization':`${token}`
    };
    try{
      const response=await axios.patch(path,{},{headers});
      console.log(response.data);
      if(response.data=="GroupFinished"){
        setGroupState(-2);
        toast.info("그룹이 종료되었어요.");
      }
    }catch(error){
      const errMsg=error.response.data;
      if(errMsg=="CannotFinishWhileRecruiting"){
        handleSwalGroupFinishForce();
      }
    }
  }

    const makeGroup = async (maxCapacity) => {
        const path="http://localhost:8080/api/v1/group/new";
        const body={
            articleId:articleId,
            maxCapacity:maxCapacity
        };
        try{
            const response=await axios.post(path,body,{});
            setGroupState(1);
            toast.success('그룹을 만들었어요! "내 그룹"을 확인하세요.');

        }catch(error){
            const errMsg=error.response.data;
            if(errMsg=="DuplicatedParticipation"){
                toast.warn("이미 그룹에 속해있어요.");
            }
        }
    }

    const handleSwalMaxCapacity = async () => {
        const { value } = await Swal.fire({
          confirmButtonColor:"#FF8C00",
          title: "최대 인원수",
          confirmButtonText:"다음",
          cancelButtonText:"취소",
          input:'number',
          inputPlaceholder: "최소 2명, 최대 10명",
          showCancelButton: true,
          inputValidator: (value) => {
            return new Promise((resolve) => {
                value=parseInt(value);
              if (!value) {
                resolve("최대 인원수를 입력해주세요!");
              }
              if (value && value>=2 && value<=10) {
                resolve();
                makeGroup(value);      
              } else {
                resolve("최대 인원수는 2명에서 10명까지 가능해요!");
              }
            });
          }
        });
      }

    
    useEffect(()=>{
      if(selectComponentIndex!=5){
        return ;
      }
      checkGroupState(articleId,setGroupState);
    },[selectComponentIndex])

    useEffect(()=>{
      if(selectComponentIndex!=5){
        return ;
      }
      mapButtonText();
      handleBlurStyle();
    },[groupState]);


      
    return (
        <div>
            <button className="group-start-button" onClick={()=>mapFunctions()}
                    style={{backgroundColor:bgColor,transition: 'background-color 0.8s ease'}}>
                {buttontext}
            </button>
        </div>
        
    );
}
export default GroupStartButton;
