import { Button } from "@mui/material";
import axios from "axios";
import { toast } from "react-toastify";
import Swal from "sweetalert2";

const GroupStartButton = ({articleId}) => {
    
    const makeGroup = async (maxCapacity) => {
        const path="http://localhost:8080/api/v1/group/new";
        const body={
            articleId:articleId,
            maxCapacity:maxCapacity
        };
        try{
            const response=await axios.post(path,body,{});
            console.log(response.data);
            toast.success('그룹을 만들었어요! "내 그룹"을 확인하세요.');

        }catch(error){
            const errMsg=error.response.data;
            if(errMsg=="DuplicatedParticipation"){
                toast.warn("이미 어떤 그룹에 속해있어요.");
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
      
    return (
        <div>
            <button className="group-start-button" onClick={()=>handleSwalMaxCapacity()}>
                그룹 시작
            </button>
        </div>
        
    );
}
export default GroupStartButton;
