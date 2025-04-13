import axios from 'axios';
import userDefault from '../../images/userDefault.png';

export const getProfileImages = async (ParamType, UserInfo, profileImages, setProfileImages) => {
    if (UserInfo === undefined) {return userDefault};
    const target = profileImages[ParamType][UserInfo];
    if (target === undefined) {
        await getUserProfileImage(ParamType, UserInfo, (imageUrl) => {
            const newProfileImages = {...profileImages};
            newProfileImages[ParamType][UserInfo] = imageUrl;
            setProfileImages(newProfileImages);
        });
    };
}

export const getUserProfileImage = async (ParamType, UserInfo, SetImageCallback) => {
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
        SetImageCallback(imageUrl);
    }catch(error){
        console.error(error);
    }
};