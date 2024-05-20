import { Button } from "@mui/material";
import axios from "axios";

const GroupStartButton = () => {
    const makeGroup = () => {
        console.log(1);
        return 1;

    }
    return (
        <div>
            <button className="group-start-button" onClick={()=>makeGroup()}>
                그룹 만들기
            </button>
        </div>
        
    );
}
export default GroupStartButton;
