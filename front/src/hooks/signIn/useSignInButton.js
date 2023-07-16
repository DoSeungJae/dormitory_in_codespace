import {React,useState} from 'react'
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import InputForm from '../../components/common/InputForm';

const UseSignInButton = () => {
    const [id, setId] = useState('');
    const [pw, setPw] = useState('');
    const [pw2,setPw2]= useState('');
    const [tel, setTel] = useState('');
    const [nick, setNick] = useState('');
    const [idError,setIdError]=useState(false);
    const [pwError,setPwError]=useState(false);
    const [pw2Error,setpw2Error]=useState(false);
    const [telError,setTelError]=useState(false);
    const [nickError,setNickError]=useState(false);

    const rex_id = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9]{6,12}$/;
    const rex_pw = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9!@#$%^&*()]{8,16}$/;
    return (
        <div class="SignInPage">

        <div className="container p-4">
        <label class="row mt-3">

                <h6 class="col" id="signInForm">아이디</h6>
                <div class="col">
                {id.length!==0 && rex_id.test(id)===false 
                    && <small id="signInSmall">
                        양식에 맞지 않아요!
                        </small>}
                </div>

                <InputForm
                    type="text"
                    placeholder="영문, 숫자로 6~12자 사이"
                    value={id}
                    onChange={(e)=> setId(e.target.value)}
                />
            </label>

            <label class="row mt-3">
                <h6 id="signInForm" >비밀번호</h6>
                <div class="col">
                    {pw.length!==0 && rex_pw.test(pw)===false
                    && <small id="signInSmall">
                        양식에 맞지 않아요!
                    </small>}
                </div>
       
                <InputForm
                    type="password"
                    placeholder="영문, 숫자 (특수문자)로 8~16자 사이"
                    value={pw}
                    onChange={(e) => setPw(e.target.value)}
                />
            </label>

            <label class="row mt-3">
                <h6 id="signInForm">비밀번호 확인</h6>
                <div class="col">
                        {pw2.length!==0 && pw!==pw2 && 
                        <small id="signInSmall">
                        비밀번호가 일치하지 않아요!
                        </small>}
                </div>

                <InputForm
                    type="password"
                    placeholder="한 번 더 입력해주세요! "
                    value={pw2}
                    onChange={(e) => setPw2(e.target.value)}
                />
            </label>

            <label class="row mt-3">
                <h6 id="signInForm">전화번호</h6>
                {telError && <small id="signInSmall">
                    
                    </small>}
                <InputForm
                    type="text"
                    placeholder="'-'가 필요없어요!"
                    value={tel}
                    onChange={(e)=> setTel(e.target.value)}
                />
            </label>

            <label class="row mt-3">
                <h6 id="signInForm">닉네임</h6>
                {nickError && <small id="signInSmall">
                    
                    </small>}
                <InputForm
                    type="text"
                    placeholder="특수문자 없이 8자리 이내 "
                    value={nick}
                    onChange={(e)=> setNick(e.target.value)}
                />
            </label>

        </div>
    </div>

  

    );
}

export default UseSignInButton;
