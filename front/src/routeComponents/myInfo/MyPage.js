
import userDefault from '../../images/userDefault.png';
import BackButton from "../../components/home/BackButton";
import RightArrowIcon from '@mui/icons-material/ArrowForwardIos';
import ForwardButton from '../../components/myInfo/ForwardButton';

const MyPage = () => {
    return (
        <div className="App">
            <div className="App-header-myPage">
                <BackButton/>
            </div>
            <div className="App-main-myPage">
                <div className="myPage-profile">
                    <div className="profile-image"><img src={userDefault} alt="description" /></div>
                    <div className="profile-details">
                        <div className="details-nickName">닉네임</div>
                        <div className="details-dormitory">기숙사</div>
                    </div>
                </div>
                <div className="myPage-info">
                    <div className="info-writings">
                        <div>내 글</div>
                        <div><ForwardButton destIndex={11}/></div>
                        {/* 주의 */}
                    </div>
                    <div className="info-account">
                        <div className='info-title'>계정</div>
                        <div className="account-id">
                            <div>아이디</div>
                            <div>값</div>
                        </div>
                        <div className="account-eMail">
                            <div>이메일</div>
                            <div>값</div>
                        </div>
                        <div className="account-changePassWord">
                            <div>비밀번호 바꾸기</div>
                        </div>
                        <div className="account-changeDormitory">
                            <div>기숙사 바꾸기</div>
                        </div>
                    </div>
                    <div className="info-community">
                    <div className='info-title'>커뮤니티</div>
                        <div className="community-restrictionDetails">
                            <div>커뮤니티 이용 제한사항</div>
                        </div>
                        <div className="community-guide">
                            <div>커뮤니티 이용 수칙</div>
                        </div>
                    </div>
                    <div className="etc">
                    <div className='info-title'>기타</div>
                        <div className="etc-inquiry">
                            <div>문의하기</div>
                        </div>
                        <div className="etc-logOut">
                            <div>로그아웃 하기</div>
                        </div>
                        <div className="etc-deleteAccount">
                            <div>계정 탈퇴하기</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MyPage;