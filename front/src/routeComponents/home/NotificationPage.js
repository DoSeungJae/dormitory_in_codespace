import {React} from 'react'
import BackButton from '../../components/home/BackButton';

function NotificationPage(){

    return (
        <div className="App">
            <header className='App-notificationPage-header'>
                <BackButton></BackButton>
            </header>
            <div className="App-notificationPage-main">
                <div className='notificationPage-main-text'>
                    알림
                </div>
            </div>
        </div>

        
    );

}

export default NotificationPage;
