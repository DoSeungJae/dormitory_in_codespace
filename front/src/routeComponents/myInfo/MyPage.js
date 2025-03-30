import userDefault from '../../images/userDefault.png';
import BackButton from "../../components/home/BackButton";
import ForwardButton from '../../components/myInfo/ForwardButton';
import { useContext, useEffect, useState } from 'react';
import HomeSelectContext from '../../components/home/HomeSelectContext';
import axios from 'axios';
import { dorIdToDorName } from '../../components/home/HomeUtils';
import Modal from '../../components/common/Modal';
import ModalContext from '../../components/common/ModalContext';

import PasswordChangeForm from '../../components/modalForms/myInfo/PasswordChangeForm';
import DormitoryChangeForm from '../../components/modalForms/myInfo/DormitoryChangeForm';
import InquireForm from '../../components/modalForms/myInfo/InquireForm';
import LogoutForm from '../../components/modalForms/myInfo/LogoutForm';
import UserDeletionForm from '../../components/modalForms/myInfo/UserDeletionForm';
import RestrictionForm from '../../components/modalForms/myInfo/RestrictionForm';
import ProfileChangeForm from '../../components/modalForms/myInfo/ProfileChangeForm';

const MyPage = () => {
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);
    const {isOpen, openModal, closeModal}=useContext(ModalContext);
    const [user,setUser]=useState({});
    const [profileImage,setProfileImage]=useState(userDefault);
    const [profileDeleted,setProfileDeleted]=useState(false);
    const token=localStorage.getItem("token");

    const changePassword = (modalContent) => {
        openModal(modalContent);
    }

    const changeDormitory = (modalContent) => {
        openModal(modalContent);
    }

    const makeInquiry = (modalContent) => {
        openModal(modalContent);
    }

    const logout = (modalContent) => {
        openModal(modalContent);
    }

    const changeProfile = (modalContent) => {
        openModal(modalContent)
    }

    const getUser = async () => {
        const pathUserId=`${process.env.REACT_APP_HTTP_API_URL}/token/userId`;
        try {
            const response = await axios.get(pathUserId, {headers: {'Authorization': `${token}`}});
            const userId=response.data;
            try{
                const pathUser=`${process.env.REACT_APP_HTTP_API_URL}/user/${userId}`;
                const response2=await axios.get(pathUser);
                setUser(response2.data);
            }catch(error){
                console.error(error);
            }
        } catch (error) {
            console.error(error);
        }
    }

    const getUserProfileImageURL = async () => {
        console.log(1717);
        const path=`${process.env.REACT_APP_HTTP_API_URL}/file/userImageUrl`;
        const headers={
            ParamType:'USERID',
            UserInfo:user.id
        }
        try{
            const response=await axios.get(path,{headers});
            const url=response.data;
            getUserProfileImage(url);
        }catch(error){
            console.error(error);
        }
    }

    const getUserProfileImage = async (path) => {
       try{
            const response = await axios.get(path, { responseType: 'blob' })

            const contentType = response.headers['content-type'];
            const blob = new Blob([response.data], { type: contentType });
            const imageUrl=URL.createObjectURL(blob);
            setProfileImage(imageUrl);
       }catch(error){
        console.error(error);
       }
    }
    
    useEffect(()=>{
        getUser(); 
    },[])

    useEffect(()=>{
        if(selectComponentIndex!==1){
            return ;
        }
        if(profileImage!=userDefault){
            return ;
        }
        if(profileDeleted){
            return ;
        }
        getUserProfileImageURL();
    },[profileImage,selectComponentIndex]);


    useEffect(()=>{
        if(profileImage!=null){
            return ;
        }
        setProfileImage(userDefault);
    },[profileImage,selectComponentIndex])

    return (
        <div className="App">
            <div className="App-header-myPage">
                <BackButton/>
            </div>
            <div className="App-main-myPage">
                <div className="myPage-profile">
                    <div className="profile-image">
                        <img src={profileImage} alt="description" onClick={()=>changeProfile(<ProfileChangeForm setProfileDeleted={setProfileDeleted} profileImage={profileImage} setProfileImage={setProfileImage}/>)}/>
                    </div>
                    <div className="profile-details">
                        <div className="details-nickName">{user.nickName}</div>
                        <div className="details-dormitory">{user.dormId ? dorIdToDorName[user.dormId] : "?"}</div>
                    </div>
                </div>
                <div className="myPage-info">
                    <div className="info-writings" onClick={() => setSelectComponentIndex(11)}>
                        <div>내 글</div>
                        <div><ForwardButton/></div>
                    </div>
                    <div className="info-account">
                        <div className='info-title'>계정</div>
                        <div className="account-id">
                            <div>아이디(이메일)</div>
                            <div>{user.email}</div>
                        </div>
                        <div className="account-changePassWord" onClick={()=>changePassword(<PasswordChangeForm userId={user.id}/>)}>
                            <div>비밀번호 바꾸기</div>
                            <div><Modal/></div>
                        </div>
                        <div className="account-changeDormitory" onClick={()=>changeDormitory(<DormitoryChangeForm userId={user.id}/>)}>
                            <div>기숙사 바꾸기</div>
                        </div>
                    </div>
                    <div className="info-community">
                    <div className='info-title'>커뮤니티</div>
                        <div className="community-restrictionDetails" onClick={()=>openModal(<RestrictionForm/>)}>
                            <div>커뮤니티 이용 제한사항</div>
                        </div>
                        <div className="community-guide">
                            <div>커뮤니티 이용 수칙</div>
                        </div>
                    </div>
                    <div className="etc">
                    <div className='info-title'>기타</div>
                        <div className="etc-inquiry" onClick={()=>makeInquiry(<InquireForm/>)}>
                            <div>문의하기</div>
                        </div>
                        <div className="etc-logOut" onClick={()=>logout(<LogoutForm/>)}>
                            <div>로그아웃 하기</div>
                        </div>
                        <div className="etc-deleteAccount" onClick={()=>openModal(<UserDeletionForm userId={user.id}/>)}>
                            <div>계정 탈퇴하기</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MyPage;