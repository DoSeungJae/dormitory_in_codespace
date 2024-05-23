
import axios from "axios";
import { useContext, useEffect, useState } from "react";
import HomeSelectContext from "../home/HomeSelectContext";
import { toast } from "react-toastify";

function ParticipateButton({articleId}) {
    const token = localStorage.getItem("token");
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const [isProceeding, setIsProceeding]=useState(0);
    const [groupState,setGroupState]=useState(0); //0: 시작 대기, 1: 진행 중, -1: 마감
    const [isMember, setIsMember]=useState(0);
    const [buttonText,setButtonText]=useState("참여");
    const isGroupProceeding = async () => {
        const path=`http://localhost:8080/api/v1/group/${articleId}`;
        try{
            const response=await axios.get(path);
            setIsProceeding(1);
        }catch(error){
            if(error.response.data=="GroupNotFound"){
                setIsProceeding(0);
            }
        }
    }
    const participate = async ()  => {
        const path=`http://localhost:8080/api/v1/group/participate?groupId=${articleId}`;
        const headers = {
            'Authorization':`${token}`
        };
        try{
            const response=await axios.patch(path,{},{headers});
            toast.success('그룹에 참여했어요! "내 그룹"을 확인하세요.');
        }catch(error){
            const errMsg=error.response.data
            if(errMsg=="InvalidToken"){
                //토큰 예외 처리 
            }
            else if(errMsg=="GroupIdNotGiven"){
                toast.warn("해당 그룹을 찾을 수 없어요.")
            }
            else if(errMsg==="AlreadyBelongToThisGroup"){
                toast.warn("이미 이 그룹에 속해있어요.");
            }
            else if(errMsg==="DuplicatedParticipation"){
                toast.warn("다른 그룹에 이미 속해있어요.");
            }
            else if(errMsg==="GroupFull"){
                toast.warn("그룹이 꽉 찼어요!");
            }
        }
    }

    const checkIsMember = async () => {
        const path=`http://localhost:8080/api/v1/group/isMember?groupId=${articleId}`;
        const headers = {
            'Authorization' : `${token}`
        };
        try{
            const response=await axios.get(path,{headers});
            if(response.data==true){
                setIsMember(1);
            }
            else{
                setIsMember(0);
            }
        }catch(error){
            console.error(error);
        }
    }

    useEffect(()=>{
        if(selectComponentIndex!==5){
            return ;
        }
        isGroupProceeding();

    },[selectComponentIndex])

    useEffect(()=>{
        if(isProceeding!=1){
            return ;
        }
        checkIsMember();
    },[isProceeding])
    

    return (
        <>
        {isProceeding==1 && <button className="group-participate-button" onClick={()=>participate()}>{buttonText}</button>}
        </>
    );
}

export default ParticipateButton;