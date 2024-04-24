import {React} from 'react';
import BackButton from '../../components/home/BackButton';

function MyWritingPage(){

    return (
        <div className='App'>
            <header className='App-myWritingPage-header'>
                <BackButton></BackButton>
            </header>
            <div className='App-myWritingPage-main'>
                <div className='myWritings'>
                    <div className='myWritingPage-main-text'>내가 쓴 글</div>
                </div>
                <div className='commentedOn'>
                    <div className='myWritingPage-main-text'>내가 댓글을 단 글</div>
                </div>
            </div>
        </div>
    );

}

export default MyWritingPage;
