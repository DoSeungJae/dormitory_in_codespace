import {React,useState} from 'react'
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import InputForm from '../../components/common/InputForm';

const UseSignInButton = () => {
    const [id, setId] = useState('');
    const [pw, setPw] = useState('');
    const [eMail, setEMail] = useState('');
    const [nick, setNick] = useState('');
    const [idError,setIdError]=useState(false);
    const [pwError,setPwError]=useState(false);

    return (
        <div class="SignInPage">
        <div className="container" id="signInContainer">
            <h1>아이디</h1>
            <InputForm
                type="text"
                placeholder="알파벳, 숫자로 6자리 이상"
                value={id}
                onChange={(e)=> setId(e.target.value)}
            />
            
            <h1>비밀번호</h1>
            <InputForm
                type="password"
                placeholder="123"
                value={pw}
                onChange={(e) => setPw(e.target.value)}
            />
            
            <h1>이메일</h1>
            <InputForm
                type="text"
                placeholder="1234"
                value={eMail}
                onChange={(e)=> setId(e.target.value)}
            />
            

            <h1>닉네임</h1>
            <InputForm
                type="text"
                placeholder="nick"
                value={nick}
                onChange={(e)=> setId(e.target.value)}
            />
        </div>
    </div>

    );
}

export default UseSignInButton;
