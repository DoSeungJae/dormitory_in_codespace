
import userDefault from '../../images/userDefault.png';
import BackButton from "../../components/home/BackButton";

const MyPage = () => {
    return (
        <div className="App">
            <div className="App-header-myPage">
                <BackButton/>
            </div>
            <div className="App-main-myPage">
                <div className="myPage-profile">
                    <div className="profile-image"><img src={userDefault} alt="description" className='rounded-image'/></div>
                    <div className="profile-details">
                        <div className="details-nickName">닉네임</div>
                        <div className="details-dormitory">기숙사</div>
                    </div>
                </div>
                <div className="myPage-info">
                    <div className="info-writings">
                        <div>내 글</div>
                        <div>버튼</div>
                    </div>
                    <div className="info-account">
                        <div className="account-id"></div>
                        <div className="account-eMail"></div>
                        <div className="account-changePassWord"></div>
                    </div>
                    <div className="info-community">
                        <div className="community-restrictionDetails"></div>
                        <div className="community-guide"></div>
                    </div>
                    <div className="etc">
                        <div className="etc-inquiry"></div>
                        <div className="etc-logOut"></div>
                        <div className="etc-deleteAccount"></div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MyPage;