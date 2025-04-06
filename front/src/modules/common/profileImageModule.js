import axios from 'axios';

export const getUserProfileImage = async (ParamType, UserInfo, setUrlCallback) => {
    try{
        const path1=`${process.env.REACT_APP_HTTP_API_URL}/file/userImageUrl`;
        const headers={
            ParamType, //"USERID", "NICKNAME"
            UserInfo   //id, "nickname123"
        }
        const response1=await axios.get(path1,{headers});
        const path2=response1.data;
        const response2 = await axios.get(path2, { responseType: 'blob' });
        const contentType = response2.headers['content-type'];
        const blob = new Blob([response2.data], { type: contentType });
        const imageUrl=URL.createObjectURL(blob);
        setUrlCallback(imageUrl);
    }catch(error){
        console.error(error);
    }
}