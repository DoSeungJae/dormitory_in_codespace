import axios from 'axios'
import { getRelativeTime, latterTime } from './timeModule';

//const suspendedFunctionName = (suspendedFunction) => {
//    switch (suspendedFunction) {
//        case "LOGIN": return "로그인"
//        case "ARTICLE": return "글 게시/수정"
//        case "COMMENT": return "댓글 게시"
//        case "GROUP": return "그룹 생성/참여"
//        default: return ""
//    }
//} 

export const checkRestriction = async (suspendedFunction) => {
    const token=localStorage.getItem("token");
    const headers = {'Authorization':`${token}`};
    try {
        const res = await axios.get(`${process.env.REACT_APP_HTTP_API_URL}/restriction/my`, {headers});
        const dataRestricted = (data) => {
            return !data.isExpired && data.suspendedFunctions.includes(suspendedFunction)
        };
        const restricted = res.data.dtoList.some(dataRestricted);
        if (suspendedFunction==="LOGIN") { 
            const expireTimes = res.data.dtoList.filter(dataRestricted).map((data)=>{return data.expireTime});
            return {
                restricted: restricted,
                expireTime: getRelativeTime(expireTimes.reduce(latterTime))
            }
        } else {
            return restricted
        }
    } catch (error) {
        return false;
    }
}