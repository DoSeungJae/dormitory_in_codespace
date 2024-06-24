import {React, useContext, useEffect, useState} from 'react'
import BackButton from '../../components/home/BackButton';
import HomeSelectContext from '../../components/home/HomeSelectContext';
import axios from 'axios';
import NotiPreview from '../../components/notification/NotiPreview';

function NotificationPage(){
    const [notiList,setNotiList]=useState([]);
    const [isFetched,setIsFetched]=useState(0);
    const token=localStorage.getItem("token");
    const {selectComponentIndex,setSelectComponentIndex}=useContext(HomeSelectContext);

    const getNotifications= async () => {
        const path=`http://localhost:8080/api/v1/notification/myNotifications`;
        const headers={
            'Authorization':`${token}`
        };
        try{
            const response=await axios.get(path,{headers});
            const data=response.data;
            setNotiList((prev)=>[...prev,...data]);
            setIsFetched(1);
        }catch(error){
            console.error(error);
        }
    }

    useEffect(()=>{
        if(selectComponentIndex!=3){
            return ;
        }
        if(isFetched==1){
            return ;
        }
        getNotifications();
    })    

    

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
