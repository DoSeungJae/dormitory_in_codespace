
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

    const [buttonText,setButtonText]=useState("");
    const [isBlur, setIsBlur]=useState(0);

    const defaultColor = '#FDAA5A';
    const bgColor = isBlur ? 'rgba(253, 170, 90, 0.6)' : defaultColor;
    const handleBlurStyle = () => {
        if(groupState==-1 || groupState==-2 || groupState==9){
            setIsBlur(1);
        }
        else{
            setIsBlur(0);
        }
    }

    const handleClickedPreprocess = async () => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/group/stateFromExternal/${articleId}`;
        const headers={
            'Authorization':`${token}`
        };
        try{
            const response=await axios.get(path,{headers});
            const state=response.data
            if(state!=groupState){
                setGroupState(state);
                mapFunctions(state);
            }
            else{
                mapFunctions();
            }
        }catch(error){
            console.error(error);
        }
    }

    const mapFunctions = async (force) => {
        const std=force==undefined?groupState:force;
        switch(std){
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

    const checkGroupStateFromExternalPerspective = async () => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/group/stateFromExternal/${articleId}`;
        const headers={
            'Authorization':`${token}`
        };
        try{
            const response=await axios.get(path,{headers});
            setGroupState(response.data);
        }catch(error){
            console.error(error);
        }
    }

    const participate = async ()  => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/group/participate?groupId=${articleId}`;
        const headers = {
            'Authorization':`${token}`
        };
        try{
            const response=await axios.patch(path,{},{headers});
            toast.success('그룹에 참여했어요! "내 그룹"을 확인하세요.');
            setGroupState(1);
            
        }catch(error){
            const errMsg=error.response.data;
            console.log(errMsg);
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
            else if(errMsg=="CannotParticipateInTheGroupExpelledFromAgain"){
                toast.error("이 그룹에서 추방되었기 때문에 다시 참여할 수 없어요!");
            }
            
        }
    }

    const quit = async () => {
        const path=`${process.env.REACT_APP_HTTP_API_URL}/group/quit?groupId=${articleId}`;
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
                toast.error("로그인 정보가 만료되었어요, 다시 로그인해주세요.");
            }
        }
    }

    useEffect(()=>{
        if(selectComponentIndex!==5){
            return ;
        }
        checkGroupStateFromExternalPerspective();
    },[selectComponentIndex])

    useEffect(()=>{
        if(selectComponentIndex!=5){
            return ;
        }
        mapButtonText();
        handleBlurStyle();
    },[groupState]);

    

    return (
        <>
        {<button className="group-participate-button" onClick={()=>handleClickedPreprocess()} style={{
            backgroundColor:bgColor,
            transition: 'background-color 0.8s ease'}} >{buttonText}</button>}
        </>
    );
}

export default ParticipateButton;
