import { Button } from "@mui/material";
import axios from "axios";
import { useContext, useEffect, useState } from "react";
import HomeSelectContext from "../home/HomeSelectContext";

function ParticipateButton({articleId}) {
    const token = localStorage.getItem("token");
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const [isProceeding, setIsProceeding]=useState(1);
    
    const isGroupProceeding = async () => {
        const path=`http://localhost:8080/api/v1/group/${articleId}`;
        try{
            const response=await axios.get(path);
            console.log(response);
            //startGroup 기능 구현 이후 코드 작성 예정
            //startGroup 기능 구현 이후 테스트 가능
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
            console.log(response);
        }catch(error){
            console.error(error)
        }
    }
    useEffect(()=>{
        if(selectComponentIndex!==5){
            return ;
        }
        isGroupProceeding();

    },[selectComponentIndex])
    

    return (
        <>
        {isProceeding==1 && <button className="group-participate-button" onClick={()=>participate()}>그룹 참여</button>}
        </>
        

        

    );
}

export default ParticipateButton;
