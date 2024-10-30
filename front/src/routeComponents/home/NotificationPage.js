import {React, useContext, useEffect, useState} from 'react'
import BackButton from '../../components/home/BackButton';
import HomeSelectContext from '../../components/home/HomeSelectContext';
import axios from 'axios';
import NotiPreview from '../../components/notification/NotiPreview';

function NotificationPage(){
    const [notiList,setNotiList]=useState([]);
    const token=localStorage.getItem("token");
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);

    const getNotifications= async () => {
        const path=`https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev/api/v1/notification/myNotifications`;
        const headers={
            'Authorization':`${token}`
        };
        try{
            const response=await axios.get(path,{headers});
            const data=response.data;
            setNotiList((prev)=>[...prev,...data]);
        }catch(error){
            console.error(error);
        }
    }

    useEffect(()=>{
        if(selectComponentIndex!=3){
            return ;
        }
        setNotiList([]);
        getNotifications();
    },[selectComponentIndex])    


    return (
        <div className="App">
            <header className='App-notificationPage-header'>
                <BackButton></BackButton>
            </header>
            <div className="App-notificationPage-main">
                <div className='notificationPage-main-text'>
                    알림
                </div>
                <NotiPreview notiList={notiList}/>
            </div>
        </div>

        
    );

}

export default NotificationPage;
