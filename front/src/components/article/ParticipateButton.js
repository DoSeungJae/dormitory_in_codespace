
import axios from "axios";
import { useContext, useEffect, useState } from "react";
import HomeSelectContext from "../home/HomeSelectContext";
import { toast } from "react-toastify";

function ParticipateButton({articleId}) {
    const token = localStorage.getItem("token");
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    //const [isProceeding, setIsProceeding]=useState(0);
    //진행중인 group이라면 groupState를 1로 설정
    const [groupState,setGroupState]=useState(0); 
    //0: 시작 대기, 1: 진행중&%member, 2:진행중&&nonMember -1: 마감됨, -2: 종료됨, 9: 꽉 참
    const [doesGroupExist,setDoesGroupExist]=useState(0); //그룹이 존재하면 1, 그렇지 않다면 0
    //위 값으로 "시작 대기" 상태를 체크할 수 있음.
    //doesGroupExist가 0이면 대기 상태고, 그렇지 않다면 다른 상태일 것임
    const [isMember, setIsMember]=useState(0);
    const [numMembers,setNumMembers]=useState(0);
    //참여자의 숫자를 나타내는 값, 마감 혹은 종료 상태를 판단할 수 있음
    //위 기능 뿐만 아니라 다른 곳에서도 필요할 것으로 예상

    const [buttonText,setButtonText]=useState("");

    const mapFunctions = () => {
        switch(groupState){
            case 0:
                toast.info("그룹이 시작되지 않았어요.");
                break;
            case 2:
                participate();
                break;
            case 1:
                quit();
                break;
            case -1:
                toast.info("마감된 그룹은 참여할 수 없어요.")
                break;
            case -2:
                toast.info("이미 종료된 그룹이에요.");
                break;
            case 9:
                toast.info("그룹이 꽉 차서 참여할 수 없어요.")
                break;
        }
    }
    const mapButtonText = () => {
        if(groupState==0){
            setButtonText("시작 대기");
        }
        else if(groupState==2){
            setButtonText("그룹 참가");
        }
        else if(groupState==1){
            setButtonText("그룹 퇴장")
        }
        else if(groupState==-1){
            setButtonText("마감됨");
        }
        else if(groupState==-2){
            setButtonText("종료됨");
        }
        else if(groupState==9){
            setButtonText("인원 꽉참");
        }
    }

    const checkGroupState = async () => {
        let isMemberRegion=0;
        const checkIsMember = async () => {
            const path=`http://localhost:8080/api/v1/group/isMember?groupId=${articleId}`;
            const headers = {
                'Authorization' : `${token}`
            };
            try{
                const response=await axios.get(path,{headers});
                if(response.data==true){
                    setIsMember(1);
                    isMemberRegion=1;
                }
                else{
                    setIsMember(0);
                    isMemberRegion=0;
                }
            }catch(error){
                console.error(error);
            }
        } 
        const mainfunc = async () => {
            const path=`http://localhost:8080/api/v1/group/${articleId}`;
            try{
                const response=await axios.get(path);
                const isProceeding=response.data.isProceeding;
                const numMembersRegion=response.data.currentNumberOfMembers;
                const maxCapacity=response.data.maxCapacity;

                if(isProceeding && isMemberRegion==0){
                    setGroupState(2);
                }
                if(isProceeding && isMemberRegion==1){
                    setGroupState(1);
                }
                else if(isProceeding && maxCapacity===numMembersRegion){
                    setGroupState(9);
                }
                else if(!isProceeding && numMembersRegion!=0){
                    setGroupState(-1);
                }
                if(!isProceeding && numMembersRegion==0){
                    setGroupState(-2);
                }
            }catch(error){
                if(error.response.data=="GroupNotFound"){
                    setGroupState(0);
                }
            }  
        }

        try{
            await checkIsMember();
        }catch(error){
            
        }finally{
            await mainfunc();
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
            setGroupState(1);
            
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

    const quit = async () => {
        const path=`http://localhost:8080/api/v1/group/quit?groupId=${articleId}`;
        const headers = {
            'Authorization':`${token}`
        };
        try{
            const response=await axios.patch(path,{},{headers});
            toast.info("그룹을 나왔어요. 이제 다른 그룹에 참여할 수 있어요.");
            setGroupState(2);
        }catch(error){
            const errMsg=error.response.data;
            if(errMsg=="InvalidToken"){
                //토큰 예외 처리 
            }
        }
    }



    useEffect(()=>{
        if(selectComponentIndex!==5){
            return ;
        }
        checkGroupState();

    },[selectComponentIndex])

    useEffect(()=>{
        if(selectComponentIndex!=5){
            return ;
        }
        mapButtonText();
    },[groupState]);

    

    return (
        <>
        {<button className="group-participate-button" onClick={()=>mapFunctions()}>{buttonText}</button>}
        </>
    );
}

export default ParticipateButton;
